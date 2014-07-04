package com.github.drapostolos.typeparser;

/**
 * Interface for implementations that parses a given string and then converts it to
 * a specific type. Any implementation of this interface is expected to be immutable.
 * 
 * @param <T> the type to convert the parsed string to.
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide"
 *      target="_blank">User-Guide</a>
 */
public interface Parser<T> {

    /**
     * Parses the given string and converts it to an instance of type T.
     * <p>
     * Type-Parser library will internally call this method with a non-null {@code input} value. If
     * {@code input} is null (as returned from
     * {@link InputPreprocessor#prepare(String, InputPreprocessorHelper)}) this method will not be
     * called, instead a {@code null} value is used.
     * 
     * @param input input string to parse. This will never be null.
     * @param helper Helper class injected automatically by the {@link TypeParser}.
     * @return an instance of type T.
     * @throws RuntimeException Any exception thrown within this method will be wrapped and
     *         re-thrown as a {@link TypeParserException} to the client.
     */
    T parse(String input, ParserHelper helper);
}
