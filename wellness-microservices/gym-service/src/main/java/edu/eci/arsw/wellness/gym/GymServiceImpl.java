package edu.eci.arsw.wellness.gym;

import edu.eci.arsw.wellness.proto.gym.*;
import io.grpc.stub.StreamObserver;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GymServiceImpl extends GymServiceGrpc.GymServiceImplBase {
    
    private final Map<String, GymReservationInfo> reservations = new ConcurrentHashMap<>();
    private final Map<String, List<String>> studentReservations = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    
    private final Map<String, Integer> sessionCapacity = new HashMap<>();
    private final Map<String, Integer> currentOccupancy = new HashMap<>();
    
    public GymServiceImpl() {
        sessionCapacity.put("YOGA", 15);
        sessionCapacity.put("CROSSFIT", 20);
        sessionCapacity.put("SPINNING", 12);
        sessionCapacity.put("CARDIO", 10);
        
        currentOccupancy.put("YOGA", 5);
        currentOccupancy.put("CROSSFIT", 8);
        currentOccupancy.put("SPINNING", 3);
        currentOccupancy.put("CARDIO", 2);
    }
    
    @Override
    public void reserveGymSession(GymReservationRequest request,
                                  StreamObserver<GymReservationResponse> responseObserver) {
        
        String reservationId = String.format("GYM-%04d", idGenerator.getAndIncrement());
        
        int capacity = sessionCapacity.getOrDefault(request.getSessionType(), 0);
        int occupancy = currentOccupancy.getOrDefault(request.getSessionType(), 0);
        
        if (occupancy >= capacity) {
            GymReservationResponse response = GymReservationResponse.newBuilder()
                .setSuccess(false)
                .setMessage("No hay cupos disponibles para " + request.getSessionType())
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        
        currentOccupancy.put(request.getSessionType(), occupancy + 1);
        
        GymReservationInfo reservation = GymReservationInfo.newBuilder()
            .setId(reservationId)
            .setStudentId(request.getStudentId())
            .setStudentName(request.getStudentName())
            .setTimeSlot(request.getTimeSlot())
            .setSessionType(request.getSessionType())
            .setStatus("CONFIRMED")
            .build();
        
        reservations.put(reservationId, reservation);
        studentReservations.computeIfAbsent(request.getStudentId(), k -> new ArrayList<>())
                          .add(reservationId);
        
        GymReservationResponse response = GymReservationResponse.newBuilder()
            .setSuccess(true)
            .setReservationId(reservationId)
            .setMessage("Reserva de gimnasio confirmada")
            .setConfirmedSlot(request.getTimeSlot())
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void cancelGymReservation(CancelGymRequest request,
                                     StreamObserver<CancelGymResponse> responseObserver) {
        
        GymReservationInfo reservation = reservations.get(request.getReservationId());
        
        if (reservation == null || !reservation.getStudentId().equals(request.getStudentId())) {
            CancelGymResponse response = CancelGymResponse.newBuilder()
                .setSuccess(false)
                .setMessage("Reserva no encontrada")
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        
        String sessionType = reservation.getSessionType();
        currentOccupancy.put(sessionType, currentOccupancy.get(sessionType) - 1);
        
        reservations.remove(request.getReservationId());
        studentReservations.get(request.getStudentId()).remove(request.getReservationId());
        
        CancelGymResponse response = CancelGymResponse.newBuilder()
            .setSuccess(true)
            .setMessage("Reserva cancelada exitosamente")
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void getStudentReservations(StudentGymRequest request,
                                       StreamObserver<GymReservationList> responseObserver) {
        
        List<String> studentResIds = studentReservations.getOrDefault(request.getStudentId(), new ArrayList<>());
        GymReservationList.Builder listBuilder = GymReservationList.newBuilder();
        
        for (String resId : studentResIds) {
            GymReservationInfo reservation = reservations.get(resId);
            if (reservation != null) {
                listBuilder.addReservations(reservation);
            }
        }
        
        responseObserver.onNext(listBuilder.build());
        responseObserver.onCompleted();
    }
    
    @Override
    public void getAvailableSessions(EmptyGymRequest request,
                                     StreamObserver<AvailableSessionsResponse> responseObserver) {
        
        List<String> availableSlots = Arrays.asList(
            "Lunes 07:00", "Lunes 08:00", "Lunes 17:00",
            "Martes 07:00", "Martes 18:00",
            "Miércoles 08:00", "Miércoles 17:00"
        );
        
        AvailableSessionsResponse response = AvailableSessionsResponse.newBuilder()
            .addAllAvailableSlots(availableSlots)
            .putAllSessionCapacity(sessionCapacity)
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}