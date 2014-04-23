package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

public class TypeParserClassTest extends AbstractTest {

    Class<?> cls = TypeParserClassTest.class;
    String stringToParse = String.format("%s,  %s", cls.getName(), cls.getName());

    @Test
    public void canParseToGenericType() throws Exception {
        assertThat(parser.isTargetTypeParsable(new GenericType<Class<?>>() {}))
                .isTrue();
        assertThat(parser.isTargetTypeParsable(new GenericType<Class<Long>>() {}))
                .isTrue();
        assertThat(parser.parse(cls.getName(), new GenericType<Class<?>>() {}))
                .hasSameClassAs(cls);
        assertThat(parser.parse("java.lang.Long", new GenericType<Class<Long>>() {}))
                .hasSameClassAs(Long.class);
    }

    @Test
    public void canParseToClass() throws Exception {
        assertThat(parser.isTargetTypeParsable(Class.class)).isTrue();
        assertThat(parser.parse(cls.getName(), Class.class)).hasSameClassAs(cls);
    }

    @Test
    public void
            shouldThrowExceptionWhenStringIsNotParsableToClass() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("\"com.unknow.Type\" is not parsable to a Class object.");
        parser.parse("com.unknow.Type", Class.class);
    }

    @Test
    public void canParseToGenericClassArray() throws Exception {
        assertThat(parser.isTargetTypeParsable(new GenericType<Class<?>[]>() {})).isTrue();
        assertThat(parser.parse(stringToParse, new GenericType<Class<?>[]>() {}))
                .containsExactly(cls, cls);
    }

    @Test
    public void canParseToArray() throws Exception {
        assertThat(parser.isTargetTypeParsable(Class[].class)).isTrue();
        assertThat(parser.parse(stringToParse, Class[].class))
                .containsOnly(cls, cls);
    }

    @Test
    public void canParseToList() throws Exception {
        assertThat(parser.isTargetTypeParsable(new GenericType<List<Class<?>>>() {})).isTrue();
        assertThat(parser.parse(stringToParse, new GenericType<List<Class<?>>>() {}))
                .containsExactly(cls, cls);
    }

    @Test
    public void canParseToSet() throws Exception {
        assertThat(parser.isTargetTypeParsable(new GenericType<Set<Class<?>>>() {})).isTrue();
        assertThat(parser.parse(stringToParse, new GenericType<Set<Class<?>>>() {}))
                .containsExactly(cls);
    }

    @Test
    public void canParseToMap() throws Exception {
        assertThat(parser.isTargetTypeParsable(new GenericType<Map<Class<?>, Class<?>>>() {})).isTrue();
        String str = String.format("%s=%s", cls.getName(), cls.getName());
        assertThat(parser.parse(str, new GenericType<Map<Class<?>, Class<?>>>() {}))
                .contains(MapEntry.entry(cls, cls))
                .hasSize(1);
    }

}
