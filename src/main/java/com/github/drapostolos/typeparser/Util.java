package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;

final class Util {

    private static final SplitStrategy DEFAULT_SPLIT_STRATEGY = new DefaultSplitStrategy();
    private static final SplitStrategy DEFAULT_KEY_VALUE_SPLIT_STRATEGY = new DefaultKeyValueSplitStrategy();
    private static final InputPreprocessor DEFAULT_INPUT_PREPROCESSOR = new NullInputPreprocessor();

    private Util() {
        throw new AssertionError("Not meant for instantiation");
    }

    static InputPreprocessor defaultInputPreprocessor() {
        return DEFAULT_INPUT_PREPROCESSOR;
    }

    static SplitStrategy defaultSplitStrategy() {
        return DEFAULT_SPLIT_STRATEGY;
    }

    static SplitStrategy defaultKeyValueSplitStrategy() {
        return DEFAULT_KEY_VALUE_SPLIT_STRATEGY;
    }

    static String makeNullArgumentErrorMsg(String argName) {
        return String.format("Argument named '%s' is illegally set to null!", argName);
    }

    static String toString(Object o) {
        return String.format("%s {%s}", o, o.getClass());
    }

    static String formatErrorMessage(String input, String preprocessed, Type targetType, String message) {
        String messageTemplate = "\n\t"
                + "Can not parse \"%s\" {preprocessed: %s} "
                + "to type \"%s\" {instance of: %s} \n\t"
                + "due to: %s";
        String preprocessedInput = formatPreprocessedInput(preprocessed);
        String targetTypeName = getTargetTypeName(targetType);
        String className = targetType.getClass().getName();
        return String.format(messageTemplate,
                input, preprocessedInput, targetTypeName, className, message);
    }

    private static String formatPreprocessedInput(String preprocessedInput) {
        if (preprocessedInput == null) {
            return null;
        }
        return String.format("\"%s\"", preprocessedInput);
    }

    private static String getTargetTypeName(Type targetType) {
        if (targetType instanceof Class) {
            Class<?> c = (Class<?>) targetType;
            return c.getCanonicalName();
        }
        return targetType.toString();
    }
}
