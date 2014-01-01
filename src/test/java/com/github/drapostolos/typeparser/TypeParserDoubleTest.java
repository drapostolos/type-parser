package com.github.drapostolos.typeparser;

import org.junit.Test;

public class TypeParserDoubleTest extends AbstractTypeParserTestHelper{
    
    public TypeParserDoubleTest() {
        super(Double.class, double.class);
    }

    @Test public void 
    shouldThrowExceptionWhenStringIsNotParsableToDouble() throws Exception {
        assertThat("aa").throwsIllegalArgumentException()
        .whereMessageEndsWih(NUMBER_FORMAT_ERROR_MSG, "aa");
    }

    @Test
    public void canParseStringToDoubleType() throws Exception {
        assertThat("01.2").isParsedTo(1.2d);
        assertThat("1").isParsedTo(1d);
        assertThat("1d").isParsedTo(1d);
        assertThat("\t1d ").isParsedTo(1d);
        assertThat(".1").isParsedTo(0.1d);
    }

}
