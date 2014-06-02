package com.github.drapostolos.typeparser;

/**
 * A {@link KeyValueSplitStrategyException} is thrown when the
 * {@link ParserHelper#splitKeyValue(String)} method throws any exception.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide"
 *      target="_blank">User-Guide</a>
 */
public final class KeyValueSplitStrategyException extends TypeParserException {

    private static final long serialVersionUID = 1L;

    KeyValueSplitStrategyException(SplitStrategy thrower, Throwable t) {
        super("KeyValueSplitStrategy.split", thrower, t);
    }

}
