package com.github.drapostolos.typeparser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class ByteTest extends AbstractTypeTester<Byte> {

    @Override
    Byte make(String string) throws Exception {
        return Byte.valueOf(string.trim());
    }

    @Test
    public void canParseStringToByteType() throws Exception {
        canParse("55").toType(byte.class);
        canParse("\t55").toType(Byte.class);
    }

    @Test
    public void shouldThrowWhenStringIsOutOfRangeFor_byteType() throws Exception {
        String errorMessage = "Value out of range. Value:\"1234\" Radix:10.";
        shouldThrowWhenParsing("1234")
                .toTypeWithErrorMessage(byte.class, errorMessage)
                .toTypeWithErrorMessage(Byte.class, errorMessage);
    }

    @Test
    public void shouldThrowExceptionWhenStringIsNotAByte() throws Exception {
        shouldThrowWhenParsing("aaa");
        toTypeWithNumberFormatErrorMessage(byte.class);
        toTypeWithNumberFormatErrorMessage(Byte.class);
    }

    @Test
    public void canParseToGenericByteArray() throws Exception {
        canParse("55, 45, 35").toGenericArray(new GenericType<Byte[]>() {});
    }

    @Test
    public void canParseToByteArray() throws Exception {
        canParse("55, 45, 35").toArray(Byte[].class);
    }

    @Test
    public void canParseToByteList() throws Exception {
        canParse("55, 45, 35").toList(new GenericType<List<Byte>>() {});
    }

    @Test
    public void canParseToByteSet() throws Exception {
        canParse("55, 45, 55").toSet(new GenericType<Set<Byte>>() {});
    }

    @Test
    public void canParseToBooleanMap() throws Exception {
        canParse("55=56, 57=58").toMap(new GenericType<Map<Byte, Byte>>() {});
    }
}
