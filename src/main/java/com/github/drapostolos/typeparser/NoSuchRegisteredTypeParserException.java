package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.TypeParserUtility.STATIC_FACTORY_METHOD_NAME;
import static com.github.drapostolos.typeparser.TypeParserUtility.makeParseErrorMsg;

import java.lang.reflect.Type;

/**
 * Exception thrown when there is no {@link Parser} registered for a given Type.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide">User-Guide</a>
 */
public final class NoSuchRegisteredTypeParserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    NoSuchRegisteredTypeParserException(String preprocessedInput, Type targetType) {
        super(constructMessage(preprocessedInput, targetType));
    }

    private static String constructMessage(String preprocessedInput, Type targetType) {
        String message = "There is either no registered 'TypeParser' for that type, or that "
                + "type does not contain the following static factory method: '%s.%s(String)'.";
        message = String.format(message, targetType, STATIC_FACTORY_METHOD_NAME);
        return makeParseErrorMsg(preprocessedInput, targetType, message);
    }
}
