package cz.iamceph.grpchelper.wrapper.stub;

import java.util.concurrent.Executor;

import cz.iamceph.grpchelper.wrapper.channel.ChannelWrapper;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.AbstractAsyncStub;
import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 16/09/2020 - 13:17
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
abstract class StreamStubInitializer<M, R> {
    protected final ChannelWrapper channel;
    protected final Class<?> clazz;
    protected final MethodDescriptor<M, R> methodDescriptor;
    protected final Executor executor;

    protected AbstractAsyncStub<?> stub;
    protected StreamObserver<R> observer;

    void init() throws Exception {
        final var futureStubMethod = clazz.getDeclaredMethod("newStub", Channel.class);
        stub = (AbstractAsyncStub<?>) futureStubMethod.invoke(clazz, channel.getChannel());
    }
}
