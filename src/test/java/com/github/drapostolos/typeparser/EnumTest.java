package com.github.drapostolos.typeparser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.github.drapostolos.typeparser.EnumTest.MyEnum;

public class EnumTest extends AbstractTypeTester<MyEnum> {

    @Override
    MyEnum make(String string) throws Exception {
        return MyEnum.valueOf(string.trim());
    }

    enum MyEnum {
        AAA, BBB, CCC
    };

    @Test
    public void shouldThrowWhenStringIsNotParsableToEnum() throws Exception {
        shouldThrowTypeParserException()
                .causedBy(IllegalArgumentException.class)
                .containingErrorMessage("No enum const")
                .containingErrorMessage("MyEnum.DDD")
                .whenParsing("DDD")
                .to(MyEnum.class);
    }

    @Test
    public void canParseStringToEnumType() throws Exception {
        canParse("AAA").toType(MyEnum.class);
        canParse("BBB ").toType(MyEnum.class);
        canParse(" CCC\t").toType(MyEnum.class);
    }

    @Test
    public void canParseToGenericEnumArray() throws Exception {
        canParse("AAA, BBB, CCC").toGenericArray(new GenericType<MyEnum[]>() {});
    }

    @Test
    public void canParseToEnumArray() throws Exception {
        canParse("AAA, BBB, CCC").toArray(MyEnum[].class);
    }

    @Test
    public void canParseToMyEnumList() throws Exception {
        canParse("AAA, BBB, CCC").toArrayList(new GenericType<List<MyEnum>>() {});
    }

    @Test
    public void canParseToMyEnumHashSet() throws Exception {
        canParse("AAA, BBB, AAA").toLinkedHashSet(new GenericType<Set<MyEnum>>() {});
    }

    @Test
    public void canParseToMyEnumMap() throws Exception {
        canParse("AAA=BBB, BBB=CCC").toLinkedHashMap(new GenericType<Map<MyEnum, MyEnum>>() {});
    }

    @Test
    public void canParseToNull() throws Exception {
        canParse("null").toNull(MyEnum.class);
    }

}
