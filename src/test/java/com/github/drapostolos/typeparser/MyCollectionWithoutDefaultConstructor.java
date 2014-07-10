package com.github.drapostolos.typeparser;

import java.util.AbstractList;

public class MyCollectionWithoutDefaultConstructor<T> extends AbstractList<T> {

    public MyCollectionWithoutDefaultConstructor(String str) {}

    @Override
    public T get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

}
