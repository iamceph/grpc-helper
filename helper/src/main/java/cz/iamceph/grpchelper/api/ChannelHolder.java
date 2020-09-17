package cz.iamceph.grpchelper.api;

import java.util.concurrent.Executor;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import lombok.Data;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 16/09/2020 - 9:35
 */
@Data
public abstract class ChannelHolder {
    protected ChannelConfig config;
    protected ManagedChannel channel;

    public abstract void renewChannel(Executor executor);

    /**
     *
     * @param executor
     * @return true if we created new channel
     */
    public boolean checkIntegrity(Executor executor) {
        if (channel == null
                || channel.isShutdown()
                || channel.isTerminated()) {
            renewChannel(executor);
            return true;
        }
        return false;
    }

    public boolean isChannelError() {
        return isChannelError(getChannel().getState(false));
    }

    /**
     * Checks the channel state, if the channel is good to operate
     *
     * @param connectivityState from the channel
     * @return true if channel is ok :)
     */
    public boolean isChannelError(ConnectivityState connectivityState) {
        switch (connectivityState) {
            case CONNECTING:
            case TRANSIENT_FAILURE:
            case SHUTDOWN:
                return true;
            default:
                return false;
        }
    }
}
