package com.github.drapostolos.typeparser;

import org.junit.Test;

public class StringToNullTypeTest extends AbstractTypeParserTestHelper{

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
