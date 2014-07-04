package com.github.drapostolos.typeparser;

import java.util.List;

/**
 * Interface for allowing clients to register their own split strategies.
 * Any implementation of this interface is expected to be immutable.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide"
 *      target="_blank">User-Guide</a>
 */
public interface SplitStrategy {

    /**
     * Splits {@code input} string and returns a list of substrings.
     * <p/>
     * Type-Parser library will internally call this method with a non-null {@code input} value. If
     * {@code input} is null (as returned from
     * {@link InputPreprocessor#prepare(String, InputPreprocessorHelper)}) the {@link SplitStrategy}
     * will not be called, instead and an empty {@link List} will be returned.
     * <p/>
     * 
     * @param input String to split. The initial value will never be null.
     * @param helper Helper class injected automatically by the {@link TypeParser}.
     * @return A list of string computed by splitting the {@code input} string using this
     *         {@link SplitStrategy}.
     * @see ParserHelper#split(String)
     * @see ParserHelper#splitKeyValue(String)
     */
    List<String> split(String input, SplitStrategyHelper helper);

}
