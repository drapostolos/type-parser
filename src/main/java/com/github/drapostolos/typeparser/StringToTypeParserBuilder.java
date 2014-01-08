package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * TODO
 * Builder class for constructing instances of {@link StringToTypeParser}, 
 * where custom made {@link TypeParserBase} can be registered and default {@link TypeParserBase}
 * can be unregistered.
 *
 */
public final class StringToTypeParserBuilder {
    Map<Type, TypeParser<?>> typeParsers;
    Splitter splitter = TypeParserUtility.defaultSplitter();
    Splitter keyValuePairSplitter = TypeParserUtility.defaultKeyValuePairSplitter();

    StringToTypeParserBuilder() {
        // Initialize with the default typeParsers
        typeParsers = TypeParsers.copyDefault();
    }

    /**
     * Unregister any of the default registered {@link TypeParserBase}'s.
     * 
     * @param targetType associated with {@link TypeParserBase} to unregister.
     * @return {@link StringToTypeParserBuilder}
     * 
     * @throws NullPointerException if given argument is null.
     */
    public StringToTypeParserBuilder unregisterTypeParser(Class<?> targetType){
        if(targetType == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.remove(targetType);
        return this;
    }

    /**
     * TODO
     * @param targetType
     * @return
     */
    public <T> StringToTypeParserBuilder unregisterTypeParser(GenericType<T> targetType){
        if(targetType == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.remove(targetType.getType());
        return this;
    }

    /**
     * Register a custom made {@link TypeParserBase} implementation, associated with 
     * the given {@code type}.
     * 
     * @param targetType associated with given {@code typeParser}
     * @param typeParser custom made {@link TypeParserBase} implementation.
     * @return {@link StringToTypeParserBuilder}
     * 
     * @throws NullPointerException if any given argument is null.
     */
    public <T> StringToTypeParserBuilder registerTypeParser(Class<? super T> targetType, TypeParser<T> typeParser){
        if(typeParser == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("typeParser"));
        }
        if(targetType == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.put(targetType, typeParser);
        return this;
    }
    
    public <T> StringToTypeParserBuilder registerTypeParser(GenericType<T> targetType, TypeParser<T> typeParser){
        if(typeParser == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("typeParser"));
        }
        if(targetType == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.put(targetType.getType(), typeParser);
        return this;
    }
    
    public StringToTypeParserBuilder setSplitter(Splitter splitter){
        if(splitter == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("splitter"));
        }
        this.splitter = splitter;
        return this;
    }
    
    public StringToTypeParserBuilder setKeyValuePairSplitter(Splitter splitter){
        if(splitter == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("splitter"));
        }
        this.keyValuePairSplitter = splitter;
        return this;
    }
    
    /**
     * Constructs a new instance of {@link StringToTypeParser} as configured 
     * with this {@link StringToTypeParserBuilder}.
     * @return new instance of {@link StringToTypeParser}.
     */
    public StringToTypeParser build(){
        return new StringToTypeParser(this);
    }
}
