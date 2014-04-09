package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

public class TypeParserFileTest extends AbstractTest{
    
    @Test
    public void canParseStringToFile() throws Exception {
    	assertThat(parser.isTargetTypeParsable(File.class)).isTrue();
        assertThat(parser.parse("/path/to", File.class)).isEqualTo(new File("/path/to"));
    }

    @Test
    public void canParseToGenericFileArray() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<File[]>() {})).isTrue();
        assertThat(parser.parse("/path/a, /path/b, /path/c", new GenericType<File[]>() {}))
        .containsExactly(new File("/path/a"), new File("/path/b"), new File("/path/c"));
    }

    @Test
    public void canParseToFileArray() throws Exception {
    	assertThat(parser.isTargetTypeParsable(File[].class)).isTrue();
        assertThat(parser.parse("/path/a, /path/b, /path/c", File[].class))
        .containsOnly(new File("/path/a"), new File("/path/b"), new File("/path/c"));
    }

    @Test
    public void canParseToFileList() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<List<File>>() {})).isTrue();
        assertThat(parser.parse("/path/a, /path/b, /path/c", new GenericType<List<File>>() {}))
        .containsExactly(new File("/path/a"), new File("/path/b"), new File("/path/c"));
    }

    @Test
    public void canParseToFileSet() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<Set<File>>() {})).isTrue();
        assertThat(parser.parse("/path/a, /path/b, /path/a", new GenericType<Set<File>>() {}))
        .containsExactly(new File("/path/a"), new File("/path/b"));
    }

    @Test
    public void canParseToFileMap() throws Exception {
    	assertThat(parser.isTargetTypeParsable(new GenericType<Map<File, File>>() {})).isTrue();
        assertThat(parser.parse("/path/a=/path/A, /path/b=/path/B", new GenericType<Map<File, File>>() {}))
        .contains(MapEntry.entry(new File("/path/a"), new File("/path/A")))
        .contains(MapEntry.entry(new File("/path/b"), new File("/path/B")))
        .hasSize(2);
    }
}
