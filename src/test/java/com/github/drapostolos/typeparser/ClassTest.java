package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class ClassTest extends AbstractTypeTester<Class<?>> {

    Class<?> cls = ClassTest.class;
    String stringToParse = String.format("%s, java.lang.Long, %s", cls.getName(), cls.getName());

    @Override
    Class<?> make(String string) throws Exception {
        return Class.forName(string.trim());
    }

    @Test
    public void canParseToGenericType() throws Exception {
        canParse(cls.getName());
        toGenericType(new GenericType<Class<?>>() {});
    }

    @Test
    public void canParseToGenericLongType() throws Exception {
        canParse("java.lang.Long");
        toGenericType(new GenericType<Class<Long>>() {});
    }

    @Test
    public void canParseToClass() throws Exception {
        assertThat(parser.parse(cls.getName(), Class.class)).isSameAs(cls);
    }

    @Test
    public void shouldThrowWhenStringIsNotParsableToClass() throws Exception {
        shouldThrowTypeParserException()
                .containingErrorMessage("Can not parse \"com.unknown.Type\"")
                .containingErrorMessage("{preprocessed: \"com.unknown.Type\"}")
                .containingErrorMessage("java.lang.Class")
                .containingErrorMessage("ClassNotFound: com.unknown.Type")
                .whenParsing("com.unknown.Type")
                .to(Class.class);
    }

    @Test
    public void canParseToGenericClassArray() throws Exception {
        canParse(stringToParse).toGenericArray(new GenericType<Class<?>[]>() {});
    }

    @Test
    public void canParseToArray() throws Exception {
        assertThat(parser.parse(stringToParse, Class[].class))
                .containsOnly(cls, Long.class, cls);
    }

    @Test
    public void canParseToList() throws Exception {
        canParse(stringToParse).toArrayList(new GenericType<List<Class<?>>>() {});
    }

    @Test
    public void canParseToSet() throws Exception {
        canParse(stringToParse).toLinkedHashSet(new GenericType<Set<Class<?>>>() {});
    }

    @Test
    public void canParseToMap() throws Exception {
        String str = String.format("%s=%s,java.lang.Long=java.lang.Short", cls.getName(), cls.getName());
        canParse(str).toLinkedHashMap(new GenericType<Map<Class<?>, Class<?>>>() {});
    }

    @Test
    public void canParseToNull() throws Exception {
        canParse("null").toNull(new GenericType<Class<?>>() {});
    }

}
