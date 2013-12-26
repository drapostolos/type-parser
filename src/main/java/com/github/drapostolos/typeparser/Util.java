package com.github.drapostolos.typeparser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class Util {
    private Util() { /* Not meant for instantiation */}
    
    static String nullArgumentErrorMsg(String argName) {
        return String.format("Argument named '%s' is illegally set to null!", argName);
    }

    static Class<?> extractTypeParameter(TypeParser<?> typeParser) {
        return extractTypeParameter(typeParser.getClass(), typeParser);
    }
    /*
     * Pass the original TypeParser as well, to be able to print 
     * it in error message.
     */
    private static Class<?> extractTypeParameter(Class<?> c, TypeParser<?> typeParser) {
        if(c == null){
            String message = "Can not find parametirized type in %s !?!?!? Must be a bug somewhere...";
            throw new IllegalArgumentException(String.format(message, typeParser));
        }
        for (Type t : c.getGenericInterfaces()){
            if(t instanceof ParameterizedType){
                ParameterizedType type = (ParameterizedType) t;
                if(TypeParser.class.equals(type.getRawType())){
                    return  (Class<?>) type.getActualTypeArguments()[0];
                }
            }
        }
        return extractTypeParameter(c.getSuperclass(), typeParser);
    }


}
