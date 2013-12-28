package com.github.drapostolos.typeparser;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
    private static final Object STATIC_METHOD = null;
    private final Map<Class<?>, TypeParser<?>> typeParsers;

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
    
    StringToTypeParser(Map<Class<?>, TypeParser<?>> typeParsers) {
        this.typeParsers = Collections.unmodifiableMap(new HashMap<Class<?>, TypeParser<?>>(typeParsers));
    }

    /**
     * Parses the given {@code value} to the given {@code type}. Where {@code type}
     * is either one of the default supported java types or a registered {@link TypeParser}
     * (for custom made types).
     * 
     * @param value - string value to parse
     * @param type - the expected type to convert {@code value} to.
     * @return an instance of {@code type} corresponding to the given {@code value}.
     * @throws NullPointerException if any of the arguments are null.
     * @throws IllegalArgumentException if {@code value} is not parsable, or
     * if {@code type} is not recognized.
     */
    public <T> T parse(String value, Class<T> type) {
        if (value == null) {
            throw new NullPointerException(nullArgumentErrorMsg("value"));
        }
        if (type == null) {
            throw new NullPointerException(nullArgumentErrorMsg("type"));
        }
        
        // convert "null" string to null type.
        if (value.trim().equalsIgnoreCase("null")) {
            if (type.isPrimitive()) {
                String message = "'%s' primitive can not be set to null.";
                throw new IllegalArgumentException(String.format(message, type.getName()));
            }
            return null; 
        }

        Object result = null;
        if (typeParsers.containsKey(type)) {
            result = callTypeParser(value, type);
        } else if ((result = callFactoryMethodIfExisting("valueOf", value, type)) != null) {
            //
        } else if ((result = callFactoryMethodIfExisting("of", value, type)) != null) {
            //
        } else {
            String message = "There is no registered 'TypeParser' for that type, or that "
                    + "type does not contain one of the following static factory methods: "
                    + "'%s.valueOf(String)', or '%s.of(String)'.";
            message = String.format(message, type.getSimpleName(), type.getSimpleName());
            message = canNotParseErrorMsg(value, type, message);
            throw new IllegalArgumentException(message);
        }
        /*
         * This cast is correct, since all above checks ensures we're casting to
         * the right type.
         */
        @SuppressWarnings("unchecked")
        T temp = (T) result;
        return temp;
    }

    /**
     * This method is static because it is also called from {@link StringToTypeParserBuilder}.
     */
    static String nullArgumentErrorMsg(String argName) {
        return String.format("Argument named '%s' is illegally set to null!", argName);
    }

    private Object callTypeParser(String value, Class<?> type) {
        try {
            return typeParsers.get(type).parse(value);
        } catch (NumberFormatException e) {
            String message = canNotParseErrorMsg(value, type, numberFormatErrorMsg(e));
            throw new IllegalArgumentException(message, e);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(canNotParseErrorMsg(value, type, e.getMessage()), e);
        }
    }

    private String numberFormatErrorMsg(NumberFormatException e) {
        return String.format("Number format exception %s.", e.getMessage());
    }

    private String canNotParseErrorMsg(String value, Class<?> type, String message) {
        return String.format("Can not parse \"%s\" to type '%s' due to: %s", value, type.getName(), message);
    }

    private Object callFactoryMethodIfExisting(String methodName, String value, Class<?> type) {
        Method m;
        try {
            m = type.getDeclaredMethod(methodName, String.class);
            m.setAccessible(true);
            if (!Modifier.isStatic(m.getModifiers())) {
                // Static factory method does not exists, return null
                return null;
            }
        } catch (Exception e) {
            // Static factory method does not exists, return null
            return null;
        }

        try {
            if(type.isEnum()){
                value = value.trim();
            }
            return m.invoke(STATIC_METHOD, value);
        } catch (InvocationTargetException e) {
            // filter out the InvocationTargetException stacktrace/message.
            throw new IllegalArgumentException(makeErrorMsg(methodName, value, type), e.getCause());
        } catch (Throwable t) {
            throw new IllegalArgumentException(makeErrorMsg(methodName, value, type), t);
        }
    }

    private String makeErrorMsg(String methodName, String value,  Class<?> type) {
        String methodSignature = String.format("%s.%s('%s')", type.getName(), methodName, value);
        String message = " Exception thrown in static factory method '%s'. See underlying "
                + "exception for additional information.";
        return canNotParseErrorMsg(value, type, String.format(message, methodSignature));
    }
}