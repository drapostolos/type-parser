package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.TypeParserUtility.makeParseErrorMsg;

import java.lang.reflect.Type;

class TypeParserInvoker extends ParseTemplate<Object> {

    private final TypeParser parser;
    private final String preprocessedInput;

    public TypeParserInvoker(TypeParser parser, Type targetType, String preprocessedInput) {
        super(parser.typeParsers, targetType);
        this.preprocessedInput = preprocessedInput;
        this.parser = parser;
    }

    @Override
    Object actionWhenTargetTypeHasNormalTypeParser() {
        if (preprocessedInput == null) {
            return null;
        }
        return invokeTypeParser(typeParsers.normalTypeParsers.get(targetType));
    }

    @Override
    Object actionWhenTaretTypeIsGeneric(Class<?> cls) {
        StringToTypeParser<?> tp = typeParsers.assignableTypeParsers.get(cls);
        return invokeTypeParser(tp);
    }

    @Override
    Object actionWhenTargetTypeIsAssignalbleTo(Class<?> superClass) {
        StringToTypeParser<?> tp = typeParsers.assignableTypeParsers.get(superClass);
        return invokeTypeParser(tp);
    }

    @Override
    Object actionWhenTargetTypeIsArrayClass() {
        StringToTypeParser<?> tp = typeParsers.normalTypeParsers.get(TypeParsers.ANY_ARRAY);
        return invokeTypeParser(tp);
    }

    @Override
    Object actionWhenTargetTypeHasStaticFactoryMethod() {
        if (preprocessedInput == null) {
            return null;
        }
        StringToTypeParser<?> tp = typeParsers.normalTypeParsers.get(TypeParsers.ANY_CLASS_WITH_STATIC_VALUEOF_METHOD);
        return invokeTypeParser(tp);
    }

    @Override
    Object actionWhenTargetTypeIsGenericArrayType() {
        StringToTypeParser<?> tp = typeParsers.normalTypeParsers.get(TypeParsers.ANY_ARRAY);
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

    private Object invokeTypeParser(StringToTypeParser<?> typeParser) {
        try {
            StringToTypeParserHelper parseHelper = new StringToTypeParserHelper(parser, targetType);
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
