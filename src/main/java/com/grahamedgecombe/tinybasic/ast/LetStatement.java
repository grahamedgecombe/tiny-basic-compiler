package com.grahamedgecombe.tinybasic.ast;

import java.util.Objects;

public final class LetStatement extends Statement {

    private final String name;
    private final Expression value;

    public LetStatement(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LetStatement that = (LetStatement) o;

        if (!name.equals(that.name)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "LET " + name + " = " + value;
    }

}
