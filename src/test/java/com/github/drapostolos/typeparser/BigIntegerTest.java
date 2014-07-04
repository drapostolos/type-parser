package com.github.drapostolos.typeparser;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class BigIntegerTest extends AbstractTypeTester<BigInteger> {

    @Override
    BigInteger make(String string) throws Exception {
        return new BigInteger(string.trim());
    }

    @Test
    public void canParseStringToBigIntegerType() throws Exception {
        canParse("55").toType(BigInteger.class);
        canParse("\t55").toType(BigInteger.class);
    }

    @Test
    public void shouldThrowExceptionWhenStringIsNotABigIntegerType() throws Exception {
        shouldThrowTypeParserException()
                .containingNumberFormatErrorMessage()
                .whenParsing("aaa")
                .to(BigInteger.class);
    }

    @Test
    public void canParseToGenericBigIntegerArray() throws Exception {
        canParse("55, 45, 35").toGenericArray(new GenericType<BigInteger[]>() {});
    }

    @Test
    public void canParseToBigIntegerArray() throws Exception {
        canParse("55, 45,35").toArray(BigInteger[].class);
    }

    @Test
    public void canParseToBigIntegerList() throws Exception {
        canParse("55, 45,35").toArrayList(new GenericType<List<BigInteger>>() {});
    }

    @Test
    public void canParseToBigIntegerSet() throws Exception {
        canParse("55, 45, 55").toLinkedHashSet(new GenericType<Set<BigInteger>>() {});
    }

    @Test
    public void canParseToBigIntegerMap() throws Exception {
        canParse("5=55, 6=66").toLinkedHashMap(new GenericType<Map<BigInteger, BigInteger>>() {});
    }

}
