package io.github.kttobug.query.util;

import io.github.kttobug.query.SerializableFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class LambdaUtils {

    public static <T> String resolveFieldName(SerializableFunction<T, ?> func) {
        try {
            Method method = func.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) method.invoke(func);
            String methodName = lambda.getImplMethodName();
            return toPropertyName(methodName);
        } catch (Exception e) {
            throw new RuntimeException("无法从 Lambda 获取字段名", e);
        }
    }

    private static String toPropertyName(String methodName) {
        if (methodName.startsWith("get")) {
            return decapitate(methodName.substring(3));
        } else if (methodName.startsWith("is")) {
            return decapitate(methodName.substring(2));
        }
        return methodName;
    }

    private static String decapitate(String str) {
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
}
