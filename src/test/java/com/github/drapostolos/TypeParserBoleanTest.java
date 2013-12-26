package com.github.drapostolos;

import org.junit.Test;

public class TypeParserBoleanTest extends AbstractTestHelper{
    private static final String NOT_PARSABLE_TO_BOOLEAN = "\"%s\" is not parsable to a Boolean.";
    
    public TypeParserBoleanTest() {
        super(Boolean.class, boolean.class);
    }

    @Test
    public void canParseStringToTrue() throws Exception {
        assertThat("true").isParsedTo(true);
        assertThat(" true\t").isParsedTo(true);
        assertThat("1").isParsedTo(true);
        assertThat("1 ").isParsedTo(true);
    }

    @Test
    public void canParseStringToFalse() throws Exception {
        assertThat("false").isParsedTo(false);
        assertThat(" false ").isParsedTo(false);
        assertThat("0").isParsedTo(false);
        assertThat(" 0 ").isParsedTo(false);
    }

    @Test
    public void shouldThrowExceptionWhenStringIsNotParsableToBoolean() throws Exception {
        assertThat("ttrue ").throwsIllegalArgumentException()
        .whereMessageEndsWih(NOT_PARSABLE_TO_BOOLEAN, "ttrue ");
        assertThat("12 3").throwsIllegalArgumentException()
        .whereMessageEndsWih(NOT_PARSABLE_TO_BOOLEAN, "12 3");
    }
}
