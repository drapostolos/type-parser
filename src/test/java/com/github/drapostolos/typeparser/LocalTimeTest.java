package com.github.drapostolos.typeparser;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class LocalTimeTest extends AbstractTypeTester<LocalTime> {

    private static final String TIMES = "14:30:15, 14:30:16, 14:30:17";

    @Override
    LocalTime make(String string) throws Exception {
        return LocalTime.parse(string.trim());
    }

    @Test
    public void canParseStringToLocalTimeType() throws Exception {
        canParse("14:30:15").toType(LocalTime.class);
        canParse("\t14:30:15").toType(LocalTime.class);
        canParse("14:30:15.123456789").toType(LocalTime.class);
        canParse("\t14:30:15.123456789").toType(LocalTime.class);
    }

    @Test
    public void shouldThrowWhenWrongTime() throws Exception {
        shouldThrowTypeParserException()
            .containingErrorMessage("Can not parse \"140:30:15\"")
            .containingErrorMessage("due to: Text '140:30:15' could not be parsed at index 2")
            .whenParsing("140:30:15")
            .to(LocalTime.class);
    }

    @Test
    public void shouldThrowExceptionWhenStringIsNotTime() throws Exception {
        shouldThrowTypeParserException()
            .containingErrorMessage("Can not parse \"asda\"")
            .containingErrorMessage("due to: Text 'asda' could not be parsed at index 0")
            .whenParsing("asda")
            .to(LocalTime.class);
    }

    @Test
    public void canParseToGenericLocalTimeArray() throws Exception {
        canParse(TIMES).toGenericArray(new GenericType<LocalTime[]>() {});
    }

    @Test
    public void canParseToLocalLocalTimeArray() throws Exception {
        canParse(TIMES).toArray(LocalTime[].class);
    }

    @Test
    public void canParseToLocalTimeList() throws Exception {
        canParse(TIMES).toArrayList(new GenericType<List<LocalTime>>() {});
    }

    @Test
    public void canParseToLocalTimeSet() throws Exception {
        canParse("14:30:15, 14:30:16, 14:30:16").toLinkedHashSet(new GenericType<Set<LocalTime>>() {});
    }

    @Test
    public void canParseToLocalTimeMap() throws Exception {
        canParse("14:30:15=14:30:17, 14:30:16=14:30:17")
            .toLinkedHashMap(new GenericType<Map<LocalTime, LocalTime>>() {});
    }
}
