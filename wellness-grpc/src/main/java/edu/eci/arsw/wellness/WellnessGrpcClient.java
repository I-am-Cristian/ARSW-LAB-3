package edu.eci.arsw.wellness;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;
import java.util.logging.Logger;

public class WellnessGrpcClient {
    private static final Logger logger = Logger.getLogger(WellnessGrpcClient.class.getName());
    private final ManagedChannel channel;
    private final AppointmentServiceGrpc.AppointmentServiceBlockingStub stub;

    public WellnessGrpcClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = AppointmentServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() {
        channel.shutdown();
    }

    public AppointmentResponse requestAppointment(String studentId, String studentName,
                                                   String studentEmail, ServiceType serviceType,
                                                   String preferredDate) {
        AppointmentRequest request = AppointmentRequest.newBuilder()
                .setStudentId(studentId)
                .setStudentName(studentName)
                .setStudentEmail(studentEmail)
                .setServiceType(serviceType)
                .setPreferredDate(preferredDate)
                .build();
        
        return stub.requestAppointment(request);
    }

    public CancelResponse cancelAppointment(String appointmentId, String studentId) {
        CancelRequest request = CancelRequest.newBuilder()
                .setAppointmentId(appointmentId)
                .setStudentId(studentId)
                .build();
        
        return stub.cancelAppointment(request);
    }

    public AppointmentList getAppointments(String studentId) {
        StudentRequest request = StudentRequest.newBuilder()
                .setStudentId(studentId)
                .build();
        
        return stub.getAppointments(request);
    }

    public static void main(String[] args) {
        WellnessGrpcClient client = new WellnessGrpcClient("localhost", 50051);
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Sistema de Bienestar Universitario ===\n");
        
        while (true) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Solicitar cita");
            System.out.println("2. Cancelar cita");
            System.out.println("3. Consultar mis citas");
            System.out.println("4. Salir");
            System.out.print("Opción: ");
            
            int option = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea
            
            switch (option) {
                case 1:
                    System.out.print("ID Estudiante: ");
                    String studentId = scanner.nextLine();
                    System.out.print("Nombre: ");
                    String studentName = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Fecha (YYYY-MM-DD): ");
                    String date = scanner.nextLine();
                    
                    System.out.println("Tipo de servicio:");
                    System.out.println("  0. MEDICINE");
                    System.out.println("  1. PSYCHOLOGY");
                    System.out.println("  2. DENTISTRY");
                    System.out.print("Seleccione: ");
                    int serviceTypeCode = scanner.nextInt();
                    scanner.nextLine();
                    
                    ServiceType serviceType = ServiceType.forNumber(serviceTypeCode);
                    
                    AppointmentResponse response = client.requestAppointment(
                            studentId, studentName, email, serviceType, date
                    );
                    
                    if (response.getSuccess()) {
                        System.out.println("✓ " + response.getMessage());
                        System.out.println("  ID de cita: " + response.getAppointment().getId());
                    } else {
                        System.out.println("✗ " + response.getMessage());
                    }
                    break;
                    
                case 2:
                    System.out.print("ID de la cita a cancelar: ");
                    String aptId = scanner.nextLine();
                    System.out.print("Su ID de estudiante: ");
                    String studId = scanner.nextLine();
                    
                    CancelResponse cancelResponse = client.cancelAppointment(aptId, studId);
                    
                    if (cancelResponse.getSuccess()) {
                        System.out.println("✓ " + cancelResponse.getMessage());
                    } else {
                        System.out.println("✗ " + cancelResponse.getMessage());
                    }
                    break;
                    
                case 3:
                    System.out.print("ID de estudiante: ");
                    String id = scanner.nextLine();
                    
                    AppointmentList appointments = client.getAppointments(id);
                    
                    if (appointments.getAppointmentsCount() == 0) {
                        System.out.println("No tiene citas registradas.");
                    } else {
                        System.out.println("\n--- Mis Citas ---");
                        appointments.getAppointmentsList().forEach(apt -> {
                            System.out.println("  ID: " + apt.getId());
                            System.out.println("  Tipo: " + apt.getServiceType());
                            System.out.println("  Fecha: " + apt.getDate());
                            System.out.println("  Estado: " + apt.getStatus());
                            System.out.println("  ---");
                        });
                    }
                    break;
                    
                case 4:
                    System.out.println("¡Hasta luego!");
                    client.shutdown();
                    scanner.close();
                    return;
                    
                default:
                    System.out.println("Opción inválida");
            }
        }
    }
}