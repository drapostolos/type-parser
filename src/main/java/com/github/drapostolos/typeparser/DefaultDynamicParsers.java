package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.Util.STATIC_FACTORY_METHOD_NAME;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

enum DefaultDynamicParsers implements DynamicParser {

    CLASS {

        @Override
        public Object parse(String input, ParserHelper helper) {
            if (helper.isTargetTypeAssignableTo(Class.class)) {
                try {
                    return Class.forName(input.trim());
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("ClassNotFound: " + e.getMessage(), e);
                }
            }
            return TRY_NEXT;
        }
    },

    ARRAY {

        @Override
        public Object parse(String input, ParserHelper helper) {
            if (!helper.getRawTargetClass().isArray()) {
                return TRY_NEXT;
            }
            List<String> strList = helper.split(input);
            Class<?> componentType = helper.getComponentClass();
            Object result = Array.newInstance(componentType, strList.size());
            for (int i = 0; i < strList.size(); i++) {
                Object element = helper.parse(strList.get(i), componentType);
                Array.set(result, i, element);
            }
            return result;
        }
    },

    COLLECTION {

        @Override
        public Object parse(String input, ParserHelper helper) {
            if (!helper.isTargetTypeAssignableTo(Collection.class)) {
                return TRY_NEXT;
            }
            Class<?> elementType = helper.getParameterizedClassArgumentByIndex(0);
            Collection<Object> result = instantiateCollection(helper.getRawTargetClass());
            for (String value : helper.split(input)) {
                result.add(helper.parse(value, elementType));
            }
            return result;
        }

        private <T> Collection<T> instantiateCollection(Class<? extends T> rawType) {
            if (rawType.isInterface())
                return instantiateCollectionFromInterface(rawType);
            return instantiateCollectionFromClass(rawType);
        }

        @SuppressWarnings("unchecked")
        private <T> Collection<T> instantiateCollectionFromClass(Class<? extends T> rawType) {
            try {
                return (Collection<T>) rawType.newInstance();
            } catch (Exception e) {
                String message = "Cannot instantiate collection of type '%s'";
                throw new IllegalStateException(String.format(message, rawType), e);
            }
        }

        private <T> Collection<T> instantiateCollectionFromInterface(Class<? extends T> targetType) {
            if (List.class.isAssignableFrom(targetType))
                return new ArrayList<T>();
            else if (SortedSet.class.isAssignableFrom(targetType))
                return new TreeSet<T>();
            else if (Set.class.isAssignableFrom(targetType))
                return new LinkedHashSet<T>();
            return new ArrayList<T>();
        }

    },

    MAP {

        private static final int KEY = 0;
        private static final int VALUE = 1;

        @Override
        public Object parse(String input, ParserHelper helper) {
            if (!helper.isTargetTypeAssignableTo(Map.class)) {
                return TRY_NEXT;
            }
            Class<?> keyType = helper.getParameterizedClassArgumentByIndex(KEY);
            Class<?> valueType = helper.getParameterizedClassArgumentByIndex(VALUE);
            Map<Object, Object> map = instantiateMap(helper.getRawTargetClass());
            for (String entryString : helper.split(input)) {
                List<String> entry = helper.splitKeyValue(entryString);
                map.put(helper.parse(entry.get(KEY), keyType), helper.parse(entry.get(VALUE), valueType));
            }
            return map;
        }

        private <K, V> Map<K, V> instantiateMap(Class<?> rawType) {
            if (rawType.isInterface())
                return instantiateMapFromInterface(rawType);
            return instantiateMapFromClass(rawType);
        }

        private <K, V> Map<K, V> instantiateMapFromInterface(Class<?> targetType) {
            if (NavigableMap.class.isAssignableFrom(targetType))
                return new ConcurrentSkipListMap<K, V>();
            if (ConcurrentMap.class.isAssignableFrom(targetType))
                return new ConcurrentHashMap<K, V>();
            else if (SortedMap.class.isAssignableFrom(targetType))
                return new TreeMap<K, V>();
            return new LinkedHashMap<K, V>();
        }

        private <K, V> Map<K, V> instantiateMapFromClass(Class<?> rawType) {
            try {
                @SuppressWarnings("unchecked")
                Map<K, V> temp = (Map<K, V>) rawType.newInstance();
                return temp;
            } catch (Exception e) {
                String message = String.format("Cannot instantiate map of type '%s'", rawType);
                throw new IllegalArgumentException(message, e);
            }
        }

    },

