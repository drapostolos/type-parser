package com.github.drapostolos.typeparser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class BigDecimalTest extends AbstractTypeTester<BigDecimal> {

    @Override
    BigDecimal make(String string) throws Exception {
        return new BigDecimal(string.trim());
    }

    @Test
    public void canParseStringToBigDecimalType() throws Exception {
        canParse("55").toType(BigDecimal.class);
        canParse("\t55").toType(BigDecimal.class);
    }

    @Test
    public void shouldThrowExceptionWhenStringIsNotABigDecimalType() throws Exception {
        shouldThrowTypeParserException()
                .causedBy(NumberFormatException.class)
                .containingNumberFormatErrorMessage()
                .whenParsing(DUMMY_STRING)
                .to(BigDecimal.class);
    }

    @Test
    public void canParseToGenericBigDecimalArray() throws Exception {
        canParse("55, 45, 35").toGenericArray(new GenericType<BigDecimal[]>() {});
    }

    @Test
    public void canParseToBigDecimalArray() throws Exception {
        canParse("55, 45,35").toArray(BigDecimal[].class);
    }

    @Test
    public void canParseToBigDecimalList() throws Exception {
        canParse("55, 45,35").toArrayList(new GenericType<List<BigDecimal>>() {});
    }

    @Test
    public void canParseToBigDecimalSet() throws Exception {
        canParse("55, 45, 55").toLinkedHashSet(new GenericType<Set<BigDecimal>>() {});
    }

    @Test
    public void canParseToBigDecimalMap() throws Exception {
        canParse("5=55, 6=66").toLinkedHashMap(new GenericType<Map<BigDecimal, BigDecimal>>() {});
    }

}
