package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;
import java.util.Collection;

final class ParserInvoker {

    private final String preprocessedInput;
    private final Parsers parsers;
    private final ParserHelper helper;
    private final Type targetType;
    private final NullStringStrategy nullStringStrategy;

    ParserInvoker(TypeParser typeParser, Type targetType, String preprocessedInput) {
        this.targetType = targetType;
        this.preprocessedInput = preprocessedInput;
        this.parsers = typeParser.parsers;
        this.nullStringStrategy = typeParser.nullStringStrategy;
        this.helper = new ParserHelper(targetType, typeParser);
    }

    Object invoke() {
        Object result;

        result = invokeDynamicParsersForContainerTypes();
        if (result != DynamicParser.TRY_NEXT) {
            return result;
        }

        result = invokeDynamicParsersRegisteredByClient();
        if (result != DynamicParser.TRY_NEXT) {
            return result;
        }

        if (parsers.containsStaticParser(targetType)) {
            return invokeStaticParser();
        }

        result = invokeDynamicParsersForRegularTypes();
        if (result != DynamicParser.TRY_NEXT) {
            return result;
        }

        throw new NoSuchRegisteredParserException("There is no registered 'Parser' for that type.");
    }

    private Object invokeDynamicParsersForContainerTypes() {
        return invokeDynamicParsers(DefaultDynamicParsers.forContainerTypes());
    }

    private Object invokeDynamicParsersRegisteredByClient() {
        return invokeDynamicParsers(parsers.dynamicParsers());
    }

    private Object invokeDynamicParsersForRegularTypes() {
        if (isNullString()) {
            return null;
        }
        return invokeDynamicParsers(DefaultDynamicParsers.forRegularTypes());
    }

    private Object invokeDynamicParsers(Collection<? extends DynamicParser> dynamicParsers) {
        for (DynamicParser dynamicParser : dynamicParsers) {
            Object result = dynamicParser.parse(preprocessedInput, helper);
            if (result != DynamicParser.TRY_NEXT) {
                return result;
            }
        }
        return DynamicParser.TRY_NEXT;
    }

    private Object invokeStaticParser() {
        if (isNullString()) {
            if (isPrimitive(targetType)) {
                throw new UnsupportedOperationException("Primitive can not be set to null");
            }
            return null;
        }
        Parser<?> parser = parsers.getStaticParser(targetType);
        return parser.parse(preprocessedInput, helper);
    }

    private boolean isNullString() {
        NullStringStrategyHelper helper = new NullStringStrategyHelper(targetType);
        return nullStringStrategy.isNullString(preprocessedInput, helper);
    }

    private boolean isPrimitive(Type targetType) {
        if (targetType instanceof Class) {
            Class<?> c = (Class<?>) targetType;
            return c.isPrimitive();
        }
        return false;
    }
}
