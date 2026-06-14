package edu.eci.arsw.wellness.gateway;

import edu.eci.arsw.wellness.proto.appointment.*;
import edu.eci.arsw.wellness.proto.medical.*;
import edu.eci.arsw.wellness.proto.gym.*;
import edu.eci.arsw.wellness.proto.recreation.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;

public class WellnessGateway {
    
    private static final String APPOINTMENT_HOST = "localhost";
    private static final int APPOINTMENT_PORT = 50051;
    private static final String MEDICAL_HOST = "localhost";
    private static final int MEDICAL_PORT = 50052;
    private static final String GYM_HOST = "localhost";
    private static final int GYM_PORT = 50053;
    private static final String RECREATION_HOST = "localhost";
    private static final int RECREATION_PORT = 50054;
    
    private final ManagedChannel appointmentChannel;
    private final ManagedChannel medicalChannel;
    private final ManagedChannel gymChannel;
    private final ManagedChannel recreationChannel;
    
    public final AppointmentServiceGrpc.AppointmentServiceBlockingStub appointmentStub;
    public final MedicalServiceGrpc.MedicalServiceBlockingStub medicalStub;
    public final GymServiceGrpc.GymServiceBlockingStub gymStub;
    public final RecreationServiceGrpc.RecreationServiceBlockingStub recreationStub;
    
    public WellnessGateway() {
        appointmentChannel = ManagedChannelBuilder.forAddress(APPOINTMENT_HOST, APPOINTMENT_PORT).usePlaintext().build();
        medicalChannel = ManagedChannelBuilder.forAddress(MEDICAL_HOST, MEDICAL_PORT).usePlaintext().build();
        gymChannel = ManagedChannelBuilder.forAddress(GYM_HOST, GYM_PORT).usePlaintext().build();
        recreationChannel = ManagedChannelBuilder.forAddress(RECREATION_HOST, RECREATION_PORT).usePlaintext().build();
        
        appointmentStub = AppointmentServiceGrpc.newBlockingStub(appointmentChannel);
        medicalStub = MedicalServiceGrpc.newBlockingStub(medicalChannel);
        gymStub = GymServiceGrpc.newBlockingStub(gymChannel);
        recreationStub = RecreationServiceGrpc.newBlockingStub(recreationChannel);
        
        System.out.println(" WellnessGateway inicializado");
        System.out.println(" Conectado a servicios internos");
    }
    
    public AppointmentResponse requestAppointment(String studentId, String studentName, 
                                                   String studentEmail, ServiceTypeProto serviceType) {
        AppointmentRequest request = AppointmentRequest.newBuilder()
            .setStudentId(studentId).setStudentName(studentName).setStudentEmail(studentEmail)
            .setServiceType(serviceType).setPreferredDate(getCurrentDate()).build();
        return appointmentStub.requestAppointment(request);
    }
    
    public String getStudentWellnessSummary(String studentId, String studentName) {
        StringBuilder sb = new StringBuilder();
        sb.append("    RESUMEN DE BIENESTAR UNIVERSITARIO             \n");
        sb.append("Estudiante: ").append(String.format("%-36s", studentName));
        sb.append(" ID: ").append(String.format("%-40s", studentId));
        
        // Appointments
        AppointmentList appointments = appointmentStub.getAppointments(
            StudentRequest.newBuilder().setStudentId(studentId).build());
        sb.append("\n CITAS MÉDICAS\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        if (appointments.getAppointmentsCount() == 0) {
            sb.append("   No hay citas agendadas\n");
        } else {
            appointments.getAppointmentsList().forEach(a -> 
                sb.append(String.format("   • %s | %s | %s\n", a.getServiceType(), a.getScheduledDate(), a.getStatus())));
        }
        
        // Gym
        GymReservationList gym = gymStub.getStudentReservations(
            StudentGymRequest.newBuilder().setStudentId(studentId).build());
        sb.append("\n RESERVAS DE GIMNASIO\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        if (gym.getReservationsCount() == 0) {
            sb.append("   No hay reservas activas\n");
        } else {
            gym.getReservationsList().forEach(r -> 
                sb.append(String.format("   • %s | %s | %s\n", r.getSessionType(), r.getTimeSlot(), r.getStatus())));
        }
        
        // Recreation
        ResourceReservationList rec = recreationStub.getStudentReservations(
            StudentRecreationRequest.newBuilder().setStudentId(studentId).build());
        sb.append("\n RECURSOS RECREATIVOS\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        if (rec.getReservationsCount() == 0) {
            sb.append("   No hay recursos reservados\n");
        } else {
            rec.getReservationsList().forEach(r -> 
                sb.append(String.format("   • %s | Devolver: %s\n", r.getResourceId(), r.getReturnDeadline())));
        }
        
        return sb.toString();
    }
    
    public GymReservationResponse reserveGymSession(String studentId, String studentName, 
                                                     String timeSlot, String sessionType) {
        return gymStub.reserveGymSession(GymReservationRequest.newBuilder()
            .setStudentId(studentId).setStudentName(studentName)
            .setTimeSlot(timeSlot).setSessionType(sessionType.toUpperCase()).build());
    }
    
    public ResourceReservationResponse reserveRecreationResource(String studentId, String studentName,
                                                                  String resourceId, String resourceType,
                                                                  int durationHours) {
        return recreationStub.reserveResource(ResourceReservationRequest.newBuilder()
            .setStudentId(studentId).setStudentName(studentName)
            .setResourceId(resourceId).setResourceType(resourceType.toUpperCase())
            .setDurationHours(durationHours).build());
    }
    
    public CancelResponse cancelAppointment(String appointmentId, String studentId) {
        return appointmentStub.cancelAppointment(CancelRequest.newBuilder()
            .setAppointmentId(appointmentId).setStudentId(studentId).build());
    }
    
    public AvailableSlotsResponse getAvailableSlots(ServiceTypeProto serviceType) {
        return appointmentStub.getAvailableSlots(
            ServiceTypeRequest.newBuilder().setServiceType(serviceType).build());
    }
    
    public SpecialtyList getAllMedicalSpecialties() {
        return medicalStub.getSpecialties(EmptyRequest.newBuilder().build());
    }
    
    public AvailableSessionsResponse getAvailableGymSessions() {
        return gymStub.getAvailableSessions(EmptyGymRequest.newBuilder().build());
    }
    
    public AvailableResourcesResponse getAvailableRecreationResources() {
        return recreationStub.getAvailableResources(EmptyRecreationRequest.newBuilder().build());
    }
    
    private String getCurrentDate() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    }
    
    public void shutdown() throws InterruptedException {
        appointmentChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        medicalChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        gymChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        recreationChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}