package com.github.drapostolos.typeparser;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class TypeParsers {
    static final Type ANY_LIST = new GenericType<List<?>>(){}.getType();
    static final Type ANY_SET = new GenericType<Set<?>>(){}.getType();
    static final Type ANY_MAP = new GenericType<Map<?, ?>>(){}.getType();
    static final Type ANY_ARRAY = Array.class;
    static final Type ANY_CLASS_WITH_STATIC_VALUEOF_METHOD = StaticFactoryMethodTypeParser.class;
    static final Type CLASS_TYPE = new GenericType<Class<?>>(){}.getType();
    private static final Type ARRAY_OF_CLASS = new GenericType<Class<?>[]>(){}.getType();
    private static final String BOOLEAN_ERROR_MESSAGE = "\"%s\" is not parsable to a Boolean.";
    private static final String CLASS_ERROR_MESSAGE = "\"%s\" is not parsable to a Class object.";
    private static final String CHARACTER_ERROR_MESSAGE = "\"%s\" must only contain a single character.";
    private static final Map<Type, TypeParser<?>> DEFAULT_TYPE_PARSERS = new HashMap<Type, TypeParser<?>>();

    private TypeParsers() { throw new AssertionError("Not meant for instantiation"); }

    static Map<Type, TypeParser<?>> copyDefault() {
        return new HashMap<Type, TypeParser<?>>(DEFAULT_TYPE_PARSERS);
    }

    /*
     * Register all default TypeParsers.
     */
    static{
        registerTypeParser(ANY_LIST, TypeParsers.forLists());
        registerTypeParser(ANY_SET, TypeParsers.forSets());
        registerTypeParser(ANY_MAP, TypeParsers.forMaps());
        registerTypeParser(ANY_ARRAY, TypeParsers.forArrays());
        registerTypeParser(ANY_CLASS_WITH_STATIC_VALUEOF_METHOD, new StaticFactoryMethodTypeParser());
        registerTypeParser(types(Boolean.class, boolean.class), new TypeParser<Boolean>(){
            @Override
            public Boolean parse(final String value0, TypeParserHelper helper) {
                String value = value0.trim().toLowerCase();
                if(value.equals("true")){
                    return Boolean.TRUE;
                } else if(value.equals("false")){
                    return Boolean.FALSE;
                }
                throw new IllegalArgumentException(String.format(BOOLEAN_ERROR_MESSAGE, value0));
            }
        });
        registerTypeParser(types(Character.class, char.class), new TypeParser<Character>() {
            @Override
            public Character parse(String value, TypeParserHelper helper) {
                if(value.length() == 1){
                    return Character.valueOf(value.charAt(0));
                }
                throw new IllegalArgumentException(String.format(CHARACTER_ERROR_MESSAGE, value));
            }
        });
        registerTypeParser(BigDecimal.class, new TypeParser<BigDecimal>() {
            @Override
            public BigDecimal parse(String value, TypeParserHelper helper) {
                try {
                    return new BigDecimal(value.trim());
                } catch (NumberFormatException e){
                    String message = String.format("For input string: \"%s\"", value.trim());
                    throw new NumberFormatException(message);
                }
            }
        });
        registerTypeParser(types(byte.class, Byte.class), new TypeParser<Byte>() {
            @Override
            public Byte parse(String value, TypeParserHelper helper) {
                return Byte.valueOf(value.trim());
            }
        });
        registerTypeParser(types(Integer.class, int.class), new TypeParser<Integer>() {
            @Override
            public Integer parse(String value, TypeParserHelper helper) {
                return Integer.valueOf(value.trim());
            }
        });
        registerTypeParser(types(long.class, Long.class), new TypeParser<Long>() {
            @Override
            public Long parse(String value, TypeParserHelper helper) {
                return Long.valueOf(value.trim());
            }
        });
        registerTypeParser(types(Short.class, short.class), new TypeParser<Short>() {
            @Override
            public Short parse(String value, TypeParserHelper helper) {
                return Short.valueOf(value.trim());
            }
        });
        registerTypeParser(types(float.class, Float.class), new TypeParser<Float>() {
            @Override
            public Float parse(String value, TypeParserHelper helper) {
                return Float.valueOf(value);
            }
        });
        registerTypeParser(types(double.class, Double.class), new TypeParser<Double>() {
            @Override
            public Double parse(String value, TypeParserHelper helper) {
                return Double.valueOf(value);
            }
        });
        registerTypeParser(File.class, new TypeParser<File>() {
            @Override
            public File parse(String value, TypeParserHelper helper) {
                return new File(value.trim());
            }
        });
        registerTypeParser(String.class, new TypeParser<String>() {
            @Override
            public String parse(String value, TypeParserHelper helper) {
                return value;
            }
        });
        registerTypeParser(types(CLASS_TYPE, Class.class), new TypeParser<Class<?>>() {

            @Override
            public Class<?> parse(String input, TypeParserHelper helper) {
                try {
                    return Class.forName(input.trim());
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(String.format(CLASS_ERROR_MESSAGE, input));
                }
            }
        });
        registerTypeParser(ARRAY_OF_CLASS, new TypeParser<Class<?>[]>() {
            @Override
            public Class<?>[] parse(String input, TypeParserHelper helper) {
                List<String> strList = helper.split(input);
                Class<?>[] array = new Class<?>[strList.size()];
                for(int i = 0; i < strList.size(); i++){
                    Class<?> element = helper.parse(strList.get(i), Class.class);
                    array[i] = element;
                }
                return array;
            }
        });
    }

    private static List<Type> types(Type... types) {
        return Arrays.asList(types);
    }
    private static void registerTypeParser(Type type, TypeParser<?> typeParser) {
        registerTypeParser(types(type), typeParser);
    }
    private static void registerTypeParser(List<Type> types, TypeParser<?> typeParser) {
        for(Type type : types){
            DEFAULT_TYPE_PARSERS.put(type, typeParser);
        }
    }

    private static <T> TypeParser<T> forArrays(){
        return new TypeParser<T>() {
            @Override
            public T parse(String input, TypeParserHelper helper) {
                List<String> strList = helper.split(input);
                Class<?> componentType = TypeParserUtility.getComponentClass(helper.getTargetType());
                Object array = Array.newInstance(componentType, strList.size());
                for(int i = 0; i < strList.size(); i++){
                    Object element = helper.parse(strList.get(i), componentType);
                    Array.set(array, i, element);
                }
                @SuppressWarnings("unchecked")
                T temp = (T) array;
                return temp;
            }
        };
    }

    private static <T> TypeParser<List<T>> forLists() {
        return new TypeParser<List<T>>() {

            public List<T> parse(String input, TypeParserHelper helper) {
                Class<T> targetType = getParameterizedTypeArgument(helper);
                List<T> list = new ArrayList<T>();
                for(String value : helper.split(input)){
                    list.add(helper.parse(value, targetType));
                }
                return list;
            }
        };
    }

    private static <T> TypeParser<Set<T>> forSets() {
        return new TypeParser<Set<T>>() {
            public Set<T> parse(String input, TypeParserHelper helper) {
                Class<T> targetType = getParameterizedTypeArgument(helper);
                Set<T> set = new LinkedHashSet<T>();
                for(String value : helper.split(input)){
                    set.add(helper.parse(value, targetType));
                }
                return set;
            }
        };
    }

    private static <K,V> TypeParser<Map<K, V>> forMaps() {
        return new TypeParser<Map<K, V>>() {
            private static final int KEY = 0;
            private static final int VALUE = 1;
            public Map<K, V> parse(String input, TypeParserHelper helper) {
                Class<K> keyType = getParameterizedTypeArgument(helper, KEY);
                Class<V> valueType = getParameterizedTypeArgument(helper, VALUE);
                Map<K, V> map = new LinkedHashMap<K, V>();
                for(String entryString : helper.split(input)){
                    List<String> entry = helper.splitKeyValue(entryString);
                    map.put(helper.parse(entry.get(KEY), keyType), helper.parse(entry.get(VALUE), valueType));
                }
                return map;
            }
        };

    }

    private static <T> Class<T> getParameterizedTypeArgument(TypeParserHelper helper) {
        return getParameterizedTypeArgument(helper, 0);
    }
    private static <T> Class<T> getParameterizedTypeArgument(TypeParserHelper helper, int index) {
        Class<?> type = TypeParserUtility.getParameterizedTypeArguments(helper.getTargetType()).get(index);
        @SuppressWarnings("unchecked")
        Class<T> cls = (Class<T>) type;
        return cls;
    }

}
