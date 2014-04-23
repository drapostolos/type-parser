package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.TypeParserUtility.makeNullArgumentErrorMsg;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Builder class for constructing and configuring instances of {@link TypeParser}.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide">User-Guide</a>
 */
public final class TypeParserBuilder {

    TypeParsers typeParsers;
    SplitStrategy splitStrategy = TypeParserUtility.defaultSplitStrategy();
    SplitStrategy keyValueSplitStrategy = TypeParserUtility.defaultKeyValueSplitStrategy();
    InputPreprocessor inputPreprocessor = TypeParserUtility.defaultInputPreprocessor();

    TypeParserBuilder() {
        // Initialize with the default typeParsers
        typeParsers = TypeParsers.copyDefault();
    }

    /**
     * Unregister the {@link StringToTypeParser} associated with the given {@code targetType}.
     * {@code targetType} will be ignored if not associated with any {@link StringToTypeParser}.
     * 
     * @param targetType The type associated with {@link StringToTypeParser} to unregister.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if given argument is null.
     */
    public TypeParserBuilder unregisterTypeParser(Class<?> targetType) {
        if (targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.normalTypeParsers.remove(targetType);
        return this;
    }

    /**
     * Unregister the {@link StringToTypeParser} associated with the given {@code targetType}.
     * {@code targetType} will be ignored if not associated with any {@link StringToTypeParser}.
     * 
     * @param targetType The type associated with {@link StringToTypeParser} to unregister.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if given argument is null.
     */
    public <T> TypeParserBuilder unregisterTypeParser(GenericType<T> targetType) {
        if (targetType == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.normalTypeParsers.remove(targetType.getType());
        return this;
    }

    /**
     * Unregister the {@link StringToTypeParser} associated with the given {@code targetType}.
     * {@code targetType} will be ignored if not associated with any {@link StringToTypeParser}.
     * 
     * @param targetType The type associated with {@link StringToTypeParser} to unregister.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if given argument is null.
     */
    public TypeParserBuilder unregisterTypeParserForTypesAssignableTo(Class<?> targetType) {
        if (targetType == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.assignableTypeParsers.remove(targetType);
        return this;
    }

    /**
     * Register a custom made {@link StringToTypeParser} implementation, associated with
     * the given {@code targetType}.
     * 
     * @param targetType associated with given {@code typeParser}.
     * @param typeParser custom made {@link StringToTypeParser} implementation.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if any given argument is null.
     */
    public <T> TypeParserBuilder registerTypeParser(Class<T> targetType, StringToTypeParser<T> typeParser) {
        if (typeParser == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("typeParser"));
        }
        if (targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.normalTypeParsers.put(targetType, typeParser);
        return this;
    }

    /**
     * Register a custom made {@link StringToTypeParser} implementation, associated with
     * any type assignable to the given {@code targetType}.
     * 
     * @param targetType associated with given {@code typeParser}.
     * @param typeParser custom made {@link StringToTypeParser} implementation.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if any given argument is null.
     */
    public TypeParserBuilder registerTypeParserForTypesAssignableTo(Class<?> targetType,
            StringToTypeParser<?> typeParser) {
        if (typeParser == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("typeParser"));
        }
        if (targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.assignableTypeParsers.put(targetType, typeParser);
        return this;
    }

    /**
     * Register a custom made {@link StringToTypeParser} implementation, associated with
     * the given generic {@code targetType}.
     * 
     * @param targetType generic type associated with given {@code typeParser}.
     * @param typeParser custom made {@link StringToTypeParser} implementation.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if any given argument is null.
     */
    public <T> TypeParserBuilder registerTypeParser(GenericType<T> targetType, StringToTypeParser<T> typeParser) {
        if (typeParser == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("typeParser"));
        }
        if (targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }
        typeParsers.normalTypeParsers.put(targetType.getType(), typeParser);
        return this;
    }

    /**
     * Set a custom made {@link SplitStrategy} implementation to be used by
     * the {@link TypeParser} (as built by this instance).
     * <p/>
     * The default behavior, when parsing a string to a generic type (example: {@link Collection} /
     * {@link List} / {@link Set} etc.), Array or {@link Map} type, is to split by comma (',').
     * <p/>
     * Use this method to register your own {@link SplitStrategy} implementation to override the
     * default behavior.
     * 
     * @param splitStrategy {@link SplitStrategy} implementation.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if any given argument is null.
     */
    public TypeParserBuilder setSplitStrategy(SplitStrategy splitStrategy) {
        if (splitStrategy == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("splitStrategy"));
        }
        this.splitStrategy = splitStrategy;
        return this;
    }

    /**
     * Set a custom made {@link SplitStrategy} implementation to separate the {@code key} and
     * {@code value} pair in a Map entry, to be used by
     * the {@link TypeParser} (as built by this instance).
     * <p/>
     * The default behavior, when parsing a string to a {@link Map} instance, is to split each map
     * entry by a "=" sign.
     * <p/>
     * For example this input string: <br/>
     * <code>"key1=valueA,key2=valueB"</code> will first be split using the {@link SplitStrategy}
     * strategy as set with {@link #setSplitStrategy(SplitStrategy)} to get a list of map entries
     * (key/value pairs). Then each map entry is split by "=" where first element is the {@code key}
     * and the second element is the {@code value}.
     * <p/>
     * Use this method to register your own key/value {@link SplitStrategy} implementation to
     * override the default behavior. I.e replace using the "=" sign with some other character.
     * 
     * @param splitStrategy {@link SplitStrategy} implementation.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if any given argument is null.
     */
    public TypeParserBuilder setKeyValueSplitStrategy(SplitStrategy splitStrategy) {
        if (splitStrategy == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("splitStrategy"));
        }
        this.keyValueSplitStrategy = splitStrategy;
        return this;
    }

    /**
     * Set a custom made {@link InputPreprocessor} implementation to be used by
     * the {@link TypeParser} (as built by this instance).
     * <p/>
     * The default behavior, when pre-processing an input String, is:
     * <ul>
     * <li>if input string equals the string "null" (case ignored and trimmed) then a {@code null}
     * object is returned by the {@link TypeParser}</li>
     * </ul>
     * Use this method to set your own {@link InputPreprocessor} implementation to override the
     * default behavior.
     * 
     * @param inputPreprocessor {@link InputPreprocessor} implementation.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if any given argument is null.
     */
    public TypeParserBuilder setInputPreprocessor(InputPreprocessor inputPreprocessor) {
        if (inputPreprocessor == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("inputPreprocessor"));
        }
        this.inputPreprocessor = inputPreprocessor;
        return this;
    }

    /**
     * Constructs a new instance of {@link TypeParser} as configured
     * with this {@link TypeParserBuilder}.
     * 
     * @return new instance of {@link TypeParser}.
     */
    public TypeParser build() {
        return new TypeParser(this);
    }
}
