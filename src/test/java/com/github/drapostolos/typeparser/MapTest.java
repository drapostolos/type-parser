package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.assertj.core.data.MapEntry;
import org.junit.Test;

public class MapTest extends TestBase {

    @Test
    public void canParseToConcurrentNavigableMap() throws Exception {
        assertThat(parser.parse("a=A", new GenericType<ConcurrentNavigableMap<String, String>>() {}))
                .contains(MapEntry.entry("a", "A"))
                .isInstanceOf(ConcurrentNavigableMap.class);
    }

    @Test
    public void canParseToConcurrentSkipListMap() throws Exception {
        assertThat(parser.parse("a=A", new GenericType<ConcurrentSkipListMap<String, String>>() {}))
                .contains(MapEntry.entry("a", "A"))
                .isInstanceOf(ConcurrentSkipListMap.class);
    }

    @Test
    public void canParseToConcurrentMap() throws Exception {
        assertThat(parser.parse("a=A", new GenericType<ConcurrentMap<String, String>>() {}))
                .contains(MapEntry.entry("a", "A"))
                .isInstanceOf(ConcurrentMap.class);
    }

    @Test
    public void canParseToSortedMap() throws Exception {
        assertThat(parser.parse("a=A", new GenericType<SortedMap<String, String>>() {}))
                .contains(MapEntry.entry("a", "A"))
                .isInstanceOf(SortedMap.class);
    }

    @Test
    public void canParseStringToEmptyMap() throws Exception {
        GenericType<Map<String, String>> type = new GenericType<Map<String, String>>() {};
        assertThat(parser.parse("null", type)).isEmpty();
    }

    @Test
    public void shouldThrowWhenMapImplementationHasNoDefaultconstructor() throws Exception {
        shouldThrowParseException()
                .withErrorMessage("Cannot instantiate map of type '")
                .withErrorMessage(MyMap.class.getName())
                .whenParsing(DUMMY_STRING)
                .to(new GenericType<MyMap<String, String>>() {});
    }

    @Test
    public void shouldThrowExceptionWhenParsingMapWithWildcardKey() throws Exception {
        shouldThrowParseException()
                .withErrorMessage("contains illegal type argument: '?' ")
                .whenParsing(DUMMY_STRING)
                .to(new GenericType<Map<?, String>>() {});
    }

    @Test
    public void shouldThrowExceptionWhenParsingMapWithWildcardValue() throws Exception {
        shouldThrowParseException()
                .withErrorMessage("contains illegal type argument: '?' ")
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

    @Test
    public void canParseStringToLinkedHashMap() throws Exception {
        GenericType<LinkedHashMap<Long, String>> type = new GenericType<LinkedHashMap<Long, String>>() {};

        assertThat(parser.parse("1=one", type)).containsKey(1l);
        assertThat(parser.parse("1=one", type)).containsValue("one");
    }

}
