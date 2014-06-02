package com.github.drapostolos.typeparser;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class FileTest extends AbstractTypeTester<File> {

    @Override
    File make(String string) throws Exception {
        return new File(string.trim());
    }

    @Test
    public void canParseStringToFile() throws Exception {
        canParse("/path/to").toType(File.class);
    }

    @Test
    public void canParseToGenericFileArray() throws Exception {
        canParse("/path/a, /path/b, /path/c")
                .toGenericArray(new GenericType<File[]>() {});
    }

    @Test
    public void canParseToFileArray() throws Exception {
        canParse("/path/a, /path/b, /path/c")
                .toArray(File[].class);
    }

    @Test
    public void canParseToFileList() throws Exception {
        canParse("/path/a, /path/b, /path/c")
                .toArrayList(new GenericType<List<File>>() {});
    }

    @Test
    public void canParseToFileSet() throws Exception {
        canParse("/path/a, /path/b, /path/b")
                .toLinkedHashSet(new GenericType<Set<File>>() {});
    }

    @Test
    public void canParseToFileMap() throws Exception {
        canParse("/path/a=/path/A,/path/b=/path/B");
        toLinkedHashMap(new GenericType<Map<File, File>>() {});
    }
}
