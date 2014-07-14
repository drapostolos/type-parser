package com.github.drapostolos.typeparser;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ParameterCheckTest extends TestBase {

    private static final TargetType SOME_TYPE = new TargetType(TestBase.class);
    private TypeParserBuilder builder = TypeParser.newBuilder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrowWhenRegesteringArrayClassParser() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot register Parser for array class.");
        thrown.expectMessage("Register a Parser for the component type 'int' instead");
        thrown.expectMessage("as arrays are handled automatically internally in type-parser.");

        builder.registerParser(int[].class, new Parser<int[]>() {

            @Override
            public int[] parse(String input, ParserHelper helper) {
                return null;
            }
        });
    }

    @Test
    public void shouldThrowWhenDefaultSplitStrategyIsCalledWithNull() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("input");
        new SplitStrategyHelper(SOME_TYPE).splitWithDefaultSplitStrategy(null);
    }

    @Test
    public void shouldThrowWhenCheckingForNullStringInInputPreprocessorHelperWhenInputIsNull() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("input");
        TypeParser typeParser = TypeParser.newBuilder().build();
        new InputPreprocessorHelper(SOME_TYPE, typeParser).isNullString(null);
    }

    @Test
    public void shouldThrowWhenKeyValueSplitStrategyIsCalledWithNull() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("keyValue");
        TypeParser typeParser = TypeParser.newBuilder().build();
        new ParserHelper(SOME_TYPE, typeParser).splitKeyValue(null);
    }

    @Test
    public void shouldThrowWhenCheckingForNullStringInParserHelperWhenInputIsNull() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("input");
        TypeParser typeParser = TypeParser.newBuilder().build();
        new ParserHelper(SOME_TYPE, typeParser).isNullString(null);
    }

    @Test
    public void shouldThrowWhenSplitStrategyIsCalledWithNull() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("input");
        TypeParser typeParser = TypeParser.newBuilder().build();
        new ParserHelper(SOME_TYPE, typeParser).split(null);
    }

    @Test
    public void shouldThrowWhenRetrievingParameterizedClassArgumentsByNegativeIndex() throws Exception {
        thrown.expect(IndexOutOfBoundsException.class);
        thrown.expectMessage("index -1 is out of bounds.");
        thrown.expectMessage("Should be within [0, 1]");
        TypeParser typeParser = TypeParser.newBuilder().build();
        Type type = new GenericType<Map<Integer, File>>() {}.getType();
        new ParserHelper(new TargetType(type), typeParser).getParameterizedClassArgumentByIndex(-1);
    }

    @Test
    public void shouldThrowWhenRetrievingParameterizedClassArgumentsByTooHighIndex() throws Exception {
        thrown.expect(IndexOutOfBoundsException.class);
        thrown.expectMessage("index 1 is out of bounds.");
        thrown.expectMessage("Should be within [0, 0]");
        TypeParser typeParser = TypeParser.newBuilder().build();
        Type type = new GenericType<List<Integer>>() {}.getType();
        new ParserHelper(new TargetType(type), typeParser).getParameterizedClassArgumentByIndex(1);
    }

    @Test
    public void shouldThrowWhenRegisteringDynamicParserWithValueNull() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("parser");
        builder.registerDynamicParser(null);
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringTypeParser_Class_Null() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("parser");
        builder.registerParser(int.class, null);
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringTypeParser_GenericType_Null() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("parser");
        builder.registerParser(new GenericType<String>() {}, null);
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringTypeParser_NullClass_TypeParser() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("targetType");
        Class<MyClass1> arg = null;
        builder.registerParser(arg, new MyClass1());
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringTypeParser_NullGenmericType_TypeParser() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("targetType");
        GenericType<MyClass1> arg = null;
        builder.registerParser(arg, new MyClass1());
    }

    @Test
    public void shouldThrowExceptionWhenUnregisteringNullClass() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("targetType");
        Class<?> c = null;
        builder.unregisterParser(c);
    }

    @Test
    public void shouldThrowExceptionWhenSettingNullInputProcessor() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("inputPreprocessor");
        builder.setInputPreprocessor(null);
    }

    @Test
    public void shouldThrowExceptionWhenSettingNullStringStrategyWithNull() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("nullStringStrategy");
        builder.setNullStringStrategy(null);
    }

    @Test
    public void shouldThrowExceptionWhenSettingSplitStrategyWithNull() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("splitStrategy");
        builder.setSplitStrategy(null);
    }

    @Test
    public void shouldThrowExceptionWhenSettingKeyValueSplitStrategyWithNull() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("splitStrategy");
        builder.setKeyValueSplitStrategy(null);
    }

    @Test
    public void shouldThrowExceptionWhenParsingNullStringToClass() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("input");
        parser.parse(null, Object.class);
    }

    @Test
    public void shouldThrowExceptionWhenParsingStringToNullClass() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("targetType");
        Class<?> dummy = null;
        parser.parse("dummy", dummy);
    }

    @Test
    public void shouldThrowExceptionWhenParsingNullToType() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("input");
        parser.parseType(null, Object.class);
    }

    @Test
    public void shouldThrowExceptionWhenParsingStringToNullType() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("targetType");
        Type t = null;
        parser.parseType("dummy", t);
    }

    @Test
    public void shouldThrowExceptionWhenParsingNullStringToGenericType() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("input");
        parser.parse(null, new GenericType<List<Integer>>() {});
    }

    @Test
    public void shouldThrowExceptionWhenParsingStringToNullGenericType() throws Exception {
        prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed("genericType");
        GenericType<Set<Long>> t = null;
        parser.parse("dummy", t);
    }

    private void prepareExpectedExceptionWhenNullValuePassedInForArgumentNamed(String argName) {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage(String.format("Argument named '%s' is illegally set to null!", argName));
    }

}
