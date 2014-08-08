package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.assertj.core.data.MapEntry;

public abstract class AbstractTypeTester<T> extends TestBase {

    List<String> elements;

    final AbstractTypeTester<T> canParse(String toParse) {
        stringToParse = toParse;
        this.elements = Arrays.asList(stringToParse.split(","));
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
        Class<?> type = getClass().getDeclaredMethod("make", String.class).getReturnType();
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(type, indexes.length);
        for (int i = 0; i < indexes.length; i++) {
            array[i] = make(element(i));
        }
        return array;
    }

    abstract T make(String string) throws Exception;

    final public void toType(Class<T> type) throws Exception {
        assertThat(parser.parse(stringToParse, type)).isEqualTo(make(element(0)));
    }

    final public void toGenericType(GenericType<?> type) throws Exception {
        assertThat(parser.parse(stringToParse, type))
                .isSameAs(make(element(0)));
    }

    final public void toGenericArray(GenericType<T[]> type) throws Exception {
        assertThat(parser.parse(stringToParse, type)).containsExactly(allElements());
    }

    final public void toArray(Class<T[]> type) throws Exception {
        assertThat(parser.parse(stringToParse, type)).containsExactly(allElements());
    }

    final public void toArrayList(GenericType<List<T>> type) throws Exception {
        assertThat(parser.parse(stringToParse, type))
                .hasSameClassAs(new ArrayList<T>())
                .containsExactly(allElements());
    }

    final public void toLinkedHashSet(GenericType<Set<T>> type) throws Exception {
        assertThat(parser.parse(stringToParse, type))
                .hasSameClassAs(new LinkedHashSet<T>())
                .containsExactly(expectedElements(0, 1));
    }

    final public void toLinkedHashMap(GenericType<Map<T, T>> type) throws Exception {
        assertThat(parser.parse(stringToParse, type))
                .hasSameClassAs(new LinkedHashMap<T, T>())
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

    public void toNull(Class<T> type) {
        assertThat(parser.parse(stringToParse, type)).isNull();
    }

    public void toNull(GenericType<Class<?>> genericType) {
        assertThat(parser.parse(stringToParse, genericType)).isNull();
    }
}
