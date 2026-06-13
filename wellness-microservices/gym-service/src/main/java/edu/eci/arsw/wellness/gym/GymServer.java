package edu.eci.arsw.wellness.gym;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GymServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50053)
            .addService(new GymServiceImpl())
            .build();
        
        server.start();
        System.out.println("=== Gym Service ===");
        System.out.println("Servicio de Gimnasio iniciado en puerto 50053");
        System.out.println("Responsabilidad: Gestionar reservas de sesiones de gimnasio");
        System.out.println("==================");
        
        server.awaitTermination();
    }
}