package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;

class TypeParserUtility {
    private static final Splitter DEFAULT_SPLITTER = new RegexSplitter(","); 
    private static final Splitter DEFAULT_MAP_KEY_VALUE_SPLITTER = new RegexSplitter("=");

    private TypeParserUtility() { throw new AssertionError("Not meant for instantiation"); }
    
    static Splitter defaultSplitter(){
        return DEFAULT_SPLITTER;
    }

    static Splitter defaultKeyValuePairSplitter(){
        return DEFAULT_MAP_KEY_VALUE_SPLITTER;
    }

    static String makeNullArgumentErrorMsg(String argName) {
        return String.format("Argument named '%s' is illegally set to null!", argName);
    }
    
    static String makeParseErrorMsg(String input, String message, Type targetType) {
        return String.format("Can not parse \"%s\" to type \"%s\" [instance of: %s] due to: %s", 
                input, targetType, targetType.getClass().getName(), message);
    }

}
