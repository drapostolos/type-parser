package com.github.drapostolos.typeparser;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class providing helper methods to implementations of
 * {@link TypeParser} when parsing a string to a type. 
 * <p/>
 * The {@link StringToTypeParser} will automatically inject an instance of this
 * class into the {@link TypeParser} implementation.
 */
public final class ParseHelper{
    private final Type targetType;
    private final StringToTypeParser stringParser;
    private final Splitter splitter;
    private final Splitter mapKeyValueSplitter;

    ParseHelper(StringToTypeParser stringParser, Type targetType) {
        this.stringParser = stringParser;
        this.targetType = targetType;
        this.splitter = stringParser.splitter;
        this.mapKeyValueSplitter = stringParser.keyValuePairSplitter;
    }

    /**
     * This method gives access to {@link StringToTypeParser#parse(String, Class)}.
     * 
     * @param input String to parse.
     * @param targetType to parse it to.
     * @return an instance of type.
     */
    public <T> T parse(String input, Class<T> targetType) {
        return stringParser.parse(input, targetType);
    }

    /**
     * This method gives access to {@link StringToTypeParser#parseType(String, Type)}.
     * 
     * @param input String to parse.
     * @param targetType The target type to parse the given input to.
     * @return an instance of type.
     */
    public Object parseType(String input, Type targetType) {
        return stringParser.parseType(input, targetType);
    }

    /**
     * Splits the {@code input} string into a list of sub-strings by using the 
     * {@link Splitter} implementation, as registered with 
     * {@link StringToTypeParserBuilder#setSplitter(Splitter)}.
     * <p/>
     * For example this string "1, 2, 3, 4" is split into ["1", " 2", " 3", " 4"].
     * 
     * @param input String to parse. For example "THIS, THAT, OTHER"
     * @return List of strings.
     */
    public List<String> split(String input){
        return splitter.split(input, new SplitHelper(targetType));
    }

    /**
     * Splits the {@code keyValuePair} string into a list of sub-strings by using the 
     * {@link Splitter} implementation, as registered with 
     * {@link StringToTypeParserBuilder#setKeyValuePairSplitter(Splitter)}.
     * <p/>
     * For example this string "a=AAA" is split into ["a", "AAA"].
     * 
     * @param keyValuePair
     * @return
     */
    public List<String> splitKeyValuePair(String keyValuePair) {
        return mapKeyValueSplitter.split(keyValuePair, new SplitHelper(targetType));
    }

    /**
     * When the {@code targetType} (as returned from {@link TypeParser#parse(String, ParseHelper)}
     * implementation where this {@link ParseHelper} originates from) is a parameterized type,
     * this method returns a list with the type arguments.
     * <p/>
     * Example method <br/>
     *   <code> public {@code Map<String, Integer>} parse(String value, ParseHelper helper) {...}</code><br/>
     * where this method would return a list of {@code [String.class, Interger.class]}.
     * </p>
     * All type arguments must be none parameterized types (i.e. nested parameterized types are not allowed),
     * with one exception: {@link Class<?>}. <br/>
     * For example this is allowed:<br/>
     *   <code> public {@code List<Class<?>>} parse(String value, ParseHelper helper) {...}</code><br/>
     * But this is not:<br/>
     *   <code> public {@code List<MyClass<Long>>} parse(String value, ParseHelper helper) {...}</code><br/>
     * <p/>
     *      
     * @return List of {@link Class} types.
     * @throws IllegalStateException if the {@code targetType} is not a parameterized type.
     * @throws IllegalStateException if any of the parameterized type arguments is of a 
     * parameterized type (with exception of {@link Class}).
     */
     List<Class<?>> getParameterizedTypeArguments() {
        if(!(targetType instanceof ParameterizedType)){
            String message = "TargetType: '%s' [%s] must be a parameterized "
                    + "type when calling this method, but it is not.";
            throw new IllegalStateException(String.format(message, targetType, targetType.getClass()));
        }

        ParameterizedType pt = (ParameterizedType) targetType;
        List<Class<?>> result = new ArrayList<Class<?>>();
        for(Type typeArgument : pt.getActualTypeArguments()){
            if(typeArgument instanceof Class){
                result.add((Class<?>) typeArgument);
                continue;
            }
            if(typeArgument instanceof ParameterizedType){
                Type rawType = ((ParameterizedType) typeArgument).getRawType();
                if(rawType instanceof Class){
                    /*
                     * Special case to handle Class<?>
                     */
                    Class<?> cls = (Class<?>) rawType;
                    result.add(cls);
                    continue;
                }
            }
            String message = "TargetType: '%s' [%s] contains the following illegal type argument: '%s' [%s]";
            message = String.format(message, targetType, targetType.getClass(), typeArgument, typeArgument.getClass());
            throw new IllegalStateException(message);
        }
        return result;
    }

    /**
     * When the {@code targetType} (as returned from {@link TypeParser#parse(String, ParseHelper)}
     * implementation where this {@link ParseHelper} originates from) is an array, this method 
     * returns the component type.
     * <p/>
     * Example method <br/>
     *   <code> public Integer[] parse(String value, ParseHelper helper) {...}</code><br/>
     * where this method would return {@code Interger.class}.
     * </p>
     * The component type must be of a non-parameterized type, with one exception: {@link Class<?>}. <br/>
     * For example this is allowed:<br/>
     *   <code> public {@code Class<?>[]} parse(String value, ParseHelper helper) {...}</code><br/>
     * But this is not:<br/>
     *   <code> public {@code Set<Integer>[]} parse(String value, ParseHelper helper) {...}</code><br/>
     * <p/>
     *      
     * @return the component type of the array.
     * @throws IllegalStateException if the {@code targetType} is not of array type.
     * @throws IllegalStateException if the component type is of parameterized type
     * (with exception of {@link Class}).
     */
    Class<?> getComponentClass(){
        if(targetType instanceof Class){
            /*
             * Handle array classes, such as Integer[].class, Class<?>[].class etc.
             */
            Class<?> t = (Class<?>) targetType; 
            if(t.isArray()){
                return (Class<?>) t.getComponentType();
            }
        } 
        if(targetType instanceof GenericArrayType){
            Type t = ((GenericArrayType) targetType).getGenericComponentType();
            if(t instanceof Class){
                return (Class<?>) t;
            }
        }
        String message = "TargetType: '%s' [%s] is either not an array or "
                + "the componet type is generic.";
        message = String.format(message, targetType, targetType.getClass());
        throw new IllegalStateException(message);
    }

    Type getTargetType() {
        return targetType;
    }

}
