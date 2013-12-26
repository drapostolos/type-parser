package com.github.drapostolos.typeparser;

public interface TypeParser<T> {
    static final String X = "";
	
	T parse(String value);

}
