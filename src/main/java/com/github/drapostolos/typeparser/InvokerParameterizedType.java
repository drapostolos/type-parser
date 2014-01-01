package com.github.drapostolos.typeparser;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

class InvokerParameterizedType extends Invoker{
    private final ParameterizedType type;
    private final StringToTypeParser stringParser;


    InvokerParameterizedType(ParameterizedType type, Map<Class<?>, TypeParser> typeParsers, StringToTypeParser parser) {
        super((Class<?>) type.getRawType(), typeParsers);
        this.type = type;
        this.stringParser = parser;

    }

    @Override
    Object invokeTypeParser(String input) {
        ParameterizedTypeParser<?> typeParser = getTypeParser(ParameterizedTypeParser.class);
        return typeParser.parse(input, new ParameterizedTypeHelper(stringParser, type));
    }

}
