package com.github.drapostolos.typeparser;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class LocalDateTest extends AbstractTypeTester<LocalDate> {

    private static final String DATES = "2025-01-14, 2025-01-15, 2025-01-16";

    @Override
    LocalDate make(String string) throws Exception {
        return LocalDate.parse(string.trim());
    }

    @Test
    public void canParseStringToLocalDateType() throws Exception {
        canParse("2025-01-14").toType(LocalDate.class);
        canParse("\t2025-01-14").toType(LocalDate.class);
    }

    @Test
    public void shouldThrowWhenReverseDate() throws Exception {
        shouldThrowTypeParserException()
            .containingErrorMessage("Can not parse \"2025-14-01\"")
            .containingErrorMessage("due to: Text '2025-14-01' could not be parsed:")
            .containingErrorMessage("Invalid value for MonthOfYear (valid values 1 - 12): 14")
            .whenParsing("2025-14-01")
            .to(LocalDate.class);
    }

    @Test
    public void shouldThrowExceptionWhenStringIsNotDate() throws Exception {
        shouldThrowTypeParserException()
            .containingErrorMessage("Can not parse \"zarifovoe\"")
            .containingErrorMessage("due to: Text 'zarifovoe' could not be parsed at index 0")
            .whenParsing("zarifovoe")
            .to(LocalDate.class);
    }

    @Test
    public void canParseToGenericLocalDateArray() throws Exception {
        canParse(DATES).toGenericArray(new GenericType<LocalDate[]>() {});
    }

    @Test
    public void canParseToLocalLocalDateArray() throws Exception {
        canParse(DATES).toArray(LocalDate[].class);
    }

    @Test
    public void canParseToLocalDateList() throws Exception {
        canParse(DATES).toArrayList(new GenericType<List<LocalDate>>() {});
    }

    @Test
    public void canParseToLocalDateSet() throws Exception {
        canParse("2025-01-14, 2025-01-15, 2025-01-15").toLinkedHashSet(new GenericType<Set<LocalDate>>() {});
    }

    @Test
    public void canParseToLocalDateMap() throws Exception {
        canParse("2025-01-14=2025-01-16, 2025-01-15=2025-01-17")
            .toLinkedHashMap(new GenericType<Map<LocalDate, LocalDate>>() {});
    }
}
