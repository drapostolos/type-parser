package com.github.drapostolos.typeparser;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

final class TypeParserUtility {

    static final String STATIC_FACTORY_METHOD_NAME = "valueOf";
    private static final SplitStrategy DEFAULT_SPLIT_STRATEGY = new DefaultSplitStrategy();
    private static final SplitStrategy DEFAULT_KEY_VALUE_SPLIT_STRATEGY = new KeyValueSplitStrategy();
    private static final InputPreprocessor DEFAULT_INPUT_PREPROCESSOR = new NullInputPreprocessor();

    private TypeParserUtility() {
        throw new AssertionError("Not meant for instantiation");
    }

    static InputPreprocessor defaultInputPreprocessor() {
        return DEFAULT_INPUT_PREPROCESSOR;
    }

    static SplitStrategy defaultSplitStrategy() {
        return DEFAULT_SPLIT_STRATEGY;
    }

    static SplitStrategy defaultKeyValueSplitStrategy() {
        return DEFAULT_KEY_VALUE_SPLIT_STRATEGY;
    }

    static String makeNullArgumentErrorMsg(String argName) {
        return String.format("Argument named '%s' is illegally set to null!", argName);
    }

    static String makeParseErrorMsg(String input, Type targetType, String message) {
        return String.format("Can not parse \"%s\" to type \"%s\" [instance of: %s] due to: %s",
                input, targetType, targetType.getClass().getName(), message);
    }

    /*
     * Checks if the given class contains a static method
     * named valueOf(String)
     */
    static boolean containsStaticFactoryMethodNamedValueOf(Class<?> type) {
        Method method = getMethodNamedValueOf(type);
        if (method == null) {
            return false;
        }
        if (!Modifier.isStatic(method.getModifiers())) {
            return false;
        }
        if (!method.getReturnType().equals(type)) {
            return false;
        }
        return true;
    }

    /*
     * Returns the Method object for the method named valueOf(String)
     * if existing in the given class, else returns null.
     */
    static Method getMethodNamedValueOf(Class<?> targetType) {
        try {
            return targetType.getDeclaredMethod(STATIC_FACTORY_METHOD_NAME, String.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * When the {@code targetType} is a parameterized type this method
     * returns a list with the type arguments.
     * <p/>
     * All type arguments must be none parameterized types (i.e. nested parameterized types are not
     * allowed), with one exception: {@link Class<?>}. <br/>
     * 
     * @return List of {@link Class} types.
     * @throws IllegalStateException if the {@code targetType} is not a parameterized type.
     * @throws IllegalStateException if any of the parameterized type arguments is of a
     *         parameterized type (with exception of {@link Class}).
     */
    static List<Class<?>> getParameterizedTypeArguments(Type targetType) {
        if (!(targetType instanceof ParameterizedType)) {
            String message = "TargetType: '%s' [%s] must be a parameterized "
                    + "type when calling this method, but it is not.";
            throw new IllegalStateException(String.format(message, targetType, targetType.getClass()));
        }

        ParameterizedType pt = (ParameterizedType) targetType;
        List<Class<?>> result = new ArrayList<Class<?>>();
        for (Type typeArgument : pt.getActualTypeArguments()) {
            if (typeArgument instanceof Class) {
                result.add((Class<?>) typeArgument);
                continue;
            }
            if (typeArgument instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) typeArgument).getRawType();
                if (rawType instanceof Class) {
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
     * When the {@code targetType} is an array, this method
     * returns the component type.
     * <p/>
     * The component type must be of a non-parameterized type, with one exception: {@link Class<?>}.
     * <br/>
     * 
     * @return the component type of the array.
     * @throws IllegalStateException if the {@code targetType} is not of array type.
     * @throws IllegalStateException if the component type is of parameterized type
     *         (with exception of {@link Class}).
     */
    static Class<?> getComponentClass(Type targetType) {
        if (targetType instanceof Class) {
            /*
             * Handle array classes, such as Integer[].class, Class<?>[].class etc.
             */
            Class<?> t = (Class<?>) targetType;
            if (t.isArray()) {
                return (Class<?>) t.getComponentType();
            }
        }
        if (targetType instanceof GenericArrayType) {
            Type t = ((GenericArrayType) targetType).getGenericComponentType();
            if (t instanceof Class) {
                return (Class<?>) t;
            }
        }
        String message = "TargetType: '%s' [%s] is either not an array or "
                + "the componet type is generic.";
        message = String.format(message, targetType, targetType.getClass());
        throw new IllegalStateException(message);
    }

}
