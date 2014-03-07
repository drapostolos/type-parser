package com.github.drapostolos.typeparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class DefaultSplitter implements Splitter{

    @Override
    public List<String> split(String input, SplitHelper helper) {
        if(input.trim().isEmpty()){
            return new ArrayList<String>();
        }
        return Arrays.asList(input.split(","));
    }

}
