package com.github.drapostolos.typeparser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class FloatTest extends AbstractTypeTester<Float> {

    @Override
    Float make(String string) throws Exception {
        return Float.valueOf(string.trim());
    }

    @Test
    public void shouldThrowWhenStringIsNotParsableToFloat() throws Exception {
        shouldThrowTypeParserException()
                .containingNumberFormatErrorMessage()
                .whenParsing("aa")
                .to(Float.class, float.class);
    }

    @Test
    public void canParseStringToFloat() throws Exception {
        canParse("01.2").toType(float.class);
        canParse("1").toType(Float.class);
        canParse("1d").toType(Float.class);
        canParse("1").toType(Float.class);
        canParse("\t1f").toType(Float.class);
        canParse(".1").toType(Float.class);
    }

    @Test
    public void canParseToGenericFloatArray() throws Exception {
        canParse("1d, .1f, 23f").toGenericArray(new GenericType<Float[]>() {});
    }

    @Test
    public void canParseToFloatArray() throws Exception {
        canParse("1d, .1f, 23f").toArray(Float[].class);
    }

    @Test
    public void canParseToFloatList() throws Exception {
        canParse("1d, .1f, 23f").toArrayList(new GenericType<List<Float>>() {});
    }

    @Test
    public void canParseToFloatSet() throws Exception {
        canParse("1d, .1f, 1d").toLinkedHashSet(new GenericType<Set<Float>>() {});
    }

    @Test
    public void canParseToFloatMap() throws Exception {
        canParse("1=11, 2=22").toLinkedHashMap(new GenericType<Map<Float, Float>>() {});
    }

}
