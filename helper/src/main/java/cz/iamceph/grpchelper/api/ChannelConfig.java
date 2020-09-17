package cz.iamceph.grpchelper.api;

import java.util.LinkedList;
import java.util.List;

import io.grpc.ClientInterceptor;
import lombok.Data;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 16/09/2020 - 9:35
 */
@Data
public class ChannelConfig {
    private final String address;
    private final String authToken;
    private List<ClientInterceptor> interceptors = new LinkedList<>();
}
