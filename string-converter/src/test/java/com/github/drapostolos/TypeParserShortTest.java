package com.github.drapostolos;

import org.junit.Test;

public class TypeParserShortTest extends AbstractTestHelper{

    public TypeParserShortTest() {
        super(Short.class, short.class);
    }

    
    @Test
    public void canParseStringToShortType() throws Exception {
        assertThat("3").isParsedTo((short) 3);
        assertThat(" 3\t").isParsedTo((short) 3);
    }
    
    @Test
    public void shouldThrowExceptionWhenStringIsNotParsableToShort() throws Exception {
        assertThat("a").throwsIllegalArgumentException()
        .whereMessageEndsWih(NUMBER_FORMAT_ERROR_MSG, "a");
    }

}
