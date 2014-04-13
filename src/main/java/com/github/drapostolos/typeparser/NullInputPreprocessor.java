package com.github.drapostolos.typeparser;

final class NullInputPreprocessor implements InputPreprocessor {

    @Override
    public String prepare(String input, InputPreprocessorHelper helper) {
        if (input.trim().equalsIgnoreCase("null")) {
            return null;
        }
        return input;
    }
}
