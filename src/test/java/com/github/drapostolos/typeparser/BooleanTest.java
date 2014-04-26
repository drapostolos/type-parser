package com.github.drapostolos.typeparser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class BooleanTest extends AbstractTypeTester<Boolean> {

    @Override
    Boolean make(String string) throws Exception {
        return Boolean.valueOf(string.trim());
    }

    @Test
    public void canParseStringToTrue() throws Exception {
        canParse("true").toType(Boolean.class);
        canParse("true").toType(boolean.class);
        canParse(" true\t").toType(Boolean.class);
        canParse(" true\t").toType(boolean.class);
    }

    @Test
    public void canParseStringToFalse() throws Exception {
        canParse("false").toType(Boolean.class);
        canParse("false").toType(boolean.class);
        canParse(" false\t").toType(Boolean.class);
        canParse(" false\t").toType(boolean.class);
    }

    @Test
    public void shouldThrowExceptionWhenNotParsableToBoolean() throws Exception {
        shouldThrowWhenParsing("1234");
        toTypeWithErrorMessage(Boolean.class, "is not parsable to a Boolean");
        toTypeWithErrorMessage(boolean.class, "is not parsable to a Boolean");
    }

    @Test
    public void canParseToGenericBooleanArray() throws Exception {
        canParse("true, false, true").toGenericArray(new GenericType<Boolean[]>() {});
    }

    @Test
    public void canParseToBooleanArray() throws Exception {
        canParse("true, false, true").toArray(Boolean[].class);
    }

    @Test
    public void canParseToBooleanList() throws Exception {
        canParse("true, false, true").toList(new GenericType<List<Boolean>>() {});
    }

    @Test
    public void canParseToBooleanSet() throws Exception {
        canParse("true, false, true").toSet(new GenericType<Set<Boolean>>() {});
    }

    @Test
    public void canParseToBooleanMap() throws Exception {
        canParse("false=true, true=false").toMap(new GenericType<Map<Boolean, Boolean>>() {});
    }

}
