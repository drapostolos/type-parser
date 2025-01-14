package com.github.drapostolos.typeparser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class LocalDateTimeTest extends AbstractTypeTester<LocalDateTime> {

    private static final String DATE_TIMES = "2025-01-14T14:30:15, 2025-01-14T14:30:16, 2025-01-14T14:30:17";

    @Override
    LocalDateTime make(String string) throws Exception {
        return LocalDateTime.parse(string.trim());
    }

    @Test
    public void canParseStringToLocalDateTimeType() throws Exception {
        canParse("2025-01-14T14:30:15").toType(LocalDateTime.class);
        canParse("\t2025-01-14T14:30:15").toType(LocalDateTime.class);
        canParse("2025-01-14T14:30:15.123456789").toType(LocalDateTime.class);
        canParse("\t2025-01-14T14:30:15.123456789").toType(LocalDateTime.class);
    }

    @Test
    public void shouldThrowWhenWrongDate() throws Exception {
        shouldThrowTypeParserException()
            .containingErrorMessage("Can not parse \"2025-14-01T14:30:15\"")
            .containingErrorMessage("due to: Text '2025-14-01T14:30:15' could not be parsed:")
            .containingErrorMessage("Invalid value for MonthOfYear (valid values 1 - 12): 14")
            .whenParsing("2025-14-01T14:30:15")
            .to(LocalDateTime.class);
    }

    @Test
    public void shouldThrowWhenWrongTime() throws Exception {
        shouldThrowTypeParserException()
            .containingErrorMessage("Can not parse \"2025-01-14T14:300:15\"")
            .containingErrorMessage("due to: Text '2025-01-14T14:300:15' could not be parsed")
            .containingErrorMessage("unparsed text found at index 16")
            .whenParsing("2025-01-14T14:300:15")
            .to(LocalDateTime.class);
    }

    @Test
    public void shouldThrowExceptionWhenStringIsNotDateTime() throws Exception {
        shouldThrowTypeParserException()
            .containingErrorMessage("Can not parse \"asda\"")
            .containingErrorMessage("due to: Text 'asda' could not be parsed at index 0")
            .whenParsing("asda")
            .to(LocalDateTime.class);
    }

    @Test
    public void canParseToGenericLocalDateTimeArray() throws Exception {
        canParse(DATE_TIMES).toGenericArray(new GenericType<LocalDateTime[]>() {});
    }

    @Test
    public void canParseToLocalLocalDateTimeArray() throws Exception {
        canParse(DATE_TIMES).toArray(LocalDateTime[].class);
    }

    @Test
    public void canParseToLocalDateTimeList() throws Exception {
        canParse(DATE_TIMES).toArrayList(new GenericType<List<LocalDateTime>>() {});
    }

    @Test
    public void canParseToLocalDateTimeSet() throws Exception {
        canParse(
            "2025-01-14T14:30:15, 2025-01-14T14:30:16, 2025-01-14T14:30:16"
        ).toLinkedHashSet(new GenericType<Set<LocalDateTime>>() {});
    }

    @Test
    public void canParseToLocalDateTimeMap() throws Exception {
        canParse(
            "2025-01-14T14:30:15=2025-01-14T14:30:17, 2025-01-14T14:30:16=2025-01-14T14:30:17"
        ).toLinkedHashMap(new GenericType<Map<LocalDateTime, LocalDateTime>>() {});
    }
}
