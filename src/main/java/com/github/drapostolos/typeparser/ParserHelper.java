package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.Util.makeNullArgumentErrorMsg;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Helper class providing helper methods to implementations of {@link Parser} when parsing a
 * string to a type.
 * <p/>
 * The {@link TypeParser} will automatically inject an instance of this class into the
 * {@link Parser} implementation.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide"
 *      target="_blank">User-Guide</a>
 */
public final class ParserHelper extends Helper {

    private final TypeParser typeParser;
    private final SplitStrategy splitStrategy;
    private final SplitStrategy keyValueSplitStrategy;
    private final Parsers parsers;

    ParserHelper(TypeParser typeParser, Type targetType) {
        super(targetType);
        this.parsers = typeParser.parsers;
        this.typeParser = typeParser;
        this.splitStrategy = typeParser.splitStrategy;
        this.keyValueSplitStrategy = typeParser.keyValueSplitStrategy;
    }

    boolean containsStaticParser(Type type) {
        return parsers.containsStaticParser(type);
    }

    /**
     * This method gives access to {@link TypeParser#parse(String, Class)}.
     * 
     * @param input String to parse.
     * @param targetType to parse it to.
     * @return an instance of type.
     */
    public <T> T parse(String input, Class<T> targetType) {
        return typeParser.parse(input, targetType);
    }

    /**
     * This method gives access to {@link TypeParser#parseType(String, Type)}.
     * 
     * @param input String to parse.
     * @param targetType The target type to parse the given input to.
     * @return an instance of type.
     */
    public Object parseType(String input, Type targetType) {
        return typeParser.parseType(input, targetType);
    }

    /**
     * Splits the {@code input} string into a list of sub-strings by using the {@link SplitStrategy}
     * implementation, as registered with {@link TypeParserBuilder#setSplitStrategy(SplitStrategy)}.
     * <p/>
     * If {@code input} is null, an empty list is returned, without calling the registered
     * {@link SplitStrategy}.
     * <p/>
     * For example the default {@link SplitStrategy} will split this string "1, 2, 3, 4" into ["1",
     * " 2", " 3", " 4"].
     * <p/>
     * 
     * @param input String to parse. For example "THIS, THAT, OTHER"
     * @return List of strings.
     * @throws SplitStrategyException if registered {@link SplitStrategy} implementation
     *         throws exception.
     */
    public List<String> split(String input) {
        if (input == null) {
            return Collections.emptyList();
        }
        try {
            return splitStrategy.split(input, new SplitStrategyHelper(targetType));
        } catch (Throwable t) {
            throw new SplitStrategyException(splitStrategy, t);
        }

    }

    /**
     * Splits the {@code keyValue} string into a list of sub-strings by using the
     * {@link SplitStrategy} implementation, as registered with
     * {@link TypeParserBuilder#setKeyValueSplitStrategy(SplitStrategy)}.
     * <p/>
     * For example the default behavior splits this string "a=AAA=BBB" into ["a", "AAA=BBB"]. Note!
     * The the string is only split by the first occurring of "=", any subsequent "=" are ignored by
     * the {@link SplitStrategy}.
     * 
     * @param keyValue
     * @return A list of string computed by splitting the {@code keyValue} string using the KeyValue
     *         SplitStrategy.
     * @throws KeyValueSplitStrategyException if registered {@link SplitStrategy} implementation
     *         throws exception.
     */
    public List<String> splitKeyValue(String keyValue) {
        if (keyValue == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("keyValue"));
        }
        try {
            return keyValueSplitStrategy.split(keyValue, new SplitStrategyHelper(targetType));
        } catch (Throwable t) {
            throw new KeyValueSplitStrategyException(keyValueSplitStrategy, t);
        }
    }
}
