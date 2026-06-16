package edu.eci.arsw.wellness.appointment;

import edu.eci.arsw.wellness.proto.appointment.*;
import io.grpc.stub.StreamObserver;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AppointmentServiceImpl extends AppointmentServiceGrpc.AppointmentServiceImplBase {
    
    private final Map<String, AppointmentInfo> appointments = new ConcurrentHashMap<>();
    private final Map<String, List<String>> studentAppointments = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    
    private final Map<ServiceTypeProto, List<String>> availableSlots = new HashMap<>();
    
    public AppointmentServiceImpl() {
        availableSlots.put(ServiceTypeProto.MEDICINE, 
            Arrays.asList("2024-01-15 09:00", "2024-01-15 10:00", "2024-01-16 14:00"));
        availableSlots.put(ServiceTypeProto.PSYCHOLOGY,
            Arrays.asList("2024-01-15 11:00", "2024-01-16 09:00", "2024-01-17 15:00"));
        availableSlots.put(ServiceTypeProto.DENTISTRY,
            Arrays.asList("2024-01-15 13:00", "2024-01-16 11:00", "2024-01-17 10:00"));
    }
    
    @Override
    public void requestAppointment(AppointmentRequest request, 
                                   StreamObserver<AppointmentResponse> responseObserver) {
        
        String appointmentId = String.format("APP-%04d", idGenerator.getAndIncrement());
        String scheduledDate = getAvailableSlot(request.getServiceType());
        
        AppointmentInfo appointment = AppointmentInfo.newBuilder()
            .setId(appointmentId)
            .setStudentId(request.getStudentId())
            .setStudentName(request.getStudentName())
            .setServiceType(request.getServiceType())
            .setScheduledDate(scheduledDate)
            .setStatus(AppointmentStatusProto.REQUESTED)
            .build();
        
        appointments.put(appointmentId, appointment);
        studentAppointments.computeIfAbsent(request.getStudentId(), k -> new ArrayList<>())
                          .add(appointmentId);
        
        AppointmentResponse response = AppointmentResponse.newBuilder()
            .setSuccess(true)
            .setAppointmentId(appointmentId)
            .setMessage("Cita solicitada exitosamente")
            .setScheduledDate(scheduledDate)
            .setStatus(AppointmentStatusProto.REQUESTED)
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void cancelAppointment(CancelRequest request,
                                  StreamObserver<CancelResponse> responseObserver) {
        
        AppointmentInfo appointment = appointments.get(request.getAppointmentId());
        
        if (appointment == null || !appointment.getStudentId().equals(request.getStudentId())) {
            CancelResponse response = CancelResponse.newBuilder()
                .setSuccess(false)
                .setMessage("Cita no encontrada o no autorizada")
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        
        AppointmentInfo updatedAppointment = appointment.toBuilder()
            .setStatus(AppointmentStatusProto.CANCELLED)
            .build();
        appointments.put(request.getAppointmentId(), updatedAppointment);
        
        CancelResponse response = CancelResponse.newBuilder()
            .setSuccess(true)
            .setMessage("Cita cancelada exitosamente")
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void getAppointments(StudentRequest request,
                                StreamObserver<AppointmentList> responseObserver) {
        
        List<String> studentApptIds = studentAppointments.getOrDefault(request.getStudentId(), new ArrayList<>());
        AppointmentList.Builder listBuilder = AppointmentList.newBuilder();
        
        for (String apptId : studentApptIds) {
            AppointmentInfo appointment = appointments.get(apptId);
            if (appointment != null && appointment.getStatus() != AppointmentStatusProto.CANCELLED) {
                listBuilder.addAppointments(appointment);
            }
        }
        
        responseObserver.onNext(listBuilder.build());
        responseObserver.onCompleted();
    }
    
    @Override
    public void getAvailableSlots(ServiceTypeRequest request,
                                  StreamObserver<AvailableSlotsResponse> responseObserver) {
        
        List<String> slots = availableSlots.getOrDefault(request.getServiceType(), new ArrayList<>());
        
        AvailableSlotsResponse response = AvailableSlotsResponse.newBuilder()
            .addAllAvailableDates(slots)
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    private String getAvailableSlot(ServiceTypeProto serviceType) {
        List<String> slots = availableSlots.get(serviceType);
        return slots.isEmpty() ? "Próximamente disponible" : slots.get(0);
    }
}