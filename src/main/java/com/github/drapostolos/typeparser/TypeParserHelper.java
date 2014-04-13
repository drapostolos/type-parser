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

    Type getTargetType() {
        return targetType;
    }

}
