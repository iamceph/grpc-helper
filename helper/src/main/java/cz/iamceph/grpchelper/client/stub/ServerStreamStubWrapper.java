package cz.iamceph.grpchelper.client.stub;

import java.util.concurrent.Executor;

import cz.iamceph.grpchelper.api.ChannelHolder;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 16/09/2020 - 9:45
 */
public class ServerStreamStubWrapper<M, R> extends StreamStubInitializer<M, R> {
    private ServerStreamStubWrapper(ChannelHolder channel, Class<?> clazz,
                                    MethodDescriptor<M, R> methodDescriptor, Executor executor) {
        super(channel, clazz, methodDescriptor, executor);
    }

    public static <M, R> ServerStreamStubWrapper<M, R> create(ChannelHolder channel, Class<?> clazz,
                                                              MethodDescriptor<M, R> methodDescriptor, Executor executor) throws Exception {
        final var toReturn = new ServerStreamStubWrapper<>(channel, clazz, methodDescriptor, executor);
        toReturn.init();
        return toReturn;
    }

    public ServerStreamStubWrapper<M, R> withHandler(StreamObserver<R> observer) {
        this.observer = observer;
        return this;
    }

    public void send(M request) {
        if (observer == null) {
            throw new UnsupportedOperationException("ResponseHandler is null!");
        }
        send(request, observer);
    }

    public void send(M request, StreamObserver<R> observer) {
        if (channel.checkIntegrity(executor)) {
            try {
                System.out.println("Initializing new channel!!!!!!!!!!!!!!");
                init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Just sending");
        ClientCalls.asyncServerStreamingCall(
                stub.getChannel().newCall(methodDescriptor, stub.getCallOptions()), request, observer);
    }
}
