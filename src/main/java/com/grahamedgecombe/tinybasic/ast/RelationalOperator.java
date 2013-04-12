package com.grahamedgecombe.tinybasic.ast;

public enum RelationalOperator {
    EQ("="), NE("<>"), GT(">"), GTE(">="), LT("<"), LTE("<=");

    private final String string;

    private RelationalOperator(String string) {
        this.string = string;
    }

    public String toString() {
        return string;
    }
}
