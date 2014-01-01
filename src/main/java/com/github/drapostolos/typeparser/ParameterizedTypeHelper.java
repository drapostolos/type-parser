package com.github.drapostolos.typeparser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class ParameterizedTypeHelper extends Helper{
    private final ParameterizedType type;

    ParameterizedTypeHelper(StringToTypeParser stringParser, ParameterizedType type) {
        super(stringParser);
        this.type = type;
    }

    public Type[] getActualTypeArguments(){
        return type.getActualTypeArguments();
    }

    public Type getRawType(){
        return type.getRawType();
    }
}
