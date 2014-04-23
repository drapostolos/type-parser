package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

public class CharacterTest extends AbstractTest {

    @Test
    public void canParseStringToCharacterType() throws Exception {
        assertThat(parser.isTargetTypeParsable(Character.class)).isTrue();
        assertThat(parser.isTargetTypeParsable(char.class)).isTrue();
        assertThat(parser.parse(" ", Character.class)).isEqualTo(' ');
        assertThat(parser.parse("a", char.class)).isEqualTo('a');
    }

    @Test
    public void shouldThrowExceptionWhenStringHasMoreThanOneCharacter() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("\"%s\" must only contain a single character.", "aa"));
        parser.parse("aa", Character.class);
    }

    @Test
    public void canParseToGenericCharacterArray() throws Exception {
        assertThat(parser.isTargetTypeParsable(new GenericType<Character[]>() {})).isTrue();
        assertThat(parser.parse("a,b,c", new GenericType<Character[]>() {}))
                .containsExactly('a', 'b', 'c');
    }

    @Test
    public void canParseToCharacterArray() throws Exception {
        assertThat(parser.isTargetTypeParsable(char[].class)).isTrue();
        assertThat(parser.parse("a,b,c", char[].class))
                .containsOnly('a', 'b', 'c');
    }

    @Test
    public void canParseToCharacterList() throws Exception {
        assertThat(parser.isTargetTypeParsable(new GenericType<List<Character>>() {})).isTrue();
        assertThat(parser.parse("a,b,c", new GenericType<List<Character>>() {}))
                .containsExactly('a', 'b', 'c');
    }

    @Test
    public void canParseToCharacterSet() throws Exception {
        assertThat(parser.isTargetTypeParsable(new GenericType<Set<Character>>() {})).isTrue();
        assertThat(parser.parse("a,b,a", new GenericType<Set<Character>>() {}))
                .containsExactly('a', 'b');
    }

    @Test
    public void canParseToCharacterMap() throws Exception {
        assertThat(parser.isTargetTypeParsable(new GenericType<Map<Character, Character>>() {})).isTrue();
        assertThat(parser.parse("a=A,b=B", new GenericType<Map<Character, Character>>() {}))
                .contains(MapEntry.entry('a', 'A'))
                .contains(MapEntry.entry('b', 'B'))
                .hasSize(2);
    }

}
