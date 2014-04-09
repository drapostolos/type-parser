package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.TypeParserUtility.makeParseErrorMsg;

import java.lang.reflect.Type;

class TypeParserInvoker extends ParseTemplate<Object> {
	private final String preprocessedInput;

	public TypeParserInvoker(StringToTypeParser parser, Type targetType, String preprocessedInput) {
		super(parser, targetType);
		this.preprocessedInput = preprocessedInput;
	}

	@Override
	Object actionWhenTargetTypeHasTypeParser() {
		if(preprocessedInput == null) {
			return null;
		}
        return invokeTypeParser(targetType);
	}

	@Override
	Object actionWhenTargetTypeIsAssignableFromLinkedHashSet() {
        return invokeTypeParser(TypeParsers.ANY_SET);
	}

	@Override
	Object actionWhenTargetTypeIsAssignableFromLinkedHashMap() {
        return invokeTypeParser(TypeParsers.ANY_MAP);
	}

	@Override
	Object actionWhenTargetTypeIsAssignableFromArrayList() {
        return invokeTypeParser(TypeParsers.ANY_LIST);
	}

	@Override
	Object actionWhenTargetTypeIsArrayClass() {
        return invokeTypeParser(TypeParsers.ANY_ARRAY);
	}

	@Override
	Object actionWhenTargetTypeHasStaticFactoryMethod() {
		if(preprocessedInput == null) {
			return null;
		}
        return invokeTypeParser(TypeParsers.ANY_CLASS_WITH_STATIC_VALUEOF_METHOD);
	}

	@Override
	Object actionWhenTargetTypeIsGenericArrayType() {
        return invokeTypeParser(TypeParsers.ANY_ARRAY);
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

    private Object invokeTypeParser(Type key) {
        try {
            TypeParser<?> typeParser = parser.typeParsers.get(key);
            TypeParserHelper parseHelper = new TypeParserHelper(parser, targetType);
            return typeParser.parse(preprocessedInput, parseHelper);
        } catch (NumberFormatException e) {
            String message =  String.format("Number format exception %s.", e.getMessage());
            message = makeParseErrorMsg(preprocessedInput, targetType, message);
            throw new IllegalArgumentException(message, e);
        } catch (Throwable e) {
            String message = makeParseErrorMsg(preprocessedInput, targetType, e.getMessage());
            throw new IllegalArgumentException(message, e);
        }
    }


}
