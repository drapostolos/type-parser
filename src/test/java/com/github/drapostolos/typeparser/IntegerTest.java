package com.github.drapostolos.typeparser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class IntegerTest extends AbstractTypeTester<Integer> {

    @Override
    Integer make(String string) throws Exception {
        return Integer.valueOf(string.trim());
    }

    @Test
    public void shouldThrowWhenStringIsNotParsableToInteger() throws Exception {
        shouldThrowTypeParserException()
                .containingNumberFormatErrorMessage()
                .whenParsing("a")
                .to(Integer.class);
    }

    @Test
    public void canParseStringToInteger() throws Exception {
        canParse("3").toType(Integer.class);
        canParse("3").toType(int.class);
        canParse(" 3\t").toType(Integer.class);
        canParse(" 3\t").toType(int.class);
    }

    @Test
    public void canParseToGenericIntegerArray() throws Exception {
        canParse("1, 2, 3").toGenericArray(new GenericType<Integer[]>() {});
    }

    @Test
    public void canParseToIntegerArray() throws Exception {
        canParse("1, 2, 3").toArray(Integer[].class);
    }

    @Test
    public void canParseToIntegerList() throws Exception {
        canParse("1, 2, 3").toArrayList(new GenericType<List<Integer>>() {});
    }

    @Test
    public void canParseToIntegerSet() throws Exception {
        canParse("1, 2, 1").toLinkedHashSet(new GenericType<Set<Integer>>() {});
    }

    @Test
    public void canParseToIntegerMap() throws Exception {
        canParse("1=11, 2=22").toLinkedHashMap(new GenericType<Map<Integer, Integer>>() {});
    }

}
