package cz.iamceph.grpchelper.api;

import java.util.List;
import java.util.concurrent.Executor;

import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannelBuilder;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 16/09/2020 - 9:38
 */
public interface ChannelBuilder {
    ChannelHolder getFor(ManagedChannelBuilder<?> channelBuilder);

    ChannelHolder getFor(ManagedChannelBuilder<?> channelBuilder, Executor executor);

    ChannelHolder getChannel(String address, Executor executor, List<ClientInterceptor> interceptors);

    ChannelHolder getChannel(ChannelConfig config, Executor executor);

    Executor getDefaultExecutor();

    void renewChannel(ChannelHolder channelHolder, Executor executor);
}
