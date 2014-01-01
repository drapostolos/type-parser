package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;


class Helper {
    private final StringToTypeParser stringParser;

    Helper(StringToTypeParser parser) {
        this.stringParser = parser;
    }
    
    public <T> T parse(String input, Class<T> type) {
        return stringParser.parse(input, type);
    }

    public Object parseType(String input, Type type) {
        return stringParser.parseType(input, type);
    }


    
    

}
