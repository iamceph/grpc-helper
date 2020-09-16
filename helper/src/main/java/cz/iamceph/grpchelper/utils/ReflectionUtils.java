package cz.iamceph.grpchelper.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 * @created 16/09/2020 - 13:23
 */
@UtilityClass
public class ReflectionUtils {

    public List<Method> getMethodDescriptorMethods(Class<?> clazz, Class<?> resultClass) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.getReturnType().isAssignableFrom(resultClass))
                .collect(Collectors.toList());
    }
}
