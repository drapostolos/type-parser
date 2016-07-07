package com.github.drapostolos.typeparser;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TypeParserExceptionTest extends TestBase {

    private static final String ERROR_MSG = "some-error-message";

    @Test
    public void shouldThrowWhenInputPreprocessorImplementationThrows() throws Exception {
        // given
        setInputPreprocessor(new InputPreprocessor() {

            @Override
            public String prepare(String input, InputPreprocessorHelper helper) {
                throw new IllegalArgumentException(ERROR_MSG);
            }
        });

        // then
        shouldThrowTypeParserException()
                .containingErrorMessage(ERROR_MSG)
                .whenParsing(DUMMY_STRING)
                .to(String.class);
    }

    @Test
    public void shouldThrowWhenSplitStrategyImplementationThrows() throws Exception {
        // given
        setKeyValueSplitStrategy(new SplitStrategy() {

            @Override
            public List<String> split(String input, SplitStrategyHelper helper) {
                throw new IllegalArgumentException(ERROR_MSG);
            }
        });

        // then
        shouldThrowTypeParserException()
                .causedBy(IllegalArgumentException.class)
                .containingErrorMessage(ERROR_MSG)
                .whenParsing(DUMMY_STRING)
                .to(new GenericType<Map<String, String>>() {});
    }

    @Test
    public void shouldThrowWhenParserImplementationThrows() throws Exception {
        // given
        registerParser(String.class, new Parser<String>() {

            @Override
            public String parse(String input, ParserHelper helper) {
                throw new UnsupportedOperationException(ERROR_MSG);
            }
        });

        // then
        shouldThrowTypeParserException()
                .containingErrorMessage(ERROR_MSG)
                .whenParsing(DUMMY_STRING)
                .to(String.class);
    }

    @Test
    public void shouldThrowWhenDynamicParserImplementationThrows() throws Exception {
        // given
        registerDynamicParser(new DynamicParser() {

            @Override
            public Object parse(String input, ParserHelper helper) {
                throw new UnsupportedOperationException(ERROR_MSG);
            }
        });

        // then
        shouldThrowTypeParserException()
                .containingErrorMessage(ERROR_MSG)
                .whenParsing(DUMMY_STRING)
                .to(DynamicParser.class);
    }

    @Test
    public void shouldThrowWhenRecursiveParseCallThrows() throws Exception {
        shouldThrowTypeParserException()
                .causedBy(NumberFormatException.class)
                .containingErrorMessage("Can not parse \"a\" {preprocessed: \"a\"}")
                .containingNumberFormatErrorMessage()
                .whenParsing("1,2,3,a,4")
                .to(Integer[].class);
    }
}
