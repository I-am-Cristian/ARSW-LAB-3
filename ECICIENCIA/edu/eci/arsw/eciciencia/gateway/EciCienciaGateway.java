package ECICIENCIA.edu.eci.arsw.eciciencia.gateway;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.*;

public class EciCienciaGateway {
    
    // gRPC Channels
    private final ManagedChannel registryChannel;
    private final ManagedChannel agendaChannel;
    private final ManagedChannel workshopChannel;
    private final ManagedChannel capacityChannel;
    private final ManagedChannel notificationChannel;
    
    // gRPC Stubs
    private final RegistryServiceGrpc.RegistryServiceBlockingStub registryStub;
    private final AgendaServiceGrpc.AgendaServiceBlockingStub agendaStub;
    private final WorkshopServiceGrpc.WorkshopServiceBlockingStub workshopStub;
    private final CapacityServiceGrpc.CapacityServiceBlockingStub capacityStub;
    
    private final Gson gson = new Gson();
    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();
    
    public EciCienciaGateway() {
        // Initialize gRPC connections
        registryChannel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        agendaChannel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();
        workshopChannel = ManagedChannelBuilder.forAddress("localhost", 50053).usePlaintext().build();
        capacityChannel = ManagedChannelBuilder.forAddress("localhost", 50054).usePlaintext().build();
        notificationChannel = ManagedChannelBuilder.forAddress("localhost", 50055).usePlaintext().build();
        
        registryStub = RegistryServiceGrpc.newBlockingStub(registryChannel);
        agendaStub = AgendaServiceGrpc.newBlockingStub(agendaChannel);
        workshopStub = WorkshopServiceGrpc.newBlockingStub(workshopChannel);
        capacityStub = CapacityServiceGrpc.newBlockingStub(capacityChannel);
    }
    
