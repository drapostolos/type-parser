package com.github.drapostolos;

import org.junit.Test;

public class TypeParserLongTest extends AbstractTestHelper{

    public TypeParserLongTest() {
        super(Long.class, long.class);
    }

    
    @Test
    public void canParseStringToLongType() throws Exception {
        assertThat("3").isParsedTo(3l);
        assertThat(" 3\t").isParsedTo(3l);
    }
    
    @Test
    public void shouldThrowExceptionWhenStringIsNotParsableToLong() throws Exception {
        assertThat("a").throwsIllegalArgumentException()
        .whereMessageEndsWih(NUMBER_FORMAT_ERROR_MSG, "a");
    }

}
