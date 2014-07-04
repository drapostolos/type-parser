package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;

public class SingleArgumentConstructorParserTest extends TestBase {

    @Test
    public void canParseWhenArgumentIsAnySupportedType() throws Exception {
        Object o = parser.parse("/some/path", FileConstructor.class);
        assertThat(o).isInstanceOf(FileConstructor.class);
    }

    private static class FileConstructor {

        @SuppressWarnings("unused")
        FileConstructor(File f) {}

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
    public void canParseTypeWithStringConstructor() throws Exception {
        Object actual = parser.parse(DUMMY_STRING, StringConstructor.class);
        assertThat(actual).isInstanceOf(StringConstructor.class);
    }

    public static class StringConstructor {

        public StringConstructor(String s) {}

    }

    @Test
    public void canParseTypeWithObjectConstructor() throws Exception {
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
