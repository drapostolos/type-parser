package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
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

    @SuppressWarnings("unchecked")
    @Test
    public void canParseToCollectionConcreteTypes() throws Exception {
        canParseToConcreteCollectionTypes(
                new GenericType<ArrayDeque<String>>() {},
                new GenericType<ArrayList<String>>() {},
                new GenericType<ConcurrentLinkedQueue<String>>() {},
                new GenericType<ConcurrentSkipListSet<String>>() {},
                new GenericType<CopyOnWriteArrayList<String>>() {},
                new GenericType<CopyOnWriteArraySet<String>>() {},
                new GenericType<HashSet<String>>() {},
                new GenericType<LinkedBlockingDeque<String>>() {},
                new GenericType<LinkedBlockingQueue<String>>() {},
                new GenericType<LinkedHashSet<String>>() {},
                new GenericType<LinkedList<String>>() {},
                new GenericType<PriorityBlockingQueue<String>>() {},
                new GenericType<PriorityQueue<String>>() {},
                new GenericType<Stack<String>>() {},
                new GenericType<TreeSet<String>>() {},
                new GenericType<Vector<String>>() {});

    }

    private void canParseToConcreteCollectionTypes(GenericType<? extends Collection<String>>... types) {
        for (GenericType<? extends Collection<String>> type : types) {
            Class<?> rawType = toRawType(type);
            Collection<String> collection = parser.parse(DUMMY_STRING, type);
            assertThat(collection)
                    .containsExactly(DUMMY_STRING)
                    .hasSize(1)
                    .isInstanceOf(rawType);
            assertThat(collection.getClass()).isEqualTo(rawType);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void canParseToCollectionInterfaces() throws Exception {
        parseToCollectionInterfaces(
                new GenericType<BlockingQueue<String>>() {},
                new GenericType<BlockingDeque<String>>() {},
                new GenericType<Deque<String>>() {},
                new GenericType<List<String>>() {},
                new GenericType<NavigableSet<String>>() {},
                new GenericType<Queue<String>>() {},
                new GenericType<Set<String>>() {},
                new GenericType<SortedSet<String>>() {});

    }

    private void parseToCollectionInterfaces(GenericType<? extends Collection<String>>... types) {
        for (GenericType<? extends Collection<String>> type : types) {
            Class<?> rawType = toRawType(type);
            assertThat(parser.parse(DUMMY_STRING, type))
                    .containsExactly(DUMMY_STRING)
                    .hasSize(1)
                    .isInstanceOf(rawType);
        }
    }

    @Test
    public void shouldThrowWhenCollectionImplementationHasNoDefaultconstructor() throws Exception {
        shouldThrowTypeParserException()
                .containingErrorMessage("Cannot instantiate collection of type '")
                .containingErrorMessage(MyCollection.class.getName())
                .whenParsing(DUMMY_STRING)
                .to(new GenericType<MyCollection<String>>() {});
    }

    enum TestEnum {
        AAA, BBB, CCC, DDD
    }

    @Test
    public void canParseToEnumSet() throws Exception {
        assertThat(parser.parse("AAA, CCC", new GenericType<EnumSet<TestEnum>>() {}))
                .containsExactly(TestEnum.AAA, TestEnum.CCC)
                .isInstanceOf(EnumSet.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void canParseToRawCollection() throws Exception {
        assertThat(parser.parse("AAA, CCC", Collection.class))
                .containsExactly("AAA", " CCC");
    }

}
