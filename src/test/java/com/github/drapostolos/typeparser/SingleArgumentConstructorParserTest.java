package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;

public class SingleArgumentConstructorParserTest extends TestBase {

    @Test
    public void canParseTypeWhithConstructorThatTakesEnumArgument() throws Exception {
        Object o = parser.parse("A", WithEnumConstructor.class);
        assertThat(o).isInstanceOf(WithEnumConstructor.class);
    }

    private static class WithEnumConstructor {

        enum MyEnum {
            A, B, C
        }

        @SuppressWarnings("unused")
        WithEnumConstructor(MyEnum e) {}
    }

    @Test
    public void canParseTypeWhithConstructorThatTakesFileArgument() throws Exception {
        Object o = parser.parse("/some/path", WithFileConstructor.class);
        assertThat(o).isInstanceOf(WithFileConstructor.class);
    }

    private static class WithFileConstructor {

        @SuppressWarnings("unused")
        WithFileConstructor(File f) {}
    }

    @Test
    public void canParseTypeWhithConstructorThatTakesClassArgument() throws Exception {
        Object o = parser.parse("java.lang.Long", WithClassConstructor.class);
        assertThat(o).isInstanceOf(WithClassConstructor.class);
    }

    private static class WithClassConstructor {

        @SuppressWarnings("unused")
        WithClassConstructor(Class<?> f) {}
    }

    @Test
    public void canParseTypeWhithConstructorThatTakesClassWithValueOfMethod() throws Exception {
        Object o = parser.parse(DUMMY_STRING, WithValueOfConstructor.class);
        assertThat(o).isInstanceOf(WithValueOfConstructor.class);
    }

    static class WithValueOfConstructor {

        WithValueOfConstructor(WithValueOfMethod v) {}
    }

    static class WithValueOfMethod {

        static WithValueOfMethod valueOf(String s) {
            return new WithValueOfMethod();
        }
    }

    @Test
    public void shouldThrowExceptionWhenConstructorFails() throws Exception {
        shouldThrowTypeParserException()
                .containingErrorMessage("Failed when calling constructor")
                .whenParsing(DUMMY_STRING)
                .to(ThrowingConstructor.class);
    }

    public static class ThrowingConstructor {

        ThrowingConstructor(String s) {
            throw new RuntimeException(ERROR_MSG);
        }
    }

    @Test
    public void shouldThrowExceptionWhenCyclicConstructor() throws Exception {
        shouldThrowTypeParserException()
                .causedBy(StackOverflowError.class)
                .containingErrorMessage("Cyclic argument type for constructor")
                .containingErrorMessage("WithCyclicConstructor")
                .whenParsing(DUMMY_STRING)
                .to(WithCyclicConstructor.class);
    }

    public static class WithCyclicConstructor {

        WithCyclicConstructor(WithCyclicConstructor s) {}
    }

    @Test
    public void canParseTypeWithConstructorThatTakesStringArgument() throws Exception {
        Object actual = parser.parse(DUMMY_STRING, StringConstructor.class);
        assertThat(actual).isInstanceOf(StringConstructor.class);
    }

    public static class StringConstructor {

        public StringConstructor(String s) {}

    }

    @Test
    public void canParseTypeWithConstructorThatTakesObjectArgument() throws Exception {
        Object actual = parser.parse(DUMMY_STRING, ObjectConstructor.class);
        assertThat(actual).isInstanceOf(ObjectConstructor.class);
    }

    private static class ObjectConstructor {

        private ObjectConstructor(Object s) {}
    }

    @Test
    public void canParseTypeWithSingleArgumentConstructorToNullValue() throws Exception {
        Object actual = parser.parse("null", ObjectConstructor.class);
        assertThat(actual).isNull();
    }
}
