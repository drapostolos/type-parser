package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

public class TypeParserLongTest extends AbstractTest {

    @Test public void 
    shouldThrowExceptionWhenStringIsNotParsableToLong() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format(NUMBER_FORMAT_ERROR_MSG, "a"));
        parser.parse("a", Long.class);
    }

    @Test
    public void canParseStringToLong() throws Exception {
        assertThat(parser.parse("3", Long.class)).isEqualTo(3);
        assertThat(parser.parse(" 3\t", Long.class)).isEqualTo(3);
    }

    @Test
    public void canParseToGenericLongArray() throws Exception {
        assertThat(parser.parse("1, 2, 3", new GenericType<Long[]>() {}))
        .containsExactly(1l, 2l, 3l);
    }

    @Test
    public void canParseToLongArray() throws Exception {
        assertThat(parser.parse("1, 2, 3", Long[].class))
        .containsOnly(1l, 2l, 3l);
    }

    @Test
    public void canParseToLongList() throws Exception {
        assertThat(parser.parse("1, 2, 3", new GenericType<List<Long>>() {}))
        .containsExactly(1l, 2l, 3l);
    }

    @Test
    public void canParseToLongSet() throws Exception {
        assertThat(parser.parse("1, 2, 1", new GenericType<Set<Long>>() {}))
        .containsExactly(1l, 2l);
    }

    @Test
    public void canParseToLongMap() throws Exception {
        assertThat(parser.parse("1=11, 2=22", new GenericType<Map<Long, Long>>() {}))
        .contains(MapEntry.entry(1l, 11l))
        .contains(MapEntry.entry(2l, 22l))
        .hasSize(2);
    }

}
