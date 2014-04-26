package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

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
        assertThat(parser.isTargetTypeParsable(Class.class)).isTrue();
        assertThat(parser.parse(cls.getName(), Class.class)).hasSameClassAs(cls);
    }

    @Test
    public void shouldThrowWhenStringIsNotParsableToClass() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("\"com.unknow.Type\" is not parsable to a Class object.");
        parser.parse("com.unknow.Type", Class.class);
    }

    @Test
    public void canParseToGenericClassArray() throws Exception {
        canParse(stringToParse).toGenericArray(new GenericType<Class<?>[]>() {});
    }

    @Test
    public void canParseToArray() throws Exception {
        assertThat(parser.isTargetTypeParsable(Class[].class)).isTrue();
        assertThat(parser.parse(stringToParse, Class[].class))
                .containsOnly(cls, Long.class, cls);
    }

    @Test
    public void canParseToList() throws Exception {
        canParse(stringToParse).toList(new GenericType<List<Class<?>>>() {});
    }

    @Test
    public void canParseToSet() throws Exception {
        canParse(stringToParse).toSet(new GenericType<Set<Class<?>>>() {});
    }

    @Test
    public void canParseToMap() throws Exception {
        String str = String.format("%s=%s,java.lang.Long=java.lang.Short", cls.getName(), cls.getName());
        canParse(str).toMap(new GenericType<Map<Class<?>, Class<?>>>() {});
    }
}
