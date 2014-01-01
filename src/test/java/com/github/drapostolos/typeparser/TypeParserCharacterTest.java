package com.github.drapostolos.typeparser;

import org.junit.Test;

public class TypeParserCharacterTest extends AbstractTypeParserTestHelper{

    public TypeParserCharacterTest() {
        super(char.class, Character.class);
    }
    
    @Test
    public void shouldThrowExceptionWhenStringHasMoreThanOneCharacter() throws Exception {
        assertThat("aa").throwsIllegalArgumentException()
        .whereMessageEndsWih("\"%s\" must only contain a single character.", "aa");
    }

    @Test
    public void canParseStringToCharacterType() throws Exception {
        assertThat(" ").isParsedTo((char)' ');
        assertThat("a").isParsedTo(new Character('a'));
    }
}
