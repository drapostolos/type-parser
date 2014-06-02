package com.github.drapostolos.typeparser;

/**
 * A {@link ParseException} is thrown when either one of these methods throws an exception:
 * <ul>
 * <li>{@link Parser#parse(String, ParserHelper)}</li>
 * <li>{@link DynamicParser#parse(String, ParserHelper)}</li>
 * </ul>
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide"
 *      target="_blank">User-Guide</a>
 */
public final class ParseException extends TypeParserException {

    private static final long serialVersionUID = 1L;

    ParseException(String methodName, Object thrower, Throwable t) {
        super(methodName, thrower, t);
    }

}
