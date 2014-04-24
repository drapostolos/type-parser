package com.github.drapostolos.typeparser;

public class MyClass1 extends MyBaseClass implements Parser<MyClass1> {

    String value;

    /*
     * This method is called through reflection in one of the unit tests.
     */
    @SuppressWarnings("unused")
    private static MyClass1 valueOf(String value) {
        return new MyClass1(value);
    }

    @Override
    public MyClass1 parse(String value, ParserHelper helper) {
        return new MyClass1(value);
    }

    public MyClass1(String value) {
        this.value = value;
    }

    public MyClass1() {
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
        MyClass1 other = (MyClass1) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
