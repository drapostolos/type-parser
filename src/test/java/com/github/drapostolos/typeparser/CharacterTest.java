package com.github.drapostolos.typeparser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class CharacterTest extends AbstractTypeTester<Character> {

    @Override
    Character make(String string) throws Exception {
        return string.charAt(0);
    }

    @Test
    public void canParseStringToCharacterType() throws Exception {
        canParse(" ").toType(Character.class);
        canParse("a").toType(char.class);
    }

    @Test
    public void shouldThrowExceptionWhenStringHasMoreThanOneCharacter() throws Exception {
        shouldThrowTypeParserException()
                .containingErrorMessage("\"%s\" must only contain a single character.", "aa")
                .whenParsing("aa")
                .to(Character.class, char.class);
    }

    @Test
    public void canParseToGenericCharacterArray() throws Exception {
        canParse("a,b,c").toGenericArray(new GenericType<Character[]>() {});
    }

    @Test
    public void canParseToCharacterArray() throws Exception {
        canParse("a,b,c").toArray(Character[].class);
    }

    @Test
    public void canParseToCharacterList() throws Exception {
        canParse("a,b,c").toArrayList(new GenericType<List<Character>>() {});
    }

    @Test
    public void canParseToCharacterSet() throws Exception {
        canParse("a,b,a").toLinkedHashSet(new GenericType<Set<Character>>() {});
    }

    @Test
    public void canParseToCharacterMap() throws Exception {
        canParse("a=A,b=B").toLinkedHashMap(new GenericType<Map<Character, Character>>() {});
    }

}
