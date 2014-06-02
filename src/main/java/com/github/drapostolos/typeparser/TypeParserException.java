package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;

/**
 * {@link TypeParserException} is the base exceptions for all exceptions defined in
 * {@link TypeParser} library.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide"
 *      target="_blank">User-Guide</a>
 */
public class TypeParserException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String prefix = "";

    /**
     * Use this constructor to wrap an exception thrown from a client implemented callback method.
     * 
     * @param thrown - the actual exception thrown form an underlying implementation.
     * @param methodName - name of method that throws: Example "SplitStrategy.split".
     * @param thrower - the object where the exceptions originates from.
     */
    TypeParserException(String methodName, Object thrower, Throwable t) {
        super(formatMessage(methodName, thrower, t), t);
    }

    private static String formatMessage(String methodName, Object thrower, Throwable t) {
        String message =
                "%s thrown in method '%s(...)' implemented by: %s {instance of: %s}\n\t"
                        + "with message: %s.\n\t"
                        + "See underlying exception for more information.";
        return String.format(message,
                t.getClass().getSimpleName(), methodName, thrower, thrower.getClass().getName(),
                t.getMessage());
    }

    /**
     * Use this to create a low-level exception, containing only a message,
     * example {@link IllegalPrimitiveValueException}
     */
    TypeParserException(String message) {
        super(message);
    }

    /**
     * Prepend underlying error message with {@code input} and {@code targetType}.
     * 
     * @param input
     * @param preprocessedInput
     * @param targetType
     * @return this exception.
     */
    TypeParserException withPrependedErrorMessage(String input,
            String preprocessedInput, Type targetType) {
        preprocessedInput = formatPreprocessedInput(preprocessedInput);
        prefix = formatPrefix(input, preprocessedInput, targetType);
        return this;
    }

    private String formatPreprocessedInput(String preprocessedInput) {
        if (preprocessedInput == null) {
            return null;
        }
        return String.format("\"%s\"", preprocessedInput);
    }

    private String formatPrefix(String input, String preprocessedInput, Type targetType) {
        String msg = "\n\tCan not parse \"%s\" {preprocessed: %s} "
                + "to type \"%s\" {instance of: %s} \n\tdue to: ";
        String targetTypeName = targetType.getClass().getName();
        return String.format(msg, input, preprocessedInput, targetType, targetTypeName);
    }

    @Override
    final public String getMessage() {
        return prefix + super.getMessage();
    }

}
