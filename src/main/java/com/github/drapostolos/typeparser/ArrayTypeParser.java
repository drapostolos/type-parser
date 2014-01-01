package com.github.drapostolos.typeparser;

interface ArrayTypeParser extends TypeParser{
    <T> Object parse(String input, Class<T> componentType, Helper helper);
}
