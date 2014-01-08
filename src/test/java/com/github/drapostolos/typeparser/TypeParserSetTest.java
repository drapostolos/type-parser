package com.github.drapostolos.typeparser;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

public class TypeParserSetTest extends AbstractTest{
    
    @Test
    public void shouldThrowExceptionWhenParsingSetOfWildcard() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can not parse \"dummy-string\" to type \"java.util.Set<?>");
        thrown.expectMessage("due to: TargetType: 'java.util.Set<?>'");
        thrown.expectMessage("contains the following illegal type argument: '?'");
        parser.parse(DUMMY_STRING, new GenericType<Set<?>>() {});
    }
    
    @Test
    public void canChangeSplitter() throws Exception {
        // given
        StringToTypeParser parser = StringToTypeParser.newBuilder()
        .setSplitter(new Splitter() {
            @Override
            public List<String> split(String input, SplitHelper helper) {
                return Arrays.asList(input.split("AAA"));
            }})
        .build();
        
        // when
        Set<String> strSet = parser.parse("aaaAAAbbb", new GenericType<Set<String>>() {});
        
        // then
        Assertions.assertThat(strSet).containsExactly("aaa", "bbb");
    }
    
}
