package com.github.drapostolos.typeparser;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class NumberTest extends AbstractTypeTester<Number> {

    @Override
    Number make(String string) throws Exception {
        return NumberFormat.getInstance(Locale.US).parse(string.trim());
    }

    @Test
    public void shouldThrowWhenStringIsNotParsableToType() throws Exception {
        shouldThrowTypeParserException()
                .causedBy(IllegalArgumentException.class)
                .containingErrorMessage("Unparseable number")
                .whenParsing("aa")
                .to(Number.class);
    }

    @Test
    public void canParseStringToType() throws Exception {
        canParse("01.2").toType(Number.class);
        canParse("1").toType(Number.class);
        canParse("1d").toType(Number.class);
        canParse("\t1d").toType(Number.class);
        canParse(".1").toType(Number.class);
    }

    @Test
    public void canParseToGenericArray() throws Exception {
        canParse("1, 1.2, .1").toGenericArray(new GenericType<Number[]>() {});
    }

    @Test
    public void canParseToArray() throws Exception {
        canParse("1, 1.2, .1").toArray(Number[].class);
    }

    @Test
    public void canParseToList() throws Exception {
        canParse("1, 1.2, .1").toArrayList(new GenericType<List<Number>>() {});
    }

    @Test
    public void canParseToSet() throws Exception {
        canParse("1, 1.2, 1").toLinkedHashSet(new GenericType<Set<Number>>() {});
    }

    @Test
    public void canParseToMap() throws Exception {
        canParse("1=.1, 2=.2").toLinkedHashMap(new GenericType<Map<Number, Number>>() {});
    }
}
