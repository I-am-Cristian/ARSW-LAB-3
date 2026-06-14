package edu.eci.arsw.wellness.gateway;

import edu.eci.arsw.wellness.proto.appointment.*;
import edu.eci.arsw.wellness.proto.gym.*;
import edu.eci.arsw.wellness.proto.recreation.*;
import java.util.Scanner;

public class GatewayConsole {
    
    private static WellnessGateway gateway;
    private static Scanner scanner;
    private static String currentStudentId;
    private static String currentStudentName;
    private static String currentStudentEmail;
    
    public static void main(String[] args) {
        System.out.println(" SISTEMA DE BIENESTAR UNIVERSITARIO - API GATEWAY " );
        
        try {
            gateway = new WellnessGateway();
            scanner = new Scanner(System.in);
            registerStudent();
            showMainMenu();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (scanner != null) scanner.close();
            if (gateway != null) {
                try { gateway.shutdown(); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }
    
    private static void registerStudent() {
        System.out.println(" REGISTRO DE ESTUDIANTE");
        System.out.print("ID de estudiante: ");
        currentStudentId = scanner.nextLine();
        System.out.print("Nombre completo: ");
        currentStudentName = scanner.nextLine();
        System.out.print("Correo electrónico: ");
        currentStudentEmail = scanner.nextLine();
        System.out.println("\nEstudiante registrado exitosamente!\n");
    }
    
    private static void showMainMenu() {
        int option;
        do {
            System.out.println("MENÚ PRINCIPAL");
            System.out.println("1. Solicitar cita médica");
            System.out.println("2. Ver resumen de bienestar");
            System.out.println("3. Reservar sesión de gimnasio");
            System.out.println("4. Reservar recurso recreativo");
            System.out.println("5. Ver servicios disponibles");
            System.out.println("6. Cancelar cita médica");
            System.out.println("7. Ver mis actividades ");
            System.out.println("0. Salir");
            System.out.print("\n Opción: ");
            
            option = readInt();
            
            switch (option) {
                case 1 -> requestAppointment();
                case 2 -> showWellnessSummary();
                case 3 -> reserveGymSession();
                case 4 -> reserveRecreationResource();
                case 5 -> showAvailableServices();
                case 6 -> cancelAppointment();
                case 7 -> showMyActivities();
                case 0 -> System.out.println("\n ¡Hasta luego! Cuida tu salud y bienestar.\n");
                default -> System.out.println("\n Opción inválida");
            }
        } while (option != 0);
    }
    
    private static void requestAppointment() {
        System.out.println("                    SOLICITAR CITA MÉDICA                       ");
        System.out.println("   1. Medicina General");
        System.out.println("   2. Psicología");
        System.out.println("   3. Odontología");
        System.out.print("\nTipo de servicio: ");
        
        ServiceTypeProto serviceType;
        switch (readInt()) {
            case 1 -> serviceType = ServiceTypeProto.MEDICINE;
            case 2 -> serviceType = ServiceTypeProto.PSYCHOLOGY;
            case 3 -> serviceType = ServiceTypeProto.DENTISTRY;
            default -> { System.out.println(" Opción inválida"); return; }
        }
        
        System.out.print("Fecha preferida (YYYY-MM-DD) [Enter para hoy]: ");
        String date = scanner.nextLine();
        if (date.isEmpty()) date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        
        System.out.println("\n Procesando...");
        AppointmentResponse response = gateway.requestAppointment(currentStudentId, currentStudentName, currentStudentEmail, serviceType);
        
        if (response.getSuccess()) {
            System.out.println("\n ¡Cita solicitada exitosamente!");
            System.out.println("   ID: " + response.getAppointmentId());
            System.out.println("   Fecha: " + response.getScheduledDate());
            System.out.println("   Estado: " + response.getStatus());
        } else {
            System.out.println("\n Error: " + response.getMessage());
        }
        System.out.print("\nPresione Enter...");
        scanner.nextLine();
    }
    
    private static void showWellnessSummary() {
        System.out.println("\n Generando resumen...");
        System.out.println(gateway.getStudentWellnessSummary(currentStudentId, currentStudentName));
        System.out.print("Presione Enter...");
        scanner.nextLine();
    }
    
    private static void reserveGymSession() {
        System.out.println("                    RESERVAR GIMNASIO                            ");
        
        AvailableSessionsResponse sessions = gateway.getAvailableGymSessions();
        System.out.println("\n Horarios disponibles:");
        sessions.getAvailableSlotsList().forEach(s -> System.out.println("   • " + s));
        
        System.out.println("\n Tipos de sesión:");
        sessions.getSessionCapacityMap().forEach((type, cap) -> System.out.println("   • " + type + " (Cap: " + cap + ")"));
        
        System.out.print("\nHorario: ");
        String timeSlot = scanner.nextLine();
        System.out.print("Tipo de sesión: ");
        String sessionType = scanner.nextLine();
        
        System.out.println("\n Procesando...");
        GymReservationResponse response = gateway.reserveGymSession(currentStudentId, currentStudentName, timeSlot, sessionType);
        
        if (response.getSuccess()) {
            System.out.println("\n  ¡Reserva confirmada!");
            System.out.println("   ID: " + response.getReservationId());
            System.out.println("   Horario: " + response.getConfirmedSlot());
        } else {
            System.out.println("\n Error: " + response.getMessage());
        }
        System.out.print("\nPresione Enter...");
        scanner.nextLine();
    }
    
    private static void reserveRecreationResource() {
        System.out.println("                    RESERVAR RECURSO RECREATIVO                  ");
        
        AvailableResourcesResponse resources = gateway.getAvailableRecreationResources();
        System.out.println("\n Recursos disponibles:");
        resources.getAvailableResourcesMap().forEach((res, qty) -> System.out.println("   • " + res + " (" + qty + ")"));
        
        System.out.print("\nID del recurso: ");
        String resourceId = scanner.nextLine();
        System.out.print("Tipo (BOARD_GAME/SPORTS_EQUIPMENT/MUSICAL_INSTRUMENT): ");
        String resourceType = scanner.nextLine();
        System.out.print("Duración (horas): ");
        int duration = readInt();
        
        System.out.println("\n Procesando...");
        ResourceReservationResponse response = gateway.reserveRecreationResource(
            currentStudentId, currentStudentName, resourceId, resourceType, duration);
        
        if (response.getSuccess()) {
            System.out.println("\n ¡Recurso reservado!");
            System.out.println("   ID: " + response.getReservationId());
            System.out.println("   Devolver antes: " + response.getReturnDeadline());
        } else {
            System.out.println("\n Error: " + response.getMessage());
        }
        System.out.print("\nPresione Enter...");
        scanner.nextLine();
    }
    
    private static void showAvailableServices() {
        System.out.println("                    SERVICIOS DISPONIBLES                        ");
        
        System.out.println("\n ESPECIALIDADES MÉDICAS:");
        gateway.getAllMedicalSpecialties().getSpecialtiesList().forEach(s -> {
            System.out.println(s.getName());
            System.out.println( s.getDescription());
            System.out.println( s.getAvailableDoctors() + " doctores");
        });
        
        System.out.println("\n SESIONES DE GIMNASIO:");
        AvailableSessionsResponse gym = gateway.getAvailableGymSessions();
        System.out.println("  Horarios: " + String.join(", ", gym.getAvailableSlotsList()));
        
        System.out.println("\n RECURSOS RECREATIVOS:");
        gateway.getAvailableRecreationResources().getAvailableResourcesMap()
            .forEach((res, qty) -> System.out.println("   • " + res + ": " + qty));
        
        System.out.print("\nPresione Enter...");
        scanner.nextLine();
    }
    
    private static void cancelAppointment() {
        System.out.println("                    CANCELAR CITA                                 ");
        
        AppointmentList appointments = gateway.appointmentStub.getAppointments(
            StudentRequest.newBuilder().setStudentId(currentStudentId).build());
        
        if (appointments.getAppointmentsCount() == 0) {
            System.out.println("\n No tiene citas activas");
        } else {
            System.out.println("\n Sus citas:");
            appointments.getAppointmentsList().forEach(a -> 
                System.out.println("   • " + a.getId() + " | " + a.getServiceType() + " | " + a.getScheduledDate()));
            
            System.out.print("\nID de cita a cancelar: ");
            String apptId = scanner.nextLine();
            
            CancelResponse response = gateway.cancelAppointment(apptId, currentStudentId);
            if (response.getSuccess()) {
                System.out.println( response.getMessage());
            } else {
                System.out.println( response.getMessage());
            }
        }
        System.out.print("\nPresione Enter...");
        scanner.nextLine();
    }
    
    private static void showMyActivities() {
        System.out.println("                    MIS ACTIVIDADES                              ");
        
        System.out.println("\n CITAS:");
        AppointmentList appointments = gateway.appointmentStub.getAppointments(
            StudentRequest.newBuilder().setStudentId(currentStudentId).build());
        if (appointments.getAppointmentsCount() == 0) {
            System.out.println("   No hay citas");
        } else {
            appointments.getAppointmentsList().forEach(a -> 
                System.out.println("   • " + a.getServiceType() + " - " + a.getScheduledDate() + " [" + a.getStatus() + "]"));
        }
        
        System.out.println(" GIMNASIO:");
        GymReservationList gym = gateway.gymStub.getStudentReservations(
            StudentGymRequest.newBuilder().setStudentId(currentStudentId).build());
        if (gym.getReservationsCount() == 0) {
            System.out.println("   No hay reservas");
        } else {
            gym.getReservationsList().forEach(r -> 
                System.out.println("   • " + r.getSessionType() + " - " + r.getTimeSlot() + " [" + r.getStatus() + "]"));
        }
        
        System.out.println(" RECREACIÓN:");
        ResourceReservationList rec = gateway.recreationStub.getStudentReservations(
            StudentRecreationRequest.newBuilder().setStudentId(currentStudentId).build());
        if (rec.getReservationsCount() == 0) {
            System.out.println("   No hay recursos");
        } else {
            rec.getReservationsList().forEach(r -> 
                System.out.println("   • " + r.getResourceId() + " - Devolver: " + r.getReturnDeadline()));
        }
        
        System.out.print("\nPresione Enter...");
        scanner.nextLine();
    }
    
    private static int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}