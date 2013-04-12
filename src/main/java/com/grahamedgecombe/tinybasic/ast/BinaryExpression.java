package com.grahamedgecombe.tinybasic.ast;

import java.util.Objects;

public final class BinaryExpression extends Expression {

    private final BinaryOperator operator;
    private final Expression leftExpression, rightExpression;

    public BinaryExpression(BinaryOperator operator, Expression leftExpression, Expression rightExpression) {
        this.operator = operator;
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    public BinaryOperator getOperator() {
        return operator;
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BinaryExpression that = (BinaryExpression) o;

        if (!leftExpression.equals(that.leftExpression)) return false;
        if (operator != that.operator) return false;
        if (!rightExpression.equals(that.rightExpression)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, leftExpression, rightExpression);
    }

    @Override
    public String toString() {
        return "(" + leftExpression + " " + operator + " " + rightExpression + ")";
    }

}
