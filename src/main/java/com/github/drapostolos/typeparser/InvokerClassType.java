package com.github.drapostolos.typeparser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

class InvokerClassType extends Invoker {
    private static final Object STATIC_METHOD = null;
    private static final Object FACTORY_METHOD_MISSING = null;

    InvokerClassType(Class<?> targetType, Map<Class<?>, TypeParser> typeParsers) {
        super(targetType, typeParsers);
    }

    @Override
    Object invokeTypeParser(String input) {
        Object result = null;
        if (containsTypeParserForTargetType()) {
            SimpleTypeParser<?> typeParser = getTypeParser(SimpleTypeParser.class);
            result = invokeTypeParser(input, typeParser);
        } else if ((result = callFactoryMethodIfExisting("valueOf", input, targetType)) != FACTORY_METHOD_MISSING) {
            //
        } else if ((result = callFactoryMethodIfExisting("forName", input, targetType)) != FACTORY_METHOD_MISSING) {
            //
        } else {
            String message = "There is no registered 'TypeParser' for that type, or that "
                    + "type does not contain one of the following static factory methods: "
                    + "'%s.valueOf(String)', or '%s.of(String)'.";
            message = String.format(message, targetType.getSimpleName(), targetType.getSimpleName());
            message = canNotParseErrorMsg(input, message);
            throw new IllegalArgumentException(message);
        }
//        /*
//         * This cast is correct, since all above checks ensures we're casting to
//         * the right type.
//         */
//        @SuppressWarnings("unchecked")
//        T temp = (T) result;
        return result;
    }
    
    private Object invokeTypeParser(String input, SimpleTypeParser<?> typeParser) {
        try {
            return typeParser.parse(input);
        } catch (NumberFormatException e) {
            String message = canNotParseErrorMsg(input, numberFormatErrorMsg(e));
            throw new IllegalArgumentException(message, e);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(canNotParseErrorMsg(input, e.getMessage()), e);
        }
    }

    private String numberFormatErrorMsg(NumberFormatException e) {
        return String.format("Number format exception %s.", e.getMessage());
    }

    private String canNotParseErrorMsg(String value, String message) {
        return String.format("Can not parse \"%s\" to type '%s' due to: %s", value, targetType.getName(), message);
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
            throw new IllegalArgumentException(makeErrorMsg(methodName, value), e.getCause());
        } catch (Throwable t) {
            throw new IllegalArgumentException(makeErrorMsg(methodName, value), t);
        }
    }

    private String makeErrorMsg(String methodName, String value) {
        String methodSignature = String.format("%s.%s('%s')", targetType.getName(), methodName, value);
        String message = " Exception thrown in static factory method '%s'. See underlying "
                + "exception for additional information.";
        return canNotParseErrorMsg(value, String.format(message, methodSignature));
    }

}
