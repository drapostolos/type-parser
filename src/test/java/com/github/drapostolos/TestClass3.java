package com.github.drapostolos;

public class TestClass3 {
    Integer value;
    
    public static TestClass3 valueOf(String value){
        return new TestClass3(Integer.valueOf(value));
    }

    public TestClass3(Integer value) {
        this.value = value;
    }
}
