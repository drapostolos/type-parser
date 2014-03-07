package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.TypeParserUtility.makeNullArgumentErrorMsg;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Builder class for constructing and configuring instances of {@link StringToTypeParser}.
 *
 */
public final class StringToTypeParserBuilder {
    Map<Type, TypeParser<?>> typeParsers;
    Splitter splitter = TypeParserUtility.defaultSplitter();
    Splitter keyValuePairSplitter = TypeParserUtility.defaultKeyValuePairSplitter();
    InputPreprocessor inputPreprocessor = TypeParserUtility.defaultInputPreprocessor();

    StringToTypeParserBuilder() {
        // Initialize with the default typeParsers
        typeParsers = TypeParsers.copyDefault();
    }

    /**
     * Unregister the {@link TypeParser} associated with the given {@code targetType}.
     * {@code targetType} will be ignored if not associated with any {@link TypeParser}.
     * 
     * @param targetType The type associated with {@link TypeParser} to unregister.
     * @return {@link StringToTypeParserBuilder}
     * 
     * @throws NullPointerException if given argument is null.
     */
    public StringToTypeParserBuilder unregisterTypeParser(Class<?> targetType){
        if(targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.remove(targetType);
        return this;
    }

    /**
     * Unregister the {@link TypeParser} associated with the given {@code targetType}.
     * {@code targetType} will be ignored if not associated with ay {@link TypeParser}.
     * 
     * @param targetType The type associated with {@link TypeParser} to unregister.
     * @return {@link StringToTypeParserBuilder}
     * 
     * @throws NullPointerException if given argument is null.
     */
    public <T> StringToTypeParserBuilder unregisterTypeParser(GenericType<T> targetType){
        if(targetType == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.remove(targetType.getType());
        return this;
    }

    /**
     * Register a custom made {@link TypeParser} implementation, associated with 
     * the given {@code targetType}.
     * 
     * @param targetType associated with given {@code typeParser}.
     * @param typeParser custom made {@link TypeParser} implementation.
     * @return {@link StringToTypeParserBuilder}
     * 
     * @throws NullPointerException if any given argument is null.
     */
    public <T> StringToTypeParserBuilder registerTypeParser(Class<? super T> targetType, TypeParser<T> typeParser){
        if(typeParser == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("typeParser"));
        }
        if(targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.put(targetType, typeParser);
        return this;
    }
    
    /**
     * Register a custom made {@link TypeParser} implementation, associated with 
     * the given generic {@code targetType}.
     * 
     * @param targetType generic type associated with given {@code typeParser}.
     * @param typeParser custom made {@link TypeParser} implementation.
     * @return {@link StringToTypeParserBuilder}
     * 
     * @throws NullPointerException if any given argument is null.
     */
    public <T> StringToTypeParserBuilder registerTypeParser(GenericType<T> targetType, TypeParser<T> typeParser){
        if(typeParser == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("typeParser"));
        }
        if(targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.put(targetType.getType(), typeParser);
        return this;
    }
    
    /**
     * Set a custom made {@link Splitter} implementation to be used by 
     * the {@link StringToTypeParser} built by this instance.
     * <p/>
     * The default behavior, when parsing a string to a collection (e.g {@link List}, {@link Set} etc.), 
     * array or {@link Map} type, is to:
     * <ul>
     * <li>if {@code input.trim()} is empty (i.e. lenth is 0) then an empty {@code List<String>} is returned</li>
     * <li>split the {@code input} String by a comma ",".</li>
     * </ul>
     *   
     * <p/>
     * Use this method to register your own {@link Splitter} implementation to override
     * the default behavior.
     * 
     * @param splitter {@link Splitter} implementation.
     * @return {@link StringToTypeParserBuilder}
     * 
     * @throws NullPointerException if any given argument is null.
     */
    public StringToTypeParserBuilder setSplitter(Splitter splitter){
        if(splitter == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("splitter"));
        }
        this.splitter = splitter;
        return this;
    }
    
    /**
     * Set a custom made {@link Splitter} implementation to separate the 
     * {@code key} and {@code value} pair in a Map entry, as used by 
     * the {@link StringToTypeParser} built by this instance.
     * <p/>
     * The default behavior, when parsing a string to a {@link Map} instance, is to split
     * each map entry by a "=" sign. 
     * <p/>
     * For example this input string: <br/>
     * <code>"key1=valueA, key2=valueB"</code> will first be split using the {@link Splitter} strategy
     * as set with {@link #setSplitter(Splitter)} to get a list of map entries (key/value pairs). Then
     * each map entry is split by "=" where first element is the {@code key} and the second
     * element is the {@code value}.
     * <p/>
     * 
     * Use this method to register your own key/value pair {@link Splitter} implementation
     * to override the default behavior. I.e replace using the "=" sign with some other character.
     * 
     * @param splitter {@link Splitter} implementation.
     * @return {@link StringToTypeParserBuilder}
     * 
     * @throws NullPointerException if any given argument is null.
     */
    public StringToTypeParserBuilder setKeyValuePairSplitter(Splitter splitter){
        if(splitter == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("splitter"));
        }
        this.keyValuePairSplitter = splitter;
        return this;
    }
    
    /**
     * Set a custom made {@link InputPreprocessor} implementation to be used by 
     * the {@link StringToTypeParser} built by this instance.
     * <p/>
     * The default behavior, when pre-processing an input String, is:
     * <ul>
     * <li>throw {@link NullPointerException} if input String is a {@code null} object.</li>
     * <li>if input string equals the string "null" (case ignored and trimmed) then a {@code null}
     * object is returned by the {@link StringToTypeParser}</li>
     * </ul>
     * 
     * Use this method to set your own {@link InputPreprocessor} implementation
     * to override the default behavior.
     * 
     * @param inputPreprocessor {@link InputPreprocessor} implementation.
     * @return {@link StringToTypeParserBuilder}
     * 
     * @throws NullPointerException if any given argument is null.
     */
    public StringToTypeParserBuilder setInputPreprocessor(InputPreprocessor inputPreprocessor) {
        if(inputPreprocessor == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("inputPreprocessor"));
        }
        this.inputPreprocessor = inputPreprocessor;
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
