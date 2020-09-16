package cz.iamceph.grpchelper.wrapper.method;

import io.grpc.MethodDescriptor;
import lombok.experimental.UtilityClass;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 15/09/2020 - 20:25
 */
@UtilityClass
public class MethodDescriptorWrapper {

    @SuppressWarnings("unchecked")
    public <M, R> MethodDescriptor<M, R> getMethod(Class<?> clazz, String methodName) {
        try {
            final var method = clazz.getDeclaredMethod(methodName);
            final var test = method.invoke(clazz);
            return (MethodDescriptor<M, R>) test;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
