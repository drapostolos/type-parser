package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class Parsers {

    private final Map<Type, Parser<?>> staticParsers;
    private final List<DynamicParser> dynamicParsers;

    static Parsers copyDefault() {
        return new Parsers(DefaultStaticParsers.copy(), DefaultDynamicParsers.copy());
    }

    static Parsers unmodifiableCopy(Parsers parsers) {
        return new Parsers(
                Collections.unmodifiableMap(copyMap((parsers.staticParsers))),
                Collections.unmodifiableList(copyLinkedList(parsers.dynamicParsers)));
    }

    private static <K, V> Map<K, V> copyMap(Map<K, V> map) {
        return new LinkedHashMap<K, V>(map);
    }

    private static <T> LinkedList<T> copyLinkedList(List<T> list) {
        return new LinkedList<T>(list);
    }

    private Parsers(Map<Type, Parser<?>> staticParsers, List<DynamicParser> dynamicParsers) {
        this.staticParsers = staticParsers;
        this.dynamicParsers = dynamicParsers;
    }

    void removeStaticParser(Type type) {
        staticParsers.remove(type);
    }

    void addStaticParser(Type type, Parser<?> parser) {
        staticParsers.put(type, parser);
    }

    void addDynamicParser(DynamicParser parser) {
        ((LinkedList<DynamicParser>) dynamicParsers).offerFirst(parser);
    }

    boolean containsStaticParser(Type targetType) {
        return staticParsers.containsKey(targetType);
    }

    Parser<?> getStaticParser(Type targetType) {
        return staticParsers.get(targetType);
    }

    List<DynamicParser> dynamicParsers() {
        return dynamicParsers;
    }
}
