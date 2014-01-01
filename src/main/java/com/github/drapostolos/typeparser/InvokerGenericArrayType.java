package com.github.drapostolos.typeparser;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.util.Map;

class InvokerGenericArrayType extends Invoker{
    private static final int ZERO_LENGTH = 0;
    private final StringToTypeParser stringParser;
    private final GenericArrayType type;

    InvokerGenericArrayType(GenericArrayType type, 
            Map<Class<?>, TypeParser> typeParsers, 
            StringToTypeParser parser) {
        super(extractTargetType(type), typeParsers);
        this.type = type;
        this.stringParser = parser;
    }

    private static Class<?> extractTargetType(GenericArrayType genericType) {
        Class<?> type = (Class<?>) genericType.getGenericComponentType();
        return Array.newInstance(type, ZERO_LENGTH).getClass();
    }

    @Override
    Object invokeTypeParser(String input) {
        ArrayTypeParser typeParser;
        if(containsTypeParserForTargetType()){
            typeParser = getTypeParser(ArrayTypeParser.class);
        } else {
            typeParser = getTypeParser(Array.class, ArrayTypeParser.class);
        }
        Class<?> componentType = (Class<?>) type.getGenericComponentType();
        return typeParser.parse(input, componentType, new Helper(stringParser));
    }
}
