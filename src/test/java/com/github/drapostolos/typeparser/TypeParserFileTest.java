package com.github.drapostolos.typeparser;

import java.io.File;

import org.junit.Test;

public class TypeParserFileTest extends AbstractTypeParserTestHelper{
    
    public TypeParserFileTest() {
        super(File.class);
    }

    @Test
    public void canParseStringToFileType() throws Exception {
        assertThat("/path/to").isParsedTo(new File("/path/to"));
    }

}
