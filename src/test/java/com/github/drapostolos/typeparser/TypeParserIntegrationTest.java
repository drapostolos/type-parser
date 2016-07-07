package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class TypeParserIntegrationTest extends TestBase {

    @Test
    public void shouldThrowExceptionWhenParsingPrimitiveToNullValue() throws Exception {
        Class<?>[] classes = { boolean.class, byte.class, char.class, double.class, int.class,
                float.class, long.class, short.class };
        for (Class<?> c : classes) {
            try {
                parser.parse("null", c);
                fail(String.format("Primitive type '%s' is set to null, when it should not be possible"));
            } catch (TypeParserException e) {
                assertThat(e.getMessage()).contains("Primitive can not be set to null");
            }
        }
    }

    @Test
    public void canParseGenericTypeToEmptyList() throws Exception {
        GenericType<List<Long>> type = new GenericType<List<Long>>() {};
        assertThat(parser.parse("null", type)).isEmpty();
    }

    @Test
    public void canParseWrapperClassToNullValue() throws Exception {
        Class<?>[] classes = { Boolean.class, Byte.class, Character.class, Double.class,
                Integer.class, Float.class, Long.class, Short.class };
        for (Class<?> c : classes) {
            assertThat(parser.parse("null", c)).isNull();
        }
    }

    @Test
    public void shouldThrowExceptionWhenNoTypeParserIsRegisteredForTargetType() throws Exception {
        // given
        Type type = Map.class.getTypeParameters()[0];

        // then
        shouldThrow(NoSuchRegisteredParserException.class)
                .containingErrorMessage("to type \"K\" ", DUMMY_STRING)
                .containingErrorMessage("{instance of: sun.reflect.generics.reflectiveObjects.TypeVariableImpl}")
                .containingErrorMessage("due to: There is no registered 'Parser' for that type.")
                .whenParsing(DUMMY_STRING)
                .to(type);
    }

    @Test(expected = NoSuchRegisteredParserException.class)
    public void canUnregisterDefaultTypeParserByClass() throws Exception {
        // given
        assertThat(parser.parse("1", int.class)).isEqualTo(1);

        // when
        TypeParser parser = TypeParser.newBuilder()
                .unregisterParser(int.class)
                .build();

        // then 
        parser.parse("1", int.class);
    }

    @Test
    public void canRegisterTypeParserByClass() throws Exception {
        // given
        TypeParser parser = TypeParser.newBuilder()
                .registerParser(MyClass1.class, new MyClass1())
                .build();

        // then
        MyClass1 actual = parser.parse("aaa", MyClass1.class);
        assertThat(actual).isEqualTo(new MyClass1("aaa"));
    }

    private static class MyGenericParser<T> implements Parser<MyGenericParser<T>> {
        private T arg;

        /*
         * This class servers both as a Parser and an arbitrary Generic class.
         */
        MyGenericParser(T arg) {
            this.arg = arg;
        }

        @Override
        public MyGenericParser<T> parse(String input, ParserHelper helper) {
            return new MyGenericParser<T>(arg);
        }
    }

    @Test
    public void canRegisterParserForGenericTypes() throws Exception {
        // given
        TypeParser parser = TypeParser.newBuilder()
                .registerParser(new GenericType<MyGenericParser<String>>() {}, new MyGenericParser<String>("null"))
                .build();

        // then
        MyGenericParser<String> actual = parser.parse("", new GenericType<MyGenericParser<String>>() {});
        assertThat(actual).isNotNull();
    }

    @Test
    public void shouldThrowExceptionWhenNonParameterizedTypeIsExpectedToBeParameterized() throws Exception {
        // given 
        registerParser(MyClass1.class, new Parser<MyClass1>() {

            @Override
            public MyClass1 parse(String input, ParserHelper helper) {
                helper.getParameterizedClassArguments();
                return null;
            }
        });

        // then
        shouldThrowTypeParserException()
                .containingErrorMessage(MyClass1.class.toString())
                .containingErrorMessage("type must be parameterized: ")
                .whenParsing(DUMMY_STRING)
                .to(MyClass1.class);

    }

    @Test
    public void canGetTargetClassFromHelperWithoutCasting() throws Exception {
        parser = TypeParser.newBuilder()
                .registerParser(MyClass1.class, new Parser<MyClass1>() {

                    @Override
                    public MyClass1 parse(String input, ParserHelper helper) {
                        Class<MyClass1> c = helper.getTargetClass();
                        assertThat(c).isSameAs(MyClass1.class);
                        return null;
                    }
                })
                .build();

        parser.parse("aaa", MyClass1.class);
    }

    @Test
    public void shouldThrowWhenParamerizedTypeCastedToClass() throws Exception {
        // given
        registerDynamicParser(new DynamicParser() {

            @Override
            public MyClass1 parse(String input, ParserHelper helper) {
                helper.getTargetClass();
                return null;
            }
        });

        // then
        shouldThrowTypeParserException()
                .containingErrorMessage("cannot be casted to java.lang.Class")
                .containingErrorMessage("ParameterizedTypeImpl")
                .whenParsing(DUMMY_STRING)
                .to(new GenericType<MyGenericType<MyClass1>>() {});
    }

    static class MyGenericType<T> {

    }

    @Test
    public void shouldThrowExceptionWhenNonArrayIsExpectedToBeArray() throws Exception {
        // given
        registerParser(MyClass1.class, new Parser<MyClass1>() {

            @Override
            public MyClass1 parse(String input, ParserHelper helper) {
                // this call should throw.
                helper.getComponentClass();
                return null;
            }
        });

        // then
        shouldThrowTypeParserException()
                .containingErrorMessage(MyClass1.class.getName())
                .containingErrorMessage("type is not an array.")
                .whenParsing(DUMMY_STRING)
                .to(MyClass1.class);
    }

    @Test
    public void canParseStringToStringType() throws Exception {
        String actual = parser.parse(" A B ", String.class);
        assertThat(actual).isEqualTo(" A B ");
    }

    @Test
    public void canParseSubclassWhenRegisteringDynamicParserHandlingAllSubclassesOfASuperclass() throws Exception {
        // GIVEN
        DynamicParser typeParser = new DynamicParser() {

            @Override
            public Object parse(String input, ParserHelper helper) {
                if (!helper.isTargetTypeAssignableTo(MyBaseClass.class)) {
                    return TRY_NEXT;
                }
                if (input.equals("1")) {
                    return new MyClass1();
                }
                return new MyClass2();
            }

        };
        TypeParser parser = TypeParser.newBuilder()
                .registerDynamicParser(typeParser)
                .build();

        // THEN
        assertThat(parser.parse("1", MyClass1.class)).isInstanceOf(MyClass1.class);
        assertThat(parser.parse("2", MyClass2.class)).isInstanceOf(MyClass2.class);
    }

    @Test
    public void canChangeInputPreprocessor() throws Exception {
        // given
        parser = TypeParser.newBuilder()
                .setInputPreprocessor(new InputPreprocessor() {

                    @Override
                    public String prepare(String input, InputPreprocessorHelper helper) {
                        if (helper.getTargetType().equals(String.class)) {
                            if (input.equals("DEFAULT")) {
                                return "MY-DEFAULT";
                            }
                        }
                        return input;
                    }
                })
                .build();

        // then
        assertThat(parser.parse("DEFAULT", String.class)).isEqualTo("MY-DEFAULT");
        assertThat(parser.parse("nuLL", String.class)).isNull();
        assertThat(parser.parse("AAA", String.class)).isEqualTo("AAA");
        assertThat(parser.parse("null ", Integer.class)).isNull();
        ;
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfInputPreprocessorThrows() throws Exception {
        // given
        builder.setInputPreprocessor(new InputPreprocessor() {

            @Override
            public String prepare(String input, InputPreprocessorHelper helper) {
                throw new RuntimeException("some-message");
            }
        });

        // then
        shouldThrowTypeParserException()
                .containingErrorMessage("some-message")
                .whenParsing(DUMMY_STRING)
                .to(String.class);
    }

    @Test
    public void shouldThrowExceptionWhenSplitStrategyThrows() throws Exception {
        // given
        setSplitStrategy(new SplitStrategy() {

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
                .to(new GenericType<List<Integer>>() {});
    }

    @Test
    public void canUseDefaultSplitStrategyFromCustomSplitStrategy() throws Exception {
        // given
        parser = TypeParser.newBuilder()
                .setSplitStrategy(new SplitStrategy() {

                    @Override
                    public List<String> split(String input, SplitStrategyHelper helper) {
                        return helper.splitWithDefaultSplitStrategy(input);
                    }
                })
                .build();

        // when
        List<Integer> intList = parser.parse("1,2,3", new GenericType<List<Integer>>() {});

        // then
        assertThat(intList).containsExactly(1, 2, 3);

    }

    @Test
    public void shouldThrowIfKeyValueSplitStrategyThrows() throws Exception {
        // given

        setKeyValueSplitStrategy(new SplitStrategy() {

            @Override
            public List<String> split(String input, SplitStrategyHelper helper) {
                throw new RuntimeException(ERROR_MSG);
            }
        });

        // then
        shouldThrowTypeParserException()
                .containingErrorMessage(ERROR_MSG)
                .whenParsing("1=11,2=22")
                .to(new GenericType<Map<Integer, Integer>>() {});

    }

    @Test
    public void shouldThrowIfNullStringStrategyThrows() throws Exception {
        // given

        setNullStringStrategy(new NullStringStrategy() {

            @Override
            public boolean isNullString(String input, NullStringStrategyHelper helper) {
                throw new UnsupportedOperationException(ERROR_MSG);
            }
        });

        // then
        shouldThrowTypeParserException()
                .causedBy(UnsupportedOperationException.class)
                .containingErrorMessage(ERROR_MSG)
                .whenParsing("1")
                .to(Integer.class);

    }

    @Test
    public void canReturnNullObjectWhenInputIsNullString() throws Exception {
        // given
        setNullStringStrategy(new NullStringStrategy() {

            @Override
            public boolean isNullString(String input, NullStringStrategyHelper helper) {
                return input.equals("");
            }
        });
        parser = builder.build();

        // then
        assertThat(parser.parse("", Integer.class)).isNull();
    }

    @Test
    public void canReturnEmptyListWhenInputIsNullString() throws Exception {
        // given
        setNullStringStrategy(new NullStringStrategy() {

            @Override
            public boolean isNullString(String input, NullStringStrategyHelper helper) {
                return input.equals("");
            }
        });
        parser = builder.build();

        // then
        assertThat(parser.parse("", new GenericType<List<Integer>>() {})).isEmpty();
    }

    @Test
    public void canDecideIfNullStringWithinInputPreprocessor() throws Exception {
        // given
        builder.setInputPreprocessor(new InputPreprocessor() {

            @Override
            public String prepare(String input, InputPreprocessorHelper helper) {
                assertThat(helper.isNullString(input)).isTrue();
                assertThat(helper.isNullString(DUMMY_STRING)).isFalse();
                return input;
            }
        });
        parser = builder.build();

        // then
        assertThat(parser.parse("null", Long.class)).isNull();
    }

    @Test
    public void canDecideIfNullStringWithinParser() throws Exception {
        // given
        builder.registerDynamicParser(new DynamicParser() {

            @Override
            public Object parse(String input, ParserHelper helper) {
                assertThat(helper.isNullString(input)).isTrue();
                assertThat(helper.isNullString(DUMMY_STRING)).isFalse();
                return null;
            }
        });
        parser = builder.build();

        // then
        assertThat(parser.parse("null", Long.class)).isNull();
    }

    @Test
    public void shouldReturnEmptyListAndSkipCustomSplitStrategyWhenInputStringIsNull() throws Exception {
        // given
        final CountDownLatch counter = new CountDownLatch(1);
        parser = TypeParser.newBuilder()
                .setSplitStrategy(new SplitStrategy() {

                    @Override
                    public List<String> split(String input, SplitStrategyHelper helper) {
                        // Make sure this part of the code is never executed, since
                        // input string will be null.
                        counter.countDown();
                        return Arrays.asList(input.split(","));
                    }
                })
                .build();

        // when
        // This calls the above registered Parser
        assertThat(counter.getCount()).isEqualTo(1);
        assertThat(parser.parse("null", new GenericType<List<String>>() {})).isEmpty();
    }

    @Test
    public void shouldThrowWhenInputPreprocessorReturnsNull() throws Exception {
        // given
        builder.setInputPreprocessor(new InputPreprocessor() {

            @Override
            public String prepare(String input, InputPreprocessorHelper helper) {
                return null;
            }
        });
        parser = builder.build();

        // then
        shouldThrowTypeParserException()
                .causedBy(UnsupportedOperationException.class)
                .containingErrorMessage("InputPreprocessor.prepare(...) method returned a null object")
                .containingErrorMessage("when its contract states an actual String must be returned.")
                .whenParsing(DUMMY_STRING)
                .to(String.class);
    }
}
