package com.github.drapostolos.typeparser;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

abstract class Invoker {
    private static final Object NULL_TYPE = null;
    protected final Class<?> targetType;
    private final Map<Class<?>, TypeParser> typeParsers;

    static Invoker forArrayClass(Class<?> type, Map<Class<?>, TypeParser> typeParsers, StringToTypeParser stringParser) {
        return new InvokerArrayClassType(type, typeParsers, stringParser);
    }

    static Invoker forGenericArrayType(GenericArrayType type, Map<Class<?>, TypeParser> typeParsers, StringToTypeParser stringParser) {
        return new InvokerGenericArrayType(type, typeParsers, stringParser);
    }

    static Invoker forParameterizedType(ParameterizedType type, Map<Class<?>, TypeParser> typeParsers, StringToTypeParser stringParser) {
        return new InvokerParameterizedType(type, typeParsers, stringParser);
    }

    static Invoker forClass(Class<?> type, Map<Class<?>, TypeParser> typeParsers) {
        return new InvokerClassType(type, typeParsers);
    }

    protected Invoker(Class<?> targetType, Map<Class<?>, TypeParser> typeParsers) {
        this.targetType = targetType;
        this.typeParsers = typeParsers;
    }

    Object invokeTypeParserTemplate(String input) {
        if (input.trim().equalsIgnoreCase("null")) {
            if (targetType.isPrimitive()) {
                String message = "'%s' primitive can not be set to null.";
                throw new IllegalArgumentException(String.format(message, targetType.getName()));
            }
            return NULL_TYPE; 
        }
        return invokeTypeParser(input);
    }

    abstract Object invokeTypeParser(String input);
    
    protected boolean containsTypeParserForTargetType() {
        return typeParsers.containsKey(targetType);
    }

    protected <T extends TypeParser> T getTypeParser(Class<T> typeParser) {
        return getTypeParser(targetType, typeParser);
    }
    protected <T extends TypeParser> T getTypeParser(Class<?> targetType, Class<T> typeParser) {
        // check type and throw proper exception when needed.
        return (T) typeParsers.get(targetType);
    }


}