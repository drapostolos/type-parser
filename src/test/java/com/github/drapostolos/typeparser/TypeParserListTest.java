package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserListTest extends AbstractTest {
    
    @Test
    public void canParseStringToEmptyList() throws Exception {
        GenericType<List<Long>> type = new GenericType<List<Long>>() {};
        assertThat(parser.parse(" ", type)).isEmpty();
    }

    @Test
    public void shouldThrowExceptionWhenParsingListOfWildcardType() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can not parse \"dummy-string\" to type");
        thrown.expectMessage("contains the following illegal type argument: '?' ");
        parser.parse(DUMMY_STRING, new GenericType<List<?>>() {});
    }
    
    @Test
    public void canChangeSplitter() throws Exception {
        // given
        parser = StringToTypeParser.newBuilder()
        .setSplitter(new Splitter() {
            @Override
            public List<String> split(String input, SplitHelper helper) {
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
