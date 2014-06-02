package com.github.drapostolos.typeparser;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TypeParserExceptionTest extends TestBase {

    private static final String ERROR_MSG = "some-error-message";

    @Test
    public void shouldThrowInputPreprocessorExceptionWhenInputPreprocessorImplementationThrows() throws Exception {
        // given
        setInputPreprocessor(new InputPreprocessor() {

            @Override
            public String prepare(String input, InputPreprocessorHelper helper) {
                throw new IllegalArgumentException(ERROR_MSG);
            }
        });

        // then
        shouldThrow(InputPreprocessorException.class)
                .withErrorMessage("Exception thrown in method 'InputPreprocessor.prepare(...)'")
                .withErrorMessage(ERROR_MSG)
                .withErrorMessage("See underlying exception for more information")
                .whenParsing(DUMMY_STRING)
                .to(String.class);
    }

    @Test
    public void shouldThrowSplitStrategyExceptionWhenSplitStrategyImplementationThrows() throws Exception {

        // given
        setSplitStrategy(new SplitStrategy() {

            @Override
            public List<String> split(String input, SplitStrategyHelper helper) {
                throw new IllegalArgumentException(ERROR_MSG);
            }
        });

        // when
        shouldThrowParseException()
                .causedBy(SplitStrategyException.class)
                .withErrorMessage("IllegalArgumentException thrown in method 'SplitStrategy.split(...)'")
                .withErrorMessage(ERROR_MSG)
                .withErrorMessage("See underlying exception for more information.")
                .whenParsing(DUMMY_STRING)
                .to(String[].class);
    }

    @Test
    public void shouldThrowKeyValueSplitStrategyExceptionWhenSplitStrategyImplementationThrows() throws Exception {
        // given
        setKeyValueSplitStrategy(new SplitStrategy() {

            @Override
            public List<String> split(String input, SplitStrategyHelper helper) {
                throw new IllegalArgumentException(ERROR_MSG);
            }
        });

        // then
        shouldThrowParseException()
                .causedBy(KeyValueSplitStrategyException.class)
                .withErrorMessage("IllegalArgumentException thrown in method 'KeyValueSplitStrategy.split(...)'")
                .withErrorMessage(ERROR_MSG)
                .withErrorMessage("See underlying exception for more information")
                .whenParsing(DUMMY_STRING)
                .to(new GenericType<Map<String, String>>() {});
    }

    @Test
    public void shouldThrowStaticParserExceptionWhenParserImplementationThrows() throws Exception {
        // given
        registerParser(String.class, new Parser<String>() {

            @Override
            public String parse(String input, ParserHelper helper) {
                throw new UnsupportedOperationException(ERROR_MSG);
            }
        });

        // then
        shouldThrowParseException()
                .withErrorMessage("Exception thrown in method 'Parser.parse(...)'")
                .withErrorMessage(ERROR_MSG)
                .withErrorMessage("See underlying exception for more information")
                .whenParsing(DUMMY_STRING)
                .to(String.class);
    }

    @Test
    public void shouldThrowDynamicParserExceptionWhenDynamicParserImplementationThrows() throws Exception {
        // given
        registerDynamicParser(new DynamicParser() {

            @Override
            public Object parse(String input, ParserHelper helper) {
                throw new UnsupportedOperationException(ERROR_MSG);
            }
        });

        // then
        shouldThrowParseException()
                .withErrorMessage("Exception thrown in method 'DynamicParser.parse(...)'")
                .withErrorMessage(ERROR_MSG)
                .withErrorMessage("See underlying exception for more information")
                .whenParsing(DUMMY_STRING)
                .to(String[].class);
    }

    @Test
    public void shouldThrowWhenRecursiveParseCallThrows() throws Exception {
        shouldThrowParseException()
                .causedBy(ParseException.class)
                .withErrorMessage("Can not parse \"1,2,3,a,4\" {preprocessed: \"1,2,3,a,4\"}")
                .withErrorMessage("ParseException thrown in method 'DynamicParser.parse(...)'")
                .withErrorMessage("Can not parse \"a\" {preprocessed: \"a\"}")
                .withErrorMessage("NumberFormatException thrown in method 'Parser.parse(...)'")
                .whenParsing("1,2,3,a,4")
                .to(Integer[].class);
    }
}
