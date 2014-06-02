package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public abstract class TestBase {

    static final String DUMMY_STRING = "dummy-string";
    static final String ERROR_MSG = "some-error-message";
    static final String NUMBER_FORMAT_ERROR_MSG = "Number format exception For input string: \"%s\".";
    TypeParserBuilder parserBuilder = TypeParser.newBuilder();
    TypeParser parser = parserBuilder.build();
    String stringToParse;
    List<Type> targetTypes = new ArrayList<Type>();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    Class<? extends Throwable> expectedThrowable;

    final TestBase setInputPreprocessor(InputPreprocessor ip) {
        parserBuilder.setInputPreprocessor(ip);
        return this;
    }

    final <T> TestBase registerDynamicParser(DynamicParser parser) {
        parserBuilder.registerDynamicParser(parser);
        return this;
    }

    final <T> TestBase registerParser(Class<T> targetType, Parser<T> parser) {
        parserBuilder.registerParser(targetType, parser);
        return this;
    }

    final TestBase setSplitStrategy(SplitStrategy splitStrategy) {
        parserBuilder.setSplitStrategy(splitStrategy);
        return this;
    }

    final TestBase setKeyValueSplitStrategy(SplitStrategy splitStrategy) {
        parserBuilder.setKeyValueSplitStrategy(splitStrategy);
        return this;
    }

    final TestBase shouldThrowParseException() {
        expectedThrowable = ParseException.class;
        thrown.expect(expectedThrowable);
        return this;
    }

    final TestBase shouldThrow(Class<? extends Throwable> throwable) {
        expectedThrowable = throwable;
        thrown.expect(expectedThrowable);
        return this;
    }

    final <T extends Exception> TestBase causedBy(final Class<T> cause) {
        thrown.expectCause(new BaseMatcher<T>() {

            @Override
            public boolean matches(Object item) {
                boolean b = item.getClass().equals(cause);
                return b;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(cause.getName());
            }
        });
        return this;
    }

    final public TestBase withErrorMessage(Object message, Object... args) throws Exception {
        thrown.expectMessage(String.format(message.toString(), args));
        return this;
    }

    final public TestBase withNumberFormatErrorMessage() throws Exception {
        thrown.expectMessage("NumberFormatException");
        thrown.expectMessage("For input string: ");
        return this;
    }

    final TestBase whenParsing(String toParse) {
        stringToParse = toParse;
        return this;
    }

    final public void to(GenericType<?> type) throws Exception {
        targetTypes.add(type.getType());
        parse();
    }

    final public void to(Type... type) throws Exception {
        targetTypes.addAll(Arrays.asList(type));
        parse();
    }

    private void parse() throws Exception {
        TypeParser parser = parserBuilder.build();
        Exception throwThis = null;
        for (Type t : targetTypes) {
            try {
                parser.parseType(stringToParse, t);
                return;
            } catch (Exception e) {
                if (!e.getClass().equals(expectedThrowable)) {
                    throw e;
                }
                throwThis = e;
            }
        }
        throw throwThis;
    }
}
