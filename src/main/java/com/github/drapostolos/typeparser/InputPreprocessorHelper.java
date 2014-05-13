package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.TypeParserUtility.defaultInputPreprocessor;
import static com.github.drapostolos.typeparser.TypeParserUtility.makeNullArgumentErrorMsg;

import java.lang.reflect.Type;

/**
 * Helper class providing helper methods to implementations of {@link InputPreprocessor} when
 * preparing an input string for parsing.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide">User-Guide</a>
 */
public final class InputPreprocessorHelper extends Helper {

    InputPreprocessorHelper(Type targetType) {
        super(targetType);
    }

    /**
     * Prepare the input String by using the default {@link InputPreprocessor} strategy.
     * 
     * @param input String to prepare for parsing
     * @return pre-processed String to be parsed.
     * @throws NullPointerException if {@code input} is null.
     */
    public String prepareWithDefaultInputPreprocessor(String input) {
        if (input == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("input"));
        }
        return defaultInputPreprocessor().prepare(input, this);
    }

}
