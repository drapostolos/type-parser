package com.github.drapostolos.typeparser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class ShortTest extends AbstractTypeTester<Short> {

    @Override
    Short make(String string) throws Exception {
        return Short.valueOf(string.trim());
    }

    @Test
    public void shouldThrowWhenStringIsNotParsableToShort() throws Exception {
        shouldThrowTypeParserException()
                .containingNumberFormatErrorMessage()
                .whenParsing("a")
                .to(Short.class);
    }

    @Test
    public void canParseStringToShort() throws Exception {
        canParse("3").toType(Short.class);
        canParse("3").toType(short.class);
        canParse(" 3\t").toType(Short.class);
        canParse(" 3\t").toType(short.class);
    }

    @Test
    public void canParseToGenericShortArray() throws Exception {
        canParse("1, 2, 3").toGenericArray(new GenericType<Short[]>() {});
    }

    @Test
    public void canParseToShortArray() throws Exception {
        canParse("1, 2, 3").toArray(Short[].class);
    }

    @Test
    public void canParseToShortList() throws Exception {
        canParse("1, 2, 3").toArrayList(new GenericType<List<Short>>() {});
    }

    @Test
    public void canParseToShortSet() throws Exception {
        canParse("1, 2, 1").toLinkedHashSet(new GenericType<Set<Short>>() {});
    }

    @Test
    public void canParseToShortMap() throws Exception {
        canParse("1=11, 2=22").toLinkedHashMap(new GenericType<Map<Short, Short>>() {});
    }
}
