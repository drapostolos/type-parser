package com.github.drapostolos.typeparser;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

final class TypeParserUtility {
    static final String STATIC_FACTORY_METHOD_NAME = "valueOf";
    private static final Splitter DEFAULT_SPLITTER = new DefaultSplitter(); 
    private static final Splitter DEFAULT_KEY_VALUE_PAIR_SPLITTER = new KeyValuePairSplitter();
    private static final InputPreprocessor DEFAULT_INPUT_PREPROCESSOR = new NullInputPreprocessor();

    private TypeParserUtility() { throw new AssertionError("Not meant for instantiation"); }
    
    static InputPreprocessor defaultInputPreprocessor(){
        return DEFAULT_INPUT_PREPROCESSOR;
    }

    static Splitter defaultSplitter(){
        return DEFAULT_SPLITTER;
    }

    static Splitter defaultKeyValuePairSplitter(){
        return DEFAULT_KEY_VALUE_PAIR_SPLITTER;
    }

    static String makeNullArgumentErrorMsg(String argName) {
        return String.format("Argument named '%s' is illegally set to null!", argName);
    }
    
    static String makeParseErrorMsg(String input, String message, Type targetType) {
        return String.format("Can not parse \"%s\" to type \"%s\" [instance of: %s] due to: %s", 
                input, targetType, targetType.getClass().getName(), message);
    }
    
    /*
     * Checks if the given class contains a static method
     * named valueOf(String)
     */
    static boolean containsStaticMethodNamedValueOf(Class<?> type){
        Method method = getMethodNamedValueOf(type);
        if(method == null){
            return false;
        }
        if (!Modifier.isStatic(method.getModifiers())) {
            return false;
        }
        return true;
    }

    /*
     * Returns the Method object for the method named valueOf(String)
     * if existing in the given class, else returns null.
     */
    static Method getMethodNamedValueOf(Class<?> targetType){
        try {
            return targetType.getDeclaredMethod(STATIC_FACTORY_METHOD_NAME, String.class);
        } catch (Exception e) {
            return null;
        }
    }


}
