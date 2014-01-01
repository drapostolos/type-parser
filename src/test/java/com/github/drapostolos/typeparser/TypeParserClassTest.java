package com.github.drapostolos.typeparser;

import org.junit.Test;

public class TypeParserClassTest extends AbstractTypeParserTestHelper{
    
    public TypeParserClassTest() {
        super(Class.class);
    }

    @Test
    public void canParseStringToClass() throws Exception {
        assertThat(TypeParserClassTest.class.getName()).isParsedTo(TypeParserClassTest.class);
    }
    
    @Test public void 
    shouldThrowExceptionWhenStringIsNotParsableToClass() throws Exception {
        assertThat("com.unknow.Type").throwsIllegalArgumentException()
        .whereMessageEndsWih("\"com.unknow.Type\" is not parsable to a Class object.");
    }



}
