package com.github.drapostolos.typeparser;


final class Util {

    static final String STATIC_FACTORY_METHOD_NAME = "valueOf";
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

}
