package com.github.drapostolos.typeparser;


import org.fest.assertions.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.drapostolos.typeparser.StringToTypeParser;


public class StringToTypeParserTest {
    private StringToTypeParser parser = StringToTypeParser.newBuilder().build();
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
//	enum
	// File
    
    
    @Test(expected = IllegalArgumentException.class)
    public void canUnregisterDefaultTypeParsers() throws Exception {
        // given
        StringToTypeParser parser = StringToTypeParser.newBuilder()
        .unregisterTypeParser(int.class)
        .build();
        
        // then
        parser.parse("1", int.class);
    }

    @Test
    public void canRegisterAddionalTypeParsers() throws Exception {
        // given
        StringToTypeParser parser = StringToTypeParser.newBuilder()
        .registerTypeParser(TestClass1.class, new TestClass1())
        .build();
        
        // then
        TestClass1 actual = parser.parse("aaa", TestClass1.class);
        Assertions.assertThat(actual).isEqualTo(new TestClass1("aaa"));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenStaticFactoryMethodFails() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can not parse \"aaa\" to type 'com.github.drapostolos.typeparser.TestClass3' "
                + "due to:  Exception thrown in static factory method "
                + "'com.github.drapostolos.typeparser.TestClass3.valueOf('aaa')'. "
                + "See underlying exception for additional information.");
        parser.parse("aaa", TestClass3.class); 
    }

    @Test
    public void shouldThrowExceptionWhenTypeIsNotParsable() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can not parse \"aaa\" to type 'java.lang.Object' due to: "
                + "There is no registered 'TypeParser' for that type, or that type does not "
                + "contain one of the following static factory methods: 'Object.valueOf(String)', "
                + "or 'Object.of(String)'.");
        parser.parse("aaa", Object.class); 
    }

    @Test
    public void canParseTypeWithOfFactoryMethod() throws Exception {
        TestClass2 actual = parser.parse("aaa", TestClass2.class);
        Assertions.assertThat(actual).isEqualTo(new TestClass2("aaa"));
    }

    @Test
    public void canParseTypeWithValueOfFactoryMethod() throws Exception {
        TestClass1 actual = parser.parse("aaa", TestClass1.class);
        Assertions.assertThat(actual).isEqualTo(new TestClass1("aaa"));
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringNullTypeParser() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Argument named 'typeParser' is illegally set to null!");
        StringToTypeParser.newBuilder().registerTypeParser(int.class, null);
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringNullType() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Argument named 'type' is illegally set to null!");
        StringToTypeParser.newBuilder().registerTypeParser(null, new TestClass1());
    }

    @Test
    public void shouldThrowExceptionWhenUnregisteringNullTypeParser() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Argument named 'type' is illegally set to null!");
        StringToTypeParser.newBuilder().unregisterTypeParser(null);
    }

    @Test
	public void shouldThrowExceptionWhenFirstArgumentIsNullInStaticParseMethod() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Argument named 'value' is illegally set to null!");
		parser.parse(null, Object.class);
	}

	@Test
	public void shouldThrowExceptionWhenSecondArgumentIsNullInStaticParseMethod() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Argument named 'type' is illegally set to null!");
		parser.parse("dummy", null);
	}

	@Test
	public void canParseStringToStringType() throws Exception {
	    String actual = parser.parse(" A B ", String.class);
	    Assertions.assertThat(actual).isEqualTo(" A B ");
	}

}
