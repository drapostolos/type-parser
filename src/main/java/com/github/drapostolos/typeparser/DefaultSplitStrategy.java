package com.github.drapostolos.typeparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class DefaultSplitStrategy implements SplitStrategy{

    @Override
    public List<String> split(String input, SplitStrategyHelper helper) {
        if(input == null){
            return new ArrayList<String>();
        }
        return Arrays.asList(input.split(","));
    }
}


