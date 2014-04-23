package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.TypeParserUtility.makeNullArgumentErrorMsg;
import static com.github.drapostolos.typeparser.TypeParserUtility.makeParseErrorMsg;

import java.lang.reflect.Type;

/**
 * The purpose of this class is to parse a string (read from a properties file
 * or system property for example) and convert it to a specific java object/Type.
 * For example converting "1" to an {@code Integer} type, or "1,2,3" to a {@code List<Integer>}
 * type.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide">User-Guide</a>
 */
public final class StringToTypeParser {

    final TypeParsers typeParsers;
    final SplitStrategy splitStrategy;
    final SplitStrategy keyValueSplitStrategy;
    final InputPreprocessor inputPreprocessor;

    /**
     * Constructs a new instance of {@link StringToTypeParserBuilder}.
     * 
     * @return a new instance of {@link StringToTypeParserBuilder}.
     */
    public static StringToTypeParserBuilder newBuilder() {
        return new StringToTypeParserBuilder();
    }

    StringToTypeParser(StringToTypeParserBuilder builder) {
        this.typeParsers = TypeParsers.unmodifiableCopy(builder.typeParsers);
        this.splitStrategy = builder.splitStrategy;
        this.keyValueSplitStrategy = builder.keyValueSplitStrategy;
        this.inputPreprocessor = builder.inputPreprocessor;
    }

    /**
     * Parses the given {@code input} string to the given {@code targetType}.
     * <p/>
     * Example: <br/>
     * <code>
     * StringToTypeParser parser = StringToTypeParser.newBuilder().build();<br/>
     * Integer i = parser.parse("1", Integer.class);
     * </code>
     * 
     * @param input - string value to parse
     * @param targetType - the expected type to convert {@code input} to.
     * @return an instance of {@code targetType} corresponding to the given {@code input}.
     * @throws NullPointerException if {@code targetType} argument is {@code null}.
     * @throws NullPointerException if {@code input} argument is {@code null}.
     * @throws NoSuchRegisteredTypeParserException if there is no registered {@link TypeParser}
     *         associated with the given {@code targetType}, or if {@code targetType} does not
     *         contain a static factory method with signature {@code valueOf(String)}.
     * @throws IllegalArgumentException if the {@link TypeParser} associated with the
     *         given {@code targetType} throws exception while parsing the given {@code input}.
     * @throws IllegalArgumentException if the {@link InputPreprocessor} throws exception
     *         while preparing the given {@code input} for parsing.
     * @throws IllegalArgumentException if the {@link InputPreprocessor} decides to return
     *         a null object while {@code targetType} is of a primitive type.
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
     * StringToTypeParser parser = StringToTypeParser.newBuilder().build();<br/>
     * {@code List<Integer>} list = parser.parse("1, 2", new {@code GenericType<List<Integer>>}() {});
     * </code><br/>
     * Note the ending "{}".
     * 
     * @param input - string value to parse.
     * @param genericType - the expected generic type to convert {@code input} to.
     * @return an instance of {@code genericType} corresponding to the given {@code input}.
     * @throws NullPointerException if {@code genericType} argument is {@code null}.
     * @throws NullPointerException if {@code input} argument is {@code null}.
     * @throws NoSuchRegisteredTypeParserException if there is no registered {@link TypeParser}
     *         associated with the given {@code genericType}.
     * @throws IllegalArgumentException if the {@link TypeParser} associated with the
     *         given {@code genericType} throws exception while parsing the given {@code input}.
     * @throws IllegalArgumentException if the {@link InputPreprocessor} throws exception
     *         while preparing the given {@code input} for parsing.
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
     * @throws NullPointerException if {@code targetType} argument is {@code null}.
     * @throws NullPointerException if {@code input} argument is {@code null}.
     * @throws NoSuchRegisteredTypeParserException if there is no registered {@link TypeParser}
     *         associated with the given {@code targetType}, or if {@code targetType} does not
     *         contain a static factory method with signature {@code valueOf(String)}.
     * @throws IllegalArgumentException if the {@link TypeParser} associated with the
     *         given {@code targetType} throws exception while parsing the given {@code input}.
     * @throws IllegalArgumentException if the {@link InputPreprocessor} throws exception
     *         while preparing the given {@code input} for parsing.
     * @throws IllegalArgumentException if the {@link InputPreprocessor} decides to return
     *         a null object while {@code targetType} is of a primitive type.
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

    /**
     * Checks if the given {@code genericType} corresponds to a registered {@link TypeParser}.
     * Returns true if it does, otherwise false.
     * 
     * @param genericType - Generic Type known at compile time.
     * @return true if {@code genericType} corresponds to a {@link TypeParser},
     *         otherwise false.
     */
    public boolean isTargetTypeParsable(GenericType<?> genericType) {
        return isTargetTypeParsable(genericType.getType());
    }

    /**
     * Checks if the given {@code targetType} corresponds to a registered {@link TypeParser}.
     * Returns true if it does, otherwise false.
     * 
     * @param targetType - the {@link Type} to check. Example Integer.class.
     * @return true if {@code genericType} corresponds to a {@link TypeParser},
     *         otherwise false.
     */
    public boolean isTargetTypeParsable(Type targetType) {
        TargetTypeChecker isTargetTypeParsable = new TargetTypeChecker(this, targetType);
        return isTargetTypeParsable.execute();
    }

    private Object parseType2(final String input, Type targetType) {
        String preprocessedInput = preProcessInputString(input, targetType);
        if (preprocessedInput == null) {
            if (isPrimitive(targetType)) {
                String message = "'%s' primitive can not be set to null. Input: \"%s\"; Preprocessed input: \"%s\"";
                throw new IllegalArgumentException(String.format(message, targetType, input, preprocessedInput));
            }
        }
        TypeParserInvoker invoker = new TypeParserInvoker(this, targetType, preprocessedInput);
        return invoker.execute();
    }

    private String preProcessInputString(String input, Type targetType) {
        try {
            return inputPreprocessor.prepare(input, new InputPreprocessorHelper(targetType));
        } catch (Exception e) {
            String message = "Exception thrown from InputPreprocessor: %s [%s] with message:  "
                    + "%s. See underlying exception for more information.";
            message = String.format(message,
                    inputPreprocessor, inputPreprocessor.getClass(), e.getMessage());
            message = makeParseErrorMsg(input, targetType, message);
            throw new IllegalArgumentException(message, e);
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
