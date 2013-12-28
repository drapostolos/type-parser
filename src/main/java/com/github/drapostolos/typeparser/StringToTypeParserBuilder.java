package com.github.drapostolos.typeparser;

import java.util.Map;

/**
 * Builder class for constructing instances of {@link StringToTypeParser}, 
 * where custom made {@link TypeParser} can be registered and default {@link TypeParser}
 * can be unregistered.
 *
 */
public final class StringToTypeParserBuilder {
    private Map<Class<?>, TypeParser<?>> typeParsers;

    StringToTypeParserBuilder() {
        // Initialize with the default typeParsers
        typeParsers = DefaultTypeParsers.copy();
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
            throw new NullPointerException(StringToTypeParser.nullArgumentErrorMsg("type"));
        }
        typeParsers.remove(type);
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
            throw new NullPointerException(StringToTypeParser.nullArgumentErrorMsg("typeParser"));
        }
        if(type == null) {
            throw new NullPointerException(StringToTypeParser.nullArgumentErrorMsg("type"));
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
        return new StringToTypeParser(typeParsers);
    }
}
