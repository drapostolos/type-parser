package com.github.drapostolos.typeparser;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class UriTest extends AbstractTypeTester<URI> {

    private static final String STRING = "http://host1.com/, http://host2.com/, http://host3.com/";

    @Override
    URI make(String str) throws Exception {
        return new URI(str.trim());
    }

    @Test
    public void shouldThrowWhenStringIsIncorrectUri() throws Exception {
        shouldThrowWhenParsing("strange URI");
        toTypeWithErrorMessage(URI.class, "Can not parse input string:");
        toTypeWithErrorMessage(URI.class, "to an URI due to underlying exception.");
    }

    @Test
    public void canParseStringToUri() throws Exception {
        canParse("http://host.com/").toType(URI.class);
    }

    @Test
    public void canParseToGenericUriArray() throws Exception {
        canParse(STRING).toGenericArray(new GenericType<URI[]>() {});

    }

    @Test
    public void canParseToUriArray() throws Exception {
        canParse(STRING).toArray(URI[].class);
    }

    @Test
    public void canParseToUriList() throws Exception {
        canParse(STRING).toList(new GenericType<List<URI>>() {});
    }

    @Test
    public void canParseToUriSet() throws Exception {
        canParse("http://host1.com/, http://host2.com/, http://host1.com/");
        toSet(new GenericType<Set<URI>>() {});
    }

    @Test
    public void canParseToUriMap() throws Exception {
        canParse("http://host1.com/=http://host11.com/, http://host2.com/=http://host22.com/");
        toMap(new GenericType<Map<URI, URI>>() {});
    }

}
