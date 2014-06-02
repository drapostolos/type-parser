package com.github.drapostolos.typeparser;

import java.util.AbstractMap;
import java.util.Set;

public class MyMap<K, V> extends AbstractMap<K, V> {

    public MyMap(String str) {
        /*
         * This constructor is here to remove the default empty constructor.
         */
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

}
