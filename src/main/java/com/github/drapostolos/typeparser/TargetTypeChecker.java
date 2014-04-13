package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;
import java.util.List;

class TargetTypeChecker extends ParseTemplate<Boolean> {

    public TargetTypeChecker(StringToTypeParser parser, Type targetType) {
        super(parser, targetType);
    }

    @Override
    Boolean actionWhenTargetTypeHasTypeParser() {
        return true;
    }

    @Override
    Boolean actionWhenTargetTypeIsAssignableFromLinkedHashSet() {
        Class<?> type = TypeParserUtility.getParameterizedTypeArguments(targetType).get(0);
        return parser.isTargetTypeParsable(type);
    }

    @Override
    Boolean actionWhenTargetTypeIsAssignableFromLinkedHashMap() {
        List<Class<?>> types = TypeParserUtility.getParameterizedTypeArguments(targetType);
        Class<?> keyType = types.get(0);
        Class<?> valueType = types.get(1);
        return parser.isTargetTypeParsable(keyType) && parser.isTargetTypeParsable(valueType);
    }

    @Override
    Boolean actionWhenTargetTypeIsAssignableFromArrayList() {
        Class<?> type = TypeParserUtility.getParameterizedTypeArguments(targetType).get(0);
        return parser.isTargetTypeParsable(type);
    }

    @Override
    Boolean actionWhenTargetTypeIsArrayClass() {
        Class<?> type = TypeParserUtility.getComponentClass(targetType);
        return parser.isTargetTypeParsable(type);
    }

    @Override
    Boolean actionWhenTargetTypeHasStaticFactoryMethod() {
        return true;
    }

    @Override
    Boolean actionWhenTargetTypeIsGenericArrayType() {
        Class<?> type = TypeParserUtility.getComponentClass(targetType);
        return parser.isTargetTypeParsable(type);
    }

    @Override
    Boolean lastAction() {
        return false;
    }

}
