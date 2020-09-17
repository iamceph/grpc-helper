package cz.iamceph.grpchelper.network.methods;

import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

import io.grpc.MethodDescriptor;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 17/09/2020 - 9:57
 */
@RequiredArgsConstructor
public class BiDiMethodHandler<M, R> implements ServerCalls.BidiStreamingMethod<M, R> {
    private final MethodDescriptor<M, R> methodDescriptor;

    @Override
    public StreamObserver<M> invoke(StreamObserver<R> responseObserver) {
        return asyncUnimplementedStreamingCall(methodDescriptor, responseObserver);
    }
}
