package com.github.drapostolos.typeparser;

import static com.github.drapostolos.typeparser.TypeParserUtility.containsStaticFactoryMethodNamedValueOf;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

abstract class ParseTemplate<T> {
	final Type targetType;
	final StringToTypeParser parser;

	protected ParseTemplate(StringToTypeParser parser, Type targetType) {
		this.targetType = targetType;
		this.parser = parser; 
	}

	abstract T actionWhenTargetTypeHasTypeParser();
	abstract T actionWhenTargetTypeIsAssignableFromLinkedHashSet();
	abstract T actionWhenTargetTypeIsAssignableFromLinkedHashMap();
	abstract T actionWhenTargetTypeIsAssignableFromArrayList();
	abstract T actionWhenTargetTypeIsArrayClass();
	abstract T actionWhenTargetTypeHasStaticFactoryMethod();
	abstract T actionWhenTargetTypeIsGenericArrayType();
	abstract T lastAction();
	
	final T execute() {
        if(parser.typeParsers.containsKey(targetType)){
            return actionWhenTargetTypeHasTypeParser();
        } 
        
        if(targetType instanceof ParameterizedType){
            ParameterizedType type = (ParameterizedType) targetType;
            Class<?> rawType = (Class<?>) type.getRawType();
            if(rawType.isAssignableFrom(LinkedHashSet.class)){
            	return actionWhenTargetTypeIsAssignableFromLinkedHashSet();
            }
            if(rawType.isAssignableFrom(LinkedHashMap.class)){
            	return actionWhenTargetTypeIsAssignableFromLinkedHashMap();
            }
            if(rawType.isAssignableFrom(ArrayList.class)){
            	return actionWhenTargetTypeIsAssignableFromArrayList();
            }
        }

        if(targetType instanceof Class){
            Class<?> cls = (Class<?>) targetType;
            if(cls.isArray()){
            	return actionWhenTargetTypeIsArrayClass();
            }
            if(containsStaticFactoryMethodNamedValueOf(cls)){
            	return actionWhenTargetTypeHasStaticFactoryMethod();
            }
        }
        
        if(targetType instanceof GenericArrayType){
        	return actionWhenTargetTypeIsGenericArrayType();
        }
        
        return lastAction();
	}

}
