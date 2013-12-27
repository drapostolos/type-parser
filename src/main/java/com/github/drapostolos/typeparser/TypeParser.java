package com.github.drapostolos.typeparser;

/**
 * Interface for implementations that parses a given string and then converts it to
 * a specific type.
 *
 * @param <T> the type to convert the parsed string to. 
 */
public interface TypeParser<T> {

    /**
     * Parses the given string and converts it to an instance of type T.
     * 
     * @param value the String to parse
     * 
     * @return an instance of type T.
     */
    T parse(String value);

}
