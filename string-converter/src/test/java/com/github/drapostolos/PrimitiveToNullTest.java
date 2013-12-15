package com.github.drapostolos;

import org.junit.Test;

public class PrimitiveToNullTest extends AbstractTestHelper{

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
