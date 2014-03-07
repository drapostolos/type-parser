package com.github.drapostolos.typeparser;

import java.util.Arrays;
import java.util.List;

final class KeyValuePairSplitter implements Splitter{

    @Override
    public List<String> split(String input, SplitHelper helper) {
        return Arrays.asList(input.split("="));
    }

}
