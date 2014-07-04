package com.github.drapostolos.typeparser;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

abstract class Helper {

    final Type targetType;
    final Class<?> rawTargetType;

    Helper(Type targetType) {
        this.targetType = targetType;
        this.rawTargetType = extractRawTargetType();
    }

    private Class<?> extractRawTargetType() {
        if (targetType instanceof Class) {
            return (Class<?>) targetType;
        }
        if (isTargetTypeParameterized()) {
            ParameterizedType type = (ParameterizedType) targetType;
            return (Class<?>) type.getRawType();
        }
        if (targetType instanceof GenericArrayType) {
            GenericArrayType array = (GenericArrayType) targetType;
            Type componentType = array.getGenericComponentType();
            if (componentType instanceof Class) {
                return (Class<?>) Array.newInstance((Class<?>) componentType, 0).getClass();
            }
            if (componentType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) componentType;
                Class<?> rawType = (Class<?>) parameterizedType.getRawType();
                return (Class<?>) Array.newInstance(rawType, 0).getClass();
            }
        }
        return targetType.getClass();
    }

    @Override
    public String toString() {
        return Util.toString(targetType);
    }

    /**
     * Returns the type to parse the input string to.
     * 
     * @return the {@link Type} to parse to.
     */
    final public Type getTargetType() {
        return targetType;
    }

    /**
     * When the {@code targetType} is a parameterized type this method
     * returns a list with the type arguments.
     * <p/>
     * All type arguments must be none parameterized types (i.e. nested parameterized types are not
     * allowed), with one exception: {@link Class}. <br/>
     * 
     * @return List of {@link Class} types.
     * @throws UnsupportedOperationException if the {@code targetType} is not a parameterized type.
     * @throws UnsupportedOperationException if any of the parameterized type arguments is of a
     *         parameterized type (with exception of {@link Class}).
     */
    final public <T> List<Class<T>> getParameterizedClassArguments() {
        if (!(isTargetTypeParameterized())) {
            String message = "type must be parameterized: \"%s\" {instance of: %s}.";
            throw new UnsupportedOperationException(String.format(message, targetType, targetType.getClass()));
        }

        ParameterizedType pt = (ParameterizedType) targetType;
        List<Class<T>> result = new ArrayList<Class<T>>();
        for (Type typeArgument : pt.getActualTypeArguments()) {
            if (typeArgument instanceof Class) {
                /*
                 * This cast is correct since we check typeArgument is instance of Class
                 */
                @SuppressWarnings("unchecked")
                Class<T> cls = (Class<T>) typeArgument;
                result.add(cls);
                continue;
            }
            if (typeArgument instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) typeArgument).getRawType();
                if (rawType instanceof Class) {
                    /*
                     * Special case to handle Class<?>
                     * This cast is correct since we check rawType is instance of Class
                     */
                    @SuppressWarnings("unchecked")
                    Class<T> cls = (Class<T>) rawType;
                    result.add(cls);
                    continue;
                }
            }
            String message = "That type contains illegal type argument: '%s' [%s]";
            message = String.format(message, typeArgument, typeArgument.getClass());
            throw new UnsupportedOperationException(message);
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
     * @throws UnsupportedOperationException if the {@code targetType} is not of array type.
     * @throws UnsupportedOperationException if the component type is of parameterized type
     *         (with exception of {@link Class}).
     */
    Class<?> getComponentClass() {
        if (rawTargetType.isArray()) {
            return rawTargetType.getComponentType();
        }
        throw new UnsupportedOperationException("type is not an array.");
    }

    /**
     * Convenient method for retrieving elements by index position in the list as returned from
     * method {@link ParserHelper#getParameterizedClassArguments()}
     * 
     * @param index in list of type arguments.
     * @return Type argument.
     * @throws IllegalArgumentException when {@code index} is negative or larger
     *         tan number of elements in list.
     * @throws UnsupportedOperationException if the {@code targetType} is not a parameterized type.
     * @throws UnsupportedOperationException if any of the parameterized type arguments is of a
     *         parameterized type (with exception of {@link Class}).
     */
    final public <T> Class<T> getParameterizedClassArgumentByIndex(int index) {
        if (index < 0) {
            String message = "Argument named 'index' is illegally "
                    + "set to negative value: %s. Must be positive.";
            throw new IllegalArgumentException(String.format(message, index));
        }
        List<Class<T>> list = getParameterizedClassArguments();
        if (index >= list.size()) {
            String message = "Argument named 'index' is illegally "
                    + "set to value: %s. List size is: %s.";
            throw new IllegalArgumentException(String.format(message, index, list.size()));
        }
        return list.get(index);
    }

    /**
     * Returns the {@link Class} object of the targetType.
     * Assuming targeType is an instance of {@link Class},
     * otherwise throws an {@link UnsupportedOperationException}.
     * 
     * @return {@link Class} object of the targetType.
     * @throws UnsupportedOperationException if targetType is not an instance of {@link Class}.
     */
    final public <T> Class<T> getTargetClass() {
        if (targetType instanceof Class) {
            @SuppressWarnings("unchecked")
            Class<T> temp = (Class<T>) targetType;
            return temp;
        }
        String message = "%s [%s] cannot be casted to java.lang.Class";
        message = String.format(message, targetType, targetType.getClass());
        throw new UnsupportedOperationException(message);
    }

    /**
     * Checks if {@code targetType} is of Parameterized type, return true if it is,
     * otherwise false.
     * <p>
     * Note!<br/>
     * If {@code targetType} is of a raw collection type (e.g. java.lang.List.class), then false is
     * returned.
     * 
     * @return
     */
    final public boolean isTargetTypeParameterized() {
        if (targetType instanceof ParameterizedType) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the raw format of {@code targetType} is assignable to the given {@code type}.
     * <p/>
     * Examples: <br \>
     * <code>isTargetTypeAssignableTo(Number.class); // true</code> if targetType is Integer.class<br \>
     * <code>isTargetTypeAssignableTo(List.class); // true</code> if targetType is
     * {@code List<Long>} <br \>
     * 
     * @param type check if {@code targetType} is assignable to this type.
     * @return true if {@code targetType} is assignable to the given {@code type}, otherwise false.
     */
    final public boolean isTargetTypeAssignableTo(Class<?> type) {
        return type.isAssignableFrom(rawTargetType);
    }

    /**
     * Checks if {@code targetType} is equal to the given {@code type}.
     * 
     * @param type to compare with {@code targetType}.
     * @return true if {@code targetType} is equal to the given {@code type}.
     */
    final public boolean isTargetTypeEqualTo(Class<?> type) {
        return targetType.equals(type);
    }

    /**
     * Checks if {@code targetType} is equal to the generic type as represented by
     * {@code genericType}.
     * 
     * @param genericType a generic type to compare with {@code targetType}.
     * @return true if {@code targetType} is equal to the generic type represented by the given
     *         {@code genericType}.
     */
    final public boolean isTargetTypeEqualTo(GenericType<?> genericType) {
        return targetType.equals(genericType.getType());
    }

    /**
     * Checks if at least one of the given {@code types} is equal to
     * the raw {@code targetType} (as returned from {@link #getRawTargetClass()}).
     * 
     * @param types to check equality with the raw {@code targetType}
     * @return true if at least one of the given {@code types} is equal to the raw
     *         {@code targetType}.
     */
    final public boolean isRawTargetClassAnyOf(Class<?>... types) {
        for (Class<?> type : types) {
            if (type.equals(rawTargetType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the raw format of {@code targetType}.
     * <p \>
     * Example <br \>
     * String => String.class<br \>
     * {@code List<Integer>} => List.class<br \>
     * {@code String[]} => String[].class<br \>
     * {@code Class<?>[]} => Class[].class<br \>
     * 
     * @return the raw format of {@code targetType}
     */
    @SuppressWarnings("unchecked")
    final public <T> Class<T> getRawTargetClass() {
        return (Class<T>) rawTargetType;
    }
}
