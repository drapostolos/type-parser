package com.github.drapostolos.typeparser;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder class for constructing instances of {@link StringToTypeParser}, 
 * where custom made {@link TypeParser} can be registered and default {@link TypeParser}
 * can be unregistered.
 *
 */
public final class StringToTypeParserBuilder {
    private final static Map<Class<?>, TypeParser<?>> defaultTypeParsers = new HashMap<Class<?>, TypeParser<?>>();
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

    /**
     * Unregister any of the default registered {@link TypeParser}'s.
     * 
     * @param type associated with {@link TypeParser} to unregister.
     * @return {@link StringToTypeParserBuilder}
     * 
     * @throws NullPointerException if given argument is null.
     */
    public StringToTypeParserBuilder unregisterTypeParser(Class<?> type){
        if(type == null) {
            throw new NullPointerException(Util.nullArgumentErrorMsg("type"));
        }
        typeParsers.remove(type);
        return this;
    }

    /**
     * Register a custom made {@link TypeParser} implementation. The type associated
     * with this {@code typeParser} will automatically be extracted from {@code typeParser}.
     * 
     * @param typeParser custom made {@link TypeParser} implementation.
     * @return {@link StringToTypeParserBuilder}
     * 
     * @throws NullPointerException if any given argument is null.
     */
    public StringToTypeParserBuilder registerTypeParser(TypeParser<?> typeParser){
        if(typeParser == null) {
            throw new NullPointerException(Util.nullArgumentErrorMsg("typeParser"));
        }
        typeParsers.put(Util.extractTypeParameter(typeParser), typeParser);
        return this;
    }
    
    /**
     * Register a custom made {@link TypeParser} implementation, associated with 
     * the given {@code type}.
     * 
     * @param type associated with given {@code typeParser}
     * @param typeParser custom made {@link TypeParser} implementation.
     * @return {@link StringToTypeParserBuilder}
     * 
     * @throws NullPointerException if any given argument is null.
     */
    public <T> StringToTypeParserBuilder registerTypeParser(Class<T> type, TypeParser<? extends T> typeParser){
        if(typeParser == null) {
            throw new NullPointerException(Util.nullArgumentErrorMsg("typeParser"));
        }
        if(type == null) {
            throw new NullPointerException(Util.nullArgumentErrorMsg("type"));
        }
        typeParsers.put(type, typeParser);
        return this;
    }
    
    /**
     * Constructs a new instance of {@link StringToTypeParser} as configured 
     * with this {@link StringToTypeParserBuilder}.
     * @return new instance of {@link StringToTypeParser}.
     */
    public StringToTypeParser build(){
        return new StringToTypeParser(new HashMap<Class<?>, TypeParser<?>>(typeParsers));
    }
}
