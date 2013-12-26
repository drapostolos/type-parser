package com.github.drapostolos;

import org.junit.Test;

public class StringToNullTypeTest extends AbstractTestHelper{

    public StringToNullTypeTest() {
        super(Boolean.class, Byte.class, Character.class, Double.class, 
                Integer.class, Float.class, Long.class, Short.class);
    }
    
    /*
     * Will test for all types defined above.
     */
    @Test
    public void canParseStringToNull() throws Exception {
        assertThat("null").isParsedTo(null);
    }



}
