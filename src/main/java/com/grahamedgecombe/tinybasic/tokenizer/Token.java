package com.grahamedgecombe.tinybasic.tokenizer;

import java.util.Objects;
import java.util.Optional;

public final class Token {

    public enum Type {
        EOF,
        LF,
        VAR,
        KEYWORD,
        NUMBER,
        STRING,
        OP_PLUS,
        OP_MINUS,
        OP_MULT,
        OP_DIV,
        LPAREN,
        RPAREN,
        OP_EQ,
        OP_NE,
        OP_GT,
        OP_GTE,
        OP_LT,
        OP_LTE,
        COMMA
    }

    private final Type type;
    private final Optional<String> value;

    public Token(Type type) {
        this.type = type;
        this.value = Optional.empty();
    }

    public Token(Type type, String value) {
        this.type = type;
        this.value = Optional.of(value);
    }

    public Type getType() {
        return type;
    }

    public Optional<String> getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (type != token.type) return false;
        if (!value.equals(token.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {
        return Token.class.getSimpleName() + " [type=" + type + ", value=" + value + "]";
    }

}
