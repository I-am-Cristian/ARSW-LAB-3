package edu.eci.arsw.wellness.client;

import edu.eci.arsw.wellness.proto.appointment.*;
import edu.eci.arsw.wellness.proto.medical.*;
import edu.eci.arsw.wellness.proto.gym.*;
import edu.eci.arsw.wellness.proto.recreation.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;

public class WellnessClient {
    
    private final ManagedChannel appointmentChannel;
    private final ManagedChannel medicalChannel;
    private final ManagedChannel gymChannel;
    private final ManagedChannel recreationChannel;
    
    private final AppointmentServiceGrpc.AppointmentServiceBlockingStub appointmentStub;
    private final MedicalServiceGrpc.MedicalServiceBlockingStub medicalStub;
    private final GymServiceGrpc.GymServiceBlockingStub gymStub;
    private final RecreationServiceGrpc.RecreationServiceBlockingStub recreationStub;
    
    public WellnessClient() {
        appointmentChannel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        medicalChannel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();
        gymChannel = ManagedChannelBuilder.forAddress("localhost", 50053).usePlaintext().build();
        recreationChannel = ManagedChannelBuilder.forAddress("localhost", 50054).usePlaintext().build();
        
        appointmentStub = AppointmentServiceGrpc.newBlockingStub(appointmentChannel);
        medicalStub = MedicalServiceGrpc.newBlockingStub(medicalChannel);
        gymStub = GymServiceGrpc.newBlockingStub(gymChannel);
        recreationStub = RecreationServiceGrpc.newBlockingStub(recreationChannel);
    }
    
    public void shutdown() {
        appointmentChannel.shutdown();
        medicalChannel.shutdown();
        gymChannel.shutdown();
        recreationChannel.shutdown();
    }
    
    public void showMenu() {
        System.out.println("\n=== SISTEMA DE BIENESTAR UNIVERSITARIO ===");
        System.out.println("1. Solicitar cita médica");
        System.out.println("2. Ver especialidades médicas");
        System.out.println("3. Reservar sesión de gimnasio");
        System.out.println("4. Reservar recurso recreativo");
        System.out.println("5. Ver mis citas");
        System.out.println("6. Ver mis reservas de gimnasio");
        System.out.println("7. Ver mis recursos recreativos");
        System.out.println("8. Cancelar cita");
        System.out.println("9. Salir");
        System.out.print("Seleccione una opción: ");
    }
    
    public void requestAppointment(String studentId, String studentName, String studentEmail) {
        AppointmentRequest request = AppointmentRequest.newBuilder()
            .setStudentId(studentId)
            .setStudentName(studentName)
            .setStudentEmail(studentEmail)
            .setServiceType(ServiceTypeProto.MEDICINE)
            .setPreferredDate("2024-01-15")
            .build();
        
        AppointmentResponse response = appointmentStub.requestAppointment(request);
        
        if (response.getSuccess()) {
            System.out.println("\n✓ Cita solicitada exitosamente!");
            System.out.println("  ID Cita: " + response.getAppointmentId());
            System.out.println("  Fecha: " + response.getScheduledDate());
            System.out.println("  Estado: " + response.getStatus());
        } else {
            System.out.println("\n✗ Error: " + response.getMessage());
        }
    }
    
    public void showSpecialties() {
        SpecialtyList specialties = medicalStub.getSpecialties(EmptyRequest.newBuilder().build());
        
        System.out.println("\n=== ESPECIALIDADES MÉDICAS ===");
        for (SpecialtyInfo specialty : specialties.getSpecialtiesList()) {
            System.out.println("\n" + specialty.getName());
            System.out.println("   Descripción: " + specialty.getDescription());
            System.out.println("   Doctores disponibles: " + specialty.getAvailableDoctors());
            System.out.println("   Tratamientos: " + String.join(", ", specialty.getCommonTreatmentsList()));
        }
    }
    
    public void reserveGymSession(String studentId, String studentName) {
        GymReservationRequest request = GymReservationRequest.newBuilder()
            .setStudentId(studentId)
            .setStudentName(studentName)
            .setTimeSlot("Lunes 17:00")
            .setSessionType("CROSSFIT")
            .build();
        
        GymReservationResponse response = gymStub.reserveGymSession(request);
        
        if (response.getSuccess()) {
            System.out.println("\n Reserva de gimnasio confirmada!");
            System.out.println("  ID Reserva: " + response.getReservationId());
            System.out.println("  Horario: " + response.getConfirmedSlot());
        } else {
            System.out.println("\n Error: " + response.getMessage());
        }
    }
    
