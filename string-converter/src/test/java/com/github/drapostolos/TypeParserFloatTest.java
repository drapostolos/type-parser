package com.github.drapostolos;

import org.junit.Test;

public class TypeParserFloatTest extends AbstractTestHelper{

    public TypeParserFloatTest() {
        super(float.class, Float.class);
    }
    
    @Test public void 
    shouldThrowExceptionWhenStringIsNotParsableToFloat() throws Exception {
        assertThat("aa").throwsIllegalArgumentException()
        .whereMessageEndsWih(NUMBER_FORMAT_ERROR_MSG, "aa");
    }

    @Test
    public void canParseStringToFloatType() throws Exception {
        assertThat("01.2").isParsedTo(1.2f);
        assertThat("1").isParsedTo(1f);
        assertThat("1d").isParsedTo(1f);
        assertThat("\t1d ").isParsedTo(1f);
        assertThat(".1").isParsedTo(0.1f);
    }


}
