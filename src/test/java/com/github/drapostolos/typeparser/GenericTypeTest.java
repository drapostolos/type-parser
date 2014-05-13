package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.junit.Test;

public class GenericTypeTest {

    @SuppressWarnings("rawtypes")
    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenGenericTypeIsNotParameterized() throws Exception {
        new GenericType() {};
    }

    @Test
    public void canExtractParameterizedTypeFromSubclass() throws Exception {
        GenericType<List<String>> gt = new GenericType<List<String>>() {};
        ParameterizedType pt = (ParameterizedType) gt.getType();
        assertThat(pt.getRawType()).isSameAs(List.class);
        assertThat(pt.getActualTypeArguments()).containsExactly(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNotDirectSubclass() throws Exception {
        new Subclass() {};
    }

    static class Subclass extends GenericType<List<Integer>> {

    }

}
