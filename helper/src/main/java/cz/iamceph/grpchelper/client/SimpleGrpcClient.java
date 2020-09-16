package cz.iamceph.grpchelper.client;

import java.util.LinkedList;
import java.util.List;

import cz.iamceph.grpchelper.handler.BaseChannelHandler;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 15/09/2020 - 20:36
 */
public class SimpleGrpcClient<M, R> {
    private final List<BaseChannelHandler<M, R>> handlers = new LinkedList<>();

    public void registerHandler(BaseChannelHandler<M, R> handler) {
        handlers.add(handler);
    }

    public void removeHandler(BaseChannelHandler<M, R> handler) {
        handlers.remove(handler);
    }

    public List<BaseChannelHandler<M, R>> getHandlers() {
        return handlers;
    }
}
