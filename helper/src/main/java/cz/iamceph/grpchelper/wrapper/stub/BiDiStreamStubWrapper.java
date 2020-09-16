package cz.iamceph.grpchelper.wrapper.stub;

import java.util.concurrent.Executor;

import cz.iamceph.grpchelper.wrapper.channel.ChannelWrapper;
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

    private BiDiStreamStubWrapper(ChannelWrapper channel, Class<?> clazz,
                                  MethodDescriptor<M, R> methodDescriptor, Executor executor) {
        super(channel, clazz, methodDescriptor, executor);
    }

    public static <M, R> BiDiStreamStubWrapper<M, R> create(ChannelWrapper channel, Class<?> clazz,
                                                            MethodDescriptor<M, R> methodDescriptor, Executor executor) throws Exception {
        final var toReturn = new BiDiStreamStubWrapper<>(channel, clazz, methodDescriptor, executor);
        toReturn.init();
        return toReturn;
    }

    public BiDiStreamStubWrapper<M, R> withHandler(StreamObserver<R> observer) {
        this.observer = observer;
        return this;
    }

    public StreamObserver<M> initClientStream() {
        if (observer == null) {
            throw new UnsupportedOperationException("ResponseHandler is null!");
        }
        return initBiStream(observer);
    }

    public StreamObserver<M> initBiStream(StreamObserver<R> observer) {
        if (channel.checkIntegrity(executor)) {
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ClientCalls.asyncBidiStreamingCall(
                stub.getChannel().newCall(methodDescriptor, stub.getCallOptions()), observer);
    }
}
