package edu.eci.arsw.wellness.data;

import edu.eci.arsw.wellness.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WellnessRepository {
    private final Map<String, Appointment> appointments = new ConcurrentHashMap<>();
    private int idCounter = 1;

    public synchronized Appointment createAppointment(AppointmentRequest request) {
        String appointmentId = String.format("APT-%04d", idCounter++);
        
        Appointment appointment = Appointment.newBuilder()
                .setId(appointmentId)
                .setStudentId(request.getStudentId())
                .setStudentName(request.getStudentName())
                .setServiceType(request.getServiceType())
                .setDate(request.getPreferredDate())
                .setStatus(AppointmentStatus.REQUESTED)
                .setCreatedAt(java.time.LocalDate.now().toString())
                .build();
        
        appointments.put(appointmentId, appointment);
        return appointment;
    }

    public boolean cancelAppointment(String appointmentId, String studentId) {
        Appointment appointment = appointments.get(appointmentId);
        
        if (appointment == null) {
            return false;
        }
        
        // Verificar que la cita pertenece al estudiante
        if (!appointment.getStudentId().equals(studentId)) {
            return false;
        }
        
        // Verificar que la cita no esté ya cancelada o atendida
        if (appointment.getStatus() == AppointmentStatus.CANCELLED ||
            appointment.getStatus() == AppointmentStatus.ATTENDED) {
            return false;
        }
        
        // Actualizar estado a CANCELLED
        Appointment updatedAppointment = appointment.toBuilder()
                .setStatus(AppointmentStatus.CANCELLED)
                .build();
        appointments.put(appointmentId, updatedAppointment);
        return true;
    }

    public List<Appointment> getAppointmentsByStudent(String studentId) {
        return appointments.values().stream()
                .filter(a -> a.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public Appointment getAppointment(String appointmentId) {
        return appointments.get(appointmentId);
    }
}