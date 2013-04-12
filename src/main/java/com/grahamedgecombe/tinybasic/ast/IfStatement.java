package com.grahamedgecombe.tinybasic.ast;

import java.util.Objects;

public final class IfStatement extends Statement {

    private final RelationalOperator operator;
    private final Expression leftExpression, rightExpression;
    private final Statement statement;

    public IfStatement(RelationalOperator operator, Expression leftExpression, Expression rightExpression, Statement statement) {
        this.operator = operator;
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.statement = statement;
    }

    public RelationalOperator getOperator() {
        return operator;
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }

    public Statement getStatement() {
        return statement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IfStatement that = (IfStatement) o;

        if (!leftExpression.equals(that.leftExpression)) return false;
        if (operator != that.operator) return false;
        if (!rightExpression.equals(that.rightExpression)) return false;
        if (!statement.equals(that.statement)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, leftExpression, rightExpression, statement);
    }

    @Override
    public String toString() {
        return "IF " + leftExpression + " " + operator + " " + rightExpression + " THEN " + statement;
    }

}
