package com.grahamedgecombe.tinybasic.ast;

import java.util.Objects;

public final class UnaryExpression extends Expression {

    private final UnaryOperator operator;
    private final Expression expression;

    public UnaryExpression(UnaryOperator operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    public UnaryOperator getOperator() {
        return operator;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnaryExpression that = (UnaryExpression) o;

        if (!expression.equals(that.expression)) return false;
        if (operator != that.operator) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, expression);
    }

    @Override
    public String toString() {
        return "(" + operator + expression + ")";
    }

}
