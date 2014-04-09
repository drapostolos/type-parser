package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class TypeParserSetTest extends AbstractTest{
    
    @Test
    public void canParseStringToEmptySet() throws Exception {
        GenericType<Set<String>> type = new GenericType<Set<String>>() {};
        assertThat(parser.parse("null", type)).isEmpty();
    }

    @Test
    public void shouldThrowExceptionWhenParsingSetOfWildcard() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can not parse \"dummy-string\" to type \"java.util.Set<?>");
        thrown.expectMessage("due to: TargetType: 'java.util.Set<?>'");
        thrown.expectMessage("contains the following illegal type argument: '?'");
        parser.parse(DUMMY_STRING, new GenericType<Set<?>>() {});
    }
    
    @Test
    public void canChangeSplitStrategyr() throws Exception {
        // given
        StringToTypeParser parser = StringToTypeParser.newBuilder()
        .setSplitStrategy(new SplitStrategy() {
            @Override
            public List<String> split(String input, SplitStrategyHelper helper) {
                return Arrays.asList(input.split("AAA"));
            }})
        .build();
        
        // when
        Set<String> strSet = parser.parse("aaaAAAbbb", new GenericType<Set<String>>() {});
        
        // then
        assertThat(strSet).containsExactly("aaa", "bbb");
    }
    
    @Test
    public void canParseToLinkedHashSet() throws Exception {
        // when
        Set<String> strSet = parser.parse("aaa,bbb", new GenericType<LinkedHashSet<String>>() {});
        
        // then
        assertThat(strSet).containsExactly("aaa", "bbb");
        assertThat(parser.isTargetTypeParsable(new GenericType<LinkedHashSet<String>>() {})).isTrue();
    }
    
}
