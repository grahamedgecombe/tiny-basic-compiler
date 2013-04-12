package com.grahamedgecombe.tinybasic.ast;

public final class ImmediateString extends StringExpression {

    private final String value;

    public ImmediateString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImmediateString that = (ImmediateString) o;

        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
