package com.github.drapostolos.typeparser;

import java.util.Collection;
import java.util.Map;

/**
 * Interface for allowing clients to do their own preparation
 * of the input string to parse. Any implementation of this
 * interface is expected to be immutable.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide"
 *      target="_blank">User-Guide</a>
 */
public interface InputPreprocessor {

    /**
     * Prepares the input string to be parsed.
     * <p/>
     * If a null object is returned from this method, the called {@link Parser} implementation is
     * expected to return either:
     * <ul>
     * <li>An empty {@link Collection}/ {@link Map} / Array type.</li>
     * <li>A null object for types other than above.</li>
     * </ul>
     * <p/>
     * Any exceptions thrown by this method will be caught in the {@link TypeParser} and re-thrown
     * converted to a {@link InputPreprocessorException}.
     * 
     * @param input String to prepare for parsing. The initial value will never be null.
     * @param helper Helper class injected automatically by the {@link TypeParser}.
     * @return a prepared string to be parsed
     * @throws InputPreprocessorException Any exception thrown by implementation is wrapped within a
     *         {@link InputPreprocessorException}.
     */
    String prepare(String input, InputPreprocessorHelper helper);
}
