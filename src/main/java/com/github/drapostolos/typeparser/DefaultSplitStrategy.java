package com.github.drapostolos.typeparser;

import java.util.Arrays;
import java.util.List;

final class DefaultSplitStrategy implements SplitStrategy {

    @Override
    public List<String> split(String input, SplitStrategyHelper helper) {
        return Arrays.asList(input.split(","));
    }
}
