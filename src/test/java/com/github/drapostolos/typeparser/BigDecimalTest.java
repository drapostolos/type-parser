package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

public class BigDecimalTest extends AbstractTest {

    @Test
    public void canParseStringToBigDecimalType() throws Exception {
        assertThat(parser.isTargetTypeParsable(BigDecimal.class)).isTrue();
        assertThat(parser.parse("55", BigDecimal.class)).isEqualTo(new BigDecimal("55"));
        assertThat(parser.parse("\t55 ", BigDecimal.class)).isEqualTo(new BigDecimal("55"));
    }

    @Test
    public void shouldThrowExceptionWhenStringIsNotABigDecimalByte() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format(NUMBER_FORMAT_ERROR_MSG, "aaa"));
        parser.parse("aaa", BigDecimal.class);
    }

    @Test
    public void canParseToGenericBigDecimalArray() throws Exception {
        assertThat(parser.isTargetTypeParsable(new GenericType<BigDecimal[]>() {})).isTrue();
        assertThat(parser.parse("55, 45, 35", new GenericType<BigDecimal[]>() {}))
                .containsExactly(new BigDecimal("55"), new BigDecimal("45"), new BigDecimal("35"));
    }

    @Test
    public void canParseToBigDecimalacerArray() throws Exception {
        assertThat(parser.isTargetTypeParsable(BigDecimal[].class)).isTrue();
        assertThat(parser.parse("55, 45,35", BigDecimal[].class))
                .containsOnly(new BigDecimal("55"), new BigDecimal("45"), new BigDecimal("35"));
    }

    @Test
    public void canParseToBigDecimalList() throws Exception {
        assertThat(parser.isTargetTypeParsable(new GenericType<List<BigDecimal>>() {})).isTrue();
        assertThat(parser.parse("55, 45,35", new GenericType<List<BigDecimal>>() {}))
                .containsExactly(new BigDecimal("55"), new BigDecimal("45"), new BigDecimal("35"));
    }

    @Test
    public void canParseToBigDecimalSet() throws Exception {
        assertThat(parser.isTargetTypeParsable(new GenericType<Set<BigDecimal>>() {})).isTrue();
        assertThat(parser.parse("55, 45, 55", new GenericType<Set<BigDecimal>>() {}))
                .containsExactly(new BigDecimal("55"), new BigDecimal("45"));
    }

    @Test
    public void canParseToBigDecimalMap() throws Exception {
        assertThat(parser.isTargetTypeParsable(new GenericType<Map<BigDecimal, BigDecimal>>() {})).isTrue();
        assertThat(parser.parse("5=55, 6=66", new GenericType<Map<BigDecimal, BigDecimal>>() {}))
                .contains(MapEntry.entry(new BigDecimal("5"), new BigDecimal("55")))
                .contains(MapEntry.entry(new BigDecimal("6"), new BigDecimal("66")))
                .hasSize(2);
    }

}
