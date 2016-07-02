package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class StaticFactoryMethodParserTest extends TestBase {

    @Test
    public void canParseTypeWhitStaticFactoryMethodThatTakesNumberArgument() throws Exception {
        Object o = parser.parse("123", WithStaticFactoryMethodTakingANumber.class);
        assertThat(o).isInstanceOf(WithStaticFactoryMethodTakingANumber.class);
    }

    static class WithStaticFactoryMethodTakingANumber {

        static WithStaticFactoryMethodTakingANumber staticFactoryMethod(Number e) {
            return new WithStaticFactoryMethodTakingANumber();
        }
    }

    @Test
    public void canParseTypeWhitValueOfMethodThatTakesLongArgument() throws Exception {
        Object o = parser.parse("123", WithValueOfMethodTakingLongArgument.class);
        assertThat(o).isInstanceOf(WithValueOfMethodTakingLongArgument.class);
    }

    static class WithValueOfMethodTakingLongArgument {

        static WithValueOfMethodTakingLongArgument valueOf(Long l) {
            return new WithValueOfMethodTakingLongArgument();
        }
    }

    @Test
    public void canParseTypeWhitValueOfMethodThatTakesEnumArgument() throws Exception {
        Object o = parser.parse("B", WithValueOfMethodTakingEnumArgument.class);
        assertThat(o).isInstanceOf(WithValueOfMethodTakingEnumArgument.class);
    }

    static class WithValueOfMethodTakingEnumArgument {

        enum MyEnum {
            A, B, C
        }

        static WithValueOfMethodTakingEnumArgument valueOf(MyEnum e) {
            return new WithValueOfMethodTakingEnumArgument();
        }
    }

    @Test
    public void canParseTypeWhitValueOfMethodThatTakesClassArgument() throws Exception {
        Object o = parser.parse("java.lang.Long", WithValueOfMethodTakingClassArgument.class);
        assertThat(o).isInstanceOf(WithValueOfMethodTakingClassArgument.class);
    }

    static class WithValueOfMethodTakingClassArgument {

        static WithValueOfMethodTakingClassArgument valueOf(Class<?> e) {
            return new WithValueOfMethodTakingClassArgument();
        }
    }

    @Test
    public void canParseTypeWhitValueOfMethodThatTakesClassWithSingleArgumentConstructor() throws Exception {
        Object o = parser.parse(DUMMY_STRING, WithValueOfMethodTakingSingleValueConstructor.class);
        assertThat(o).isInstanceOf(WithValueOfMethodTakingSingleValueConstructor.class);
    }

    static class WithValueOfMethodTakingSingleValueConstructor {

        static WithValueOfMethodTakingSingleValueConstructor valueOf(WithSingleArgumentConstructor e) {
            return new WithValueOfMethodTakingSingleValueConstructor();
        }
    }

    static class WithSingleArgumentConstructor {

        public WithSingleArgumentConstructor(String s) {}
    }

    @Test
    public void shouldThrowExceptionWhenCyclicValueOfMethod() throws Exception {
        shouldThrowTypeParserException()
                .causedBy(StackOverflowError.class)
                .containingErrorMessage("Cyclic argument type for method")
                .containingErrorMessage("static")
                .containingErrorMessage("WithCyclicValueOfMethod.valueOf")
                .whenParsing(DUMMY_STRING)
                .to(WithCyclicValueOfMethod.class);
    }

    static class WithCyclicValueOfMethod {

        static WithCyclicValueOfMethod valueOf(StaticFactoryMethodParserTest s) {
            return null; // This triggers an if clause that "continue"s the loop.  
        }
        static WithCyclicValueOfMethod valueOf(WithCyclicValueOfMethod s) {
            return new WithCyclicValueOfMethod();
        }
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
    public void canParseTypeWithValueOfMethodThatTakesStringArgument() throws Exception {
        MyClass1 actual = parser.parse(DUMMY_STRING, MyClass1.class);
        assertThat(actual).isEqualTo(new MyClass1(DUMMY_STRING));
    }

    @Test
    public void canParseTypeWithValueOfMethodThatTakesStringArgumentToNullValue() throws Exception {
        MyClass1 actual = parser.parse("null", MyClass1.class);
        assertThat(actual).isNull();
    }

    @Test
    public void canParseTypeWithValueOfMethodThatTakesObjectArgument() throws Exception {
        Object actual = parser.parse(DUMMY_STRING, WithValueOfMethodTakingObjectArgument.class);
        assertThat(actual).isInstanceOf(WithValueOfMethodTakingObjectArgument.class);
    }

    static class WithValueOfMethodTakingObjectArgument {

        static WithValueOfMethodTakingObjectArgument valueOf(Object s) {
            return new WithValueOfMethodTakingObjectArgument();
        }
    }

}
