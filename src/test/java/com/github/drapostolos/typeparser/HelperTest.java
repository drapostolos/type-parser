package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class HelperTest extends TestBase {

    @Test
    public void canDecideIfRawTargetClassIsWithinAListOfRawTargetClassesWhenItIs() throws Exception {
        // given
        Class<?> type = Integer.class;
        Helper helper = new Helper(new TargetType(type)) {};

        // when
        assertThat(helper.isRawTargetClassAnyOf(Integer.class, String.class)).isTrue();
    }

    @Test
    public void canDecideIfRawTargetClassIsWithinAListOfRawTargetClassesWhenItIsNot() throws Exception {
        // given
        Class<?> type = Integer.class;
        Helper helper = new Helper(new TargetType(type)) {};

        // when
        assertThat(helper.isRawTargetClassAnyOf(File.class, String.class)).isFalse();
    }

    @Test
    public void toStringPrintsSameAsTypeToString() throws Exception {
        // given
        final GenericType<?> genType = new GenericType<String>() {};

        setInputPreprocessor(new InputPreprocessor() {

            @Override
            public String prepare(String input, InputPreprocessorHelper helper) {
                assertThat(helper.toString()).contains(genType.toString());
                return input;
            }
        });
        parser = builder.build();

        // when
        assertThat(parser.parse("null", genType)).isNull();
    }

    @Test
    public void canDecideTargetTypeIsInstanceOfMapWhenTargetTypeIsMap() throws Exception {
        // given
        Type type = new GenericType<Map<String, URL>>() {}.getType();
        Helper helper = new Helper(new TargetType(type)) {};

        // when
        assertThat(helper.isTargetTypeAssignableTo(Map.class)).isTrue();
    }

    @Test
    public void canDecideTargetTypeIsNotAssignableToMapWhenTargetTypeIsList() throws Exception {
        // given
        Type type = new GenericType<List<String>>() {}.getType();
        Helper helper = new Helper(new TargetType(type)) {};

        // when
        assertThat(helper.isTargetTypeAssignableTo(Map.class)).isFalse();
    }

    @Test
    public void canDecideTargetTypeIsAssignableToSuperClassWhenTargetTypeIsSubclass() throws Exception {
        // given
        Type type = new GenericType<Integer>() {}.getType();
        Helper helper = new Helper(new TargetType(type)) {};

        // when

        assertThat(helper.isTargetTypeAssignableTo(Number.class)).isTrue();
    }

    @Test
    public void canDecideTargetTypeIsEqualToStringWhenTargetTypeIsString() throws Exception {
        // given
        Type type = new GenericType<String>() {}.getType();
        Helper helper = new Helper(new TargetType(type)) {};

        // when
        assertThat(helper.isTargetTypeEqualTo(String.class)).isTrue();
    }

    @Test
    public void canDecideTargetTypeIsNotEqualToStringWhenTargetTypeIsInteger() throws Exception {
        // given
        Type type = new GenericType<Integer>() {}.getType();
        Helper helper = new Helper(new TargetType(type)) {};

        // when
        assertThat(helper.isTargetTypeEqualTo(String.class)).isFalse();
    }

    @Test
    public void canDecideTargetTypeIsEqualToListOfStringWhenTargetTypeIsListOfString() throws Exception {
        // given
        Type type = new GenericType<List<String>>() {}.getType();
        Helper helper = new Helper(new TargetType(type)) {};

        // when

        assertThat(helper.isTargetTypeEqualTo(new GenericType<List<String>>() {})).isTrue();
    }

    @Test
    public void canDecideTargetTypeIsNotEqualToListOfStringWhenTargetTypeIsListOfInteger() throws Exception {
        // given
        Type type = new GenericType<List<String>>() {}.getType();
        Helper helper = new Helper(new TargetType(type)) {};

        // when
        GenericType<List<Integer>> listOfInteger = new GenericType<List<Integer>>() {};
        assertThat(helper.isTargetTypeEqualTo(listOfInteger)).isFalse();
    }

    @Test
    public void canGetRawClassWhenTargetTypeIsClass() throws Exception {
        // given
        Type type = new GenericType<Integer>() {}.getType();
        Helper helper = new Helper(new TargetType(type)) {};

        // when
        assertThat(helper.getRawTargetClass()).isSameAs(Integer.class);
    }

    @Test
    public void canGetRawClassWhenTargetTypeIsGenericType() throws Exception {
        // given

        Type type = new GenericType<Set<Integer>>() {}.getType();
        Helper helper = new Helper(new TargetType(type)) {};

        // when
        assertThat(helper.getRawTargetClass()).isSameAs(Set.class);
    }

    @Test
    public <T> void canGetRawClassWhenTargetTypeIsGenericArray() throws Exception {

        // given
        Type type = new GenericType<String[]>() {}.getType();
        Helper helper = new Helper(new TargetType(type)) {};

        // then
        assertThat(helper.getRawTargetClass()).isSameAs(String[].class);
    }

    @Test
    public <T> void canGetRawClassWhenTargetTypeIsGenericArrayOfParameterizedType() throws Exception {

        // given
        Type type = new GenericType<Class<?>[]>() {}.getType();
        Helper helper = new Helper(new TargetType(type)) {};
        Method m = getClass().getDeclaredMethod("m2");

        // then
        assertThat(helper.getRawTargetClass()).isSameAs(m.getReturnType());
    }

    Class<?>[] m2() {
        return null;
    }

}
