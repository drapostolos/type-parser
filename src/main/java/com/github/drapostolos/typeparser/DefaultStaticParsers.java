package com.github.drapostolos.typeparser;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class DefaultStaticParsers {

    private static final Map<Type, Parser<?>> DEFAULT_STATIC_PARSERS;

    private DefaultStaticParsers() {
        throw new AssertionError("Not meant for instantiation");
    }

    static Map<Type, Parser<?>> copy() {
        return new LinkedHashMap<Type, Parser<?>>(DEFAULT_STATIC_PARSERS);
    }

    static {
        Map<Type, Parser<?>> map = newLinkedHashMap();
        addStaticParser(map, types(Byte.class, byte.class), new Parser<Byte>() {

            @Override
            public Byte parse(String input, ParserHelper helper) {
                return Byte.valueOf(input.trim());
            }
        });
        addStaticParser(map, types(Integer.class, int.class), new Parser<Integer>() {

            @Override
            public Integer parse(String input, ParserHelper helper) {
                return Integer.valueOf(input.trim());
            }
        });
        addStaticParser(map, types(Long.class, long.class), new Parser<Long>() {

            @Override
            public Long parse(String input, ParserHelper helper) {
                return Long.valueOf(input.trim());
            }
        });
        addStaticParser(map, types(Short.class, short.class), new Parser<Short>() {

            @Override
            public Short parse(String input, ParserHelper helper) {
                return Short.valueOf(input.trim());
            }
        });
        addStaticParser(map, types(Float.class, float.class), new Parser<Float>() {

            @Override
            public Float parse(String input, ParserHelper helper) {
                return Float.valueOf(input);
            }
        });

        addStaticParser(map, types(Double.class, double.class), new Parser<Double>() {

            @Override
            public Double parse(String input, ParserHelper helper) {
                return Double.valueOf(input);
            }
        });

        addStaticParser(map, types(Boolean.class, boolean.class),
                new Parser<Boolean>() {

                    @Override
                    public Boolean parse(final String input, ParserHelper helper) {
                        String value = input.trim().toLowerCase();
                        if (value.equals("true")) {
                            return Boolean.TRUE;
                        } else if (value.equals("false")) {
                            return Boolean.FALSE;
                        }
                        String message = "\"%s\" is not parsable to a Boolean.";
                        throw new IllegalArgumentException(String.format(message, input));
                    }
                });

        addStaticParser(map, types(Character.class, char.class),
                new Parser<Character>() {

                    @Override
                    public Character parse(String input, ParserHelper helper) {
                        if (input.length() == 1) {
                            return Character.valueOf(input.charAt(0));
                        }
                        String message = "\"%s\" must only contain a single character.";
                        throw new IllegalArgumentException(String.format(message, input));
                    }
                });
        addStaticParser(map, BigInteger.class, new Parser<BigInteger>() {

            @Override
            public BigInteger parse(String input, ParserHelper helper) {
                return new BigInteger(input.trim());
            }
        });
        addStaticParser(map, BigDecimal.class, new Parser<BigDecimal>() {

            @Override
            public BigDecimal parse(String input, ParserHelper helper) {
                return new BigDecimal(input.trim());
            }
        });
        addStaticParser(map, URL.class, new Parser<URL>() {

            @Override
            public URL parse(String input, ParserHelper helper) {
                try {
                    return new URL(input.trim());
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException(e.getMessage(), e);
                }
            }
        });
        addStaticParser(map, URI.class, new Parser<URI>() {

            @Override
            public URI parse(String input, ParserHelper helper) {
                try {
                    return new URI(input.trim());
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException(e.getMessage(), e);
                }
            }
        });
        addStaticParser(map, File.class, new Parser<File>() {

            @Override
            public File parse(String input, ParserHelper helper) {
                return new File(input.trim());
            }
        });
        addStaticParser(map, String.class, new Parser<String>() {

            @Override
            public String parse(String input, ParserHelper helper) {
                return input;
            }
        });
        DEFAULT_STATIC_PARSERS = Collections.unmodifiableMap(map);
    }

    private static List<Type> types(Type... types) {
        return Arrays.asList(types);
    }

    private static void addStaticParser(Map<Type, Parser<?>> map, List<Type> types, Parser<?> parser) {
        for (Type type : types) {
            addStaticParser(map, type, parser);
        }
    }

    private static void addStaticParser(Map<Type, Parser<?>> map, Type type, Parser<?> parser) {
        map.put(type, parser);
    }

    private static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }

}
