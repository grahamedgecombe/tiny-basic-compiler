package com.grahamedgecombe.tinybasic.ast;

public enum UnaryOperator {
    PLUS('+'), MINUS('-');

    private final char character;

    private UnaryOperator(char character) {
        this.character = character;
    }

    public String toString() {
        return String.valueOf(character);
    }
}
