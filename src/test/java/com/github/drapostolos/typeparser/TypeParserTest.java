package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class TypeParserTest extends TestBase {

    @Test
    public void canNotParseTheseTypesByDefault() throws Exception {
        assertThat(parser.isTargetTypeParsable(Math.class)).isFalse();
        assertThat(parser.isTargetTypeParsable(TypeParser.class)).isFalse();
        assertThat(parser.isTargetTypeParsable(TypeParserBuilder.class)).isFalse();
        assertThat(parser.isTargetTypeParsable(new GenericType<TreeSet<String>>() {}.getType())).isFalse();
        assertThat(parser.isTargetTypeParsable(new GenericType<TreeMap<String, String>>() {}.getType())).isFalse();
        assertThat(parser.isTargetTypeParsable(new GenericType<List<Thread>>() {}.getType())).isFalse();
    }

    @Test
    public void shouldThrowExceptionWhenParsingPrimitiveToNullValue() throws Exception {
        Class<?>[] classes = { boolean.class, byte.class, char.class, double.class, int.class,
                float.class, long.class, short.class };
        for (Class<?> c : classes) {
            try {
                parser.parse("null", c);
                fail(String.format("Primitive type '%s' is set to null, when it should not be possible"));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void canParseGenericTypeToEmptyList() throws Exception {
        GenericType<List<Long>> type = new GenericType<List<Long>>() {};
        assertThat(parser.parse("null", type)).isEmpty();
        // What to do with null and Collection? return null or empty collection?
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
    public void shouldThrowExceptionWhenNoTypeParserIsRegisteredforTargetType() throws Exception {
        thrown.expect(NoSuchRegisteredTypeParserException.class);
        thrown.expectMessage(String.format("Can not parse \"%s\" to type \"K\" ", DUMMY_STRING));
        thrown.expectMessage("[instance of: sun.reflect.generics.reflectiveObjects.TypeVariableImpl]");
        thrown.expectMessage("due to: There is either no registered 'TypeParser' for that type,");
        thrown.expectMessage("or that type does not contain the following static factory method: 'K.valueOf(String)'.");
        Type type = Map.class.getTypeParameters()[0];
        parser.parseType(DUMMY_STRING, type);
    }

    @Test
    public void shouldThrowExceptionWhenTargetTypeIsUnsupportedSetImplementation() throws Exception {
        thrown.expect(NoSuchRegisteredTypeParserException.class);
        parser.parse("a,b,c", new GenericType<TreeSet<String>>() {});
    }

    @Test
    public void shouldThrowExceptionWhenTargetTypeIsUnsupportedListImplementation() throws Exception {
        thrown.expect(NoSuchRegisteredTypeParserException.class);
        parser.parse("a,b,c", new GenericType<LinkedList<String>>() {});
    }

    @Test
    public void shouldThrowExceptionWhenTargetTypeIsUnsupportedMapImplementation() throws Exception {
        thrown.expect(NoSuchRegisteredTypeParserException.class);
        parser.parse("a=A,b=B,c=C", new GenericType<TreeMap<String, String>>() {});
    }

    @Test
    public void shouldThrowExceptionWhenFactoryMethodIsNotStatic() throws Exception {
        thrown.expect(NoSuchRegisteredTypeParserException.class);
        thrown.expectMessage("does not contain the following static factory method: ");
        thrown.expectMessage("MyClass2.valueOf(String)'");
        parser.parse(DUMMY_STRING, MyClass2.class);
    }

    @Test(expected = NoSuchRegisteredTypeParserException.class)
    public void canUnregisterParserForTypesAssignableToSets() throws Exception {
        // given
        GenericType<Set<Integer>> type = new GenericType<Set<Integer>>() {};
        assertThat(parser.parse("1,2", type)).containsExactly(1, 2);

        // when
        TypeParser parser = TypeParser.newBuilder()
                .unregisterParserForTypesAssignableTo(LinkedHashSet.class)
                .build();

        // then 
        parser.parse("1,2", type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void canUnregisterGenericTypeParser() throws Exception {
        // given
        assertThat(parser.parse("java.lang.Long", new GenericType<Class<?>[]>() {}))
                .containsExactly(Long.class);

        // when
        TypeParser parser = TypeParser.newBuilder()
                .unregisterParser(new GenericType<Class<?>[]>() {})
                .build();

        // then 
        parser.parse("java.lang.Long", new GenericType<Class<?>[]>() {});
    }

    @Test(expected = NoSuchRegisteredTypeParserException.class)
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

    @Test
    public void canRegisterTypeParserByGenericType() throws Exception {
        // given
        TypeParser parser = TypeParser.newBuilder()
                .registerParser(new GenericType<MyClass1>() {}, new MyClass1())
                .build();

        // then
        MyClass1 actual = parser.parse("aaa", MyClass1.class);
        assertThat(actual).isEqualTo(new MyClass1("aaa"));
    }

    @Test
    public void shouldThrowExceptionWhenStaticFactoryMethodFails() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Exception thrown in static factory method ");
        parser.parse("aaa", MyClass3.class);
    }

    @Test
    public void shouldThrowExceptionWhenNonParameterizedTypeIsExpectedToBeParameterized() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(MyClass1.class.toString());
        thrown.expectMessage("must be a parameterized type.");
        parser = TypeParser.newBuilder()
                .registerParser(MyClass1.class, new Parser<MyClass1>() {

                    public MyClass1 parse(String input, ParserHelper helper) {
                        TypeParserUtility.getParameterizedTypeArguments(helper.getTargetType());
                        return null;
                    }
                })
                .build();

        parser.parse("aaa", MyClass1.class);
    }

    @Test
    public void canGetTargetClassFromHelperWithoutCasting() throws Exception {
        parser = TypeParser.newBuilder()
                .registerParser(MyClass1.class, new Parser<MyClass1>() {

                    public MyClass1 parse(String input, ParserHelper helper) {
                        Class<MyClass1> c = helper.getTargetClass();
                        assertThat(c).hasSameClassAs(MyClass1.class);
                        return null;
                    }
                })
                .build();

        parser.parse("aaa", MyClass1.class);
    }

    @Test
    public void shouldThrowWhenParamerizedTypeCastedToClass() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("cannot be casted to java.lang.Class");
        thrown.expectMessage("ParameterizedTypeImpl");
        parser = TypeParser.newBuilder()
                .registerParserForTypesAssignableTo(List.class, new Parser<MyClass1>() {

                    @Override
                    public MyClass1 parse(String input, ParserHelper helper) {
                        helper.getTargetClass();
                        return null;
                    }
                })
                .build();

        parser.parse("aaa", new GenericType<List<MyClass1>>() {});
    }

    @Test
    public void shouldThrowExceptionWhenNonArrayIsExpectedToBeArray() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(MyClass1.class.toString());
        thrown.expectMessage("is either not an array or the componet type is generic.");
        parser = TypeParser.newBuilder()
                .registerParser(MyClass1.class, new Parser<MyClass1>() {

                    public MyClass1 parse(String input, ParserHelper helper) {
                        TypeParserUtility.getComponentClass(helper.getTargetType());
                        return null;
                    }
                })
                .build();

        parser.parse("aaa", MyClass1.class);
    }

    @Test
    public void shouldThrowExceptionWhenStaticFactoryMethodReturnsWrongType() throws Exception {
        thrown.expect(NoSuchRegisteredTypeParserException.class);
        parser.parse("aaa", MyClass4.class);
    }

    @Test
    public void canParseTypeWithStaticFactoryMethodNamedValueOf() throws Exception {
        MyClass1 actual = parser.parse("aaa", MyClass1.class);
        assertThat(actual).isEqualTo(new MyClass1("aaa"));
    }

    @Test
    public void canParseTypeWithStaticFactoryMethodToNullValue() throws Exception {
        MyClass1 actual = parser.parse("null", MyClass1.class);
        assertThat(actual).isNull();
    }

    @Test
    public void canParseStringToStringType() throws Exception {
        String actual = parser.parse(" A B ", String.class);
        assertThat(actual).isEqualTo(" A B ");
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
                        return helper.prepareWithDefaultInputPreprocessor(input);
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
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Exception thrown from InputPreprocessor");
        thrown.expectMessage("some-message");

        // when
        parser = TypeParser.newBuilder()
                .setInputPreprocessor(new InputPreprocessor() {

                    @Override
                    public String prepare(String input, InputPreprocessorHelper helper) {
                        throw new RuntimeException("some-message");
                    }
                })
                .build();

        // then
        parser.parse(DUMMY_STRING, String.class);

    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfSplitStrategyThrows() throws Exception {
        // given
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Exception thrown from SplitStrategy");
        thrown.expectMessage("some-message");

        // when
        parser = TypeParser.newBuilder()
                .setSplitStrategy(new SplitStrategy() {

                    @Override
                    public List<String> split(String input, SplitStrategyHelper helper) {
                        throw new RuntimeException("some-message");
                    }
                })
                .build();

        // then
        parser.parse(DUMMY_STRING, new GenericType<List<Integer>>() {});

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
    public void shouldThrowIllegalArgumentExceptionIfKeyValueSplitStrategyThrows() throws Exception {
        // given
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Exception thrown from SplitStrategy");
        thrown.expectMessage("some-message");

        // when
        parser = TypeParser.newBuilder()
                .setKeyValueSplitStrategy(new SplitStrategy() {

                    @Override
                    public List<String> split(String input, SplitStrategyHelper helper) {
                        throw new RuntimeException("some-message");
                    }
                })
                .build();

        // then
        parser.parse("1=11,2=22", new GenericType<Map<Integer, Integer>>() {});

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
                .registerParserForTypesAssignableTo(List.class,
                        new Parser<List<Integer>>() {

                            @Override
                            public List<Integer> parse(String input, ParserHelper helper) {
                                /*
                                 * assert helper.split(input) returns empty string without
                                 * calling above registered SplitStrategy, since input is null.
                                 */
                                assertThat(helper.split(input)).isEmpty();
                                return null;
                            }
                        })
                .build();

        // when
        // This calls the above registered Parser
        assertThat(counter.getCount()).isEqualTo(1);
        parser.parse("null", new GenericType<List<String>>() {});
    }

}
