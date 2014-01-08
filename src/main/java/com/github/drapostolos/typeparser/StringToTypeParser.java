package com.github.drapostolos.typeparser;

import java.io.File;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The purpose of this class is to parse a string (read from a file for example)
 * and convert it to a specific java object/Type (for example converting "1" to
 * an Integer type).
 * <p>
 * By default, converting a string toe following types are supported:
 *  <ul>
 *  <li> {@link Byte} (byte)</li>
 *  <li> {@link Short} (short)</li>
 *  <li> {@link Integer} (int)</li>
 *  <li> {@link Long} (long)</li>
 *  <li> {@link Float} (float)</li>
 *  <li> {@link Double} (double)</li>
 *  <li> {@link Boolean} (boolean)</li>
 *  <li> {@link Character} (char)</li>
 *  <li> {@link String} </li>
 *  <li> {@link File} </li>
 *  </ul>
 * <p>
 * In addition to the above, any type that contains a static factory method 
 * with the following signatures can also be converted.
 * <ul>
 *  <li> {@code .valueOf(String)}</li>
 * </ul>
 * (For example any {@link Enum} type, or {@link EnumSet})
 * <p>
 * 
 * 
 *  *  <li> Any types with a static factory method named: {@code valueOf(String)} (Example: {@link Enum})</li>
 *  <li> Any types with a static factory method named: {@code of(String)} (Example: {@link EnumSet}) </li>
 *  
 * 
 * 
 * Parsing and converting to additional types can be done by either:
 *  <ul>
 *  <li> Register your own implementations of the {@link TypeParserBase} interface</li>
 *  <li> Add one of these static factory methods in your class:  {@code valueOf(String)} or  {@code of(String)}</li>
 *  </ul>
 * <p>
 * Here's an example how to register your own {@link TypeParserBase}'s:
 * </br></br>
 * {@code StringToTypeParser parser = StringToTypeParser.newBuilder() }</br>
 * {@code .registerTypeParser(new XTypeParser())}</br>
 * {@code .registerTypeParser(new YTypeParser())}</br>
 * {@code .build();}</br>
 * </br>
 * {@code X x = parser.parseType("some-string", X.class);}</br>
 *
 */
public final class StringToTypeParser {
    private final Map<Type, TypeParser<?>> typeParsers;
    final Splitter splitter;
    final Splitter keyValuePairSplitter;

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
    }

    /**
     * TODO
     * Parses the given {@code input} to the given {@code targetType}. Where {@code targetType}
     * is either one of the default supported java types or a registered {@link TypeParser}
     * (for custom made types).
     * 
     * @param input - string value to parse
     * @param targetType - the expected type to convert {@code value} to.
     * @return an instance of {@code type} corresponding to the given {@code value}.
     * @throws NullPointerException if any of the arguments are null.
     * @throws IllegalArgumentException if {@code input} is not parsable, or
     * if {@code type} is not recognized.
     */
    public <T> T parse(String input, Class<T> targetType) {
        if (input == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("input"));
        }
        if (targetType == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("targetType"));
        }
        
        @SuppressWarnings("unchecked")
        T temp = (T) parseType2(input, targetType);
        return temp;

    }

    /**
     * TODO
     * @param input
     * @param genericType
     * @return
     */
    public <T> T parse(String input, GenericType<T> genericType) {
        if (input == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("input"));
        }
        if (genericType == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("genericType"));
        }

        @SuppressWarnings("unchecked")
        T temp = (T) parseType2(input, genericType.getType());
        return temp;
    }

    /**
     * TODO
     * @param input
     * @param targetType
     * @return
     */
    public Object parseType(String input, Type targetType) {
        if (input == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("input"));
        }
        if (targetType == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("targetType"));
        }
        
        return parseType2(input, targetType);

    }
    
    private Object parseType2(String input, Type targetType) {
        if(typeParsers.containsKey(targetType)){
            return invokeTypeParser(input, targetType, targetType);
        } 

        if(targetType instanceof ParameterizedType){
            ParameterizedType type = (ParameterizedType) targetType;
            Class<?> rawType = (Class<?>) type.getRawType();
            if(List.class.isAssignableFrom(rawType)){
                return invokeTypeParser(input, TypeParsers.ANY_LIST, targetType);
            }
            if(Set.class.isAssignableFrom(rawType)){
                return invokeTypeParser(input, TypeParsers.ANY_SET, targetType);
            }
            if(Map.class.isAssignableFrom(rawType)){
                return invokeTypeParser(input, TypeParsers.ANY_MAP, targetType);
            }
        }
        
        if(targetType instanceof Class){
            Class<?> cls = (Class<?>) targetType;
            if(cls.isArray()){
                return invokeTypeParser(input, TypeParsers.ANY_ARRAY, targetType);
            }
            FactoryMethodInvoker invoker  = new FactoryMethodInvoker(cls);
            if(invoker.containsFactoryMethod()){
                return invoker.invokeFactoryMethod(input);
            }
        }
        
        if(targetType instanceof GenericArrayType){
            return invokeTypeParser(input, TypeParsers.ANY_ARRAY, targetType);
        }
        
        /*
         * If execution reaches here, it means there is no TypeParser for 
         * the given targetType. What remains is to make a descriptive error 
         * message and throw exception. 
         */
        String message = "There is either no registered 'TypeParser' for that type, or that "
                + "type does not contain the following static factory method: '%s.%s(String)'.";
        message = String.format(message, targetType, FactoryMethodInvoker.FACTORY_METHOD_NAME);
        message = TypeParserUtility.makeParseErrorMsg(input, message, targetType);
        throw new IllegalArgumentException(message);
    }

    private Object invokeTypeParser(String input, Type key, Type targetType) {
        /*
         * TODO
         * preProcess input? interface InputPreprocessor 
         */
        if (input.trim().equalsIgnoreCase("null")) {
            if (isPrimitive(targetType)) {
                String message = "'%s' primitive can not be set to null.";
                throw new IllegalArgumentException(String.format(message, targetType));
            }
            return null; 
        }

        try {
            TypeParser<?> typeParser = typeParsers.get(key);
            ParseHelper parseHelper = new ParseHelper(this, targetType);
            return typeParser.parse(input, parseHelper);
        } catch (NumberFormatException e) {
            String message =  String.format("Number format exception %s.", e.getMessage());
            message = TypeParserUtility.makeParseErrorMsg(input, message, targetType);
            throw new IllegalArgumentException(message, e);
        } catch (RuntimeException e) {
            String message = TypeParserUtility.makeParseErrorMsg(input, e.getMessage(),targetType);
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