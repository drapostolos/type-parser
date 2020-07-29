package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.assertj.core.data.MapEntry;
import org.junit.Test;

public class MapTest extends TestBase {

    @SuppressWarnings("unchecked")
    @Test
    public void canParseToMapInterfaces() throws Exception {
        parseToMapInterfaces(
                new GenericType<ConcurrentMap<String, String>>() {},
                new GenericType<ConcurrentNavigableMap<String, String>>() {},
                new GenericType<NavigableMap<String, String>>() {},
                new GenericType<SortedMap<String, String>>() {});

    }

	private void parseToMapInterfaces(@SuppressWarnings("unchecked") GenericType<? extends Map<String, String>>... types) {
        for (GenericType<? extends Map<String, String>> type : types) {
            Class<?> rawType = toRawType(type);
            assertThat(parser.parse("a=A", type))
                    .containsOnly(MapEntry.entry("a", "A"))
                    .isInstanceOf(rawType);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void canParseToConcreteMapTypes() throws Exception {
        parseToConcreteMapTypes(
                new GenericType<ConcurrentHashMap<String, String>>() {},
                new GenericType<ConcurrentSkipListMap<String, String>>() {},
                new GenericType<HashMap<String, String>>() {},
                new GenericType<LinkedHashMap<String, String>>() {},
                new GenericType<WeakHashMap<String, String>>() {},
                new GenericType<IdentityHashMap<String, String>>() {},
                new GenericType<Hashtable<String, String>>() {},
                new GenericType<TreeMap<String, String>>() {}
                );

    }

    private void parseToConcreteMapTypes(@SuppressWarnings("unchecked") GenericType<? extends Map<String, String>>... types) {
        for (GenericType<? extends Map<String, String>> type : types) {
            Class<?> rawType = toRawType(type);
            Map<String, String> map = parser.parse("a=A", type);
            assertThat(map)
                    .containsOnly(MapEntry.entry("a", "A"))
                    .isInstanceOf(rawType);
            assertThat(map.getClass())
                    .isEqualTo(rawType);
        }
    }

    @Test
    public void canParseToEmptyMap() throws Exception {
        GenericType<Map<String, String>> type = new GenericType<Map<String, String>>() {};
        assertThat(parser.parse("null", type))
                .isInstanceOf(Map.class)
                .isEmpty();
    }

    @Test
    public void shouldThrowWhenMapImplementationHasNoDefaultconstructor() throws Exception {
        shouldThrowTypeParserException()
                .containingErrorMessage("Cannot instantiate map of type '")
                .containingErrorMessage(MyMap.class.getName())
                .whenParsing(DUMMY_STRING)
                .to(new GenericType<MyMap<String, String>>() {});
    }

    @Test
    public void shouldThrowExceptionWhenParsingMapWithWildcardKey() throws Exception {
        shouldThrowTypeParserException()
                .containingErrorMessage("contains illegal type argument: '?' ")
                .whenParsing(DUMMY_STRING)
                .to(new GenericType<Map<?, String>>() {});
    }

    @Test
    public void shouldThrowExceptionWhenParsingMapWithWildcardValue() throws Exception {
        shouldThrowTypeParserException()
                .containingErrorMessage("contains illegal type argument: '?' ")
                .whenParsing(DUMMY_STRING)
                .to(new GenericType<Map<String, ?>>() {});
    }

    @Test
    public void canChangeKeyValueSplitStrategy() throws Exception {
        // given
        TypeParser parser = TypeParser.newBuilder()
                .setKeyValueSplitStrategy(new SplitStrategy() {

                    @Override
                    public List<String> split(String input, SplitStrategyHelper helper) {
                        return Arrays.asList(input.split("#"));
                    }
                })
                .build();

        // when
        Map<String, String> map = parser.parse("aaa#AAA,bbb#BBB", new GenericType<Map<String, String>>() {});

        // then
        assertThat(map).hasSize(2);
        assertThat(map.get("aaa")).isEqualTo("AAA");
        assertThat(map.get("bbb")).isEqualTo("BBB");
    }

}
