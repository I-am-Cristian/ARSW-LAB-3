package edu.eci.arsw.wellness.recreation;

import edu.eci.arsw.wellness.proto.recreation.*;
import io.grpc.stub.StreamObserver;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecreationServiceImpl extends RecreationServiceGrpc.RecreationServiceImplBase {
    
    private final Map<String, ResourceReservationInfo> reservations = new ConcurrentHashMap<>();
    private final Map<String, List<String>> studentReservations = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    private final Map<String, Integer> availableResources = new HashMap<>();
    
    public RecreationServiceImpl() {
        availableResources.put("BOARD_GAME_Ajedrez", 3);
        availableResources.put("BOARD_GAME_Monopoly", 2);
        availableResources.put("SPORTS_EQUIPMENT_Balon", 5);
        availableResources.put("SPORTS_EQUIPMENT_Raqueta", 4);
        availableResources.put("MUSICAL_INSTRUMENT_Guitarra", 2);
        availableResources.put("MUSICAL_INSTRUMENT_Teclado", 1);
    }
    
    @Override
    public void reserveResource(ResourceReservationRequest request,
                                StreamObserver<ResourceReservationResponse> responseObserver) {
        
        String reservationId = String.format("REC-%04d", idGenerator.getAndIncrement());
        int available = availableResources.getOrDefault(request.getResourceId(), 0);
        
        if (available <= 0) {
            responseObserver.onNext(ResourceReservationResponse.newBuilder()
                .setSuccess(false).setMessage("Recurso no disponible").build());
            responseObserver.onCompleted();
            return;
        }
        
        availableResources.put(request.getResourceId(), available - 1);
        
        String returnDeadline = LocalDateTime.now().plusHours(request.getDurationHours())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        
        ResourceReservationInfo reservation = ResourceReservationInfo.newBuilder()
            .setId(reservationId).setStudentId(request.getStudentId()).setStudentName(request.getStudentName())
            .setResourceId(request.getResourceId()).setResourceType(request.getResourceType())
            .setReservedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
            .setReturnDeadline(returnDeadline).setStatus("ACTIVE").build();
        
        reservations.put(reservationId, reservation);
        studentReservations.computeIfAbsent(request.getStudentId(), k -> new ArrayList<>()).add(reservationId);
        
        responseObserver.onNext(ResourceReservationResponse.newBuilder()
            .setSuccess(true).setReservationId(reservationId).setMessage("Recurso reservado")
            .setReturnDeadline(returnDeadline).build());
        responseObserver.onCompleted();
    }
    
    @Override
    public void returnResource(ReturnResourceRequest request,
                               StreamObserver<ReturnResourceResponse> responseObserver) {
        
        ResourceReservationInfo reservation = reservations.get(request.getReservationId());
        
        if (reservation == null || !reservation.getStudentId().equals(request.getStudentId())) {
            responseObserver.onNext(ReturnResourceResponse.newBuilder().setSuccess(false)
                .setMessage("Reserva no encontrada").build());
            responseObserver.onCompleted();
            return;
        }
        
        availableResources.put(reservation.getResourceId(), availableResources.get(reservation.getResourceId()) + 1);
        reservations.remove(request.getReservationId());
        studentReservations.get(request.getStudentId()).remove(request.getReservationId());
        
        responseObserver.onNext(ReturnResourceResponse.newBuilder().setSuccess(true)
            .setMessage("Recurso devuelto").build());
        responseObserver.onCompleted();
    }
    
    @Override
    public void getStudentReservations(StudentRecreationRequest request,
                                       StreamObserver<ResourceReservationList> responseObserver) {
        
        List<String> studentResIds = studentReservations.getOrDefault(request.getStudentId(), new ArrayList<>());
        ResourceReservationList.Builder builder = ResourceReservationList.newBuilder();
        
        for (String resId : studentResIds) {
            ResourceReservationInfo res = reservations.get(resId);
            if (res != null) builder.addReservations(res);
        }
        
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
    
    @Override
    public void getAvailableResources(EmptyRecreationRequest request,
                                      StreamObserver<AvailableResourcesResponse> responseObserver) {
        
        responseObserver.onNext(AvailableResourcesResponse.newBuilder()
            .putAllAvailableResources(availableResources)
            .addAllResourcesByType(Arrays.asList("Juegos de Mesa", "Equipos Deportivos", "Instrumentos Musicales"))
            .build());
        responseObserver.onCompleted();
    }
}