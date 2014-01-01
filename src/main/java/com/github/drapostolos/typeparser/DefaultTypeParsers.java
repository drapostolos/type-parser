package com.github.drapostolos.typeparser;

import java.io.File;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.runners.Parameterized;

/*
 * When adding new default type parsers: add them at the bottom (+ add TypeParser*Test.java class with unit tests).
 */
class DefaultTypeParsers {
    private DefaultTypeParsers() { new AssertionError("Not meant for instantiation"); }
    private static final String BOOLEAN_ERROR_MESSAGE = "\"%s\" is not parsable to a Boolean.";
    private static final String CLASS_ERROR_MESSAGE = "\"%s\" is not parsable to a Class object.";
    private static final String CHARACTER_ERROR_MESSAGE = "\"%s\" must only contain a single character.";
    private static final String DEFAULT_DELIMITER = ",";
    private static final String DEFAULT_KEY_VALUE_DELIMITER = "=";
    private static final Map<Class<?>, TypeParser> DEFAULT_TYPE_PARSERS = new HashMap<Class<?>, TypeParser>();
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE = new HashMap<Class<?>, Class<?>>();

    static Map<Class<?>, TypeParser> copy() {
        return new HashMap<Class<?>, TypeParser>(DEFAULT_TYPE_PARSERS);
    }

    static{
        WRAPPER_TO_PRIMITIVE.put(Boolean.class, boolean.class);
        WRAPPER_TO_PRIMITIVE.put(Byte.class, byte.class);
        WRAPPER_TO_PRIMITIVE.put(Short.class, short.class);
        WRAPPER_TO_PRIMITIVE.put(Character.class, char.class);
        WRAPPER_TO_PRIMITIVE.put(Integer.class, int.class);
        WRAPPER_TO_PRIMITIVE.put(Long.class, long.class);
        WRAPPER_TO_PRIMITIVE.put(Float.class, float.class);
        WRAPPER_TO_PRIMITIVE.put(Double.class, double.class);
    }

    private static <T> void put(Class<? super T> type, SimpleTypeParser<T> typeParser){
        DEFAULT_TYPE_PARSERS.put(type, typeParser);
        if(WRAPPER_TO_PRIMITIVE.containsKey(type)){
            // add primitive targetType if existing, example int.class, boolean.class etc.
            Class<?> primitiveType = WRAPPER_TO_PRIMITIVE.get(type);
            DEFAULT_TYPE_PARSERS.put(primitiveType, typeParser);
        }
    }

    private static <T> void put(Class<? super T> type, ParameterizedTypeParser<T> typeParser) {
        DEFAULT_TYPE_PARSERS.put(type, typeParser);
    }

    private static <T> void put(Class<?> type, ArrayTypeParser typeParser) {
        DEFAULT_TYPE_PARSERS.put(type, typeParser);
    }

    static{
        put(Array.class, TypeParsers.array2D(DEFAULT_DELIMITER));
        put(List.class, TypeParsers.list(DEFAULT_DELIMITER));
        put(Set.class, TypeParsers.set(DEFAULT_DELIMITER));
        put(Map.class, TypeParsers.map(DEFAULT_DELIMITER, DEFAULT_KEY_VALUE_DELIMITER));

        put(Boolean.class, new SimpleTypeParser<Boolean>() {
            @Override
            public Boolean parse(final String value0) {
                String value = value0.trim().toLowerCase();
                if(value.equals("true")){
                    return Boolean.TRUE;
                } else if(value.equals("false")){
                    return Boolean.FALSE;
                }
                throw new IllegalArgumentException(String.format(BOOLEAN_ERROR_MESSAGE, value0));
            }
        });

        put(Character.class, new SimpleTypeParser<Character>() {
            @Override
            public Character parse(String value) {
                if(value.length() == 1){
                    return Character.valueOf(value.charAt(0));
                }
                throw new IllegalArgumentException(String.format(CHARACTER_ERROR_MESSAGE, value));
            }
        });

        put(Byte.class, new SimpleTypeParser<Byte>() {
            @Override
            public Byte parse(String value) {
                return Byte.valueOf(value.trim());
            }
        });

        put(Integer.class, new SimpleTypeParser<Integer>() {
            @Override
            public Integer parse(String value) {
                return Integer.valueOf(value.trim());
            }
        });

        put(Long.class, new SimpleTypeParser<Long>() {
            @Override
            public Long parse(String value) {
                return Long.valueOf(value.trim());
            }
        });

        put(Short.class, new SimpleTypeParser<Short>() {
            @Override
            public Short parse(String value) {
                return Short.valueOf(value.trim());
            }
        });

        put(Float.class, new SimpleTypeParser<Float>() {
            @Override
            public Float parse(String value) {
                return Float.valueOf(value);
            }
        });

        put(Double.class, new SimpleTypeParser<Double>() {
            @Override
            public Double parse(String value) {
                return Double.valueOf(value);
            }
        });

        put(File.class, new SimpleTypeParser<File>() {
            @Override
            public File parse(String value) {
                return new File(value.trim());
            }
        });

        put(String.class, new SimpleTypeParser<String>() {
            @Override
            public String parse(String value) {
                return value;
            }
        });

        put(Class.class, new SimpleTypeParser<Class<?>>() {

            @Override
            public Class<?> parse(String input) {
                try {
                    return Class.forName(input);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(String.format(CLASS_ERROR_MESSAGE, input));
                }
            }
        });
    }
}
