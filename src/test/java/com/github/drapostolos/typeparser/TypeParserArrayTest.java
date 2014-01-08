package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserArrayTest extends AbstractTest{

    @Test
    public <T> void shouldThrowExceptionWhenParsingGenericArrayOfUnknownType() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can not parse \"dummy-string\" to type \"T[]\"");
        thrown.expectMessage("due to: TargetType: 'T[]'");
        thrown.expectMessage("is either not an array or the componet type is generic.");
        parser.parse(DUMMY_STRING, new GenericType<T[]>() {});

        //        .whereErrorMessageContains("type does not contain the following static factory method: 'T[].valueOf(String)'.");
    }

    @Test
    public void canParseToGenericIntegerArray() throws Exception {
        Integer[] intArray = parser.parse("1, 2, 3", new GenericType<Integer[]>() {});
        assertThat(intArray).containsExactly(1, 2, 3);
    }

    @Test
    public void canParseToIntegerArray() throws Exception {
        Integer[] intArray = parser.parse("1, 2, 3", Integer[].class);
        assertThat(intArray).containsExactly(1, 2, 3);
    }

    @Test
    public void canParseToIntArray() throws Exception {
        int[] intArray = parser.parse("1, 2, 3", int[].class);
        assertThat(intArray).containsOnly(1, 2, 3);
    }

    @Test
    public void canParseToGenericClassArray() throws Exception {
        Class<?>[] intArray = parser.parse("java.lang.Integer", new GenericType<Class<?>[]>() {});
        assertThat(intArray).containsExactly(Integer.class);
    }

    @Test
    public void canParseToClassArray() throws Exception {
        Class<?>[] intArray = parser.parse("java.lang.Integer", Class[].class);
        assertThat(intArray).containsExactly(Integer.class);
    }

    @Test
    public void testCustomMadeArrayclassTypeParser() throws Exception {
        // given
        parser = StringToTypeParser.newBuilder()
                .registerTypeParser(int[].class, new TypeParser<int[]>(){
                    @Override
                    public int[] parse(String input, ParseHelper helper) {
                        return new int[]{5, 4};
                    }})
                .build();

        // when
        int[] intArray = parser.parse("2", int[].class);

        // then
        assertThat(intArray).containsOnly(5, 4);
    }

    @Test
    public void canChangeSplitter() throws Exception {
        // given
        StringToTypeParser parser = StringToTypeParser.newBuilder()
                .setSplitter(new Splitter() {
                    @Override
                    public List<String> split(String input, SplitHelper helper) {
                        return Arrays.asList(input.split("AAA"));
                    }
                })
                .build();

        // when
        String[] strArray = parser.parse("aaaAAAbbb", String[].class);

        // then
        assertThat(strArray).containsExactly("aaa", "bbb");
    }

}
