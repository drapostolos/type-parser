package com.github.drapostolos.typeparser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class DefeatConstructorInstantiationTest {

    @Test(expected = InvocationTargetException.class)
    public void shouldThrowAssertionErrorWhenInstantiatinTypeParsers() throws Exception {
        throwExceptionWhenInstantiating(TypeParsers.class);
    }

    @Test(expected = InvocationTargetException.class)
    public void shouldThrowAssertionErrorWhenInstantiatinTypeParserUtility() throws Exception {
        throwExceptionWhenInstantiating(TypeParserUtility.class);
    }

    private void throwExceptionWhenInstantiating(Class<?> cls) throws Exception {
        Constructor<?> c = cls.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }

}
