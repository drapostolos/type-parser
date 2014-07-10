package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class StaticFactoryMethodParserTest extends TestBase {

    public static class WithStaticFactoryMethodLong {

        static WithStaticFactoryMethodLong valueOf(Long l) {
            return new WithStaticFactoryMethodLong();
        }

    }

    @Test
    public void canParseWhenArgumentIsAnySupportedType() throws Exception {
        Object o = parser.parse("123", WithStaticFactoryMethodLong.class);
        assertThat(o).isInstanceOf(WithStaticFactoryMethodLong.class);
    }

    @Test(expected = NoSuchRegisteredParserException.class)
    public void shouldThrowExceptionWhenFactoryMethodIsNotStatic() throws Exception {
        parser.parse(DUMMY_STRING, MyClass2.class);
    }

    @Test
    public void shouldThrowExceptionWhenStaticFactoryMethodFails() throws Exception {
        shouldThrowTypeParserException()
                .containingErrorMessage("Failed when calling static factory method ")
                .whenParsing(DUMMY_STRING)
                .to(MyClass3.class);
    }

    @Test
    public void canParseTypeWithStaticFactoryMethodNamedValueOf() throws Exception {
        MyClass1 actual = parser.parse(DUMMY_STRING, MyClass1.class);
        assertThat(actual).isEqualTo(new MyClass1(DUMMY_STRING));
    }

    @Test
    public void canParseTypeWithStaticFactoryMethodToNullValue() throws Exception {
        MyClass1 actual = parser.parse("null", MyClass1.class);
        assertThat(actual).isNull();
    }

}
