package com.github.drapostolos.typeparser;

/**
 * A {@link SplitStrategyException} is thrown when the {@link ParserHelper#split(String)} method
 * throws any exception.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide"
 *      target="_blank">User-Guide</a>
 */
public final class SplitStrategyException extends TypeParserException {

    private static final long serialVersionUID = 1L;

    SplitStrategyException(SplitStrategy thrower, Throwable t) {
        super("SplitStrategy.split", thrower, t);
    }

}
