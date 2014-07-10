package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArrayTest extends TestBase {

    @Test
    public void canParseStringToEmptyArray() throws Exception {
        assertThat(parser.parse("null", String[].class)).isEmpty();
    }

    @Test
    public <T> void shouldThrowExceptionWhenParsingGenericArrayOfUnknownType() throws Exception {
        shouldThrow(NoSuchRegisteredParserException.class)
                .containingErrorMessage("Can not parse \"dummy-string\"")
                .containingErrorMessage("to type \"T[]\"")
                .containingErrorMessage("due to: There is no registered 'Parser' for that type.")
                .whenParsing(DUMMY_STRING)
                .to(new GenericType<T[]>() {});
    }

    @Test
    public void shouldThrowExceptionWhenParsingIntArrayContainingNonInt() throws Exception {
        shouldThrow(TypeParserException.class)
                .containingErrorMessage("Can not parse \" a\" {preprocessed: \" a\"} ")
                .containingErrorMessage("to type \"int\" {instance of: java.lang.Class}")
                .containingErrorMessage("due to: NumberFormatException")
                .containingNumberFormatErrorMessage()
                .whenParsing("1, a")
                .to(int[].class);
    }

    @Test
    public void shouldThrowExceptionWhenParsingIntegerArrayContainingNonInteger() throws Exception {
        shouldThrow(TypeParserException.class)
                .containingErrorMessage("Can not parse \" a\" {preprocessed: \" a\"} ")
                .containingErrorMessage("to type \"java.lang.Integer\" {instance of: java.lang.Class}")
                .containingErrorMessage("due to: NumberFormatException")
                .whenParsing("1, a")
                .to(Integer[].class);
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
    public void canParseToEmpty() throws Exception {
        Integer[] intArray = parser.parse("null", Integer[].class);
        assertThat(intArray)
                .isInstanceOf(Integer[].class)
                .isEmpty();
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
        String[] strArray = parser.parse("aaaAAAbbb", String[].class);

        // then
        assertThat(strArray).containsExactly("aaa", "bbb");
    }

}
