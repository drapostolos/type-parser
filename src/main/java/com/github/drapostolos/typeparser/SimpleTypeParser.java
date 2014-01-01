package com.github.drapostolos.typeparser;

/**
 * Interface for implementations that parses a given string and then converts it to
 * a specific type. Any implementation of this interface is expected to be immutable.
 * 
 * TODO NO Parameterized T
 *
 * @param <T> the type to convert the parsed string to. 
 */
public interface SimpleTypeParser<T> extends TypeParser{

    /**
     * Parses the given string and converts it to an instance of type T.
     * 
     * @param value input string to parse
     * 
     * @return an instance of type T.
     */
    T parse(String input);
}
