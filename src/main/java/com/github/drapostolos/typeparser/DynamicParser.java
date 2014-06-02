package com.github.drapostolos.typeparser;

/**
 * This interface defines a {@link Parser} that decides during
 * runtime if it can parse the {@code input} string to the {@code targetType}.
 * If a DynamicParser can not parse, then the {@link DynamicParser#parse(String, ParserHelper)}
 * method must return the {@link DynamicParser#TRY_NEXT} constant.
 * <p>
 * The {@link TypeParser} will internally call all registered {@link DynamicParser} until the first
 * one found that does NOT return a {@link DynamicParser#TRY_NEXT} object.
 * <p>
 * Any implementation of this interface is expected to be immutable.
 * <p>
 * {@link DynamicParser} will be called in the opposite order as registered with
 * {@link TypeParserBuilder#registerDynamicParser(DynamicParser)}, i.e. last one registered is
 * called first.
 * 
 * @see <a href="https://github.com/drapostolos/type-parser/wiki/User-Guide"
 *      target="_blank">User-Guide</a>
 */
public interface DynamicParser extends Parser<Object> {

    /**
     * The next available {@link DynamicParser} will be called if this constant is returned from
     * the {@link #parse(String, ParserHelper)} method.
     * <p>
     * This constant is used to distinguish if a value returned form
     * {@link #parse(String, ParserHelper)} is a "real" value (and thus propagated out to the
     * client) or if next {@link DynamicParser} should be called.
     */
    public static final Object TRY_NEXT = new Object();

    /**
     * Parses the given string and converts it to an instance of {@code targetType}.
     * If not possible to parser, then {@link DynamicParser#TRY_NEXT} constant must
     * be returned by implementation.
     * 
     * @param input string to parse. This may be {@code null} and implementations of this
     *        interface are expected to return a sensible value in such case. For example return
     *        empty Collections (Arrays, Maps etc.) instead of {@code null}. Other classes may
     *        return {@code null}.
     * @param helper Helper class injected automatically by the {@link TypeParser}.
     * @return an instance of the {@code targetType}, or {@link DynamicParser#TRY_NEXT} if not
     *         possible to parse.
     * @throws ParseException Any exception thrown within this method will be wrapped and thrown as
     *         a {@link ParseException}.
     */
    Object parse(String input, ParserHelper helper);
}
