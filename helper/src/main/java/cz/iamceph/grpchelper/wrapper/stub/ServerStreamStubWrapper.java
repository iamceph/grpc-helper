package cz.iamceph.grpchelper.wrapper.stub;

import java.util.concurrent.Executor;

import cz.iamceph.grpchelper.wrapper.channel.ChannelWrapper;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 16/09/2020 - 9:45
 */
public class ServerStreamStubWrapper<M, R> extends StreamStubInitializer<M, R> {

    private ServerStreamStubWrapper(ChannelWrapper channel, Class<?> clazz,
                                    MethodDescriptor<M, R> methodDescriptor, Executor executor) {
        super(channel, clazz, methodDescriptor, executor);
    }

    public static <M, R> ServerStreamStubWrapper<M, R> create(ChannelWrapper channel, Class<?> clazz,
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
                init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ClientCalls.asyncServerStreamingCall(
                stub.getChannel().newCall(methodDescriptor, stub.getCallOptions()), request, observer);
    }
}
