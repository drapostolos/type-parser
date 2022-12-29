package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

public class OptionalTest extends TestBase {

    @Test
    public void canParseSpaceToOptionalString() throws Exception {
        GenericType<Optional<String>> type = new GenericType<Optional<String>>() {};
        assertThat(parser.parse(" ", type)).contains(" ");
    }

    @Test
    public void canParseStringToEmptyOptional() throws Exception {
        GenericType<Optional<String>> type = new GenericType<Optional<String>>() {};
        assertThat(parser.parse("null", type)).isEmpty();
    }

    @Test
    public void canParseStringToEmptyOptionalWithWildcard() throws Exception {
        GenericType<Optional<?>> type = new GenericType<Optional<?>>() {};
        assertThat(parser.parse("null", type)).isEmpty();
    }

    @Test
	public void canParseStringToOptionalListOfIntegers() throws Exception {
    	Optional<List<Integer>> integers = parser.parse("1,2,3", new GenericType<Optional<List<Integer>>>() {});
    	assertThat(integers.get()).containsExactly(1, 2, 3);
	}
    
    @Test
    public void canParseStringToOptionalLong() throws Exception {
        GenericType<Optional<Long>> type = new GenericType<Optional<Long>>() {};
        assertThat(parser.parse("1", type)).contains(1l);
    }

    @Test
    public void shouldThrowExceptionWhenParsingWildcardType() throws Exception {
        shouldThrow(NoSuchRegisteredParserException.class)
        .containingErrorMessage("Can not parse \"dummy-string\"")
        .containingErrorMessage("to type \"?\"")
        .containingErrorMessage("due to: There is no registered 'Parser' for that type.")
        .whenParsing(DUMMY_STRING)
        .to(new GenericType<Optional<?>>() {});
    }

    @Test
    public void canChangeNullStrategy() throws Exception {
        parser = TypeParser.newBuilder()
                .setNullStringStrategy((input, helper) -> input.isEmpty())
                .build();

        assertThat(parser.parse("", new GenericType<Optional<String>>() {})).isEmpty();
    }
}
