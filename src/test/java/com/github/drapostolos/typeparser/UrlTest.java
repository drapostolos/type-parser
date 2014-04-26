package com.github.drapostolos.typeparser;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class UrlTest extends AbstractTypeTester<URL> {

    private static final String STRING = "http://host1.com/, http://host2.com/, http://host3.com/";

    @Override
    URL make(String str) throws Exception {
        return new URL(str.trim());
    }

    @Test
    public void shouldThrowWhenStringIsMalformedUrl() throws Exception {
        shouldThrowWhenParsing("strange-URL");
        toTypeWithErrorMessage(URL.class, "Can not parse input string:");
        toTypeWithErrorMessage(URL.class, "to an URI due to underlying exception.");
    }

    @Test
    public void canParseStringToUri() throws Exception {
        canParse("http://host.com/").toType(URL.class);
    }

    @Test
    public void canParseToGenericUriArray() throws Exception {
        canParse(STRING).toGenericArray(new GenericType<URL[]>() {});

    }

    @Test
    public void canParseToUriArray() throws Exception {
        canParse(STRING).toArray(URL[].class);
    }

    @Test
    public void canParseToUriList() throws Exception {
        canParse(STRING).toList(new GenericType<List<URL>>() {});
    }

    @Test
    public void canParseToUriSet() throws Exception {
        canParse("http://host1.com/, http://host2.com/, http://host1.com/");
        toSet(new GenericType<Set<URL>>() {});
    }

    @Test
    public void canParseToUriMap() throws Exception {
        canParse("http://host1.com/=http://host11.com/, http://host2.com/=http://host22.com/");
        toMap(new GenericType<Map<URL, URL>>() {});
    }

}
