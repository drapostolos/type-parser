package com.github.drapostolos.typeparser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class FactoryMethodInvoker {
    static final String FACTORY_METHOD_NAME = "valueOf";
    private static final Object STATIC_METHOD = null;
    private final Class<?> targetType;

    FactoryMethodInvoker(Class<?> cls) {
        this.targetType = cls;
    }

    boolean containsFactoryMethod(){
        try {
            Method method = getFactoryMethod(targetType);
            if (!Modifier.isStatic(method.getModifiers())) {
                return false;
            }
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private Method getFactoryMethod(Class<?> targetType){
        try {
            Method m = targetType.getDeclaredMethod(FACTORY_METHOD_NAME, String.class);
            m.setAccessible(true);
            return m;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Object invokeFactoryMethod(String input) {
        Method method = getFactoryMethod(targetType);
        try {
            if(targetType.isEnum()){
                input = input.trim();
            }
            return method.invoke(STATIC_METHOD, input);
        } catch (InvocationTargetException e) {
            // filter out the InvocationTargetException stacktrace/message.
            throw new IllegalArgumentException(makeErrorMsg(input, targetType), e.getCause());
        } catch (Throwable t) {
            throw new IllegalArgumentException(makeErrorMsg(input, targetType), t);
        }
    }

    private String makeErrorMsg(String input, Class<?> targetType) {
        String methodSignature = String.format("%s.%s('%s')", targetType.getName(), FACTORY_METHOD_NAME, input);
        String message = " Exception thrown in static factory method '%s'. "
                + "See underlying exception for additional information.";
        message = String.format(message, methodSignature);
        return TypeParserUtility.makeParseErrorMsg(input, message, targetType);
    }


}
