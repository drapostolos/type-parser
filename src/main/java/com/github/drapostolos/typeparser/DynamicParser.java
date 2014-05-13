package com.github.drapostolos.typeparser;

interface DynamicParser<T> extends Parser<T> {

    boolean canParse(String input, ParserHelper helper);
}
