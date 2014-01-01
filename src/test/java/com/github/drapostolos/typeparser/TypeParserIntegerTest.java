package com.github.drapostolos.typeparser;

import org.junit.Test;

public class TypeParserIntegerTest extends AbstractTypeParserTestHelper{

    public TypeParserIntegerTest() {
        super(Integer.class, int.class);
    }

    
    @Test
    public void canParseStringToIntegerType() throws Exception {
        assertThat("3").isParsedTo(3);
        assertThat(" 3\t").isParsedTo(3);
    }
    
    @Test
    public void shouldThrowExceptionWhenStringIsNotParsableToInteger() throws Exception {
        assertThat("a").throwsIllegalArgumentException()
        .whereMessageEndsWih(NUMBER_FORMAT_ERROR_MSG, "a");
    }

}
