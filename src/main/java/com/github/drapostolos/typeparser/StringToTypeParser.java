package com.github.drapostolos.typeparser;

import java.io.File;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

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
 *  <li> {@code .of(String)}</li>
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
 *  <li> Register your own implementations of the {@link TypeParser} interface</li>
 *  <li> Add one of these static factory methods in your class:  {@code valueOf(String)} or  {@code of(String)}</li>
 *  </ul>
 * <p>
 * Here's an example how to register your own {@link TypeParser}'s:
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
    private final Map<Class<?>, TypeParser> typeParsers;

    /**
     * Use this method to get a new {@link StringToTypeParserBuilder} instance
     * to register your own {@link TypeParser} implementations, or remove any of the 
     * default {@link TypeParser}'s.
     * 
     * @return new instance of {@link StringToTypeParserBuilder}
     */
    public static StringToTypeParserBuilder newBuilder() {
        return new StringToTypeParserBuilder();
    }
    
    StringToTypeParser(Map<Class<?>, TypeParser> typeParsers) {
        this.typeParsers = Collections.unmodifiableMap(new HashMap<Class<?>, TypeParser>(typeParsers));
    }

    /**
     * Parses the given {@code value} to the given {@code type}. Where {@code type}
     * is either one of the default supported java types or a registered {@link TypeParser}
     * (for custom made types).
     * 
     * @param input - string value to parse
     * @param type - the expected type to convert {@code value} to.
     * @return an instance of {@code type} corresponding to the given {@code value}.
     * @throws NullPointerException if any of the arguments are null.
     * @throws IllegalArgumentException if {@code value} is not parsable, or
     * if {@code type} is not recognized.
     */
    public <T> T parse(String input, Class<T> type) {
        if (input == null) {
            throw new NullPointerException(nullArgumentErrorMsg("input"));
        }
        if (type == null) {
            throw new NullPointerException(nullArgumentErrorMsg("type"));
        }
        
        @SuppressWarnings("unchecked")
        T temp = (T) parseType(input, type);
        return temp;
        
//        Invoker invoker = Invoker.forClass((Class<?>) type, typeParsers);
//        @SuppressWarnings("unchecked")
//        T temp = (T) invoker.invokeTypeParserTemplate(input);
//        return temp;

//        
//        @SuppressWarnings("unchecked")
//        T temp = (T) SimpleTypeParserInvoker.fromType(type, typeParsers).invokeTypeParserTemplate(input);
//        return temp;
    }

    /**
     * TODO ...
     * @param input
     * @param type
     * @return
     */
    public Object parseType(String input, Type type) {
        if (input == null) {
            throw new NullPointerException(nullArgumentErrorMsg("input"));
        }
        if (type == null) {
            throw new NullPointerException(nullArgumentErrorMsg("type"));
        }

        Invoker invoker;
        if(type instanceof GenericArrayType){
            invoker = Invoker.forGenericArrayType((GenericArrayType) type, typeParsers, this);
        } else if(type instanceof ParameterizedType){
            invoker = Invoker.forParameterizedType((ParameterizedType) type, typeParsers, this);
        } else if(type instanceof Class) {
            Class<?> someClass = (Class<?>) type;
            if(someClass.isArray()){
                invoker = Invoker.forArrayClass((Class<?>) type, typeParsers, this);
            } else {
                invoker = Invoker.forClass((Class<?>) type, typeParsers);
            }
        } else {
            // TODO
            throw new RuntimeException("FIXME!!!");
        }

        return invoker.invokeTypeParserTemplate(input);
    }

    /**
     * This method is static because it is also called from {@link StringToTypeParserBuilder}.
     */
    static String nullArgumentErrorMsg(String argName) {
        return String.format("Argument named '%s' is illegally set to null!", argName);
    }



}