package com.github.drapostolos.typeparser;

/**
 * A {@link IllegalPrimitiveValueException} is thrown when the {@link TypeParser} tries to
 * set a primitive type to a {@code null} value.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide"
 *      target="_blank">User-Guide</a>
 */
public final class IllegalPrimitiveValueException extends TypeParserException {

    private static final long serialVersionUID = 1L;

    IllegalPrimitiveValueException(String message) {
        super(message);
    }

}
