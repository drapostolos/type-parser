package com.github.drapostolos.typeparser;

import java.util.HashMap;
import java.util.Map;

public final class StringToTypeParserBuilder {
    private static final Map<Class<?>, TypeParser<?>> defaultTypeParsers = new HashMap<Class<?>, TypeParser<?>>();
    private final static Map<Class<?>, Class<?>> wrapperToPrimitiveMapper = new HashMap<Class<?>, Class<?>>();

    private Map<Class<?>, TypeParser<?>> typeParsers;

    static {
        wrapperToPrimitiveMapper.put(Boolean.class, boolean.class);
        wrapperToPrimitiveMapper.put(Byte.class, byte.class);
        wrapperToPrimitiveMapper.put(Short.class, short.class);
        wrapperToPrimitiveMapper.put(Character.class, char.class);
        wrapperToPrimitiveMapper.put(Integer.class, int.class);
        wrapperToPrimitiveMapper.put(Long.class, long.class);
        wrapperToPrimitiveMapper.put(Float.class, float.class);
        wrapperToPrimitiveMapper.put(Double.class, double.class);

        for(TypeParser<?> typeParser : DefaultTypeParsers.list()){
            Class<?> type = Util.extractTypeParameter(typeParser);
            defaultTypeParsers.put(type, typeParser);
            if(wrapperToPrimitiveMapper.containsKey(type)){
                // add primitive version, example int.class, boolean.class etc.
                defaultTypeParsers.put(wrapperToPrimitiveMapper.get(type), typeParser);
            }
        }
    }

    StringToTypeParserBuilder() {
        // Initialize with the default typeParsers
        typeParsers = new HashMap<Class<?>, TypeParser<?>>(defaultTypeParsers);
    }

    public StringToTypeParserBuilder unregisterTypeParser(Class<?> type){
        if(type == null) {
            throw new NullPointerException(Util.nullArgumentErrorMsg("type"));
        }
        typeParsers.remove(type);
        return this;
    }
    
    public StringToTypeParserBuilder registerTypeParser(TypeParser<?> typeParser){
        if(typeParser == null) {
            throw new NullPointerException(Util.nullArgumentErrorMsg("typeParser"));
        }
        typeParsers.put(Util.extractTypeParameter(typeParser), typeParser);
        return this;
    }
    
    public StringToTypeParser build(){
        return new StringToTypeParser(new HashMap<Class<?>, TypeParser<?>>(typeParsers));
    }
}
