package cz.iamceph.grpchelper.client.handler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cz.iamceph.grpchelper.api.ChannelHolder;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 17/09/2020 - 8:46
 */
public class SimpleClientChannelHandler<M, R> extends ClientChannelHandler<M, R> {

    public SimpleClientChannelHandler(Class<?> serviceClazz, ChannelHolder channel) {
        super(serviceClazz, channel, Executors.newFixedThreadPool(4));
    }

    public SimpleClientChannelHandler(Class<?> serviceClazz, ChannelHolder channel, Executor executor) {
        super(serviceClazz, channel, executor);
    }
}
