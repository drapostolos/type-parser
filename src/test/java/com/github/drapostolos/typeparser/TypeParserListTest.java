package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class TypeParserListTest extends AbstractTest {

    @Test
    public void canRegisterListTypeParserThatOverridesDefaultArrayListTypeParser() throws Exception {
        // GIVEN
        StringToTypeParser parser = StringToTypeParser.newBuilder()
                .registerTypeParserForTypesAssignableTo(List.class, new TypeParser<List<String>>() {

                    @Override
                    public List<String> parse(String input, TypeParserHelper helper) {
                        return Arrays.asList("my-string");
                    }
                })
                .build();

        // WHEN
        List<String> s = parser.parse("something", new GenericType<List<String>>() {});

        // THEN
        assertThat(s).containsExactly("my-string");
    }

    @Test
    public void canParseSpaceToSingleElementList() throws Exception {
        GenericType<List<String>> type = new GenericType<List<String>>() {};
        assertThat(parser.parse(" ", type)).containsExactly(" ");
    }

    @Test
    public void canParseStringToEmptyList() throws Exception {
        GenericType<List<String>> type = new GenericType<List<String>>() {};
        assertThat(parser.parse("null", type)).isEmpty();
    }

    @Test
    public void canParseStringToArrayList() throws Exception {
        GenericType<ArrayList<Long>> type = new GenericType<ArrayList<Long>>() {};
        assertThat(parser.parse(" 1", type)).containsExactly(1l);
    }

    @Test
    public void canParseStringToIterable() throws Exception {
        GenericType<Iterable<Integer>> type = new GenericType<Iterable<Integer>>() {};
        Iterable<Integer> collection = parser.parse("1,2,3", type);
        assertThat(collection).containsExactly(1, 2, 3);
    }

    @Test
    public void canParseStringToCollection() throws Exception {
        GenericType<Collection<Integer>> type = new GenericType<Collection<Integer>>() {};
        Collection<Integer> collection = parser.parse("1,2,3", type);
        assertThat(collection).containsExactly(1, 2, 3);
    }

    @Test
    public void shouldThrowExceptionWhenParsingListOfWildcardType() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can not parse \"dummy-string\" to type");
        thrown.expectMessage("contains the following illegal type argument: '?' ");
        parser.parse(DUMMY_STRING, new GenericType<List<?>>() {});
    }

    @Test
    public void canChangeSplitStrategy() throws Exception {
        // given
        parser = StringToTypeParser.newBuilder()
                .setSplitStrategy(new SplitStrategy() {

                    @Override
                    public List<String> split(String input, SplitStrategyHelper helper) {
                        return Arrays.asList(input.split("A+"));
                    }
                })
                .build();

        // when
        List<String> strList = parser.parse("aaaAAAbbb", new GenericType<List<String>>() {});

        // then
        assertThat(strList).containsExactly("aaa", "bbb");
    }

}
