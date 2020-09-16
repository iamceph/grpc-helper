package cz.iamceph.grpchelper.builder;

import java.util.List;
import java.util.concurrent.Executor;

import cz.iamceph.grpchelper.wrapper.channel.ChannelWrapper;
import cz.iamceph.grpchelper.wrapper.config.ChannelConfig;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannelBuilder;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 16/09/2020 - 9:38
 */
public interface ChannelBuilder {
    ChannelWrapper getFor(ManagedChannelBuilder<?> channelBuilder);

    ChannelWrapper getFor(ManagedChannelBuilder<?> channelBuilder, Executor executor);

    ChannelWrapper getChannel(String address, Executor executor, List<ClientInterceptor> interceptors);

    ChannelWrapper getChannel(ChannelConfig config, Executor executor);

    Executor getDefaultExecutor();

    void renewChannel(ChannelWrapper channelWrapper, Executor executor);
}
