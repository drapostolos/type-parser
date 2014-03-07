package com.github.drapostolos.typeparser;

import java.util.List;

/**
 * Interface for allowing clients to register their own split strategies.
 * Any implementation of this interface is expected to be immutable.
 */
public interface Splitter {
    
    /**
     * Splits a string and returns a list of substrings.
     * @param input String to split.
     * @param helper Helper class injected automatically by the {@link StringToTypeParser}.
     * @return
     */
    List<String> split(String input, SplitHelper helper);

}
