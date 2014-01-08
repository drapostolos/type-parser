package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

public class TypeParserDoubleTest extends AbstractTest{
    
    @Test public void 
    shouldThrowExceptionWhenStringIsNotParsableToDouble() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format(NUMBER_FORMAT_ERROR_MSG, "aa"));
        parser.parse("aa", Double.class);
    }

    @Test
    public void canParseStringToDoubleType() throws Exception {
        assertThat(parser.parse("01.2", Double.class)).isEqualTo(1.2d);
        assertThat(parser.parse("1", double.class)).isEqualTo(1d);
        assertThat(parser.parse("1d", double.class)).isEqualTo(1d);
        assertThat(parser.parse("\t1d ", double.class)).isEqualTo(1d);
        assertThat(parser.parse(".1", double.class)).isEqualTo(0.1d);
    }

    @Test
    public void canParseToGenericDoubleArray() throws Exception {
        assertThat(parser.parse("1, 1.2, .1", new GenericType<Double[]>() {}))
        .containsExactly(1d, 1.2d, 0.1d);
    }

    @Test
    public void canParseToCharacerArray() throws Exception {
        assertThat(parser.parse("1, 1.2, .1", double[].class))
        .containsOnly(1d, 1.2d, 0.1d);
    }

    @Test
    public void canParseToDoubleList() throws Exception {
        assertThat(parser.parse("1, 1.2, .1", new GenericType<List<Double>>() {}))
        .containsExactly(1d, 1.2d, 0.1d);
    }

    @Test
    public void canParseToDoubleSet() throws Exception {
        assertThat(parser.parse("1, 1.2, 1", new GenericType<Set<Double>>() {}))
        .containsExactly(1d, 1.2d);
    }

    @Test
    public void canParseToDoubleMap() throws Exception {
        assertThat(parser.parse("1=.1, 2=.2", new GenericType<Map<Double, Double>>() {}))
        .contains(MapEntry.entry(1d, .1d))
        .contains(MapEntry.entry(2d, .2d))
        .hasSize(2);
    }
}
