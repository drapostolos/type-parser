package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * Class holding the various Parsers used by this instance of TypeParser.
 */
class Parsers {

    private final Map<Type, Parser<?>> staticParsers;
    private final List<DynamicParser> dynamicParsers;
    private final DynamicParser[] dynamicParserArray;

    /*
     * Return a copy of the default parsers, which allows modifications
     * (like adding/removing parsers).
     */
    static Parsers copyDefault() {
        return new Parsers(
                DefaultStaticParsers.copy(),
                new ArrayList<DynamicParser>());
    }

    /*
     * Once no more modifications are allowed to the given Parsers instance,
     * call this method to retrieve an unmodifiable copy.
     */
    static Parsers unmodifiableCopy(Parsers parsers) {
        return new Parsers(
                Collections.unmodifiableMap(copyMap(parsers.staticParsers)),
                Collections.unmodifiableList(copyList(parsers.dynamicParsers)));
    }

    private static <K, V> Map<K, V> copyMap(Map<K, V> map) {
        return new LinkedHashMap<K, V>(map);
    }

    private static <T> List<T> copyList(List<T> list) {
        return new ArrayList<T>(list);
    }

    private Parsers(Map<Type, Parser<?>> staticParsers, List<DynamicParser> dynamicParsers) {
        this.staticParsers = staticParsers;
        this.dynamicParsers = dynamicParsers;
        /*
         * This is used for optimization
         */
        this.dynamicParserArray = dynamicParsers.toArray(new DynamicParser[0]);
    }

    boolean containsStaticParser(Type targetType) {
        return staticParsers.containsKey(targetType);
    }

    Parser<?> getStaticParser(Type targetType) {
        return staticParsers.get(targetType);
    }

    void removeStaticParser(Type type) {
        staticParsers.remove(type);
    }

    void addStaticParser(Type type, Parser<?> parser) {
        staticParsers.put(type, parser);
    }

    void addDynamicParser(DynamicParser parser) {
        dynamicParsers.add(parser);
    }

    DynamicParser[] dynamicParsers() {
        return dynamicParserArray;
    }
}
