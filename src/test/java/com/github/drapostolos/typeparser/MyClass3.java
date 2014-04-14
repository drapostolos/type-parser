package com.github.drapostolos.typeparser;

public class MyClass3 {

    Integer value;

    public static MyClass3 valueOf(String value) {
        return new MyClass3(Integer.valueOf(value));
    }

    public MyClass3(Integer value) {
        this.value = value;
    }
}
