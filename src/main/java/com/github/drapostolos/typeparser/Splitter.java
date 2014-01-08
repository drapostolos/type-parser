package com.github.drapostolos.typeparser;

import java.util.List;

/**
 * Immutable
 *
 */
public interface Splitter {
    
    List<String> split(String input, SplitHelper helper);

}
