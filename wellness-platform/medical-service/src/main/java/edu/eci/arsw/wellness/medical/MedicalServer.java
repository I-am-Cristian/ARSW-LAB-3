package edu.eci.arsw.wellness.medical;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class MedicalServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50052)
            .addService(new MedicalServiceImpl())
            .build();
        
        server.start();
        System.out.println("=== Medical Service ===");
        System.out.println("Servicio Médico iniciado en puerto 50052");
        System.out.println("Responsabilidad: Información de especialidades médicas");
        System.out.println("======================");
        
        server.awaitTermination();
    }
}