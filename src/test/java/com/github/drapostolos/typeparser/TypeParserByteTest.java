package com.github.drapostolos.typeparser;

import org.junit.Test;

public class TypeParserByteTest extends AbstractTypeParserTestHelper{

    public TypeParserByteTest() {
        super(byte.class, Byte.class);
    }

    @Test
    public void canParseStringToByteType() throws Exception {
        assertThat("55").isParsedTo((byte) 55);
        assertThat("\t55 ").isParsedTo((byte) 55);
    }

    @Test
    public void shouldThrowExceptionWhenStringIsOutOfRangeForByteType() throws Exception {
        assertThat("1234").throwsIllegalArgumentException()
        .whereMessageEndsWih("Value out of range. Value:\"1234\" Radix:10.");
    }

    @Test
    public void shouldThrowExceptionWhenStringIsNotAByte() throws Exception {
        assertThat("aaa").throwsIllegalArgumentException()
        .whereMessageEndsWih(NUMBER_FORMAT_ERROR_MSG, "aaa");
    }

}
