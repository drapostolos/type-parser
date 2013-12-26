package com.github.drapostolos;

public interface TypeParser<T> {
    static final String X = "";
	
	T parse(String value);

}
