package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.Util.decorateParser;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

enum Parsers implements Parser<Object> {
    BYTE(Byte.class, byte.class) {

        @Override
        public Byte parse(String input, ParserHelper helper) {
            return Byte.valueOf(input.trim());
        }
    },
    INTEGER(Integer.class, int.class) {

        @Override
        public Integer parse(String input, ParserHelper helper) {
            return Integer.valueOf(input.trim());
        }
    },
    LONG(Long.class, long.class) {

        @Override
        public Long parse(String input, ParserHelper helper) {
            return Long.valueOf(input.trim());
        }
    },
    SHORT(Short.class, short.class) {

        @Override
        public Short parse(String input, ParserHelper helper) {
            return Short.valueOf(input.trim());
        }
    },
    FLOAT(Float.class, float.class) {

        @Override
        public Float parse(String input, ParserHelper helper) {
            return Float.valueOf(input);
        }
    },
    DOUBLE(Double.class, double.class) {

        @Override
        public Double parse(String input, ParserHelper helper) {
            return Double.valueOf(input);
        }
    },
    BOOLEAN(Boolean.class, boolean.class) {

        @Override
        public Boolean parse(final String input, ParserHelper helper) {
            String value = input.trim().toLowerCase();
            if ("true".equals(value)) {
                return Boolean.TRUE;
            } else if ("false".equals(value)) {
                return Boolean.FALSE;
            }
            String message = "\"%s\" is not parsable to a Boolean.";
            throw new IllegalArgumentException(String.format(message, input));
        }
    },
    CHARCTER(Character.class, char.class) {

        @Override
        public Character parse(String input, ParserHelper helper) {
            if (input.length() == 1) {
                return Character.valueOf(input.charAt(0));
            }
            String message = "\"%s\" must only contain a single character.";
            throw new IllegalArgumentException(String.format(message, input));
        }
    },
    BIG_INTEGER(BigInteger.class) {

        @Override
        public BigInteger parse(String input, ParserHelper helper) {
            return new BigInteger(input.trim());
        }
    },
    BIG_DECIMAL(BigDecimal.class) {

        @Override
        public BigDecimal parse(String input, ParserHelper helper) {
            try {
                return new BigDecimal(input.trim());
            } catch (NumberFormatException e) {
                /*
                 * The NumberFormatException thrown by BigDecimal contains
                 * an empty error message. The below is done to address that.
                 */
                String message = "NumberFormatException For input string: \"" + input + "\"";
                NumberFormatException e2 = new NumberFormatException(message);
                e2.setStackTrace(e.getStackTrace());
                throw e2;
            }
        }
    },
    URL(URL.class) {

        @Override
        public URL parse(String input, ParserHelper helper) {
            try {
                return new URL(input.trim());
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("MalformedURLException: " + e.getMessage(), e);
            }
        }
    },
    URI(URI.class) {

        @Override
        public URI parse(String input, ParserHelper helper) {
            try {
                return new URI(input.trim());
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }
    },
    FILE(File.class) {

        @Override
        public File parse(String input, ParserHelper helper) {
            return new File(input.trim());
        }
    },
    STRING(String.class) {

        @Override
        public String parse(String input, ParserHelper helper) {
            return input;
        }
    },
    OBJECT(Object.class) {

        @Override
        public Object parse(String input, ParserHelper helper) {
            return input;
        }
    },
    NUMBER(Number.class) {

        private final ThreadLocal<NumberFormat> NUMBER_FORMAT = new ThreadLocal<NumberFormat>() {

            @Override
            protected NumberFormat initialValue() {
                return NumberFormat.getInstance(Locale.US);
            }
        };

        @Override
        public Number parse(String input, ParserHelper helper) {
            try {
                return NUMBER_FORMAT.get().parse(input.trim());
            } catch (ParseException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }
    },
    PATH(Path.class) {

        @Override
        public Path parse(String input, ParserHelper helper) {
            return Paths.get(input.trim());
        }
    },
;

    private static final Map<Type, Parser<?>> DEFAULT_PARSERS;
    private Type[] types;

    private Parsers(Type... types) {
        this.types = types;
    }

    static {
        DEFAULT_PARSERS = new LinkedHashMap<Type, Parser<?>>();
        for (Parsers parser : values()) {
            for (Type type : parser.types) {
                DEFAULT_PARSERS.put(type, decorateParser(type, parser));
            }
        }
    }

    static Map<Type, Parser<?>> copyDefault() {
        return new LinkedHashMap<Type, Parser<?>>(DEFAULT_PARSERS);
    }

}
