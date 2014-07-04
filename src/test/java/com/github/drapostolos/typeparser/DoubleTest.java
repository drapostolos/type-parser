package com.github.drapostolos.typeparser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class DoubleTest extends AbstractTypeTester<Double> {

    @Override
    Double make(String string) throws Exception {
        return Double.valueOf(string.trim());
    }

    @Test
    public void shouldThrowWhenStringIsNotParsableToDouble() throws Exception {
        shouldThrowTypeParserException()
                .containingNumberFormatErrorMessage()
                .whenParsing("aa")
                .to(Double.class, double.class);
    }

    @Test
    public void canParseStringToDoubleType() throws Exception {
        canParse("01.2").toType(Double.class);
        canParse("1").toType(double.class);
        canParse("1d").toType(double.class);
        canParse("\t1d").toType(double.class);
        canParse(".1").toType(double.class);
    }

    @Test
    public void canParseToGenericDoubleArray() throws Exception {
        canParse("1, 1.2, .1").toGenericArray(new GenericType<Double[]>() {});
    }

    @Test
    public void canParseToCharacerArray() throws Exception {
        canParse("1, 1.2, .1").toArray(Double[].class);
    }

    @Test
    public void canParseToDoubleList() throws Exception {
        canParse("1, 1.2, .1").toArrayList(new GenericType<List<Double>>() {});
    }

    @Test
    public void canParseToDoubleSet() throws Exception {
        canParse("1, 1.2, 1").toLinkedHashSet(new GenericType<Set<Double>>() {});
    }

    @Test
    public void canParseToDoubleMap() throws Exception {
        canParse("1=.1, 2=.2").toLinkedHashMap(new GenericType<Map<Double, Double>>() {});
    }
}
