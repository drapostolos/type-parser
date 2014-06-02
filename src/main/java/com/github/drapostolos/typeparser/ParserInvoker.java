package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;

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
        if (parsers.containsStaticParser(targetType)) {
            if (preprocessedInput == null) {
                return null;
            }
            return invokeStaticParser();
        }
        for (DynamicParser dynamicParser : parsers.dynamicParsers()) {
            Object result = invokeDynamicParser(dynamicParser, helper);
            if (result != DynamicParser.TRY_NEXT) {
                return result;
            }
        }
        throw new NoSuchRegisteredParserException("There is no registered 'Parser' for that type.");
    }

    private Object invokeStaticParser() {
        Parser<?> parser = parsers.getStaticParser(targetType);
        try {
            return parser.parse(preprocessedInput, helper);
        } catch (Throwable t) {
            throw new ParseException("Parser.parse", parser, t);
        }
    }

    private Object invokeDynamicParser(DynamicParser dynamicParser, ParserHelper helper) {
        try {
            return dynamicParser.parse(preprocessedInput, helper);
        } catch (Throwable t) {
            throw new ParseException("DynamicParser.parse", dynamicParser, t);
        }
    }
}
