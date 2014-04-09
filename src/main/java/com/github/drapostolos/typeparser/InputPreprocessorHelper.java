package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;

/**
 * Helper class providing helper methods to implementations of
 * {@link InputPreprocessor} when preparing an input string for parsing.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide">User-Guide</a>
 */
public final class InputPreprocessorHelper {
    private final Type targetType;

    InputPreprocessorHelper(Type targetType) {
        this.targetType = targetType;
    }
    
    /**
     * Returns the type to parse the input string to.
     * @return the {@link Type} to parse to.
     */
    public Type getTargetType(){
        return targetType;
    }

    /**
     * Prepare the input String by using the default {@link InputPreprocessor} strategy.
     * @param input String to prepare for parsing
     * @return pre-processed String to be parsed.
     */
    public String prepareWithDefaultInputPreprocessor(String input) {
        return TypeParserUtility.defaultInputPreprocessor().prepare(input, this);
    }
    
    

}
