package com.github.drapostolos.typeparser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class LongTest extends AbstractTypeTester<Long> {

    @Override
    Long make(String string) throws Exception {
        return Long.valueOf(string.trim());
    }

    @Test
    public void shouldThrowWhenStringIsNotParsableToLong() throws Exception {
        shouldThrowWhenParsing("a")
                .toTypeWithErrorMessage(Long.class, String.format(NUMBER_FORMAT_ERROR_MSG, "a"));
    }

    @Test
    public void canParseStringToLong() throws Exception {
        canParse("3").toType(Long.class);
        canParse("3").toType(long.class);
        canParse(" 3\t").toType(Long.class);
        canParse(" 3\t").toType(long.class);
    }

    @Test
    public void canParseToGenericLongArray() throws Exception {
        canParse("1, 2, 3").toGenericArray(new GenericType<Long[]>() {});
    }

    @Test
    public void canParseToLongArray() throws Exception {
        canParse("1, 2, 3").toArray(Long[].class);
    }

    @Test
    public void canParseToLongList() throws Exception {
        canParse("1, 2, 3").toList(new GenericType<List<Long>>() {});
    }

    @Test
    public void canParseToLongSet() throws Exception {
        canParse("1, 2, 1").toSet(new GenericType<Set<Long>>() {});
    }

    @Test
    public void canParseToLongMap() throws Exception {
        canParse("1=11, 2=22").toMap(new GenericType<Map<Long, Long>>() {});
    }

}
