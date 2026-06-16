package edu.eci.arsw.wellness.appointment;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class AppointmentServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50051)
            .addService(new AppointmentServiceImpl())
            .build();
        
        server.start();
        System.out.println("=== Appointment Service ===");
        System.out.println("Servicio de Citas iniciado en puerto 50051");
        System.out.println("Responsabilidad: Gestionar citas médicas, psicológicas y odontológicas");
        System.out.println("================================");
        
        server.awaitTermination();
    }
}