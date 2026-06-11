package edu.eci.arsw.wellness;

import edu.eci.arsw.wellness.data.WellnessRepository;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.logging.Logger;

public class WellnessGrpcServer {
    private static final Logger logger = Logger.getLogger(WellnessGrpcServer.class.getName());
    private final int port;
    private final Server server;

    public WellnessGrpcServer(int port) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(new AppointmentServiceImpl())
                .build();
    }

    public void start() throws IOException {
        server.start();
        logger.info("Wellness gRPC Server iniciado en puerto " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Cerrando servidor...");
            WellnessGrpcServer.this.stop();
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        WellnessGrpcServer server = new WellnessGrpcServer(50051);
        server.start();
        server.blockUntilShutdown();
    }

    static class AppointmentServiceImpl extends AppointmentServiceGrpc.AppointmentServiceImplBase {
        private final WellnessRepository repository = new WellnessRepository();

        @Override
        public void requestAppointment(AppointmentRequest request,
                                       StreamObserver<AppointmentResponse> responseObserver) {
            logger.info("Solicitud de cita recibida - Estudiante: " + request.getStudentName());
            
            AppointmentResponse.Builder responseBuilder = AppointmentResponse.newBuilder();
            
            try {
                // Validaciones
                if (request.getStudentId().isEmpty() || request.getStudentName().isEmpty()) {
                    responseBuilder.setSuccess(false)
                            .setMessage("ERROR: ID y nombre del estudiante son obligatorios");
                } else if (request.getPreferredDate().isEmpty()) {
                    responseBuilder.setSuccess(false)
                            .setMessage("ERROR: Fecha preferida es obligatoria");
                } else {
                    Appointment appointment = repository.createAppointment(request);
                    responseBuilder.setSuccess(true)
                            .setMessage("Cita solicitada exitosamente")
                            .setAppointment(appointment);
                }
            } catch (Exception e) {
                responseBuilder.setSuccess(false)
                        .setMessage("ERROR: " + e.getMessage());
            }
            
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }

        @Override
        public void cancelAppointment(CancelRequest request,
                                      StreamObserver<CancelResponse> responseObserver) {
            logger.info("Cancelación de cita - ID: " + request.getAppointmentId());
            
            boolean cancelled = repository.cancelAppointment(
                    request.getAppointmentId(),
                    request.getStudentId()
            );
            
            CancelResponse response;
            if (cancelled) {
                response = CancelResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Cita cancelada exitosamente")
                        .build();
            } else {
                response = CancelResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("ERROR: No se pudo cancelar la cita (verifique ID o estado)")
                        .build();
            }
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void getAppointments(StudentRequest request,
                                    StreamObserver<AppointmentList> responseObserver) {
            logger.info("Consulta de citas - Estudiante ID: " + request.getStudentId());
            
            var appointments = repository.getAppointmentsByStudent(request.getStudentId());
            
            AppointmentList response = AppointmentList.newBuilder()
                    .addAllAppointments(appointments)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}