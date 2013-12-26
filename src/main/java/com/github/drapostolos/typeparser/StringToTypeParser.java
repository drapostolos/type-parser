package com.github.drapostolos.typeparser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public final class StringToTypeParser {
    private static final StringToTypeParser defaultTypeParser = newBuilder().build();
    private final Map<Class<?>, TypeParser<?>> typeParsers;

    public static StringToTypeParserBuilder newBuilder() {
        return new StringToTypeParserBuilder();
    }
    
    public static <T> T parse(String value, Class<T> type){
        return defaultTypeParser.parseType(value, type);
    }
    
    StringToTypeParser(HashMap<Class<?>, TypeParser<?>> typeParsers) {
        this.typeParsers = typeParsers;
    }

    public <T> T parseType(String value, Class<T> type) {
        if (value == null) {
            throw new NullPointerException(Util.nullArgumentErrorMsg("value"));
        }
        if (type == null) {
            throw new NullPointerException(Util.nullArgumentErrorMsg("type"));
        }

        if (value.trim().equalsIgnoreCase("null")) {
            if (type.isPrimitive()) {
                String message = "'%s' primitive can not be set to null.";
                throw new IllegalArgumentException(String.format(message,
                        type.getName()));
            }
            return null;
        }

        Object result = null;
        if (type == String.class) {
            result = value;
        } else if (typeParsers.containsKey(type)) {
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

    private static String numberFormatErrorMsg(NumberFormatException e) {
        return String.format("Number format exception %s.", e.getMessage());
    }

    private static String canNotParseErrorMsg(String value, Class<?> type, String message) {
        return String.format("Can not parse \"%s\" to type '%s' due to: %s", value, type.getName(), message);
    }

    private static Object callFactoryMethodIfExisting(String methodName, String value, Class<?> type) {
        Method m;
        try {
            m = type.getDeclaredMethod(methodName, String.class);
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
                return m.invoke(null, value.trim());
            } else {
                return m.invoke(null, value);
            }
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(makeErrorMsg(methodName, value, type), e.getCause());
        } catch (Throwable t) {
            throw new IllegalArgumentException(makeErrorMsg(methodName, value, type), t);
        }
    }

    private static String makeErrorMsg(String methodName, String value,  Class<?> type) {
        String methodSignature = String.format("%s.%s('%s')", type.getName(), methodName, value);
        String message = " Exception thrown in static factory method '%s'. See underlying "
                + "exception for additional information.";
        return canNotParseErrorMsg(value, type, String.format(message, methodSignature));
    }
}