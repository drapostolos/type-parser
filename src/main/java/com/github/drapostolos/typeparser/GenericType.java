package com.github.drapostolos.typeparser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class GenericType<T> {
    private final Type type;
    
    static <T> GenericType<T> forClass(Class<T> type) {
        return new GenericType<T>(type){};
    }

    public GenericType() {
        if(GenericType.class != getClass().getSuperclass()){
            String errorMsg = "'%s' must be a direct subclass of '%s'";
            errorMsg = String.format(errorMsg, getClass().getName(), GenericType.class.getName());
            throw new IllegalArgumentException(errorMsg);
        }
        Type t = getClass().getGenericSuperclass();
        if(t instanceof ParameterizedType){
            ParameterizedType superClass = (ParameterizedType) t;
            type = superClass.getActualTypeArguments()[0];
        } else {
            String message = "'%s' must be parameterized (for example \"new GenericType<List<Integer>>(){}\"), "
                    + "it can not be of raw type \"new GenericType(){}\".";
            throw new IllegalStateException(String.format(message, getClass().getName()));
        }
    }
    
    GenericType(Class<?> type) {
        this.type = type;
    }

    final Type getType(){
        return type;
    }

    @Override
    final public String toString() {
        return type.toString();
    }

    @Override
   final public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    final public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if(obj instanceof GenericType){
            GenericType<?> other = (GenericType<?>) obj;
            if(type.equals(other.type)){
                return true;
            }
        }
        return false;
    }


    
    
}
