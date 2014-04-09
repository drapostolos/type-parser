package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

public class TypeParserIntegerTest extends AbstractTest {

    @Test public void 
    shouldThrowExceptionWhenStringIsNotParsableToInteger() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format(NUMBER_FORMAT_ERROR_MSG, "a"));
        parser.parse("a", Integer.class);
    }

    @Test
    public void canParseStringToInteger() throws Exception {
    	assertThat(parser.isTargetTypeParsable(Integer.class)).isTrue();
    	assertThat(parser.isTargetTypeParsable(int.class)).isTrue();
        assertThat(parser.parse("3", Integer.class)).isEqualTo(3);
        assertThat(parser.parse(" 3\t", Integer.class)).isEqualTo(3);
        assertThat(parser.parse("3", int.class)).isEqualTo(3);
        assertThat(parser.parse(" 3\t", int.class)).isEqualTo(3);
    }

    @Test
    public void canParseToGenericIntegerArray() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<Integer[]>() {})).isTrue();
        assertThat(parser.parse("1, 2, 3", new GenericType<Integer[]>() {}))
        .containsExactly(1, 2, 3);
    }

    @Test
    public void canParseToIntegerArray() throws Exception {
    	assertThat(parser.isTargetTypeParsable(Integer[].class)).isTrue();
        assertThat(parser.parse("1, 2, 3", Integer[].class))
        .containsOnly(1, 2, 3);
    }

    @Test
    public void canParseToIntegerList() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<List<Integer>>() {})).isTrue();
        assertThat(parser.parse("1, 2, 3", new GenericType<List<Integer>>() {}))
        .containsExactly(1, 2, 3);
    }

    @Test
    public void canParseToIntegerSet() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<Set<Integer>>() {})).isTrue();
        assertThat(parser.parse("1, 2, 1", new GenericType<Set<Integer>>() {}))
        .containsExactly(1, 2);
    }

    @Test
    public void canParseToIntegerMap() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<Map<Integer, Integer>>() {})).isTrue();
        assertThat(parser.parse("1=11, 2=22, 3=33", new GenericType<Map<Integer, Integer>>() {}))
        .contains(MapEntry.entry(1, 11))
        .contains(MapEntry.entry(2, 22))
        .contains(MapEntry.entry(3, 33))
        .hasSize(3);
    }
    
}
