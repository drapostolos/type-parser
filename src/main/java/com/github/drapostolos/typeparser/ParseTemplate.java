package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.TypeParserUtility.containsStaticFactoryMethodNamedValueOf;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

abstract class ParseTemplate<T> {

    final Type targetType;
    final TypeParsers typeParsers;

    protected ParseTemplate(TypeParsers typeParsers, Type targetType) {
        this.targetType = targetType;
        this.typeParsers = typeParsers;
    }

    abstract T actionWhenTargetTypeHasNormalTypeParser();

    abstract T actionWhenTaretTypeIsGeneric(Class<?> cls);

    abstract T actionWhenTargetTypeIsArrayClass();

    abstract T actionWhenTargetTypeIsGenericArrayType();

    abstract T actionWhenTargetTypeIsAssignalbleTo(Class<?> superClass);

    abstract T actionWhenTargetTypeHasStaticFactoryMethod();

    abstract T lastAction();

    final T execute() {
        if (typeParsers.normalTypeParsers.containsKey(targetType)) {
            return actionWhenTargetTypeHasNormalTypeParser();
        }
        if (targetType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) targetType;
            Class<?> rawTargetType = (Class<?>) type.getRawType();

            for (Class<?> rawSuperType : typeParsers.assignableTypeParsers.keySet()) {
                if (rawTargetType.isAssignableFrom(rawSuperType)) {
                    return actionWhenTaretTypeIsGeneric(rawSuperType);
                }
            }
        }

        if (targetType instanceof Class) {
            Class<?> targetClass = (Class<?>) targetType;
            if (targetClass.isArray()) {
                return actionWhenTargetTypeIsArrayClass();
            }
            for (Class<?> superClass : typeParsers.assignableTypeParsers.keySet()) {
                if (superClass.isAssignableFrom(targetClass)) {
                    return actionWhenTargetTypeIsAssignalbleTo(superClass);
                }
            }
            if (containsStaticFactoryMethodNamedValueOf(targetClass)) {
                return actionWhenTargetTypeHasStaticFactoryMethod();
            }
        }

        if (targetType instanceof GenericArrayType) {
            return actionWhenTargetTypeIsGenericArrayType();
        }

        return lastAction();
    }

}
