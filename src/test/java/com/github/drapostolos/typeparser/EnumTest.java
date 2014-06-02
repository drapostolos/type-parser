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

    public enum MyEnum {
        AAA, BBB, CCC
    };

    @Test
    public void shouldThrowWhenStringIsNotParsableToEnum() throws Exception {
        shouldThrowParseException()
                .withErrorMessage("EnumTest$MyEnum.valueOf(java.lang.String)")
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
    public void canParseToMyEnumSet() throws Exception {
        canParse("AAA, BBB, AAA").toLinkedHashSet(new GenericType<Set<MyEnum>>() {});
    }

    @Test
    public void canParseToMyEnumMap() throws Exception {
        canParse("AAA=BBB, BBB=CCC").toLinkedHashMap(new GenericType<Map<MyEnum, MyEnum>>() {});
    }

}
