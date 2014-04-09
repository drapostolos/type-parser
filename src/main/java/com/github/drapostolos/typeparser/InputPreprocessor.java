package com.github.drapostolos.typeparser;

import java.util.Collection;
import java.util.Map;

/**
 * Interface for allowing clients to do their own preparation 
 * of the input string to parse. Any implementation of this 
 * interface is expected to be immutable.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide">User-Guide</a>
 */
public interface InputPreprocessor {
    /**
     * Prepares the input string to be parsed. 
     * <p/>
     * If a null object is returned from this method, the {@link StringToTypeParser}
     * will return either:
     * <ul>
     * <li>An empty {@link Collection}/ {@link Map} / Array type (matching the requested type). Example: an empty {@code ArrayList<Integer>}.  </li>
     * <li>A null object for raw types.</li>
     * </ul>
     * <p/>
     * Any exceptions thrown by this method will be caught in the {@link StringToTypeParser}
     * and re-thrown converted to a {@link IllegalArgumentException}.
     * 
     * @param input String to prepare for parsing.
     * @param helper Helper class injected automatically by the {@link StringToTypeParser}.
     * @return a prepared string to be parsed
     * 
     */
    String prepare(String input, InputPreprocessorHelper helper);
}
