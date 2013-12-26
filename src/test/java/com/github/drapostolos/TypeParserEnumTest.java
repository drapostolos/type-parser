package com.github.drapostolos;

import org.junit.Test;

public class TypeParserEnumTest extends AbstractTestHelper{
    
    public TypeParserEnumTest() {
        super(MyEnum.class);
    }

    public enum MyEnum {AAA, BBB, CCC};
    
    @Test public void 
    shouldThrowExceptionWhenStringIsNotParsableToEnum() throws Exception {
        assertThat("DDD").throwsIllegalArgumentException()
        .whereMessageEndsWih("TypeParserEnumTest$MyEnum.valueOf('DDD')'. See underlying exception for additional information.");
    }

    @Test
    public void canParseStringToEnumType() throws Exception {
        assertThat("AAA").isParsedTo(MyEnum.AAA);
        assertThat("BBB ").isParsedTo(MyEnum.BBB);
        assertThat(" CCC\t").isParsedTo(MyEnum.CCC);
    }

}
