package com.github.drapostolos.typeparser;


import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

public class TypeParserBooleanTest extends AbstractTest{

    @Test
    public void canParseStringToTrue() throws Exception {
    	assertThat(parser.isTargetTypeParsable(Boolean.class)).isTrue();
    	assertThat(parser.isTargetTypeParsable(boolean.class)).isTrue();
        assertThat(parser.parse("true", Boolean.class)).isEqualTo(Boolean.TRUE);
        assertThat(parser.parse(" true\t", Boolean.class)).isEqualTo(Boolean.TRUE);
        assertThat(parser.parse("true", boolean.class)).isEqualTo(true);
        assertThat(parser.parse(" true\t", boolean.class)).isEqualTo(true);
    }

    @Test
    public void canParseStringToFalse() throws Exception {
        assertThat(parser.parse("false", Boolean.class)).isEqualTo(Boolean.FALSE);
        assertThat(parser.parse(" false ", Boolean.class)).isEqualTo(Boolean.FALSE);
        assertThat(parser.parse("false", boolean.class)).isEqualTo(false);
        assertThat(parser.parse(" false ", boolean.class)).isEqualTo(false);
    }
    
    @Test
    public void shouldThrowExceptionWhenNotParsableToBoolean() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("is not parsable to a Boolean");
        parser.parse("1234", Boolean.class);
    }

    @Test
    public void canParseToGenericBooleanArray() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<Boolean[]>() {})).isTrue();
        assertThat(parser.parse("true, false, true", new GenericType<Boolean[]>() {}))
        .containsExactly(true, false, true);
    }

    @Test
    public void canParseToBooleanArray() throws Exception {
    	assertThat(parser.isTargetTypeParsable(Boolean[].class)).isTrue();
        assertThat(parser.parse("true, false, true", Boolean[].class))
        .containsExactly(true, false, true);
    }

    @Test
    public void canParseToBooleanList() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<List<Boolean>>() {})).isTrue();
        assertThat(parser.parse("true, false, true", new GenericType<List<Boolean>>() {}))
        .containsExactly(true, false, true);
    }

    @Test
    public void canParseToBooleanSet() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<Set<Boolean>>() {})).isTrue();
        assertThat(parser.parse("true, false, true", new GenericType<Set<Boolean>>() {}))
        .containsExactly(true, false);
    }

    @Test
    public void canParseToBooleanMap() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<Map<Boolean, Boolean>>() {})).isTrue();
        assertThat(parser.parse("false=true, true=false", new GenericType<Map<Boolean, Boolean>>() {}))
        .contains(MapEntry.entry(Boolean.FALSE, Boolean.TRUE))
        .contains(MapEntry.entry(Boolean.TRUE, Boolean.FALSE))
        .hasSize(2);
    }

}
