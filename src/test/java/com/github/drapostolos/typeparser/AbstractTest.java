package com.github.drapostolos.typeparser;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

public abstract class AbstractTest {

    static final String DUMMY_STRING = "dummy-string";
    static final String NUMBER_FORMAT_ERROR_MSG = "Number format exception For input string: \"%s\".";
    TypeParser parser = TypeParser.newBuilder().build();

    @Rule
    public ExpectedException thrown = ExpectedException.none();
}
