package com.github.drapostolos.typeparser;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class Parsers {

    static final Type ANY_ARRAY = Array.class;
    static final Type ANY_CLASS_WITH_STATIC_VALUEOF_METHOD = StaticFactoryMethodParser.class;
    private static final Type ARRAY_OF_CLASS = new GenericType<Class<?>[]>() {}.getType();
    private static final String BOOLEAN_ERROR_MESSAGE = "\"%s\" is not parsable to a Boolean.";
    private static final String CLASS_ERROR_MESSAGE = "\"%s\" is not parsable to a Class object.";
    private static final String CHARACTER_ERROR_MESSAGE = "\"%s\" must only contain a single character.";
    private static final Map<Type, Parser<?>> DEFAULT_PARSERS;
    private static final Map<Class<?>, Parser<?>> DEFAULT_ASSIGNABLE_PARSERS;
    private static final Parser<?> CLASS_PARSER = forClass();
    final Map<Type, Parser<?>> parsers;
    final Map<Class<?>, Parser<?>> assignableParsers;

    private Parsers(
            Map<Type, Parser<?>> parsers,
            Map<Class<?>, Parser<?>> assignableParsers) {
        this.parsers = parsers;
        this.assignableParsers = assignableParsers;
    }

    static Parsers copyDefault() {
        return new Parsers(
                copyMap(DEFAULT_PARSERS),
                copyMap(DEFAULT_ASSIGNABLE_PARSERS));
    }

    static Parsers unmodifiableCopy(Parsers tp) {
        return new Parsers(
                unmodifiableCopy(tp.parsers),
                unmodifiableReverseCopy(tp.assignableParsers));
    }

    private static <K, V> Map<K, V> unmodifiableCopy(Map<K, V> map) {
        return Collections.unmodifiableMap(copyMap(map));
    }

    private static <K, V> Map<K, V> copyMap(Map<K, V> map) {
        return new LinkedHashMap<K, V>(map);
    }

    private static <K, V> Map<K, V> unmodifiableReverseCopy(Map<K, V> map) {
        List<K> keys = new ArrayList<K>(map.keySet());
        Collections.reverse(keys);
        LinkedHashMap<K, V> m2 = new LinkedHashMap<K, V>();
        for (K key : keys) {
            m2.put(key, map.get(key));
        }
        Collections.unmodifiableMap(m2);
        return m2;
    }

    static {
        DEFAULT_ASSIGNABLE_PARSERS = newLinkedHashMap();
        DEFAULT_PARSERS = newLinkedHashMap();
    }

    private static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }

    static {
        DEFAULT_ASSIGNABLE_PARSERS.put(LinkedHashMap.class, forLinkedHashMaps());
        DEFAULT_ASSIGNABLE_PARSERS.put(ArrayList.class, forArrayLists());
        DEFAULT_ASSIGNABLE_PARSERS.put(LinkedHashSet.class, forLinkedHashSets());
        DEFAULT_ASSIGNABLE_PARSERS.put(Class.class, CLASS_PARSER);
    }

    static {
        registerParser(ANY_ARRAY, Parsers.forArrays());
        registerParser(ANY_CLASS_WITH_STATIC_VALUEOF_METHOD, new StaticFactoryMethodParser());
        registerParser(types(Class.class, new GenericType<Class<?>>() {}.getType()), CLASS_PARSER);
        registerParser(types(Boolean.class, boolean.class), new Parser<Boolean>() {

            @Override
            public Boolean parse(final String value0, ParserHelper helper) {
                String value = value0.trim().toLowerCase();
                if (value.equals("true")) {
                    return Boolean.TRUE;
                } else if (value.equals("false")) {
                    return Boolean.FALSE;
                }
                throw new IllegalArgumentException(String.format(BOOLEAN_ERROR_MESSAGE, value0));
            }
        });
        registerParser(types(Character.class, char.class), new Parser<Character>() {

            @Override
            public Character parse(String value, ParserHelper helper) {
                if (value.length() == 1) {
                    return Character.valueOf(value.charAt(0));
                }
                throw new IllegalArgumentException(String.format(CHARACTER_ERROR_MESSAGE, value));
            }
        });
        registerParser(BigInteger.class, new Parser<BigInteger>() {

            @Override
            public BigInteger parse(String value, ParserHelper helper) {
                try {
                    return new BigInteger(value.trim());
                } catch (NumberFormatException e) {
                    String message = String.format("For input string: \"%s\"", value.trim());
                    throw new NumberFormatException(message);
                }
            }
        });
        registerParser(BigDecimal.class, new Parser<BigDecimal>() {

            @Override
            public BigDecimal parse(String value, ParserHelper helper) {
                try {
                    return new BigDecimal(value.trim());
                } catch (NumberFormatException e) {
                    String message = String.format("For input string: \"%s\"", value.trim());
                    throw new NumberFormatException(message);
                }
            }
        });
        registerParser(URL.class, new Parser<URL>() {

            @Override
            public URL parse(String value, ParserHelper helper) {
                try {
                    return new URL(value.trim());
                } catch (MalformedURLException e) {
                    String message = "Can not parse input string: \"%s\" "
                            + "to an URL due to underlying exception.";
                    throw new RuntimeException(String.format(message, value.trim()));
                }
            }
        });
        registerParser(URI.class, new Parser<URI>() {

            @Override
            public URI parse(String value, ParserHelper helper) {
                try {
                    return new URI(value.trim());
                } catch (URISyntaxException e) {
                    String message = "Can not parse input string: \"%s\" "
                            + "to an URI due to underlying exception.";
                    throw new RuntimeException(String.format(message, value.trim()));
                }
            }
        });
        registerParser(types(byte.class, Byte.class), new Parser<Byte>() {

            @Override
            public Byte parse(String value, ParserHelper helper) {
                return Byte.valueOf(value.trim());
            }
        });
        registerParser(types(Integer.class, int.class), new Parser<Integer>() {

            @Override
            public Integer parse(String value, ParserHelper helper) {
                return Integer.valueOf(value.trim());
            }
        });
        registerParser(types(long.class, Long.class), new Parser<Long>() {

            @Override
            public Long parse(String value, ParserHelper helper) {
                return Long.valueOf(value.trim());
            }
        });
        registerParser(types(Short.class, short.class), new Parser<Short>() {

            @Override
            public Short parse(String value, ParserHelper helper) {
                return Short.valueOf(value.trim());
            }
        });
        registerParser(types(float.class, Float.class), new Parser<Float>() {

            @Override
            public Float parse(String value, ParserHelper helper) {
                return Float.valueOf(value);
            }
        });
        registerParser(types(double.class, Double.class), new Parser<Double>() {

            @Override
            public Double parse(String value, ParserHelper helper) {
                return Double.valueOf(value);
            }
        });
        registerParser(File.class, new Parser<File>() {

            @Override
            public File parse(String value, ParserHelper helper) {
                return new File(value.trim());
            }
        });
        registerParser(String.class, new Parser<String>() {

            @Override
            public String parse(String value, ParserHelper helper) {
                return value;
            }
        });
        registerParser(ARRAY_OF_CLASS, new Parser<Class<?>[]>() {

            @Override
            public Class<?>[] parse(String input, ParserHelper helper) {
                List<String> strList = helper.split(input);
                Class<?>[] array = new Class<?>[strList.size()];
                for (int i = 0; i < strList.size(); i++) {
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

    private static void registerParser(Type type, Parser<?> typeParser) {
        registerParser(types(type), typeParser);
    }

    private static void registerParser(List<Type> types, Parser<?> typeParser) {
        for (Type type : types) {
            DEFAULT_PARSERS.put(type, typeParser);
        }
    }

    private static Parser<Class<?>> forClass() {
        return new Parser<Class<?>>() {

            @Override
            public Class<?> parse(String input, ParserHelper helper) {
                try {
                    return Class.forName(input.trim());
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(String.format(CLASS_ERROR_MESSAGE, input));
                }
            }
        };
    }

    private static <T> Parser<T> forArrays() {
        return new Parser<T>() {

            @Override
            public T parse(String input, ParserHelper helper) {
                List<String> strList = helper.split(input);
                Class<?> componentType = TypeParserUtility.getComponentClass(helper.getTargetType());
                Object array = Array.newInstance(componentType, strList.size());
                for (int i = 0; i < strList.size(); i++) {
                    Object element = helper.parse(strList.get(i), componentType);
                    Array.set(array, i, element);
                }
                @SuppressWarnings("unchecked")
                T temp = (T) array;
                return temp;
            }
        };
    }

    private static <T> Parser<ArrayList<T>> forArrayLists() {
        return new Parser<ArrayList<T>>() {

            public ArrayList<T> parse(String input, ParserHelper helper) {
                @SuppressWarnings("unchecked")
                Class<T> targetType = (Class<T>) helper.getParameterizedTypeArguments().get(0);
                ArrayList<T> list = new ArrayList<T>();
                for (String value : helper.split(input)) {
                    list.add(helper.parse(value, targetType));
                }
                return list;
            }
        };
    }

    private static <T> Parser<Set<T>> forLinkedHashSets() {
        return new Parser<Set<T>>() {

            public Set<T> parse(String input, ParserHelper helper) {
                @SuppressWarnings("unchecked")
                Class<T> targetType = (Class<T>) helper.getParameterizedTypeArguments().get(0);
                Set<T> set = new LinkedHashSet<T>();
                for (String value : helper.split(input)) {
                    set.add(helper.parse(value, targetType));
                }
                return set;
            }
        };
    }

    private static <K, V> Parser<LinkedHashMap<K, V>> forLinkedHashMaps() {
        return new Parser<LinkedHashMap<K, V>>() {

            private static final int KEY = 0;
            private static final int VALUE = 1;

            public LinkedHashMap<K, V> parse(String input, ParserHelper helper) {
                @SuppressWarnings("unchecked")
                Class<K> keyType = (Class<K>) helper.getParameterizedTypeArguments().get(KEY);
                @SuppressWarnings("unchecked")
                Class<V> valueType = (Class<V>) helper.getParameterizedTypeArguments().get(VALUE);
                LinkedHashMap<K, V> map = newLinkedHashMap();
                for (String entryString : helper.split(input)) {
                    List<String> entry = helper.splitKeyValue(entryString);
                    map.put(helper.parse(entry.get(KEY), keyType), helper.parse(entry.get(VALUE), valueType));
                }
                return map;
            }
        };

    }
}
