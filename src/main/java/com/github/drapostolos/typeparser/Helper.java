package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.TypeParserUtility.getParameterizedTypeArguments;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

abstract class Helper {

    final protected Type targetType;

    Helper(Type targetType) {
        this.targetType = targetType;
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
     * @throws IllegalStateException if the {@code targetType} is not a parameterized type.
     * @throws IllegalStateException if any of the parameterized type arguments is of a
     *         parameterized type (with exception of {@link Class}).
     */
    final public <T> List<Class<T>> getParameterizedClassArguments() {
        return getParameterizedTypeArguments(targetType);
    }

    /**
     * Convenient method for retrieving elements by index position in the list as returned from
     * method {@link ParserHelper#getParameterizedClassArguments()}
     * 
     * @param index in list of type arguments.
     * @return Type argument.
     * @throws IllegalArgumentException when {@code index} is negative or larger
     *         tan number of elements in list.
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
     * otherwise throws an {@link IllegalStateException}.
     * 
     * @return {@link Class} object of the targetType.
     * @throws IllegalStateException if targetType is not an instance of {@link Class}.
     */
    final public <T> Class<T> getTargetClass() {
        if (targetType instanceof Class) {
            @SuppressWarnings("unchecked")
            Class<T> temp = (Class<T>) targetType;
            return temp;
        }
        String message = "%s [%s] cannot be casted to java.lang.Class";
        message = String.format(message, targetType, targetType.getClass());
        throw new IllegalStateException(message);
    }

    /**
     * Checks if {@code targetType} is assignable to the given {@code type}.
     * <p/>
     * Examples: <br \>
     * <code>isTargetTypeAssignableTo(Number.class); // true<code> if targetType is Integer.class<br \> 
     * <code>isTargetTypeAssignableTo(List.class); // true if<code> targetType is {@code List<Long>} <br \>
     * 
     * @param type check if {@code targetType} is assignable to this type.
     * @return true if {@code targetType} is assignable to the given {@code type}, otherwise false.
     */
    final public boolean isTargetTypeAssignableTo(Class<?> type) {
        return type.isAssignableFrom(getRawTargetClass());
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
    final public <T> Class<T> getRawTargetClass() {
        if (targetType instanceof Class) {
            @SuppressWarnings("unchecked")
            Class<T> temp = (Class<T>) targetType;
            return temp;
        }
        if (targetType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) targetType;
            @SuppressWarnings("unchecked")
            Class<T> temp = (Class<T>) type.getRawType();
            return temp;
        }
        if (targetType instanceof GenericArrayType) {
            GenericArrayType array = (GenericArrayType) targetType;
            Type componentType = array.getGenericComponentType();
            if (componentType instanceof Class) {
                @SuppressWarnings("unchecked")
                Class<T> temp = (Class<T>) Array.newInstance((Class<T>) componentType, 0).getClass();
                return temp;
            }
            if (componentType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) componentType;
                @SuppressWarnings("unchecked")
                Class<T> rawType = (Class<T>) parameterizedType.getRawType();
                @SuppressWarnings("unchecked")
                Class<T> temp = (Class<T>) Array.newInstance(rawType, 0).getClass();
                return temp;
            }
        }
        String message = "Cannot extract raw type from: %s [%s].";
        message = String.format(message, targetType, targetType.getClass());
        throw new IllegalStateException(message);
    }
}
