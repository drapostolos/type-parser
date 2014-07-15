package com.github.drapostolos.typeparser;

import java.lang.reflect.ParameterizedType;
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
    TypeParserBuilder builder = TypeParser.newBuilder();
    TypeParser parser = builder.build();
    String stringToParse;
    List<Type> targetTypes = new ArrayList<Type>();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    Class<? extends Throwable> expectedThrowable;

    final TestBase setInputPreprocessor(InputPreprocessor ip) {
        builder.setInputPreprocessor(ip);
        return this;
    }

    final <T> TestBase registerDynamicParser(DynamicParser parser) {
        builder.registerDynamicParser(parser);
        return this;
    }

    final <T> TestBase registerParser(Class<T> targetType, Parser<T> parser) {
        builder.registerParser(targetType, parser);
        return this;
    }

    final TestBase setSplitStrategy(SplitStrategy splitStrategy) {
        builder.setSplitStrategy(splitStrategy);
        return this;
    }

    final TestBase setKeyValueSplitStrategy(SplitStrategy splitStrategy) {
        builder.setKeyValueSplitStrategy(splitStrategy);
        return this;
    }

    final TestBase setNullStringStrategy(NullStringStrategy nullStringStrategy) {
        builder.setNullStringStrategy(nullStringStrategy);
        return this;
    }

    final TestBase shouldThrowTypeParserException() {
        expectedThrowable = TypeParserException.class;
        thrown.expect(expectedThrowable);
        return this;
    }

    final TestBase shouldThrow(Class<? extends Throwable> throwable) {
        expectedThrowable = throwable;
        thrown.expect(expectedThrowable);
        return this;
    }

    final <T extends Throwable> TestBase causedBy(final Class<T> cause) {
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

    final public TestBase containingErrorMessage(Object message, Object... args) throws Exception {
        thrown.expectMessage(String.format(message.toString(), args));
        return this;
    }

    final public TestBase containingNumberFormatErrorMessage() throws Exception {
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
        TypeParser parser = builder.build();
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

    final Class<?> toRawType(GenericType<?> t) {
        return (Class<?>) ((ParameterizedType) t.getType()).getRawType();
    }

}
