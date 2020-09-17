package cz.iamceph.grpchelper.client.stub;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import cz.iamceph.grpchelper.utils.FutureUtils;
import cz.iamceph.grpchelper.api.ChannelHolder;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.AbstractFutureStub;
import io.grpc.stub.ClientCalls;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 15/09/2020 - 20:32
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FutureStubWrapper<M, R> {
    private final ChannelHolder channel;
    private final Class<?> clazz;
    private final MethodDescriptor<M, R> methodDescriptor;
    private final Executor executor;

    private AbstractFutureStub<?> stub;

    public static <M, R> FutureStubWrapper<M, R> create(ChannelHolder channel, Class<?> clazz,
                                                        MethodDescriptor<M, R> methodDescriptor, Executor executor) throws Exception {
        final var toReturn = new FutureStubWrapper<>(channel, clazz, methodDescriptor, executor);
        toReturn.init();
        return toReturn;
    }

    public CompletableFuture<R> send(M request) {
        if (channel.checkIntegrity(executor)) {
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return FutureUtils.toCompletableFuture(
                ClientCalls.futureUnaryCall(
                        stub.getChannel().newCall(methodDescriptor, stub.getCallOptions()), request));
    }

    private void init() throws Exception {
        final var futureStubMethod = clazz.getDeclaredMethod("newFutureStub", Channel.class);
        stub = (AbstractFutureStub<?>) futureStubMethod.invoke(clazz, channel.getChannel());
    }
}
