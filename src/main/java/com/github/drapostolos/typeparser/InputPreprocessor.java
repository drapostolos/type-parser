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
     * 
     * @param input String to prepare for parsing. The initial value will never be null.
     * @param helper Helper class injected automatically by the {@link TypeParser}.
     * @return a prepared string to be parsed
     * @throws RuntimeException Any exception thrown within this method will be wrapped and
     *         re-thrown as a {@link TypeParserException} to the client.
     */
    String prepare(String input, InputPreprocessorHelper helper);
}
