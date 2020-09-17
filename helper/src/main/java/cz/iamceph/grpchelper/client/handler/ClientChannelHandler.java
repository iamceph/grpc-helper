package cz.iamceph.grpchelper.client.handler;

import java.util.concurrent.Executor;

import cz.iamceph.grpchelper.client.stub.BiDiStreamStubWrapper;
import cz.iamceph.grpchelper.client.stub.ClientStreamStubWrapper;
import cz.iamceph.grpchelper.client.stub.FutureStubWrapper;
import cz.iamceph.grpchelper.client.stub.ServerStreamStubWrapper;
import cz.iamceph.grpchelper.utils.ReflectionUtils;
import cz.iamceph.grpchelper.api.ChannelHolder;
import io.grpc.MethodDescriptor;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 16/09/2020 - 9:43
 */
public abstract class ClientChannelHandler<M, R> {
    protected final Class<?> serviceClazz;
    protected final ChannelHolder channel;
    protected final Executor executor;

    protected BiDiStreamStubWrapper<M, R> bidiStreamStubWrapper;
    protected ClientStreamStubWrapper<M, R> clientStreamStubWrapper;
    protected ServerStreamStubWrapper<M, R> serverStreamStubWrapper;
    protected FutureStubWrapper<M, R> futureStubWrapper;

    public ClientChannelHandler(Class<?> serviceClazz, ChannelHolder channel, Executor executor) {
        this.serviceClazz = serviceClazz;
        this.channel = channel;
        this.executor = executor;

        init();
    }

    public BiDiStreamStubWrapper<M, R> biDiStream() {
        if (bidiStreamStubWrapper == null) {
            throw new UnsupportedOperationException("BiDiStreamStubWrapper does not exists, you are probably missing it in .proto file!");
        }
        return bidiStreamStubWrapper;
    }

    public ClientStreamStubWrapper<M, R> clientStream() {
        if (clientStreamStubWrapper == null) {
            throw new UnsupportedOperationException("ClientStreamStubWrapper does not exists, you are probably missing it in .proto file!");
        }
        return clientStreamStubWrapper;
    }

    public ServerStreamStubWrapper<M, R> serverStream() {
        if (serverStreamStubWrapper == null) {
            throw new UnsupportedOperationException("ServerStreamStubWrapper does not exists, you are probably missing it in .proto file!");
        }
        return serverStreamStubWrapper;
    }

    public FutureStubWrapper<M, R> futureNonStream() {
        if (futureStubWrapper == null) {
            throw new UnsupportedOperationException("FutureStubWrapper does not exists, you are probably missing it in .proto file!");
        }
        return futureStubWrapper;
    }

    @SuppressWarnings("unchecked")
    private void init() {
        final var methodDescriptors = ReflectionUtils.getMethodDescriptorMethods(serviceClazz, MethodDescriptor.class);

        if (methodDescriptors.isEmpty()) {
            throw new RuntimeException("MethodDescriptors are empty, are you sure that class " + serviceClazz.getSimpleName() + " is the class you need?");
        }

        methodDescriptors.forEach(method -> {
            try {
                final var methodDescriptor = (MethodDescriptor<M, R>) method.invoke(serviceClazz);
                switch (methodDescriptor.getType()) {
                    case UNARY:
                        futureStubWrapper = FutureStubWrapper.create(channel, serviceClazz, methodDescriptor, executor);
                        break;
                    case CLIENT_STREAMING:
                        clientStreamStubWrapper = ClientStreamStubWrapper.create(channel, serviceClazz, methodDescriptor, executor);
                        break;
                    case BIDI_STREAMING:
                        bidiStreamStubWrapper = BiDiStreamStubWrapper.create(channel, serviceClazz, methodDescriptor, executor);
                        break;
                    case SERVER_STREAMING:
                        serverStreamStubWrapper = ServerStreamStubWrapper.create(channel, serviceClazz, methodDescriptor, executor);
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
