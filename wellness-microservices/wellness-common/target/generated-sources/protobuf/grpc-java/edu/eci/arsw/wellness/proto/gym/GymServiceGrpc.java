package edu.eci.arsw.wellness.proto.gym;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: gym.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class GymServiceGrpc {

  private GymServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "wellness.gym.GymService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.wellness.proto.gym.GymReservationRequest,
      edu.eci.arsw.wellness.proto.gym.GymReservationResponse> getReserveGymSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReserveGymSession",
      requestType = edu.eci.arsw.wellness.proto.gym.GymReservationRequest.class,
      responseType = edu.eci.arsw.wellness.proto.gym.GymReservationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.wellness.proto.gym.GymReservationRequest,
      edu.eci.arsw.wellness.proto.gym.GymReservationResponse> getReserveGymSessionMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.wellness.proto.gym.GymReservationRequest, edu.eci.arsw.wellness.proto.gym.GymReservationResponse> getReserveGymSessionMethod;
    if ((getReserveGymSessionMethod = GymServiceGrpc.getReserveGymSessionMethod) == null) {
      synchronized (GymServiceGrpc.class) {
        if ((getReserveGymSessionMethod = GymServiceGrpc.getReserveGymSessionMethod) == null) {
          GymServiceGrpc.getReserveGymSessionMethod = getReserveGymSessionMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.wellness.proto.gym.GymReservationRequest, edu.eci.arsw.wellness.proto.gym.GymReservationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReserveGymSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.wellness.proto.gym.GymReservationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.wellness.proto.gym.GymReservationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GymServiceMethodDescriptorSupplier("ReserveGymSession"))
              .build();
        }
      }
    }
    return getReserveGymSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.wellness.proto.gym.CancelGymRequest,
      edu.eci.arsw.wellness.proto.gym.CancelGymResponse> getCancelGymReservationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CancelGymReservation",
      requestType = edu.eci.arsw.wellness.proto.gym.CancelGymRequest.class,
      responseType = edu.eci.arsw.wellness.proto.gym.CancelGymResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.wellness.proto.gym.CancelGymRequest,
      edu.eci.arsw.wellness.proto.gym.CancelGymResponse> getCancelGymReservationMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.wellness.proto.gym.CancelGymRequest, edu.eci.arsw.wellness.proto.gym.CancelGymResponse> getCancelGymReservationMethod;
    if ((getCancelGymReservationMethod = GymServiceGrpc.getCancelGymReservationMethod) == null) {
      synchronized (GymServiceGrpc.class) {
        if ((getCancelGymReservationMethod = GymServiceGrpc.getCancelGymReservationMethod) == null) {
          GymServiceGrpc.getCancelGymReservationMethod = getCancelGymReservationMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.wellness.proto.gym.CancelGymRequest, edu.eci.arsw.wellness.proto.gym.CancelGymResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CancelGymReservation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.wellness.proto.gym.CancelGymRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.wellness.proto.gym.CancelGymResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GymServiceMethodDescriptorSupplier("CancelGymReservation"))
              .build();
        }
      }
    }
    return getCancelGymReservationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.wellness.proto.gym.StudentGymRequest,
      edu.eci.arsw.wellness.proto.gym.GymReservationList> getGetStudentReservationsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetStudentReservations",
      requestType = edu.eci.arsw.wellness.proto.gym.StudentGymRequest.class,
      responseType = edu.eci.arsw.wellness.proto.gym.GymReservationList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.wellness.proto.gym.StudentGymRequest,
      edu.eci.arsw.wellness.proto.gym.GymReservationList> getGetStudentReservationsMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.wellness.proto.gym.StudentGymRequest, edu.eci.arsw.wellness.proto.gym.GymReservationList> getGetStudentReservationsMethod;
    if ((getGetStudentReservationsMethod = GymServiceGrpc.getGetStudentReservationsMethod) == null) {
      synchronized (GymServiceGrpc.class) {
        if ((getGetStudentReservationsMethod = GymServiceGrpc.getGetStudentReservationsMethod) == null) {
          GymServiceGrpc.getGetStudentReservationsMethod = getGetStudentReservationsMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.wellness.proto.gym.StudentGymRequest, edu.eci.arsw.wellness.proto.gym.GymReservationList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetStudentReservations"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.wellness.proto.gym.StudentGymRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.wellness.proto.gym.GymReservationList.getDefaultInstance()))
              .setSchemaDescriptor(new GymServiceMethodDescriptorSupplier("GetStudentReservations"))
              .build();
        }
      }
    }
    return getGetStudentReservationsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.wellness.proto.gym.EmptyGymRequest,
      edu.eci.arsw.wellness.proto.gym.AvailableSessionsResponse> getGetAvailableSessionsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAvailableSessions",
      requestType = edu.eci.arsw.wellness.proto.gym.EmptyGymRequest.class,
      responseType = edu.eci.arsw.wellness.proto.gym.AvailableSessionsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.wellness.proto.gym.EmptyGymRequest,
      edu.eci.arsw.wellness.proto.gym.AvailableSessionsResponse> getGetAvailableSessionsMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.wellness.proto.gym.EmptyGymRequest, edu.eci.arsw.wellness.proto.gym.AvailableSessionsResponse> getGetAvailableSessionsMethod;
    if ((getGetAvailableSessionsMethod = GymServiceGrpc.getGetAvailableSessionsMethod) == null) {
      synchronized (GymServiceGrpc.class) {
        if ((getGetAvailableSessionsMethod = GymServiceGrpc.getGetAvailableSessionsMethod) == null) {
          GymServiceGrpc.getGetAvailableSessionsMethod = getGetAvailableSessionsMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.wellness.proto.gym.EmptyGymRequest, edu.eci.arsw.wellness.proto.gym.AvailableSessionsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAvailableSessions"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.wellness.proto.gym.EmptyGymRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.wellness.proto.gym.AvailableSessionsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GymServiceMethodDescriptorSupplier("GetAvailableSessions"))
              .build();
        }
      }
    }
    return getGetAvailableSessionsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GymServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GymServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GymServiceStub>() {
        @java.lang.Override
        public GymServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GymServiceStub(channel, callOptions);
        }
      };
    return GymServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GymServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GymServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GymServiceBlockingStub>() {
        @java.lang.Override
        public GymServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GymServiceBlockingStub(channel, callOptions);
        }
      };
    return GymServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GymServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GymServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GymServiceFutureStub>() {
        @java.lang.Override
        public GymServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GymServiceFutureStub(channel, callOptions);
        }
      };
    return GymServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void reserveGymSession(edu.eci.arsw.wellness.proto.gym.GymReservationRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.proto.gym.GymReservationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReserveGymSessionMethod(), responseObserver);
    }

    /**
     */
    default void cancelGymReservation(edu.eci.arsw.wellness.proto.gym.CancelGymRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.proto.gym.CancelGymResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCancelGymReservationMethod(), responseObserver);
    }

    /**
     */
    default void getStudentReservations(edu.eci.arsw.wellness.proto.gym.StudentGymRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.proto.gym.GymReservationList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetStudentReservationsMethod(), responseObserver);
    }

    /**
     */
    default void getAvailableSessions(edu.eci.arsw.wellness.proto.gym.EmptyGymRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.proto.gym.AvailableSessionsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAvailableSessionsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service GymService.
   */
  public static abstract class GymServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return GymServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service GymService.
   */
  public static final class GymServiceStub
      extends io.grpc.stub.AbstractAsyncStub<GymServiceStub> {
    private GymServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GymServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GymServiceStub(channel, callOptions);
    }

    /**
     */
    public void reserveGymSession(edu.eci.arsw.wellness.proto.gym.GymReservationRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.proto.gym.GymReservationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReserveGymSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cancelGymReservation(edu.eci.arsw.wellness.proto.gym.CancelGymRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.proto.gym.CancelGymResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCancelGymReservationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getStudentReservations(edu.eci.arsw.wellness.proto.gym.StudentGymRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.proto.gym.GymReservationList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetStudentReservationsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAvailableSessions(edu.eci.arsw.wellness.proto.gym.EmptyGymRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.proto.gym.AvailableSessionsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAvailableSessionsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service GymService.
   */
  public static final class GymServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<GymServiceBlockingStub> {
    private GymServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GymServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GymServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public edu.eci.arsw.wellness.proto.gym.GymReservationResponse reserveGymSession(edu.eci.arsw.wellness.proto.gym.GymReservationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReserveGymSessionMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.wellness.proto.gym.CancelGymResponse cancelGymReservation(edu.eci.arsw.wellness.proto.gym.CancelGymRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCancelGymReservationMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.wellness.proto.gym.GymReservationList getStudentReservations(edu.eci.arsw.wellness.proto.gym.StudentGymRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetStudentReservationsMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.wellness.proto.gym.AvailableSessionsResponse getAvailableSessions(edu.eci.arsw.wellness.proto.gym.EmptyGymRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAvailableSessionsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service GymService.
   */
  public static final class GymServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<GymServiceFutureStub> {
    private GymServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GymServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GymServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.wellness.proto.gym.GymReservationResponse> reserveGymSession(
        edu.eci.arsw.wellness.proto.gym.GymReservationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReserveGymSessionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.wellness.proto.gym.CancelGymResponse> cancelGymReservation(
        edu.eci.arsw.wellness.proto.gym.CancelGymRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCancelGymReservationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.wellness.proto.gym.GymReservationList> getStudentReservations(
        edu.eci.arsw.wellness.proto.gym.StudentGymRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetStudentReservationsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.wellness.proto.gym.AvailableSessionsResponse> getAvailableSessions(
        edu.eci.arsw.wellness.proto.gym.EmptyGymRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAvailableSessionsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RESERVE_GYM_SESSION = 0;
  private static final int METHODID_CANCEL_GYM_RESERVATION = 1;
  private static final int METHODID_GET_STUDENT_RESERVATIONS = 2;
  private static final int METHODID_GET_AVAILABLE_SESSIONS = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RESERVE_GYM_SESSION:
          serviceImpl.reserveGymSession((edu.eci.arsw.wellness.proto.gym.GymReservationRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.proto.gym.GymReservationResponse>) responseObserver);
          break;
        case METHODID_CANCEL_GYM_RESERVATION:
          serviceImpl.cancelGymReservation((edu.eci.arsw.wellness.proto.gym.CancelGymRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.proto.gym.CancelGymResponse>) responseObserver);
          break;
        case METHODID_GET_STUDENT_RESERVATIONS:
          serviceImpl.getStudentReservations((edu.eci.arsw.wellness.proto.gym.StudentGymRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.proto.gym.GymReservationList>) responseObserver);
          break;
        case METHODID_GET_AVAILABLE_SESSIONS:
          serviceImpl.getAvailableSessions((edu.eci.arsw.wellness.proto.gym.EmptyGymRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.proto.gym.AvailableSessionsResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getReserveGymSessionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.wellness.proto.gym.GymReservationRequest,
              edu.eci.arsw.wellness.proto.gym.GymReservationResponse>(
                service, METHODID_RESERVE_GYM_SESSION)))
        .addMethod(
          getCancelGymReservationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.wellness.proto.gym.CancelGymRequest,
              edu.eci.arsw.wellness.proto.gym.CancelGymResponse>(
                service, METHODID_CANCEL_GYM_RESERVATION)))
        .addMethod(
          getGetStudentReservationsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.wellness.proto.gym.StudentGymRequest,
              edu.eci.arsw.wellness.proto.gym.GymReservationList>(
                service, METHODID_GET_STUDENT_RESERVATIONS)))
        .addMethod(
          getGetAvailableSessionsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.wellness.proto.gym.EmptyGymRequest,
              edu.eci.arsw.wellness.proto.gym.AvailableSessionsResponse>(
                service, METHODID_GET_AVAILABLE_SESSIONS)))
        .build();
  }

  private static abstract class GymServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GymServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.eci.arsw.wellness.proto.gym.GymProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GymService");
    }
  }

  private static final class GymServiceFileDescriptorSupplier
      extends GymServiceBaseDescriptorSupplier {
    GymServiceFileDescriptorSupplier() {}
  }

  private static final class GymServiceMethodDescriptorSupplier
      extends GymServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    GymServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (GymServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GymServiceFileDescriptorSupplier())
              .addMethod(getReserveGymSessionMethod())
              .addMethod(getCancelGymReservationMethod())
              .addMethod(getGetStudentReservationsMethod())
              .addMethod(getGetAvailableSessionsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
