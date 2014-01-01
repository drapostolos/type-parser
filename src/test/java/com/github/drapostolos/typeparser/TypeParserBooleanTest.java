package com.github.drapostolos.typeparser;

import org.junit.Test;

public class TypeParserBooleanTest extends AbstractTypeParserTestHelper{
    private static final String NOT_PARSABLE_TO_BOOLEAN = "\"%s\" is not parsable to a Boolean.";
    
    public TypeParserBooleanTest() {
        super(Boolean.class, boolean.class);
    }

    @Test
    public void canParseStringToTrue() throws Exception {
        assertThat("true").isParsedTo(true);
        assertThat(" true\t").isParsedTo(true);
    }

    @Test
    public void canParseStringToFalse() throws Exception {
        assertThat("false").isParsedTo(false);
        assertThat(" false ").isParsedTo(false);
    }

    @Test
    public void shouldThrowExceptionWhenStringIsNotParsableToBoolean() throws Exception {
        assertThat("ttrue ").throwsIllegalArgumentException()
        .whereMessageEndsWih(NOT_PARSABLE_TO_BOOLEAN, "ttrue ");
        assertThat("12 3").throwsIllegalArgumentException()
        .whereMessageEndsWih(NOT_PARSABLE_TO_BOOLEAN, "12 3");
    }
}
