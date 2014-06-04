package com.github.drapostolos.typeparser;

public class WithStaticFactoryMethodLong {

    static WithStaticFactoryMethodLong valueOf(Long l) {
        return new WithStaticFactoryMethodLong();
    }

}
