package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.TypeParserUtility.STATIC_FACTORY_METHOD_NAME;
import static com.github.drapostolos.typeparser.TypeParserUtility.getMethodNamedValueOf;
import static com.github.drapostolos.typeparser.TypeParserUtility.makeParseErrorMsg;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class StaticFactoryMethodTypeParser implements TypeParser<Object>{
    private static final Object STATIC_METHOD = null;

    public Object parse(String input, TypeParserHelper helper) {
        Class<?> targetType = (Class<?>) helper.getTargetType();
        Method method = getMethodNamedValueOf(targetType);
        try {
            if(targetType.isEnum()){
                input = input.trim();
            }
            method.setAccessible(true);
            return method.invoke(STATIC_METHOD, input);
        } catch (InvocationTargetException e) {
            // filter out the InvocationTargetException stacktrace/message.
            throw new IllegalArgumentException(makeErrorMsg(input, targetType), e.getCause());
        } catch (Throwable t) {
            throw new IllegalArgumentException(makeErrorMsg(input, targetType), t);
        }
    }

    private String makeErrorMsg(String input, Class<?> targetType) {
        String methodSignature = String.format("%s.%s('%s')", targetType.getName(), STATIC_FACTORY_METHOD_NAME, input);
        String message = " Exception thrown in static factory method '%s'. "
                + "See underlying exception for additional information.";
        message = String.format(message, methodSignature);
        return makeParseErrorMsg(input, targetType, message);
    }
}
