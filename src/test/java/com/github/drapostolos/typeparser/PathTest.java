package com.github.drapostolos.typeparser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class PathTest extends AbstractTypeTester<Path> {

    @Override
    Path make(String string) throws Exception {
        return Paths.get(string.trim());
    }

    @Test
    public void canParseStringToPath() throws Exception {
        canParse("").toType(Path.class);
        canParse("rel").toType(Path.class);
        canParse("/abs").toType(Path.class);
    }
    
    @Test
    public void canParseToGenericPathArray() throws Exception {
        canParse("/some/path0, /some/path1, /some/path2").toGenericArray(new GenericType<Path[]>() {});
    }

    @Test
    public void canParseToPathArray() throws Exception {
        canParse("/some/path0, /some/path1, /some/path2").toArray(Path[].class);
    }

    @Test
    public void canParseToPathList() throws Exception {
        canParse("/some/path0, /some/path1, /some/path2").toArrayList(new GenericType<List<Path>>() {});
    }

    @Test
    public void canParseToPathSet() throws Exception {
        canParse("/some/path0, /some/path1, /some/path0").toLinkedHashSet(new GenericType<Set<Path>>() {});
    }

    @Test
    public void canParseToPathMap() throws Exception {
        canParse("/some/path0=/some/path, /some/path1=/some/path").toLinkedHashMap(new GenericType<Map<Path, Path>>() {});
    }

}
