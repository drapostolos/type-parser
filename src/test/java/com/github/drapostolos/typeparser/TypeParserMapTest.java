package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TypeParserMapTest extends AbstractTest {

    @Test
    public void canParseStringToEmptyMap() throws Exception {
        GenericType<Map<String, String>> type = new GenericType<Map<String, String>>() {};
        assertThat(parser.parse("null", type)).isEmpty();
    }

    @Test
    public void shouldThrowExceptionWhenParsingMapWithWildcardKey() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can not parse \"dummy-string\" to type");
        thrown.expectMessage("contains the following illegal type argument: '?' ");
        parser.parse(DUMMY_STRING, new GenericType<Map<?, String>>() {});
    }

    @Test
    public void shouldThrowExceptionWhenParsingMapWithWildcardValue() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can not parse \"dummy-string\" to type");
        thrown.expectMessage("contains the following illegal type argument: '?' ");
        parser.parse(DUMMY_STRING, new GenericType<Map<String, ?>>() {});
    }

    @Test
    public void canChangeKeyValueSplitStrategy() throws Exception {
        // given
        StringToTypeParser parser = StringToTypeParser.newBuilder()
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
    public void canParseStringToLinkedHashMapList() throws Exception {
        GenericType<LinkedHashMap<Long, String>> type = new GenericType<LinkedHashMap<Long, String>>() {};
        assertThat(parser.parse("1=one", type)).containsKey(1l);
        assertThat(parser.parse("1=one", type)).containsValue("one");
    }

}
