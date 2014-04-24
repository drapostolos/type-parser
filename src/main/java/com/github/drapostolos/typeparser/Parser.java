package com.github.drapostolos.typeparser;

/**
 * Interface for implementations that parses a given string and then converts it to
 * a specific type. Any implementation of this interface is expected to be immutable.
 * 
 * @param <T> the type to convert the parsed string to.
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide">User-Guide</a>
 */
public interface Parser<T> {

    /**
     * Parses the given string and converts it to an instance of type T.
     * 
     * @param input input string to parse.
     * @param helper Helper class injected automatically by the {@link TypeParser}.
     * @return an instance of type T.
     */
    T parse(String input, ParserHelper helper);
}
