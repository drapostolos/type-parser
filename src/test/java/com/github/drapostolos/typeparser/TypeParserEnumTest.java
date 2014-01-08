package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

public class TypeParserEnumTest extends AbstractTest{
    
    public enum MyEnum {AAA, BBB, CCC};
    
    @Test public void 
    shouldThrowExceptionWhenStringIsNotParsableToEnum() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("TypeParserEnumTest$MyEnum.valueOf('DDD')'. See underlying exception for additional information.");
        parser.parse("DDD", MyEnum.class);
    }

    @Test
    public void canParseStringToEnumType() throws Exception {
        assertThat(parser.parse("AAA", MyEnum.class)).isEqualTo(MyEnum.AAA);
        assertThat(parser.parse("BBB ", MyEnum.class)).isEqualTo(MyEnum.BBB);
        assertThat(parser.parse(" CCC\t", MyEnum.class)).isEqualTo(MyEnum.CCC);
    }

    @Test
    public void canParseToGenericEnumArray() throws Exception {
        assertThat(parser.parse("AAA, BBB, CCC", new GenericType<MyEnum[]>() {}))
        .containsExactly(MyEnum.AAA, MyEnum.BBB, MyEnum.CCC);
    }

    @Test
    public void canParseToEnumArray() throws Exception {
        assertThat(parser.parse("AAA, BBB, CCC", MyEnum[].class))
        .containsOnly(MyEnum.AAA, MyEnum.BBB, MyEnum.CCC);
    }

    @Test
    public void canParseToMyEnumList() throws Exception {
        assertThat(parser.parse("AAA, BBB, CCC", new GenericType<List<MyEnum>>() {}))
        .containsExactly(MyEnum.AAA, MyEnum.BBB, MyEnum.CCC);
    }

    @Test
    public void canParseToMyEnumSet() throws Exception {
        assertThat(parser.parse("AAA, BBB, AAA", new GenericType<Set<MyEnum>>() {}))
        .containsExactly(MyEnum.AAA, MyEnum.BBB);
    }

    @Test
    public void canParseToMyEnumMap() throws Exception {
        assertThat(parser.parse("AAA=BBB, BBB=CCC, CCC=AAA", new GenericType<Map<MyEnum, MyEnum>>() {}))
        .contains(MapEntry.entry(MyEnum.AAA, MyEnum.BBB))
        .contains(MapEntry.entry(MyEnum.BBB, MyEnum.CCC))
        .contains(MapEntry.entry(MyEnum.CCC, MyEnum.AAA))
        .hasSize(3);
    }

}
