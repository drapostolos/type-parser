package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class StringToTypeParserTest extends AbstractTest {

    @Test
    public void canNotParseTheseTypesByDefault() throws Exception {
        assertThat(parser.isTargetTypeParsable(Math.class)).isFalse();
        assertThat(parser.isTargetTypeParsable(StringToTypeParser.class)).isFalse();
        assertThat(parser.isTargetTypeParsable(StringToTypeParserBuilder.class)).isFalse();
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
    public void canUnregisterTypeParserForTypesAssignableToSets() throws Exception {
        // given
        GenericType<Set<Integer>> type = new GenericType<Set<Integer>>() {};
        assertThat(parser.parse("1,2", type)).containsExactly(1, 2);

        // when
        StringToTypeParser parser = StringToTypeParser.newBuilder()
                .unregisterTypeParserForTypesAssignableTo(LinkedHashSet.class)
                .build();

        // then 
        parser.parse("1,2", type);
    }

    @Test(expected = NoSuchRegisteredTypeParserException.class)
    public void canUnregisterDefaultTypeParserByClass() throws Exception {
        // given
        assertThat(parser.parse("1", int.class)).isEqualTo(1);

        // when
        StringToTypeParser parser = StringToTypeParser.newBuilder()
                .unregisterTypeParser(int.class)
                .build();

        // then 
        parser.parse("1", int.class);
    }

    @Test
    public void canRegisterTypeParserByClass() throws Exception {
        // given
        StringToTypeParser parser = StringToTypeParser.newBuilder()
                .registerTypeParser(MyClass1.class, new MyClass1())
                .build();

        // then
        MyClass1 actual = parser.parse("aaa", MyClass1.class);
        assertThat(actual).isEqualTo(new MyClass1("aaa"));
    }

    @Test
    public void canRegisterTypeParserByGenericType() throws Exception {
        // given
        StringToTypeParser parser = StringToTypeParser.newBuilder()
                .registerTypeParser(new GenericType<MyClass1>() {}, new MyClass1())
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
        thrown.expectMessage("must be a parameterized type when calling this method, but it is not.");
        parser = StringToTypeParser.newBuilder()
                .registerTypeParser(MyClass1.class, new TypeParser<MyClass1>() {

                    public MyClass1 parse(String input, TypeParserHelper helper) {
                        TypeParserUtility.getParameterizedTypeArguments(helper.getTargetType());
                        return null;
                    }
                })
                .build();

        parser.parse("aaa", MyClass1.class);
    }

    @Test
    public void shouldThrowExceptionWhenNonArrayIsExpectedToBeArray() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(MyClass1.class.toString());
        thrown.expectMessage("is either not an array or the componet type is generic.");
        parser = StringToTypeParser.newBuilder()
                .registerTypeParser(MyClass1.class, new TypeParser<MyClass1>() {

                    public MyClass1 parse(String input, TypeParserHelper helper) {
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
        parser = StringToTypeParser.newBuilder()
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
        parser = StringToTypeParser.newBuilder()
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

}
