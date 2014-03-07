package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.TypeParserUtility.STATIC_FACTORY_METHOD_NAME;
import static com.github.drapostolos.typeparser.TypeParserUtility.containsStaticMethodNamedValueOf;
import static com.github.drapostolos.typeparser.TypeParserUtility.makeNullArgumentErrorMsg;
import static com.github.drapostolos.typeparser.TypeParserUtility.makeParseErrorMsg;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The purpose of this class is to parse a string (read from a file for example)
 * and convert it to a specific java object/Type. For example converting "1" to
 * an {@code Integer} type, or "1,2,3" to a {@code List<Integer>} type.
 */
public final class StringToTypeParser {
    private final Map<Type, TypeParser<?>> typeParsers;
    final Splitter splitter;
    final Splitter keyValuePairSplitter;
    final InputPreprocessor inputPreprocessor;;

    /**
     * @return a new instance of {@link StringToTypeParserBuilder}
     */
    public static StringToTypeParserBuilder newBuilder() {
        return new StringToTypeParserBuilder();
    }

    StringToTypeParser(StringToTypeParserBuilder builder) {
        this.typeParsers = Collections.unmodifiableMap(new HashMap<Type, TypeParser<?>>(builder.typeParsers));
        this.splitter = builder.splitter;
        this.keyValuePairSplitter = builder.keyValuePairSplitter;
        this.inputPreprocessor = builder.inputPreprocessor;
    }

    /**
     * Parses the given {@code input} string to the given {@code targetType}. 
     * <p/>
     * Example: <br/><code>
     * StringToTypeParser parser = StringToTypeParser.newBuilder().build();<br/>
     * Integer i = parser.parse("1", Integer.class);
     * </code>
     * 
     * @param input - string value to parse
     * @param targetType - the expected type to convert {@code input} to.
     * @return an instance of {@code targetType} corresponding to the given {@code input}.
     * @throws NullPointerException if {@code targetType} argument is {@code null}.
     * @throws NullPointerException if {@code input} argument is {@code null}, when 
     * default {@link InputPreprocessor} strategy is used. This behavior can be changed by
     * setting your own {@link InputPreprocessor} strategy with {@link StringToTypeParserBuilder#setInputPreprocessor(InputPreprocessor)}.
     * @throws IllegalArgumentException if {@code input} is not parsable, or
     * if {@code targetType} is not recognized.
     */
    public <T> T parse(String input, Class<T> targetType) {
        if (input == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("input"));
        }
        if (targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }
        
