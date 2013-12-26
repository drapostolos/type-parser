package com.github.drapostolos.typeparser;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

import com.github.drapostolos.typeparser.TypeParser;
import com.github.drapostolos.typeparser.Util;

public class UtilTest implements TypeParser<Integer>{

    @Test
    public void canFindParameterizedTypeWhenClassImplementsTypeParser() throws Exception {
        Assertions.assertThat(Util.extractTypeParameter(this)).hasSameClassAs(Integer.class);
    }
    
    @Test
    public void canFindParameterizedTypeWhenBaseClassImplementsTypeParser() throws Exception {
        Assertions.assertThat(Util.extractTypeParameter(new UtilTest(){})).hasSameClassAs(Integer.class);
    }
    
    @Override
    public Integer parse(String value) {
        return Integer.valueOf(value);
    }

}
