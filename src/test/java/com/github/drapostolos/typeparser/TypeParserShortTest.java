package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

public class TypeParserShortTest extends AbstractTest {

    @Test public void 
    shouldThrowExceptionWhenStringIsNotParsableToShort() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format(NUMBER_FORMAT_ERROR_MSG, "a"));
        parser.parse("a", Short.class);
    }

    @Test
    public void canParseStringToShort() throws Exception {
        assertThat(parser.parse("3", Short.class)).isEqualTo((short) 3);
        assertThat(parser.parse(" 3\t", Short.class)).isEqualTo((short) 3);
    }

    @Test
    public void canParseToGenericShortArray() throws Exception {
        assertThat(parser.parse("1, 2, 3", new GenericType<Short[]>() {}))
        .containsExactly((short) 1, (short) 2, (short) 3);
    }

    @Test
    public void canParseToShortArray() throws Exception {
        assertThat(parser.parse("1, 2, 3", Short[].class))
        .containsOnly((short) 1, (short) 2, (short) 3);
    }

    @Test
    public void canParseToShortList() throws Exception {
        assertThat(parser.parse("1, 2, 3", new GenericType<List<Short>>() {}))
        .containsExactly((short) 1, (short) 2, (short) 3);
    }

    @Test
    public void canParseToShortSet() throws Exception {
        assertThat(parser.parse("1, 2, 1", new GenericType<Set<Short>>() {}))
        .containsExactly((short) 1, (short) 2);
    }

    @Test
    public void canParseToShortMap() throws Exception {
        assertThat(parser.parse("1=11, 2=22", new GenericType<Map<Short, Short>>() {}))
        .contains(MapEntry.entry((short) 1, (short) 11))
        .contains(MapEntry.entry((short) 2, (short) 22))
        .hasSize(2);
    }

}
