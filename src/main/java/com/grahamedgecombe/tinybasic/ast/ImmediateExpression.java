package com.grahamedgecombe.tinybasic.ast;

public final class ImmediateExpression extends Expression {

    private final int value;

    public ImmediateExpression(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImmediateExpression that = (ImmediateExpression) o;

        if (value != that.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value;
    }

}
