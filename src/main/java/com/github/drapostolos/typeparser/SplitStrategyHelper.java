package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Helper class providing helper methods to implementations of {@link SplitStrategy} when splitting
 * a string into sub-strings.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide">User-Guide</a>
 */
public final class SplitStrategyHelper {

    private static final SplitStrategyHelper IGNORED = null;
    private final Type targetType;

    SplitStrategyHelper(Type targetType) {
        this.targetType = targetType;
    }

    /**
     * Gives access to the default split strategy, which splits
     * the {@code input} String by using a comma (,).
     * 
     * @param input String to split.
     * @return List of substrings of the input string
     */
    public List<String> splitWithDefaultSplitStrategy(String input) {
        return TypeParserUtility.defaultSplitStrategy().split(input, IGNORED);
    }

    /**
     * @return the Type to parse to. Example: Integers.class.
     */
    public Type getTargetType() {
        return targetType;
    }
}
