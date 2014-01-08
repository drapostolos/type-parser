package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.junit.Test;

public class GenericTypeTest {
    
    

    @SuppressWarnings("rawtypes")
    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenGenericTypeIsNotParameterized() throws Exception {
        new GenericType() {};
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void canCreateGenericTypeFromRawTypeList() throws Exception {
        GenericType<List> gt = GenericType.forClass(List.class);
        assertThat(gt).isEqualTo(new GenericType<List>(){});
    }
    
    @Test
    public void testToString() throws Exception {
        GenericType<String> gt = GenericType.forClass(String.class);
        assertThat(gt.toString()).isEqualTo("class java.lang.String");
    }
    
    @Test
    public void canCreateGenericTypeFromClass() throws Exception {
        GenericType<String> gt = GenericType.forClass(String.class);
        assertThat(gt).isEqualTo(new GenericType<String>(){});
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
        new Subclass(){};
    }
    static class Subclass extends GenericType<List<Integer>> {
        
    }

}
