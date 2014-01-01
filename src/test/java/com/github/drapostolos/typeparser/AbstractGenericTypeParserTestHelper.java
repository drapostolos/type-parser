package com.github.drapostolos.typeparser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;


abstract class AbstractGenericTypeParserTestHelper {
    private StringToTypeParser stringParser = StringToTypeParser.newBuilder().build();
    private Method method;


    protected AbstractGenericTypeParserTestHelper assertMethod(Class<?>... args) throws Exception {
        method = getClass().getDeclaredMethod("m", args);
        return this;
    }

    protected void isCallableWith(String... inputs) throws Throwable  {
        Object[] args = new Object[inputs.length];
        Type[] types = method.getGenericParameterTypes();
        for(int i = 0; i < inputs.length; i++){
            args[i] = stringParser.parseType(inputs[i], types[i]);
        }
        try {
            method.invoke(this, args);
        } catch (InvocationTargetException e) {
            throw (Throwable) e.getTargetException();
        }
    }

}
