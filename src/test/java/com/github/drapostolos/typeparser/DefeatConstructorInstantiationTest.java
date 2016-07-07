package com.github.drapostolos.typeparser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class DefeatConstructorInstantiationTest {

    @Test(expected = InvocationTargetException.class)
    public void shouldThrowAssertionErrorWhenInstantiatingUtil() throws Exception {
        throwExceptionWhenInstantiating(Util.class);
    }

    @Test(expected = InvocationTargetException.class)
    public void shouldThrowWhenInstantiatingDynamicParsers() throws Exception {
        throwExceptionWhenInstantiating(DynamicParsers.class);
    }

    private void throwExceptionWhenInstantiating(Class<?> cls) throws Exception {
        Constructor<?> c = cls.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }

}
