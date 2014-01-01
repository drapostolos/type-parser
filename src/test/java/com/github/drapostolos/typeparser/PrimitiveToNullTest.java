package com.github.drapostolos.typeparser;

import org.junit.Test;

public class PrimitiveToNullTest extends AbstractTypeParserTestHelper{

    public PrimitiveToNullTest() {
        super(boolean.class, byte.class, char.class, double.class, 
                int.class, float.class, long.class, short.class);
    }
    
    @Test
    public void shouldThrowExceptionWhenParsingStringToNullPrimitiveType() throws Exception {
        assertThat("null").throwsIllegalArgumentException()
        .whereMessageEndsWih("primitive can not be set to null.");
    }



}
