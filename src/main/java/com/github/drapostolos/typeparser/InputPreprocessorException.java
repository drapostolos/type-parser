package com.github.drapostolos.typeparser;

/**
 * A {@link InputPreprocessorException} is thrown when the registered {@link InputPreprocessor}
 * throws any exception.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide"
 *      target="_blank">User-Guide</a>
 */
public final class InputPreprocessorException extends TypeParserException {

    private static final long serialVersionUID = 1L;

    InputPreprocessorException(InputPreprocessor thrower, Throwable t) {
        super("InputPreprocessor.prepare", thrower, t);
    }

}
