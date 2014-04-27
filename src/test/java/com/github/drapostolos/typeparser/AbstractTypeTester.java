package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public abstract class AbstractTypeTester<T> extends TestBase {

    List<String> elements;
    String stringToParse;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    final AbstractTypeTester<T> shouldThrowWhenParsing(String toParse) {
        stringToParse = toParse;
        return this;
    }

    final AbstractTypeTester<T> canParse(String toParse) {
        stringToParse = toParse;
        this.elements = Arrays.asList(toParse.split(","));
        return this;
    }

    final String element(int index) {
        return elements.get(index);
    }

    private T[] allElements() throws Exception {
        int[] result = new int[elements.size()];
        for (int i = 0; i < elements.size(); i++) {
            result[i] = i;
        }
        return expectedElements(result);
    }

    final T[] expectedElements(int... indexes) throws Exception {
        T element = make(element(0));
        Class<?> type = element.getClass();
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(type, indexes.length);
        array[0] = element;
        for (int i = 1; i < indexes.length; i++) {
            array[i] = make(element(i));
        }
        return array;
    }

    abstract T make(String string) throws Exception;

    final public AbstractTypeTester<T> toTypeWithErrorMessage(Class<T> type, String message) {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(message);
        parser.parse(stringToParse, type);
        return this;
    }

    final public AbstractTypeTester<T> toTypeWithNumberFormatErrorMessage(Class<T> type) {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format(NUMBER_FORMAT_ERROR_MSG, stringToParse));
        parser.parse(stringToParse, type);
        return this;
    }

    final public void toType(Class<T> type) throws Exception {
        assertThat(parser.isTargetTypeParsable(type)).isTrue();
        assertThat(parser.parse(stringToParse, type)).isEqualTo(make(element(0)));
    }

    final public void toGenericType(GenericType<?> type) throws Exception {
        assertThat(parser.isTargetTypeParsable(type)).isTrue();
        assertThat(parser.parse(stringToParse, type))
                .hasSameClassAs(make(element(0)));
    }

    final public void toGenericArray(GenericType<T[]> type) throws Exception {
        assertThat(parser.isTargetTypeParsable(type)).isTrue();
        assertThat(parser.parse(stringToParse, type)).containsExactly(allElements());
    }

    final public void toArray(Class<T[]> type) throws Exception {
        assertThat(parser.isTargetTypeParsable(type)).isTrue();
        assertThat(parser.parse(stringToParse, type)).containsExactly(allElements());
    }

    final public void toList(GenericType<List<T>> type) throws Exception {
        assertThat(parser.isTargetTypeParsable(type)).isTrue();
        assertThat(parser.parse(stringToParse, type)).containsExactly(allElements());
    }

    final public void toSet(GenericType<Set<T>> type) throws Exception {
        assertThat(parser.isTargetTypeParsable(type)).isTrue();
        assertThat(parser.parse(stringToParse, type)).containsExactly(expectedElements(0, 1));
    }

    final public void toMap(GenericType<Map<T, T>> type) throws Exception {
        assertThat(parser.isTargetTypeParsable(type)).isTrue();
        assertThat(parser.parse(stringToParse, type))
                .contains(MapEntry.entry(key(0), value(0)))
                .contains(MapEntry.entry(key(1), value(1)))
                .hasSize(2);
    }

    private T key(int i) throws Exception {
        String str = element(i).split("=")[0];
        return make(str);
    }

    private T value(int i) throws Exception {
        String str = element(i).split("=")[1];
        return make(str);
    }
}
