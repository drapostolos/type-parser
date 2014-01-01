package com.github.drapostolos.typeparser;



interface ParameterizedTypeParser<T> extends TypeParser{
      T parse(String input, ParameterizedTypeHelper helper);
}
