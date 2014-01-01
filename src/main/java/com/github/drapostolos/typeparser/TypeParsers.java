package com.github.drapostolos.typeparser;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

final class TypeParsers {
    private TypeParsers() { throw new AssertionError("Not meant for instantiation"); }

    static ArrayTypeParser array2D(final String delimiter){
        return new ArrayTypeParser() {

            @Override
            public <T> Object parse(String input, Class<T> type, Helper helper) {
                String[] strArray = input.split(delimiter);
                List<T> list = new ArrayList<T>();
                for(String value : strArray){
                    list.add(helper.parse(value, type));
                }
                @SuppressWarnings("unchecked")
                T[] result = list.toArray((T[]) Array.newInstance(type, list.size()));
                return result;
            }
        };
    }
    
    static ParameterizedTypeParser<List<?>> list(final String delimiter) {
        return new ParameterizedTypeParser<List<?>>() {

            @SuppressWarnings("unchecked")
            public List<?> parse(String input, ParameterizedTypeHelper helper) {
                
                // TODO add check only one type argument exists (we don't support anything else at the moment)
                boolean todo;
                Type t = helper.getActualTypeArguments()[0];
                System.out.println(t.getClass());
                Class<?> targetType = (Class<?>) helper.getActualTypeArguments()[0];
                @SuppressWarnings("rawtypes")
                List list = new ArrayList();
                for(String value : input.split(delimiter)){
                    list.add(helper.parse(value, targetType));
                }
                return list;
            }
        };
    }
    
    static ParameterizedTypeParser<Set<?>> set(final String delimiter) {
        return new ParameterizedTypeParser<Set<?>>() {

            @SuppressWarnings("unchecked")
            public Set<?> parse(String input, ParameterizedTypeHelper helper) {
                
                // TODO add check only one type argument exists (we don't support anything else at the moment)
                boolean todo;
                Type targetType = helper.getActualTypeArguments()[0];
//                System.out.println(t.getClass());
//                Class<?> targetType = (Class<?>) helper.getActualTypeArguments()[0];
                @SuppressWarnings("rawtypes")
                Set set = new TreeSet();
                for(String value : input.split(delimiter)){
                    set.add(helper.parseType(value, targetType));
                }
                return set;
            }
        };
    }
    
    static ParameterizedTypeParser<Map<?, ?>> map(final String delimiter, final String keyValueDelimiter) {
        return new ParameterizedTypeParser<Map<?, ?>>() {

            @SuppressWarnings("unchecked")
            public Map<?, ?> parse(String input, ParameterizedTypeHelper helper) {
                
                // TODO add check only one type argument exists (we don't support anything else at the moment)
                boolean todo;
                Class<?> keyType = (Class<?>) helper.getActualTypeArguments()[0];
                Class<?> valueType = (Class<?>) helper.getActualTypeArguments()[1];
                @SuppressWarnings("rawtypes")
                Map map = new HashMap();
                for(String value : input.split(delimiter)){
                    String[] entry = value.split(keyValueDelimiter);
                    map.put(helper.parse(entry[0], keyType), helper.parse(entry[1], valueType));
                }
                return map;
            }
        };
    }
    

}
