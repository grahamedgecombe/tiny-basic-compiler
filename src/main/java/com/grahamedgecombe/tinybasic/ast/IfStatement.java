package com.grahamedgecombe.tinybasic.ast;

import com.grahamedgecombe.tinybasic.stackir.Instruction;
import com.grahamedgecombe.tinybasic.stackir.InstructionSequence;
import com.grahamedgecombe.tinybasic.stackir.Opcode;

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

    @Override
    public void compile(InstructionSequence seq) {
        leftExpression.compile(seq);
        rightExpression.compile(seq);

        String thenLabel = seq.createGeneratedLabel();
        String endLabel = seq.createGeneratedLabel();

        Opcode opcode;
        switch (operator) {
            case EQ:
                opcode = Opcode.JMPEQ;
                break;
            case NE:
                opcode = Opcode.JMPNE;
                break;
            case LTE:
                opcode = Opcode.JMPLTE;
                break;
            case LT:
                opcode = Opcode.JMPLT;
                break;
            case GT:
                opcode = Opcode.JMPGT;
                break;
            case GTE:
                opcode = Opcode.JMPGTE;
                break;
            default:
                throw new AssertionError();
        }

        seq.append(
            new Instruction(opcode, thenLabel),
            new Instruction(Opcode.JMP, endLabel),
            new Instruction(Opcode.LABEL, thenLabel)
        );

        statement.compile(seq);

        seq.append(new Instruction(Opcode.LABEL, endLabel));
    }

}
