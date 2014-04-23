package com.github.drapostolos.typeparser;

import java.util.ArrayList;
import java.util.List;

public class MyTypeParser implements StringToTypeParser<List<?>> {

    @Override
    public List<?> parse(String input, TypeParserHelper helper) {
        // TODO Auto-generated method stub
        return new ArrayList<String>();
    }

}
