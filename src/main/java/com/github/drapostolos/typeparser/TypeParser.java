package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.Util.makeNullArgumentErrorMsg;

import java.lang.reflect.Type;

/**
 * The purpose of this class is to parse a simple string (read from a properties file
 * or system property for example) and convert it to a specific java object/Type.
 * For example converting "1" to an {@code Integer} type, or "1,2,3" to a {@code List<Integer>}
 * type.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide"
 *      target="_blank">User-Guide</a>
 */
public final class TypeParser {

    final Parsers parsers;
    final SplitStrategy splitStrategy;
    final SplitStrategy keyValueSplitStrategy;
    final InputPreprocessor inputPreprocessor;

    /**
     * Constructs a new instance of {@link TypeParserBuilder}.
     * 
     * @return a new instance of {@link TypeParserBuilder}.
     */
    public static TypeParserBuilder newBuilder() {
        return new TypeParserBuilder();
    }

    TypeParser(TypeParserBuilder builder) {
        this.parsers = Parsers.unmodifiableCopy(builder.parsers);
        this.splitStrategy = builder.splitStrategy;
        this.keyValueSplitStrategy = builder.keyValueSplitStrategy;
        this.inputPreprocessor = builder.inputPreprocessor;
    }

    /**
     * Parses the given {@code input} string to the given {@code targetType}.
     * <p/>
     * Example: <br/>
     * <code>
     * TypeParser parser = TypeParser.newBuilder().build();<br/>
     * Integer i = parser.parse("1", Integer.class);
     * </code>
     * 
     * @param input - string value to parse
     * @param targetType - the expected type to convert {@code input} to.
     * @return an instance of {@code targetType} corresponding to the given {@code input}.
     * @throws NullPointerException if any given argument is {@code null}.
     * @throws ParseException if the {@link Parser} associated with the
     *         given {@code targetType} throws exception while parsing the given {@code input}.
     * @throws InputPreprocessorException if the registered {@link InputPreprocessor} throws
     *         exception while preparing the given {@code input} for parsing.
     * @throws IllegalPrimitiveValueException if the registered {@link InputPreprocessor} decides to
     *         return a null object while {@code targetType} is of a primitive type.
     * @throws NoSuchRegisteredParserException if there is no registered {@link Parser} for the
     *         given {@code targetType}.
     */
    public <T> T parse(String input, Class<T> targetType) {
        if (input == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("input"));
        }
        if (targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }

        @SuppressWarnings("unchecked")
        T temp = (T) parseType2(input, targetType);
        return temp;

    }

    /**
     * Parses the given {@code input} string to the given {@code genericType}.
     * <p/>
     * Example: <br/>
     * <code>
     * TypeParser parser = TypeParser.newBuilder().build();<br/>
     * {@code List<Integer>} list = parser.parse("1, 2", new {@code GenericType<List<Integer>>}() {});
     * </code><br/>
     * Note the ending "{}".
     * 
     * @param input - string value to parse.
     * @param genericType - the expected generic type to convert {@code input} to.
     * @return an instance of {@code genericType} corresponding to the given {@code input}.
     * @throws NullPointerException if any given argument is {@code null}.
     * @throws ParseException if the {@link Parser} associated with the
     *         given {@code targetType} throws exception while parsing the given {@code input}.
     * @throws InputPreprocessorException if the registered {@link InputPreprocessor} throws
     *         exception while preparing the given {@code input} for parsing.
     * @throws IllegalPrimitiveValueException if the registered {@link InputPreprocessor} decides to
     *         return a null object while {@code targetType} is of a primitive type.
     * @throws NoSuchRegisteredParserException if there is no registered {@link Parser} for the
     *         given {@code targetType}.
     */
    public <T> T parse(String input, GenericType<T> genericType) {
        if (input == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("input"));
        }
        if (genericType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("genericType"));
        }

        @SuppressWarnings("unchecked")
        T temp = (T) parseType2(input, genericType.getType());
        return temp;
    }

    /**
     * Parses the given {@code input} string to the given {@code targetType}.
     * <p/>
     * 
     * @param input - string value to parse.
     * @param targetType - the expected type to convert {@code input} to.
     * @return an instance of {@code targetType} corresponding to the given {@code input}.
     * @throws NullPointerException if any given argument is {@code null}.
     * @throws ParseException if the {@link Parser} associated with the
     *         given {@code targetType} throws exception while parsing the given {@code input}.
     * @throws InputPreprocessorException if the registered {@link InputPreprocessor} throws
     *         exception while preparing the given {@code input} for parsing.
     * @throws IllegalPrimitiveValueException if the registered {@link InputPreprocessor} decides to
     *         return a null object while {@code targetType} is of a primitive type.
     * @throws NoSuchRegisteredParserException if there is no registered {@link Parser} for the
     *         given {@code targetType}.
     */
    public Object parseType(String input, Type targetType) {
        if (input == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("input"));
        }
        if (targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }

        return parseType2(input, targetType);

    }

    private Object parseType2(final String input, final Type targetType) {
        String preprocessedInput = null;
        try {
            preprocessedInput = preProcessInputString(input, targetType);
            throwIfNullAndPrimitive(preprocessedInput, targetType, input);

            ParserInvoker invoker = new ParserInvoker(this, targetType, preprocessedInput);
            return invoker.invoke();
        } catch (TypeParserException e) {
            throw e.withPrependedErrorMessage(input, preprocessedInput, targetType);
        }
    }

    private String preProcessInputString(String input, Type targetType) {
        try {
            return inputPreprocessor.prepare(input, new InputPreprocessorHelper(targetType));
        } catch (Throwable t) {
            throw new InputPreprocessorException(inputPreprocessor, t);
        }
    }

    private void throwIfNullAndPrimitive(String preprocessedInput, Type targetType, String input) {
        if (preprocessedInput == null) {
            if (isPrimitive(targetType)) {
                throw new IllegalPrimitiveValueException("Primitive can not be set to null");
            }
        }
    }

    private boolean isPrimitive(Type targetType) {
        if (targetType instanceof Class) {
            Class<?> c = (Class<?>) targetType;
            return c.isPrimitive();
        }
        return false;
    }
}
