package com.github.drapostolos.typeparser;


import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.junit.Test;


public class StringToTypeParserTest extends AbstractTest{

    @Test
    public void shouldThrowExceptionWhenParsingPrimitiveToNullValue() throws Exception {
        Class<?>[] classes = {boolean.class, byte.class, char.class, double.class, int.class, 
                float.class, long.class, short.class};
        for(Class<?> c : classes){
            try {
                parser.parse("null", c);
                fail(String.format("Primitive type '%s' is set to null, when it should not be possible"));
            } catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void canParseWrapperClassToNullValue() throws Exception {
        Class<?>[] classes = {Boolean.class, Byte.class, Character.class, Double.class, 
                Integer.class, Float.class, Long.class, Short.class};
        for(Class<?> c : classes){
            assertThat(parser.parse("null", c)).isNull();
        }
    }

    @Test
    public void shouldThrowExceptionWhenParsingToUnknownType() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("Can not parse \"%s\" to type \"K\" ", DUMMY_STRING));
        thrown.expectMessage("[instance of: sun.reflect.generics.reflectiveObjects.TypeVariableImpl]");
        thrown.expectMessage("due to: There is either no registered 'TypeParser' for that type,");
        thrown.expectMessage("or that type does not contain the following static factory method: 'K.valueOf(String)'.");
        Type type = Map.class.getTypeParameters()[0];
        parser.parseType(DUMMY_STRING, type);
    }

    @Test
    public void shouldThrowExceptionWhenFactoryMethodIsNotStatic() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("does not contain the following static factory method: ");
        thrown.expectMessage("TestClass2.valueOf(String)'");
        parser.parse(DUMMY_STRING, TestClass2.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void canUnregisterDefaultTypeParserByGenericType() throws Exception {
        // given
        GenericType<List<Integer>> type = new GenericType<List<Integer>>(){};
        assertThat(parser.parse("1,2", type)).containsExactly(1, 2);
        
        // when
        StringToTypeParser parser = StringToTypeParser.newBuilder()
                .unregisterTypeParser(new GenericType<List<?>>() {})
                .build();

        // then 
        parser.parse("1,2", type);
    }

    @Test(expected = IllegalArgumentException.class)
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
                .registerTypeParser(TestClass1.class, new TestClass1())
                .build();

        // then
        TestClass1 actual = parser.parse("aaa", TestClass1.class);
        assertThat(actual).isEqualTo(new TestClass1("aaa"));
    }

    @Test
    public void canRegisterTypeParserByGenericType() throws Exception {
        // given
        StringToTypeParser parser = StringToTypeParser.newBuilder()
                .registerTypeParser(new GenericType<TestClass1>() {}, new TestClass1())
                .build();

        // then
        TestClass1 actual = parser.parse("aaa", TestClass1.class);
        assertThat(actual).isEqualTo(new TestClass1("aaa"));
    }

    @Test
    public void shouldThrowExceptionWhenStaticFactoryMethodFails() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Exception thrown in static factory method ");
        parser.parse("aaa", TestClass3.class); 
    }

    @Test
    public void shouldThrowExceptionWhenNonParameterizedTypeIsExpectedToBeParameterized() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(TestClass1.class.toString());
        thrown.expectMessage("must be a parameterized type when calling this method, but it is not.");
        parser = StringToTypeParser.newBuilder()
                .registerTypeParser(TestClass1.class, new TypeParser<TestClass1>() {
                    public TestClass1 parse(String input, ParseHelper helper) {
                        helper.getParameterizedTypeArguments();
                        return null;
                    }
                })
                .build();
        
        parser.parse("aaa", TestClass1.class); 
    }

    @Test
    public void shouldThrowExceptionWhenNonArrayIsExpectedToBeArray() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(TestClass1.class.toString());
        thrown.expectMessage("is either not an array or the componet type is generic.");
        parser = StringToTypeParser.newBuilder()
                .registerTypeParser(TestClass1.class, new TypeParser<TestClass1>() {
                    public TestClass1 parse(String input, ParseHelper helper) {
                        helper.getComponentClass();
                        return null;
                    }
                })
                .build();
        
        parser.parse("aaa", TestClass1.class); 
    }

    @Test
    public void canParseTypeWithStaticFactoryMethodNamedValueOf() throws Exception {
        TestClass1 actual = parser.parse("aaa", TestClass1.class);
        assertThat(actual).isEqualTo(new TestClass1("aaa"));
    }

    @Test
    public void canParseStringToStringType() throws Exception {
        String actual = parser.parse(" A B ", String.class);
        assertThat(actual).isEqualTo(" A B ");
    }

}
