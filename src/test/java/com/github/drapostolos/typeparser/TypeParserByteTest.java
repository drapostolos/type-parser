package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

public class TypeParserByteTest extends AbstractTest{

    @Test
    public void canParseStringToByteType() throws Exception {
        assertThat(parser.parse("55", byte.class)).isEqualTo((byte) 55);
        assertThat(parser.parse("\t55 ", Byte.class)).isEqualTo((byte) 55);
    }

    @Test
    public void shouldThrowExceptionWhenStringIsOutOfRangeFor_byteType() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Value out of range. Value:\"1234\" Radix:10.");
        parser.parse("1234", byte.class);
    }

    @Test
    public void shouldThrowExceptionWhenStringIsOutOfRangeForByteType() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Value out of range. Value:\"1234\" Radix:10.");
        parser.parse("1234", Byte.class);
    }

    @Test
    public void shouldThrowExceptionWhenStringIsNotAByte() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format(NUMBER_FORMAT_ERROR_MSG, "aaa"));
        parser.parse("aaa", byte.class);
    }

    @Test
    public void canParseToGenericByteArray() throws Exception {
        assertThat(parser.parse("55, 45, 35", new GenericType<Byte[]>() {}))
        .containsExactly((byte) 55, (byte) 45, (byte) 35);
    }

    @Test
    public void canParseToByteArray() throws Exception {
        assertThat(parser.parse("55, 45, 35", Byte[].class))
        .containsExactly((byte) 55, (byte) 45, (byte) 35);
    }

    @Test
    public void canParseToByteList() throws Exception {
        assertThat(parser.parse("55, 45, 35", new GenericType<List<Byte>>() {} ))
        .containsExactly((byte) 55, (byte) 45, (byte) 35);
    }

    @Test
    public void canParseToByteSet() throws Exception {
        assertThat(parser.parse("55, 45, 35", new GenericType<Set<Byte>>() {}))
        .containsExactly((byte) 55, (byte) 45, (byte) 35);
    }

    @Test
    public void canParseToBooleanMap() throws Exception {
        assertThat(parser.parse("55=56, 57=58", new GenericType<Map<Byte, Byte>>() {}))
        .contains(MapEntry.entry((byte) 55, (byte) 56))
        .contains(MapEntry.entry((byte) 57, (byte) 58))
        .hasSize(2);
    }
}
