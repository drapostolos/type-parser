package com.github.drapostolos.typeparser;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

public class GenericTypeParserTest extends AbstractGenericTypeParserTestHelper{

    @Test
    public void canParseTo_listOfString() throws Throwable {
        assertMethod(List.class).isCallableWith("AAA,BBB");
    }
    void m(List<String> list){
        assertThat(list).containsExactly("AAA", "BBB");
    }


    @Test
    public void canParseTo_arrayOfString_listOfInt_file() throws Throwable {
        assertMethod(String[].class, List.class, File.class).isCallableWith("AA,BB", "1,2,3", "/path/to");
    }
    void m(String[] strArray, List<Integer> intList, File file){
        assertThat(strArray).containsExactly("AA", "BB");
        assertThat(intList).containsExactly(1, 2, 3);
        assertThat(file).isEqualTo(new File("/path/to/"));
    }


    @Test
    public void canParseTo_arrayOfString() throws Throwable {
        assertMethod(String[].class).isCallableWith("AA,DDD");
    }
    void m(String[] strArray){
        assertThat(strArray).containsExactly("AA", "DDD");
    }


    @Test
    public void canParseTo_MapOfLongFile() throws Throwable {
        assertMethod(Map.class).isCallableWith("a=/path/to/a,b=/path/to/b");
    }
    void m(Map<String, File> files){
        System.out.println("Files: " + files);
        assertThat(files).hasSize(2);
        assertThat(files.get("a")).isEqualTo( new File("/path/to/a"));
        assertThat(files.get("b")).isEqualTo( new File("/path/to/b"));
        
    }


    @Test
    public void canParseTo_SetOfClass() throws Throwable {
        String thisClassName = GenericTypeParserTest.class.getName(); 
        assertMethod(Set.class).isCallableWith(thisClassName, thisClassName);
    }
    void m(Set<Class<?>> set){
        assertThat(set).hasSize(1);
        assertThat(set).contains(GenericTypeParserTest.class);
    }


}
