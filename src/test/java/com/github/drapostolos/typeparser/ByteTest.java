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
        shouldThrowTypeParserException()
                .containingErrorMessage("Value out of range. Value:\"1234\" Radix:10")
                .whenParsing("1234")
                .to(byte.class, Byte.class);
    }

    @Test
    public void shouldThrowExceptionWhenStringIsNotAByte() throws Exception {
        shouldThrowTypeParserException()
                .containingNumberFormatErrorMessage()
                .whenParsing("aaa")
                .to(byte.class, Byte.class);
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
        canParse("55, 45, 35").toArrayList(new GenericType<List<Byte>>() {});
    }

    @Test
    public void canParseToByteSet() throws Exception {
        canParse("55, 45, 55").toLinkedHashSet(new GenericType<Set<Byte>>() {});
    }

    @Test
    public void canParseToBooleanMap() throws Exception {
        canParse("55=56, 57=58").toLinkedHashMap(new GenericType<Map<Byte, Byte>>() {});
    }
}
