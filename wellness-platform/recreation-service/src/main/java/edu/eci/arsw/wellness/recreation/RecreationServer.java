package edu.eci.arsw.wellness.recreation;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class RecreationServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50054)
            .addService(new RecreationServiceImpl())
            .build();
        
        server.start();
        System.out.println("=== Recreation Service ===");
        System.out.println("Servicio de Recreación iniciado en puerto 50054");
        System.out.println("=========================");
        
        server.awaitTermination();
    }
}