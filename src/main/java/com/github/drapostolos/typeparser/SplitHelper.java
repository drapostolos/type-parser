package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;
import java.util.List;

public final class SplitHelper {
    private static final SplitHelper IGNORED = null;
    private final Type targetType;

    SplitHelper(Type targetType) {
        this.targetType = targetType;
    }

    public List<String> defaultSplit(String input){
        return TypeParserUtility.defaultSplitter().split(input, IGNORED);
    }

    public Type getTargetType() {
        return targetType;
    }
}