    public void reserveRecreationResource(String studentId, String studentName) {
        ResourceReservationRequest request = ResourceReservationRequest.newBuilder()
            .setStudentId(studentId)
            .setStudentName(studentName)
            .setResourceId("BOARD_GAME_Ajedrez")
            .setResourceType("BOARD_GAME")
            .setDurationHours(2)
            .build();
        
        ResourceReservationResponse response = recreationStub.reserveResource(request);
        
        if (response.getSuccess()) {
            System.out.println("\n Recurso recreativo reservado!");
            System.out.println("  ID Reserva: " + response.getReservationId());
            System.out.println("  Devolver antes de: " + response.getReturnDeadline());
        } else {
            System.out.println("\n Error: " + response.getMessage());
        }
    }
    
    public void showMyAppointments(String studentId) {
        StudentRequest request = StudentRequest.newBuilder()
            .setStudentId(studentId)
            .build();
        
        AppointmentList appointments = appointmentStub.getAppointments(request);
        
        System.out.println("\n=== MIS CITAS ===");
        if (appointments.getAppointmentsCount() == 0) {
            System.out.println("No tiene citas agendadas");
        } else {
            for (AppointmentInfo appt : appointments.getAppointmentsList()) {
                System.out.println("\n Cita ID: " + appt.getId());
                System.out.println("   Servicio: " + appt.getServiceType());
                System.out.println("   Fecha: " + appt.getScheduledDate());
                System.out.println("   Estado: " + appt.getStatus());
            }
        }
    }
    
    public void showMyGymReservations(String studentId) {
        StudentGymRequest request = StudentGymRequest.newBuilder()
            .setStudentId(studentId)
            .build();
        
        GymReservationList reservations = gymStub.getStudentReservations(request);
        
        System.out.println("\n=== MIS RESERVAS DE GIMNASIO ===");
        if (reservations.getReservationsCount() == 0) {
            System.out.println("No tiene reservas de gimnasio");
        } else {
            for (GymReservationInfo res : reservations.getReservationsList()) {
                System.out.println("\n Reserva ID: " + res.getId());
                System.out.println("   Tipo: " + res.getSessionType());
                System.out.println("   Horario: " + res.getTimeSlot());
                System.out.println("   Estado: " + res.getStatus());
            }
        }
    }
    
    public void showMyRecreationResources(String studentId) {
        StudentRecreationRequest request = StudentRecreationRequest.newBuilder()
            .setStudentId(studentId)
            .build();
        
        ResourceReservationList reservations = recreationStub.getStudentReservations(request);
        
        System.out.println("\n=== MIS RECURSOS RECREATIVOS ===");
        if (reservations.getReservationsCount() == 0) {
            System.out.println("No tiene recursos recreativos reservados");
        } else {
            for (ResourceReservationInfo res : reservations.getReservationsList()) {
                System.out.println("\n Reserva ID: " + res.getId());
                System.out.println("   Recurso: " + res.getResourceId());
                System.out.println("   Tipo: " + res.getResourceType());
                System.out.println("   Devolver antes: " + res.getReturnDeadline());
                System.out.println("   Estado: " + res.getStatus());
            }
        }
    }
    
    public static void main(String[] args) {
        WellnessClient client = new WellnessClient();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n BIENVENIDO AL SISTEMA DE BIENESTAR UNIVERSITARIO ");
        System.out.print("\nIngrese su ID de estudiante: ");
        String studentId = scanner.nextLine();
        System.out.print("Ingrese su nombre: ");
        String studentName = scanner.nextLine();
        System.out.print("Ingrese su email: ");
        String studentEmail = scanner.nextLine();
        
        int option;
        do {
            client.showMenu();
            option = scanner.nextInt();
            scanner.nextLine();
            
            switch (option) {
                case 1:
                    client.requestAppointment(studentId, studentName, studentEmail);
                    break;
                case 2:
                    client.showSpecialties();
                    break;
                case 3:
                    client.reserveGymSession(studentId, studentName);
                    break;
                case 4:
                    client.reserveRecreationResource(studentId, studentName);
                    break;
                case 5:
                    client.showMyAppointments(studentId);
                    break;
                case 6:
                    client.showMyGymReservations(studentId);
                    break;
                case 7:
                    client.showMyRecreationResources(studentId);
                    break;
                case 8:
                    System.out.println("Función de cancelación disponible próximamente");
                    break;
                case 9:
                    System.out.println("Gracias por usar el sistema. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        } while (option != 9);
        
        client.shutdown();
        scanner.close();
    }
}