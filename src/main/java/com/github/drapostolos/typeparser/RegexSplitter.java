package com.github.drapostolos.typeparser;

import java.util.Arrays;
import java.util.List;

class RegexSplitter implements Splitter{
    private final String regex;
    
    RegexSplitter(String regex) {
        this.regex = regex;
    }

    @Override
    public List<String> split(String input, SplitHelper helper) {
        return Arrays.asList(input.split(regex));
    }

}