    public void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // Define routes
        server.createContext("/api/register", new RegisterHandler());
        server.createContext("/api/agenda", new AgendaHandler());
        server.createContext("/api/reserve", new ReserveHandler());
        server.createContext("/api/my-reservations", new MyReservationsHandler());
        server.createContext("/api/capacity", new CapacityHandler());
        server.createContext("/api/dashboard", new DashboardHandler());
        
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        
        System.out.println("=== ECICIENCIA API GATEWAY ===");
        System.out.println("Gateway iniciado en http://localhost:8080");
        System.out.println("Endpoints disponibles:");
        System.out.println("  POST /api/register - Registrar asistente");
        System.out.println("  GET  /api/agenda?time=... - Consultar agenda");
        System.out.println("  POST /api/reserve - Reservar taller");
        System.out.println("  GET  /api/my-reservations?id=... - Mis reservas");
        System.out.println("  GET  /api/capacity?activity=... - Ver aforo");
        System.out.println("  GET  /api/dashboard - Estadísticas globales");
    }
    
    // Handler para registro de asistentes
    class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                return;
            }
            
            String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines().reduce("", (a, b) -> a + b);
            
            JsonObject request = gson.fromJson(body, JsonObject.class);
            
            // Forward to Registry Service
            RegisterRequest grpcRequest = RegisterRequest.newBuilder()
                .setName(request.get("name").getAsString())
                .setEmail(request.get("email").getAsString())
                .setInstitution(request.get("institution").getAsString())
                .setRole(AttendeeRole.valueOf(request.get("role").getAsString()))
                .setPhone(request.get("phone").getAsString())
                .build();
            
            AttendeeResponse response = registryStub.registerAttendee(grpcRequest);
            
            // Send notification (async)
            if (response.getSuccess()) {
                sendNotification(request.get("email").getAsString(), 
                    "Bienvenido a ECICIENCIA", 
                    "Tu registro ha sido exitoso");
            }
            
            sendResponse(exchange, response.getSuccess() ? 200 : 400, 
                gson.toJson(Map.of(
                    "success", response.getSuccess(),
                    "message", response.getMessage(),
                    "attendee", response.getAttendee()
                )));
        }
    }
    
    // Handler para consultar agenda
    class AgendaHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                return;
            }
            
            String query = exchange.getRequestURI().getQuery();
            ActivityList activities;
            
            if (query != null && query.contains("time=")) {
                String timeSlot = query.split("=")[1];
                TimeSlotRequest request = TimeSlotRequest.newBuilder()
                    .setStartTime(timeSlot)
                    .setEndTime(timeSlot + "T23:59:59")
                    .build();
                activities = agendaStub.getActivitiesByTimeSlot(request);
            } else if (query != null && query.contains("category=")) {
                String category = query.split("=")[1];
                CategoryRequest request = CategoryRequest.newBuilder()
                    .setCategory(category)
                    .build();
                activities = agendaStub.getActivitiesByCategory(request);
            } else {
                activities = agendaStub.getSchedule(EmptyRequest.newBuilder().build());
            }
            
            // Enhance with capacity info
            List<Map<String, Object>> enhancedActivities = new ArrayList<>();
            for (ActivityInfo activity : activities.getActivitiesList()) {
                CapacityInfo capacity = capacityStub.getActivityCapacity(
                    CapacityRequest.newBuilder()
                        .setActivityId(activity.getId())
                        .build());
                
                Map<String, Object> enhanced = new HashMap<>();
                enhanced.put("activity", activity);
                enhanced.put("availability", Map.of(
                    "available_spots", capacity.getAvailableSpots(),
                    "occupancy_rate", capacity.getOccupancyRate()
                ));
                enhancedActivities.add(enhanced);
            }
            
            sendResponse(exchange, 200, gson.toJson(Map.of(
                "activities", enhancedActivities,
                "total", activities.getTotalCount()
            )));
        }
    }
    
    // Handler para reservas
    class ReserveHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                return;
            }
            
            String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines().reduce("", (a, b) -> a + b);
            
            JsonObject request = gson.fromJson(body, JsonObject.class);
            String attendeeId = request.get("attendee_id").getAsString();
            String activityId = request.get("activity_id").getAsString();
            
            // 1. Check capacity
            CapacityInfo capacity = capacityStub.getActivityCapacity(
                CapacityRequest.newBuilder()
                    .setActivityId(activityId)
                    .build());
            
            if (capacity.getAvailableSpots() <= 0) {
                sendResponse(exchange, 409, gson.toJson(Map.of(
                    "success", false,
                    "message", "No hay cupos disponibles para esta actividad"
                )));
                return;
            }
            
            // 2. Reserve spot in capacity service
            SpotResponse spotResponse = capacityStub.reserveSpot(
                SpotReservationRequest.newBuilder()
                    .setActivityId(activityId)
                    .setAttendeeId(attendeeId)
                    .build());
            
            if (!spotResponse.getSuccess()) {
                sendResponse(exchange, 400, gson.toJson(Map.of(
                    "success", false,
                    "message", spotResponse.getMessage()
                )));
                return;
            }
            
            // 3. Create reservation in workshop service
            ReservationResponse reservation = workshopStub.reserveWorkshop(
                ReservationRequest.newBuilder()
                    .setAttendeeId(attendeeId)
                    .setActivityId(activityId)
                    .setReservationDate(new Date().toString())
                    .build());
            
            // 4. Send confirmation notification
            AttendeeInfo attendee = registryStub.getAttendee(
                GetAttendeeRequest.newBuilder()
                    .setAttendeeId(attendeeId)
                    .build());
            
            ActivityInfo activity = agendaStub.getActivityDetails(
                ActivityRequest.newBuilder()
                    .setActivityId(activityId)
                    .build());
            
            sendNotification(attendee.getEmail(), 
                "Reserva Confirmada - ECICIENCIA",
                String.format("Tu reserva para '%s' ha sido confirmada. Ubicación: %s, Horario: %s",
                    activity.getTitle(), activity.getLocation(), activity.getStartTime()));
            
            sendResponse(exchange, 200, gson.toJson(Map.of(
                "success", true,
                "reservation_id", reservation.getReservationId(),
                "status", reservation.getStatus(),
                "message", "Reserva exitosa",
                "spot_number", spotResponse.getSpotNumber()
            )));
        }
    }
    
    // Handler para consultar reservas de un asistente
    class MyReservationsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                return;
            }
            
            String query = exchange.getRequestURI().getQuery();
            String attendeeId = query.split("=")[1];
            
            ReservationList reservations = workshopStub.getAttendeeReservations(
                AttendeeReservationsRequest.newBuilder()
                    .setAttendeeId(attendeeId)
                    .build());
            
            // Enhance with activity details
            List<Map<String, Object>> enhancedReservations = new ArrayList<>();
            for (ReservationInfo reservation : reservations.getReservationsList()) {
                ActivityInfo activity = agendaStub.getActivityDetails(
                    ActivityRequest.newBuilder()
                        .setActivityId(reservation.getActivityId())
                        .build());
                
                Map<String, Object> enhanced = new HashMap<>();
                enhanced.put("reservation", reservation);
                enhanced.put("activity_details", activity);
                enhancedReservations.add(enhanced);
            }
            
            sendResponse(exchange, 200, gson.toJson(Map.of(
                "reservations", enhancedReservations,
                "total", reservations.getReservationsCount()
            )));
        }
    }
    
    // Handler para consultar capacidad
    class CapacityHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                return;
            }
            
            String query = exchange.getRequestURI().getQuery();
            
            if (query != null && query.contains("activity=")) {
                String activityId = query.split("=")[1];
                CapacityInfo capacity = capacityStub.getActivityCapacity(
                    CapacityRequest.newBuilder()
                        .setActivityId(activityId)
                        .build());
                
                sendResponse(exchange, 200, gson.toJson(capacity));
            } else {
                GlobalMetrics metrics = capacityStub.getGlobalMetrics(
                    EmptyRequest.newBuilder().build());
                
                sendResponse(exchange, 200, gson.toJson(metrics));
            }
        }
    }
    
    // Dashboard con estadísticas agregadas
    class DashboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                return;
            }
            
            // Gather data from multiple services
            CompletableFuture<AttendeeList> attendeesFuture = 
                CompletableFuture.supplyAsync(() -> 
                    registryStub.listAttendees(EmptyRequest.newBuilder().build()));
            
            CompletableFuture<ActivityList> activitiesFuture = 
                CompletableFuture.supplyAsync(() -> 
                    agendaStub.getSchedule(EmptyRequest.newBuilder().build()));
            
            CompletableFuture<GlobalMetrics> metricsFuture = 
                CompletableFuture.supplyAsync(() -> 
                    capacityStub.getGlobalMetrics(EmptyRequest.newBuilder().build()));
            
            CompletableFuture.allOf(attendeesFuture, activitiesFuture, metricsFuture).join();
            
            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("total_attendees", attendeesFuture.join().getAttendeesCount());
            dashboard.put("total_activities", activitiesFuture.join().getTotalCount());
            dashboard.put("global_metrics", metricsFuture.join());
            dashboard.put("timestamp", new Date().toString());
            
            sendResponse(exchange, 200, gson.toJson(dashboard));
        }
    }
    
    private void sendNotification(String email, String subject, String body) {
        // Async notification without blocking response
        CompletableFuture.runAsync(() -> {
            try {
                ManagedChannel notifChannel = ManagedChannelBuilder
                    .forAddress("localhost", 50055).usePlaintext().build();
                NotificationServiceGrpc.NotificationServiceBlockingStub notifStub = 
                    NotificationServiceGrpc.newBlockingStub(notifChannel);
                
                // Send notification (simplified)
                System.out.printf("[NOTIFICATION] To: %s | Subject: %s | Body: %s%n", 
                    email, subject, body);
                
                notifChannel.shutdown();
            } catch (Exception e) {
                System.err.println("Failed to send notification: " + e.getMessage());
            }
        });
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String response) 
            throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    
    public void shutdown() {
        registryChannel.shutdown();
        agendaChannel.shutdown();
        workshopChannel.shutdown();
        capacityChannel.shutdown();
        notificationChannel.shutdown();
    }
    
    public static void main(String[] args) throws Exception {
        EciCienciaGateway gateway = new EciCienciaGateway();
        gateway.start();
        
        // Graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gateway...");
            gateway.shutdown();
        }));
    }
}