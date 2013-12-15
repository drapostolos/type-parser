package com.github.drapostolos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class DefaultTypeParsers {

    static List<TypeParser<?>> list() {
        List<TypeParser<?>> result = new ArrayList<TypeParser<?>>();
        for(Class<?> c : DefaultTypeParsers.class.getDeclaredClasses()){
            if(TypeParser.class.isAssignableFrom(c)){
                TypeParser<?> instance;
                try {
                    instance = (TypeParser<?>) c.newInstance();
                    result.add(instance);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } 
            }
        }
        return result;
    }

    static class BooleanTypeParser implements TypeParser<Boolean> {
        @Override
        public Boolean parse(final String value0) {
            String value = value0.trim().toLowerCase();
            if(value.equals("true") || value.equals("1")){
                return Boolean.TRUE;
            } else if(value.equals("false") || value.equals("0")){
                return Boolean.FALSE;
            }
            String message = "\"%s\" is not parsable to a Boolean.";
            throw new IllegalArgumentException(String.format(message, value0));
        }
    }

    static class CharacterTypeParser implements TypeParser<Character>{
        @Override
        public Character parse(String value) {
            if(value.length() == 1){
                return Character.valueOf(value.charAt(0));
            }
            String message = "\"%s\" must only contain a single character.";
            throw new IllegalArgumentException(String.format(message, value));
        }
    }

    static class ByteTypeParser implements TypeParser<Byte> {
        @Override
        public Byte parse(String value) {
            return Byte.valueOf(value.trim());
        }
    }

    static class IntegerTypeParser implements TypeParser<Integer>{
        @Override
        public Integer parse(String value) {
            return Integer.valueOf(value.trim());
        }
    }
    
    static class LongTypeParser implements TypeParser<Long>{
        @Override
        public Long parse(String value) {
            return Long.valueOf(value.trim());
        }
    }

    static class ShortTypeParser implements TypeParser<Short>{
        @Override
        public Short parse(String value) {
            return Short.valueOf(value.trim());
        }
    }

    static class FloatTypeParser implements TypeParser<Float>{
        @Override
        public Float parse(String value) {
            return Float.valueOf(value);
        }
    }

    static class DoubleTypeParser implements TypeParser<Double>{
        @Override
        public Double parse(String value) {
            return Double.valueOf(value);
        }
    }

    static class FileTypeParser implements TypeParser<File>{
        @Override
        public File parse(String value) {
            return new File(value.trim());
        }
    }
}
