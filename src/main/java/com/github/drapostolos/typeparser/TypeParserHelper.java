package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Helper class providing helper methods to implementations of {@link TypeParser} when parsing a
 * string to a type.
 * <p/>
 * The {@link StringToTypeParser} will automatically inject an instance of this class into the
 * {@link TypeParser} implementation.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide">User-Guide</a>
 */
public final class TypeParserHelper {

    private final Type targetType;
    private final StringToTypeParser stringParser;
    private final SplitStrategy splitStrategy;
    private final SplitStrategy mapKeyValueSplitStrategy;

    TypeParserHelper(StringToTypeParser stringParser, Type targetType) {
        this.stringParser = stringParser;
        this.targetType = targetType;
        this.splitStrategy = stringParser.splitStrategy;
        this.mapKeyValueSplitStrategy = stringParser.keyValueSplitStrategy;
    }

    /**
     * This method gives access to {@link StringToTypeParser#parse(String, Class)}.
     * 
     * @param input String to parse.
     * @param targetType to parse it to.
     * @return an instance of type.
     */
    public <T> T parse(String input, Class<T> targetType) {
        return stringParser.parse(input, targetType);
    }

    /**
     * This method gives access to {@link StringToTypeParser#parseType(String, Type)}.
     * 
     * @param input String to parse.
     * @param targetType The target type to parse the given input to.
     * @return an instance of type.
     */
    public Object parseType(String input, Type targetType) {
        return stringParser.parseType(input, targetType);
    }

    /**
     * Splits the {@code input} string into a list of sub-strings by using the {@link SplitStrategy}
     * implementation, as registered with
     * {@link StringToTypeParserBuilder#setSplitStrategy(SplitStrategy)}.
     * <p/>
     * For example this string "1, 2, 3, 4" is split into ["1", " 2", " 3", " 4"].
     * 
     * @param input String to parse. For example "THIS, THAT, OTHER"
     * @return List of strings.
     */
    public List<String> split(String input) {
        return splitStrategy.split(input, new SplitStrategyHelper(targetType));
    }

    /**
     * Splits the {@code keyValue} string into a list of sub-strings by using the
     * {@link SplitStrategy} implementation, as registered with
     * {@link StringToTypeParserBuilder#setKeyValueSplitStrategy(SplitStrategy)}.
     * <p/>
     * For example this string "a=AAA" is split into ["a", "AAA"].
     * 
     * @param keyValue
     * @return A list of string computed by splitting the {@code keyValue} string using the KeyValue
     *         SplitStrategy.
     */
    public List<String> splitKeyValue(String keyValue) {
        return mapKeyValueSplitStrategy.split(keyValue, new SplitStrategyHelper(targetType));
    }

    /**
     * Returns the type to parse the input string to.
     * 
     * @return the {@link Type} to parse to.
     */
    public Type getTargetType() {
        return targetType;
    }

    /**
     * When the {@code targetType} is a parameterized type this method
     * returns a list with the type arguments.
     * <p/>
     * All type arguments must be none parameterized types (i.e. nested parameterized types are not
     * allowed), with one exception: {@link Class<?>}. <br/>
     * 
     * @return List of {@link Class} types.
     * @throws IllegalStateException if the {@code targetType} is not a parameterized type.
     * @throws IllegalStateException if any of the parameterized type arguments is of a
     *         parameterized type (with exception of {@link Class}).
     */
    public List<Class<?>> getParameterizedTypeArguments() {
        return TypeParserUtility.getParameterizedTypeArguments(targetType);
    }

}
