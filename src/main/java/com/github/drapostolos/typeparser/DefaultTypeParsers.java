package com.github.drapostolos.typeparser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/*
 * When adding new default type parsers: add them at the bottom (+ add TypeParser*Test.java class with unit tests).
 */
class DefaultTypeParsers {
    private DefaultTypeParsers() { new AssertionError("Not meant for instantiation"); }
    private static final String BOOLEAN_ERROR_MESSAGE = "\"%s\" is not parsable to a Boolean.";
    private static final String CHARACTER_ERROR_MESSAGE = "\"%s\" must only contain a single character.";
    private static final Map<Class<?>, TypeParser<?>> DEFAULT_TYPE_PARSERS = new HashMap<Class<?>, TypeParser<?>>();
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE = new HashMap<Class<?>, Class<?>>();

    static Map<Class<?>, TypeParser<?>> copy() {
        return new HashMap<Class<?>, TypeParser<?>>(DEFAULT_TYPE_PARSERS);
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

    private static <T> void put(Class<T> type, TypeParser<? extends T> typeParser){
        DEFAULT_TYPE_PARSERS.put(type, typeParser);
        if(WRAPPER_TO_PRIMITIVE.containsKey(type)){
            // add primitive targetType if existing, example int.class, boolean.class etc.
            Class<?> primitiveType = WRAPPER_TO_PRIMITIVE.get(type);
            DEFAULT_TYPE_PARSERS.put(primitiveType, typeParser);
        }
    }

    static{
        put(Boolean.class, new TypeParser<Boolean>() {
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

        put(Character.class, new TypeParser<Character>() {
            @Override
            public Character parse(String value) {
                if(value.length() == 1){
                    return Character.valueOf(value.charAt(0));
                }
                throw new IllegalArgumentException(String.format(CHARACTER_ERROR_MESSAGE, value));
            }
        });

        put(Byte.class, new TypeParser<Byte>() {
            @Override
            public Byte parse(String value) {
                return Byte.valueOf(value.trim());
            }
        });

        put(Integer.class, new TypeParser<Integer>() {
            @Override
            public Integer parse(String value) {
                return Integer.valueOf(value.trim());
            }
        });

        put(Long.class, new TypeParser<Long>() {
            @Override
            public Long parse(String value) {
                return Long.valueOf(value.trim());
            }
        });

        put(Short.class, new TypeParser<Short>() {
            @Override
            public Short parse(String value) {
                return Short.valueOf(value.trim());
            }
        });

        put(Float.class, new TypeParser<Float>() {
            @Override
            public Float parse(String value) {
                return Float.valueOf(value);
            }
        });

        put(Double.class, new TypeParser<Double>() {
            @Override
            public Double parse(String value) {
                return Double.valueOf(value);
            }
        });

        put(File.class, new TypeParser<File>() {
            @Override
            public File parse(String value) {
                return new File(value.trim());
            }
        });

        put(String.class, new TypeParser<String>() {
            @Override
            public String parse(String value) {
                return value;
            }
        });
    }
}
