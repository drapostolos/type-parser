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

    Parsers parsers;
    SplitStrategy splitStrategy = TypeParserUtility.defaultSplitStrategy();
    SplitStrategy keyValueSplitStrategy = TypeParserUtility.defaultKeyValueSplitStrategy();
    InputPreprocessor inputPreprocessor = TypeParserUtility.defaultInputPreprocessor();

    TypeParserBuilder() {
        // Initialize with the default typeParsers
        parsers = Parsers.copyDefault();
    }

    /**
     * Unregister the {@link Parser} associated with the given {@code targetType}.
     * {@code targetType} will be ignored if not associated with any {@link Parser}.
     * 
     * @param targetType The type associated with {@link Parser} to unregister.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if given argument is null.
     */
    public TypeParserBuilder unregisterParser(Class<?> targetType) {
        if (targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }
        parsers.parsers.remove(targetType);
        return this;
    }

    /**
     * Unregister the {@link Parser} associated with the given {@code targetType}.
     * {@code targetType} will be ignored if not associated with any {@link Parser}.
     * 
     * @param targetType The type associated with {@link Parser} to unregister.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if given argument is null.
     */
    public <T> TypeParserBuilder unregisterParser(GenericType<T> targetType) {
        if (targetType == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("targetType"));
        }
        parsers.parsers.remove(targetType.getType());
        return this;
    }

    /**
     * Unregister the {@link Parser} associated with the given {@code targetType}.
     * {@code targetType} will be ignored if not associated with any {@link Parser}.
     * 
     * @param targetType The type associated with {@link Parser} to unregister.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if given argument is null.
     */
    public TypeParserBuilder unregisterParserForTypesAssignableTo(Class<?> targetType) {
        if (targetType == null) {
            throw new NullPointerException(TypeParserUtility.makeNullArgumentErrorMsg("targetType"));
        }
        parsers.assignableParsers.remove(targetType);
        return this;
    }

    /**
     * Register a custom made {@link Parser} implementation, associated with
     * the given {@code targetType}.
     * 
     * @param targetType associated with given {@code parser}.
     * @param parser custom made {@link Parser} implementation.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if any given argument is null.
     */
    public <T> TypeParserBuilder registerParser(Class<T> targetType, Parser<T> parser) {
        if (parser == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("parser"));
        }
        if (targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }
        parsers.parsers.put(targetType, parser);
        return this;
    }

    /**
     * Register a custom made {@link Parser} implementation, associated with
     * any type assignable to the given {@code targetType}.
     * 
     * @param targetType associated with given {@code parser}.
     * @param parser custom made {@link Parser} implementation.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if any given argument is null.
     */
    public TypeParserBuilder registerParserForTypesAssignableTo(Class<?> targetType,
            Parser<?> parser) {
        if (parser == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("parser"));
        }
        if (targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }
        parsers.assignableParsers.put(targetType, parser);
        return this;
    }

    /**
     * Register a custom made {@link Parser} implementation, associated with
     * the given generic {@code targetType}.
     * 
     * @param targetType generic type associated with given {@code parser}.
     * @param parser custom made {@link Parser} implementation.
     * @return {@link TypeParserBuilder}
     * @throws NullPointerException if any given argument is null.
     */
    public <T> TypeParserBuilder registerParser(GenericType<T> targetType, Parser<T> parser) {
        if (parser == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("parser"));
        }
        if (targetType == null) {
            throw new NullPointerException(makeNullArgumentErrorMsg("targetType"));
        }
        parsers.parsers.put(targetType.getType(), parser);
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