    CLASS_DECLARING_STATIC_VALUE_OF_METHOD {

        private final Object STATIC_METHOD = null;

        @Override
        public Object parse(final String input, ParserHelper helper) {
            Class<?> targetType = helper.getRawTargetClass();

            boolean methodFound = false;
            Method method = null;
            Type argType = null;

            for (Method m : targetType.getDeclaredMethods()) {
                if (m.getName().equals(STATIC_FACTORY_METHOD_NAME)) {
                    if (m.getGenericParameterTypes().length == 1) {
                        if (Modifier.isStatic(m.getModifiers())) {
                            argType = m.getGenericParameterTypes()[0];
                            if (helper.containsStaticParser(argType)) {
                                method = m;
                                methodFound = true;
                                break;
                            }
                        }
                    }
                }
            }

            if (!methodFound) {
                return TRY_NEXT;
            }
            if (input == null) {
                return null;
            }
            Object argument;
            if (argType.equals(String.class)) {
                // No need to convert String to String
                if (targetType.isEnum()) {
                    argument = input.trim();
                } else {
                    argument = input;
                }
            } else {
                argument = helper.parseType(input, argType);
            }

            try {
                method.setAccessible(true);
                return method.invoke(STATIC_METHOD, argument);
            } catch (Exception e) {
                String message = "%s thrown when calling static factory method %s, "
                        + "with error message: %s";
                message = String.format(message,
                        e.getClass().getSimpleName(), method, e.getMessage());
                throw new UnsupportedOperationException(message, e);
            }
        }
    },

    CLASS_DECLARING_SINGLE_ARGUMENT_CONSTRUCTOR {

        @Override
        public Object parse(final String input, ParserHelper helper) {
            Class<?> targetType = helper.getRawTargetClass();

            boolean constructorFound = false;
            Constructor<?> constructor = null;
            Type argType = null;

            for (Constructor<?> c : targetType.getDeclaredConstructors()) {
                if (c.getGenericParameterTypes().length == 1) {
                    argType = c.getGenericParameterTypes()[0];
                    if (helper.containsStaticParser(argType) || argType.equals(Object.class)) {
                        constructor = c;
                        constructorFound = true;
                        break;
                    }
                }
            }

            if (!constructorFound) {
                return TRY_NEXT;
            }
            if (input == null) {
                return null;
            }
            Object argument;
            if (argType.equals(String.class) || argType.equals(Object.class)) {
                // No need to convert String to String
                argument = input;
            } else {
                argument = helper.parseType(input, argType);
            }

            constructor.setAccessible(true);
            try {
                return constructor.newInstance(argument);
            } catch (Exception e) {
                String message = "%s thrown when calling constructor %s, with error message: %s";
                message = String.format(message,
                        e.getClass().getSimpleName(), constructor, e.getMessage());
                throw new UnsupportedOperationException(message, e);
            }
        }
    };

    private static final List<DynamicParser> DEFAULT_DYNAMIC_PARSERS;

    static {
        List<DynamicParser> list = new ArrayList<DynamicParser>();
        for (DefaultDynamicParsers p : values()) {
            list.add(p);
        }
        DEFAULT_DYNAMIC_PARSERS = Collections.unmodifiableList(list);
    }

    static List<DynamicParser> copy() {
        return new LinkedList<DynamicParser>(DEFAULT_DYNAMIC_PARSERS);
    }
}
