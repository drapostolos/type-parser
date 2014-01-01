package com.github.drapostolos.typeparser;

import java.lang.reflect.Array;
import java.util.Map;

class InvokerArrayClassType extends Invoker{
    private final StringToTypeParser stringParser;

    InvokerArrayClassType(Class<?> targetType, Map<Class<?>, TypeParser> typeParsers, StringToTypeParser parser) {
        super(targetType, typeParsers);
        this.stringParser = parser;
    }

    @Override
    Object invokeTypeParser(String input) {
//        GenericArrayTypeParser typeParser = getTypeParser(GenericArrayTypeParser.class);
        
        ArrayTypeParser typeParser;
        if(containsTypeParserForTargetType()){
            typeParser = getTypeParser(ArrayTypeParser.class);
        } else {
            typeParser = getTypeParser(Array.class, ArrayTypeParser.class);
        }
        
        Class<?> componentType = (Class<?>) targetType.getComponentType();
        return typeParser.parse(input, componentType, new Helper(stringParser));
    }

}
