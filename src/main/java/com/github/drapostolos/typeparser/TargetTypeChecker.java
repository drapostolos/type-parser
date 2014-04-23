package com.github.drapostolos.typeparser;

import java.lang.reflect.Type;

class TargetTypeChecker extends ParseTemplate<Boolean> {

    private final TypeParser parser;

    public TargetTypeChecker(TypeParser parser, Type targetType) {
        super(parser.typeParsers, targetType);
        this.parser = parser;
    }

    @Override
    Boolean actionWhenTargetTypeHasNormalTypeParser() {
        return true;
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

    @Override
    Boolean actionWhenTaretTypeIsGeneric(Class<?> cls) {
        for (Class<?> type : TypeParserUtility.getParameterizedTypeArguments(targetType)) {
            if (!parser.isTargetTypeParsable(type)) {
                return false;
            }
        }
        return true;
    }

    @Override
    Boolean actionWhenTargetTypeIsAssignalbleTo(Class<?> superClass) {
        return true;
    }

}
