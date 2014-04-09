package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

public class TypeParserFloatTest extends AbstractTest {

    @Test public void 
    shouldThrowExceptionWhenStringIsNotParsableToFloat() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format(NUMBER_FORMAT_ERROR_MSG, "aa"));
        parser.parse("aa", Float.class);
    }

    @Test
    public void canParseStringToFloat() throws Exception {
    	assertThat(parser.isTargetTypeParsable(Float.class)).isTrue();
    	assertThat(parser.isTargetTypeParsable(float.class)).isTrue();
        assertThat(parser.parse("01.2", float.class)).isEqualTo(1.2f);
        assertThat(parser.parse("1", Float.class)).isEqualTo(1f);
        assertThat(parser.parse("1d", Float.class)).isEqualTo(1f);
        assertThat(parser.parse("\t1f", Float.class)).isEqualTo(1f);
        assertThat(parser.parse(".1", Float.class)).isEqualTo(0.1f);
    }

    @Test
    public void canParseToGenericFloatArray() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<Float[]>() {})).isTrue();
        assertThat(parser.parse("1d, .1f, 23f", new GenericType<Float[]>() {}))
        .containsExactly(1f, 0.1f, 23f);
    }

    @Test
    public void canParseToFloatArray() throws Exception {
    	assertThat(parser.isTargetTypeParsable(Float[].class)).isTrue();
        assertThat(parser.parse("1d, .1f, 23f", Float[].class))
        .containsOnly(1f, 0.1f, 23f);
    }

    @Test
    public void canParseToFloatList() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<List<Float>>() {})).isTrue();
        assertThat(parser.parse("1d, .1f, 23f", new GenericType<List<Float>>() {}))
        .containsExactly(1f, 0.1f, 23f);
    }

    @Test
    public void canParseToFloatSet() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<Set<Float>>() {})).isTrue();
        assertThat(parser.parse("1d, .1f, 23f", new GenericType<Set<Float>>() {}))
        .containsExactly(1f, 0.1f, 23f);
    }

    @Test
    public void canParseToFloatMap() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<Map<Float, Float>>() {})).isTrue();
        assertThat(parser.parse("1=11, 2=22, 3=33", new GenericType<Map<Float, Float>>() {}))
        .contains(MapEntry.entry(1f, 11f))
        .contains(MapEntry.entry(2f, 22f))
        .contains(MapEntry.entry(3f, 33f))
        .hasSize(3);
    }


}