        @SuppressWarnings("unchecked")
        T temp = (T) parseType2(input, targetType);
        return temp;

    }

    /**
     * Parses the given {@code input} string to the given {@code genericType}. 
     * <p/>
     * Example: <br/><code>
     * StringToTypeParser parser = StringToTypeParser.newBuilder().build();<br/>
     * {@code List<Integer>} list = parser.parse("1, 2", new {@code GenericType<List<Integer>>}() {});
     * </code><br/>
     * Note the ending "{}".
     * 
     * @param input - string value to parse.
     * @param genericType - the expected generic type to convert {@code input} to.
     * @return an instance of {@code genericType} corresponding to the given {@code input}.
     * @throws NullPointerException if {@code targetType} argument is {@code null}.
     * @throws NullPointerException if {@code input} argument is {@code null}, when 
     * default {@link InputPreprocessor} strategy is used. This behavior can be changed by
     * setting your own {@link InputPreprocessor} strategy with {@link StringToTypeParserBuilder#setInputPreprocessor(InputPreprocessor)}.
     * @throws IllegalArgumentException if {@code input} is not parsable, or
     * if {@code genericType} is not recognized.
     */
    public <T> T parse(String input, GenericType<T> genericType) {
        if (input == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("input"));
        }
        if (genericType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("genericType"));
        }

        @SuppressWarnings("unchecked")
        T temp = (T) parseType2(input, genericType.getType());
        return temp;
    }

    /**
     * Parses the given {@code input} string to the given {@code targetType}. 
     * <p/>
     * 
     * @param input - string value to parse.
     * @param targetType - the expected type to convert {@code input} to.
     * @return an instance of {@code targetType} corresponding to the given {@code input}.
     * @throws NullPointerException if {@code targetType} argument is {@code null}.
     * @throws NullPointerException if {@code input} argument is {@code null}, when 
     * default {@link InputPreprocessor} strategy is used. This behavior can be changed by
     * setting your own {@link InputPreprocessor} strategy with {@link StringToTypeParserBuilder#setInputPreprocessor(InputPreprocessor)}.
     * @throws IllegalArgumentException if {@code input} is not parsable.
     * @throws IllegalArgumentException if there is no registered {@link TypeParser} for the given {@code targetType}.
     */
    public Object parseType(String input, Type targetType) {
        if (input == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("input"));
        }
        if (targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }
        
        return parseType2(input, targetType);

    }
    
    private Object parseType2(final String input, Type targetType) {
        String preprocessedInput = preProcessInputString(input, targetType);
        
        if(preprocessedInput == null){
            if (isPrimitive(targetType)) {
                String message = "'%s' primitive can not be set to null. Input: \"%s\"; Preprocessed input: '%s'";
                throw new IllegalArgumentException(String.format(message, targetType, input, preprocessedInput));
            }
            return null; 
        }

        if(typeParsers.containsKey(targetType)){
            return invokeTypeParser(preprocessedInput, targetType, targetType);
        } 

        if(targetType instanceof ParameterizedType){
            ParameterizedType type = (ParameterizedType) targetType;
            Class<?> rawType = (Class<?>) type.getRawType();
            if(List.class.isAssignableFrom(rawType)){
                return invokeTypeParser(preprocessedInput, TypeParsers.ANY_LIST, targetType);
            }
            if(Set.class.isAssignableFrom(rawType)){
                return invokeTypeParser(preprocessedInput, TypeParsers.ANY_SET, targetType);
            }
            if(Map.class.isAssignableFrom(rawType)){
                return invokeTypeParser(preprocessedInput, TypeParsers.ANY_MAP, targetType);
            }
        }
        
        if(targetType instanceof Class){
            Class<?> cls = (Class<?>) targetType;
            if(cls.isArray()){
                return invokeTypeParser(preprocessedInput, TypeParsers.ANY_ARRAY, targetType);
            }
            if(containsStaticMethodNamedValueOf(cls)){
                return invokeTypeParser(preprocessedInput, TypeParsers.ANY_CLASS_WITH_STATIC_VALUEOF_METHOD, targetType);
            }
        }
        
        /*
         * In java 1.6, when retrieving a methods parameter types through
         * reflection (using java.lang.reflect.Method#getGenericParameterTypes())
         * sometimes GenericArrayType is returned. Even if it is a regular array 
         * type (e.g. java.lang.String[]). The below is to handle this case.
         */
        if(targetType instanceof GenericArrayType){
            return invokeTypeParser(preprocessedInput, TypeParsers.ANY_ARRAY, targetType);
        }
        
        /*
         * If execution reaches here, it means there is no TypeParser for 
         * the given targetType. What remains is to make a descriptive error 
         * message and throw exception. 
         */
        String message = "There is either no registered 'TypeParser' for that type, or that "
                + "type does not contain the following static factory method: '%s.%s(String)'.";
        message = String.format(message, targetType, STATIC_FACTORY_METHOD_NAME);
        message = makeParseErrorMsg(preprocessedInput, message, targetType);
        throw new IllegalArgumentException(message);
    }

    private String preProcessInputString(String input, Type targetType) {
        try {
            return inputPreprocessor.prepare(input, new InputPreprocessorHelper(targetType));
        } catch (Exception e) {
            String message = "Exception thrown from InputPreprocessor: %s [%s] with message:  "
                    + "%s. See underlying exception for more information.";
            message = String.format(message, 
                    inputPreprocessor, inputPreprocessor.getClass(), e.getMessage());
            message = makeParseErrorMsg(input, message, targetType);
            throw new IllegalArgumentException(message, e);
        }
    }

    private Object invokeTypeParser(String input, Type key, Type targetType) {
        try {
            TypeParser<?> typeParser = typeParsers.get(key);
            ParseHelper parseHelper = new ParseHelper(this, targetType);
            return typeParser.parse(input, parseHelper);
        } catch (NumberFormatException e) {
            String message =  String.format("Number format exception %s.", e.getMessage());
            message = makeParseErrorMsg(input, message, targetType);
            throw new IllegalArgumentException(message, e);
        } catch (RuntimeException e) {
            String message = makeParseErrorMsg(input, e.getMessage(),targetType);
            throw new IllegalArgumentException(message, e);
        }

    }

    private boolean isPrimitive(Type targetType) {
        if(targetType instanceof Class){
            Class<?> c = (Class<?>) targetType;
            return c.isPrimitive();
        }
        return false;
    }
}