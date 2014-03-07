package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Helper class providing helper methods to implementations of
 * {@link Splitter} when splitting a string into sub-strings.
 */
public final class SplitHelper {
    private static final SplitHelper IGNORED = null;
    private final Type targetType;

    SplitHelper(Type targetType) {
        this.targetType = targetType;
    }

    /**
     * Gives access to the default splitter strategy, which splits 
     * by using a comma (,).
     * @param input String to split.
     * @return List of substrings of the input string
     */
    public List<String> splitWithDefaultSplitter(String input){
        return TypeParserUtility.defaultSplitter().split(input, IGNORED);
    }

    /**
     * 
     * @return the Type to parse to. Example: Integers.class.
     */
    public Type getTargetType() {
        return targetType;
    }
}
