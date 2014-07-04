package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;
import java.util.Collection;

final class ParserInvoker {

    private final String preprocessedInput;
    private final Parsers parsers;
    private final ParserHelper helper;
    private final Type targetType;

    ParserInvoker(TypeParser typeParser, Type targetType, String preprocessedInput) {
        this.targetType = targetType;
        this.preprocessedInput = preprocessedInput;
        this.parsers = typeParser.parsers;
        this.helper = new ParserHelper(typeParser, targetType);
    }

    Object invoke() {
        Object result;

        result = invokeDynamicParsers(parsers.dynamicParsers());
        if (result != DynamicParser.TRY_NEXT) {
            return result;
        }

        if (parsers.containsStaticParser(targetType)) {
            if (preprocessedInput == null) {
                return null;
            }
            return invokeStaticParser();
        }

        result = invokeDynamicParsers(DefaultDynamicParsers.forRegularTypes());
        if (result != DynamicParser.TRY_NEXT) {
            return result;
        }

        throw new NoSuchRegisteredParserException("There is no registered 'Parser' for that type.");
    }

    private Object invokeDynamicParsers(Collection<? extends DynamicParser> dynamicParsers) {
        for (DynamicParser dynamicParser : dynamicParsers) {
            Object result = invokeDynamicParser(dynamicParser);
            if (result != DynamicParser.TRY_NEXT) {
                return result;
            }
        }
        return DynamicParser.TRY_NEXT;
    }

    private Object invokeDynamicParser(DynamicParser dynamicParser) {
        return dynamicParser.parse(preprocessedInput, helper);
    }

    private Object invokeStaticParser() {
        Parser<?> parser = parsers.getStaticParser(targetType);
        return parser.parse(preprocessedInput, helper);
    }
}
