package cz.iamceph.grpchelper.client.stub;

import java.util.concurrent.Executor;

import cz.iamceph.grpchelper.api.ChannelHolder;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 15/09/2020 - 20:42
 * <p>
 * BiDirectional
 */
public class BiDiStreamStubWrapper<M, R> extends StreamStubInitializer<M, R> {
    private StreamObserver<M> clientObserver;

    private BiDiStreamStubWrapper(ChannelHolder channel, Class<?> clazz,
                                  MethodDescriptor<M, R> methodDescriptor, Executor executor) {
        super(channel, clazz, methodDescriptor, executor);
    }

    public static <M, R> BiDiStreamStubWrapper<M, R> create(ChannelHolder channel, Class<?> clazz,
                                                            MethodDescriptor<M, R> methodDescriptor, Executor executor) throws Exception {
        final var toReturn = new BiDiStreamStubWrapper<>(channel, clazz, methodDescriptor, executor);
        toReturn.init();
        return toReturn;
    }

    public BiDiStreamStubWrapper<M, R> withHandler(StreamObserver<R> observer) {
        this.observer = observer;
        return this;
    }

    public BiDiStreamStubWrapper<M, R> initClientStream() {
        if (observer == null) {
            throw new UnsupportedOperationException("ResponseHandler is null!");
        }
        return initBiStream(observer);
    }

    public BiDiStreamStubWrapper<M, R> initBiStream(StreamObserver<R> observer) {
        if (channel.checkIntegrity(executor)) {
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        clientObserver = ClientCalls.asyncBidiStreamingCall(
                stub.getChannel().newCall(methodDescriptor, stub.getCallOptions()), observer);

        return this;
    }

    public void send(M message) {
        if (clientObserver == null) {
            throw new UnsupportedOperationException("ClientObserver is null!");
        }

        clientObserver.onNext(message);
    }

    public void error(Throwable throwable) {
        if (clientObserver == null) {
            throw new UnsupportedOperationException("ClientObserver is null!");
        }

        clientObserver.onError(throwable);
    }
}
