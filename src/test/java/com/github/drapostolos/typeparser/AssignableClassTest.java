package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class AssignableClassTest extends AbstractTest {

    @Test
    public void canParseSubclassOfAssignableTypeParser() throws Exception {
        // GIVEN
        StringToTypeParser<MyBaseClass> typeParser = new StringToTypeParser<MyBaseClass>() {

            @Override
            public MyBaseClass parse(String input, StringToTypeParserHelper helper) {
                if (input.equals("1")) {
                    return new MyClass1();
                }
                return new MyClass2();
            }
        };
        TypeParser parser = TypeParser.newBuilder()
                .registerTypeParserForTypesAssignableTo(MyBaseClass.class, typeParser)
                .build();

        // THEN
        assertThat(parser.parse("1", MyClass1.class)).isInstanceOf(MyClass1.class);
        assertThat(parser.parse("2", MyClass2.class)).isInstanceOf(MyClass2.class);
        assertThat(parser.isTargetTypeParsable(MyClass1.class)).isTrue();
    }

}
