package com.github.drapostolos.typeparser;

/**
 * Interface for allowing clients to do their own preparation 
 * of the input string to parse. Any implementation of this 
 * interface is expected to be immutable.
 */
public interface InputPreprocessor {
    /**
     * prepares the input string to be parsed. 
     * <p/>
     * If a null object is returned from this method, the {@link StringToTypeParser}
     * will automatically return a null object to the client.
     * <p/>
     * Any exceptions thrown by this method will be caught in the {@link StringToTypeParser}
     * and re-thrown converted to a {@link IllegalArgumentException}.
     * 
     * @param input String to prepare for parsing.
     * @param helper Helper class injected automatically by the {@link StringToTypeParser}.
     * @return a prepared string to be parsed
     * 
     */
    String prepare(String input, InputPreprocessorHelper helper);
}
