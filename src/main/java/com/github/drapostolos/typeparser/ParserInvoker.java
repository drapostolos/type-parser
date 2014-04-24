package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.TypeParserUtility.makeParseErrorMsg;

import java.lang.reflect.Type;

class ParserInvoker extends ParseTemplate<Object> {

    private final TypeParser parser;
    private final String preprocessedInput;

    public ParserInvoker(TypeParser parser, Type targetType, String preprocessedInput) {
        super(parser.parsers, targetType);
        this.preprocessedInput = preprocessedInput;
        this.parser = parser;
    }

    @Override
    Object actionWhenTargetTypeHasNormalTypeParser() {
        if (preprocessedInput == null) {
            return null;
        }
        return invokeTypeParser(typeParsers.parsers.get(targetType));
    }

    @Override
    Object actionWhenTaretTypeIsGeneric(Class<?> cls) {
        Parser<?> tp = typeParsers.assignableParsers.get(cls);
        return invokeTypeParser(tp);
    }

    @Override
    Object actionWhenTargetTypeIsAssignalbleTo(Class<?> superClass) {
        Parser<?> tp = typeParsers.assignableParsers.get(superClass);
        return invokeTypeParser(tp);
    }

    @Override
    Object actionWhenTargetTypeIsArrayClass() {
        Parser<?> tp = typeParsers.parsers.get(Parsers.ANY_ARRAY);
        return invokeTypeParser(tp);
    }

    @Override
    Object actionWhenTargetTypeHasStaticFactoryMethod() {
        if (preprocessedInput == null) {
            return null;
        }
        Parser<?> tp = typeParsers.parsers.get(Parsers.ANY_CLASS_WITH_STATIC_VALUEOF_METHOD);
        return invokeTypeParser(tp);
    }

    @Override
    Object actionWhenTargetTypeIsGenericArrayType() {
        Parser<?> tp = typeParsers.parsers.get(Parsers.ANY_ARRAY);
        return invokeTypeParser(tp);
    }

    /*
     * If execution reaches here, it means there is no TypeParser for
     * the given targetType. What remains is to make a descriptive error
     * message and throw exception.
     */
    @Override
    Object lastAction() {
        throw new NoSuchRegisteredTypeParserException(preprocessedInput, targetType);
    }

    private Object invokeTypeParser(Parser<?> typeParser) {
        try {
            ParserHelper parseHelper = new ParserHelper(parser, targetType);
            return typeParser.parse(preprocessedInput, parseHelper);
        } catch (NumberFormatException e) {
            String message = String.format("Number format exception %s.", e.getMessage());
            message = makeParseErrorMsg(preprocessedInput, targetType, message);
            throw new IllegalArgumentException(message, e);
        } catch (Throwable e) {
            String message = makeParseErrorMsg(preprocessedInput, targetType, e.getMessage());
            throw new IllegalArgumentException(message, e);
        }
    }
}
