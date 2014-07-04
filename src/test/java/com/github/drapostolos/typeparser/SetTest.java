package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class SetTest extends TestBase {

    @Test
    public void canParseStringToEmptySet() throws Exception {
        GenericType<Set<String>> type = new GenericType<Set<String>>() {};
        assertThat(parser.parse("null", type)).isEmpty();
    }

    @Test
    public void shouldThrowExceptionWhenParsingSetOfWildcard() throws Exception {
        shouldThrowTypeParserException()
                .containingErrorMessage("to type \"java.util.Set<?>")
                .containingErrorMessage("That type contains illegal type argument: '?'")
                .whenParsing(DUMMY_STRING)
                .to(new GenericType<Set<?>>() {});
    }

    @Test
    public void canChangeSplitStrategy() throws Exception {
        // given
        TypeParser parser = TypeParser.newBuilder()
                .setSplitStrategy(new SplitStrategy() {

                    @Override
                    public List<String> split(String input, SplitStrategyHelper helper) {
                        return Arrays.asList(input.split("AAA"));
                    }
                })
                .build();

        // when
        Set<String> strSet = parser.parse("aaaAAAbbb", new GenericType<Set<String>>() {});

        // then
        assertThat(strSet).containsExactly("aaa", "bbb");
    }

    @Test
    public void canParseToLinkedHashSet() throws Exception {
        // when
        LinkedHashSet<String> strSet = parser.parse("aaa,bbb", new GenericType<LinkedHashSet<String>>() {});

        // then
        assertThat(strSet)
                .hasSameClassAs(new LinkedHashSet<String>())
                .containsExactly("aaa", "bbb");
    }

}
