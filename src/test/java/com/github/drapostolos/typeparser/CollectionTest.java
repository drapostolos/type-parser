package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.junit.Test;

public class CollectionTest extends TestBase {

    private static final List<GenericType<? extends Collection<String>>> COLLECTION_CLASSES;
    static {
        List<GenericType<? extends Collection<String>>> list =
                new ArrayList<GenericType<? extends Collection<String>>>();
        list.add(new GenericType<ConcurrentLinkedQueue<String>>() {});
        list.add(new GenericType<ConcurrentSkipListSet<String>>() {});
        list.add(new GenericType<CopyOnWriteArrayList<String>>() {});
        list.add(new GenericType<CopyOnWriteArraySet<String>>() {});
        list.add(new GenericType<LinkedBlockingDeque<String>>() {});
        list.add(new GenericType<LinkedBlockingQueue<String>>() {});
        list.add(new GenericType<PriorityBlockingQueue<String>>() {});
        list.add(new GenericType<ArrayDeque<String>>() {});
        list.add(new GenericType<ArrayList<String>>() {});
        list.add(new GenericType<HashSet<String>>() {});
        list.add(new GenericType<LinkedHashSet<String>>() {});
        list.add(new GenericType<LinkedList<String>>() {});
        list.add(new GenericType<PriorityQueue<String>>() {});
        list.add(new GenericType<Stack<String>>() {});
        list.add(new GenericType<TreeSet<String>>() {});
        list.add(new GenericType<Vector<String>>() {});
        COLLECTION_CLASSES = Collections.unmodifiableList(list);
    }

    private static final List<GenericType<? extends Collection<String>>> COLLECTION_INTERFACES;
    static {
        List<GenericType<? extends Collection<String>>> list =
                new ArrayList<GenericType<? extends Collection<String>>>();
        list.add(new GenericType<BlockingQueue<String>>() {});
        list.add(new GenericType<BlockingDeque<String>>() {});
        list.add(new GenericType<Deque<String>>() {});
        list.add(new GenericType<List<String>>() {});
        list.add(new GenericType<NavigableSet<String>>() {});
        list.add(new GenericType<Queue<String>>() {});
        list.add(new GenericType<Set<String>>() {});
        list.add(new GenericType<SortedSet<String>>() {});
        COLLECTION_INTERFACES = Collections.unmodifiableList(list);
    }

    @Test
    public void canParseToCollectionClasses() throws Exception {
        for (GenericType<? extends Collection<String>> type : COLLECTION_CLASSES) {
            Class<?> rawType = toRawType(type);
            Collection<String> collection = parser.parse(DUMMY_STRING, type);
            assertThat(collection)
                    .containsExactly(DUMMY_STRING)
                    .hasSize(1)
                    .isInstanceOf(rawType);
            assertThat(collection.getClass()).isEqualTo(rawType);
        }
    }

    @Test
    public void canParseToEmptyCollectionClasses() throws Exception {
        for (GenericType<? extends Collection<String>> type : COLLECTION_CLASSES) {
            Class<?> rawType = toRawType(type);
            Collection<String> collection = parser.parse("null", type);
            assertThat(collection)
                    .isInstanceOf(rawType)
                    .isEmpty();
            assertThat(collection.getClass()).isEqualTo(rawType);
        }
    }

    @Test
    public void canParseToCollectionInterfaces() throws Exception {
        for (GenericType<? extends Collection<String>> type : COLLECTION_INTERFACES) {
            Class<?> rawType = toRawType(type);
            Collection<String> collection = parser.parse("null", type);
            assertThat(collection)
                    .isInstanceOf(rawType)
                    .isEmpty();
        }
    }

    @Test
    public void canParseToEmptyCollectionInterfaces() throws Exception {
        for (GenericType<? extends Collection<String>> type : COLLECTION_INTERFACES) {
            Class<?> rawType = toRawType(type);
            assertThat(parser.parse(DUMMY_STRING, type))
                    .containsExactly(DUMMY_STRING)
                    .hasSize(1)
                    .isInstanceOf(rawType);
        }
    }

    @Test
    public void MyCollectionWithoutDefaultConstructor() throws Exception {
        shouldThrowTypeParserException()
                .containingErrorMessage("Cannot instantiate collection of type '")
                .containingErrorMessage(MyCollectionWithoutDefaultConstructor.class.getName())
                .whenParsing(DUMMY_STRING)
                .to(new GenericType<MyCollectionWithoutDefaultConstructor<String>>() {});
    }

    enum MyEnum {
        AAA, BBB, CCC, DDD
    }

    @Test
    public void canParseToEnumSet() throws Exception {
        assertThat(parser.parse("AAA, CCC", new GenericType<EnumSet<MyEnum>>() {}))
                .containsExactly(MyEnum.AAA, MyEnum.CCC)
                .isInstanceOf(EnumSet.class);
    }

    @Test
    public void canParseToEmptyEnumSet() throws Exception {
        assertThat(parser.parse("null", new GenericType<EnumSet<MyEnum>>() {}))
                .isInstanceOf(EnumSet.class)
                .isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void canParseToRawCollection() throws Exception {
        assertThat(parser.parse("AAA, CCC", Collection.class))
                .containsExactly("AAA", " CCC");
    }

}
