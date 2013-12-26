package com.github.drapostolos.typeparser;

import com.github.drapostolos.typeparser.TypeParser;

public class TestClass1  implements TypeParser<TestClass1>{
    String value;
    
    public static TestClass1 valueOf(String value){
        return new TestClass1(value);
    }

    @Override
    public TestClass1 parse(String value) {
        return new TestClass1(value);
    }

    public TestClass1(String value) {
        this.value = value;
    }

    public TestClass1() {
        this.value = "default";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TestClass1 other = (TestClass1) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
