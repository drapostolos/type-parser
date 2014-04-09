package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserListTest extends AbstractTest {
    
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
