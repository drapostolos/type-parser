package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class TypeParserAssignableClassTest extends AbstractTest {

    @Test
    public void canParseSubclassOfAssignableTypeParser() throws Exception {
        // GIVEN
        TypeParser<MyBaseClass> typeParser = new TypeParser<MyBaseClass>() {

            @Override
            public MyBaseClass parse(String input, TypeParserHelper helper) {
                if (input.equals("1")) {
                    return new MyClass1();
                }
                return new MyClass2();
            }
        };
        StringToTypeParser parser = StringToTypeParser.newBuilder()
                .registerTypeParserForTypesAssignableTo(MyBaseClass.class, typeParser)
                .build();

        // THEN
        assertThat(parser.parse("1", MyClass1.class)).isInstanceOf(MyClass1.class);
        assertThat(parser.parse("2", MyClass2.class)).isInstanceOf(MyClass2.class);
        assertThat(parser.isTargetTypeParsable(MyClass1.class)).isTrue();
    }

}
