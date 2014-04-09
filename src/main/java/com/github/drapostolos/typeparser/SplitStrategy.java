package com.github.drapostolos.typeparser;

import java.util.List;

/**
 * Interface for allowing clients to register their own split strategies.
 * Any implementation of this interface is expected to be immutable.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide">User-Guide</a>
 */
public interface SplitStrategy {
    
    /**
     * Splits {@code input} string and returns a list of substrings. If {@code input} 
     * is null an empty list must be returned. 
     * @param input String to split.
     * @param helper Helper class injected automatically by the {@link StringToTypeParser}.
     * @return A list of string computed by splitting the {@code input} string using this {@link SplitStrategy}.
     */
    List<String> split(String input, SplitStrategyHelper helper);

}
