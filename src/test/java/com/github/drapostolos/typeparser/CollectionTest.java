package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.junit.Test;

public class CollectionTest extends TestBase {

    @Test
    public void canParseToNavigableSet() throws Exception {
        assertThat(parser.parse(DUMMY_STRING, new GenericType<NavigableSet<String>>() {}))
                .containsExactly(DUMMY_STRING)
                .isInstanceOf(NavigableSet.class)
                .hasSameClassAs(new TreeSet<String>());
    }

    @Test
    public void canParseToLinkedList() throws Exception {
        assertThat(parser.parse(DUMMY_STRING, new GenericType<LinkedList<String>>() {}))
                .containsExactly(DUMMY_STRING)
                .isInstanceOf(LinkedList.class)
                .hasSameClassAs(new LinkedList<String>());
    }

    @Test
    public void shouldThrowWhenCollectionImplementationHasNoDefaultconstructor() throws Exception {
        shouldThrowParseException()
                .withErrorMessage("Cannot instantiate collection of type '")
                .withErrorMessage(MyCollection.class.getName())
                .whenParsing(DUMMY_STRING)
                .to(new GenericType<MyCollection<String>>() {});
    }

}
