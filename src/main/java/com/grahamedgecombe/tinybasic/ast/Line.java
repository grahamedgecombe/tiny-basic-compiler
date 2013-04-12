package com.grahamedgecombe.tinybasic.ast;

import com.grahamedgecombe.tinybasic.stackir.Instruction;
import com.grahamedgecombe.tinybasic.stackir.InstructionSequence;
import com.grahamedgecombe.tinybasic.stackir.Opcode;

import java.util.Objects;

public final class Line {

    private final int number;
    private final Statement statement;

    public Line(int number, Statement statement) {
        this.number = number;
        this.statement = statement;
    }

    public int getNumber() {
        return number;
    }

    public Statement getStatement() {
        return statement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        if (number != line.number) return false;
        if (!statement.equals(line.statement)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, statement);
    }

    @Override
    public String toString() {
        return number + " " + statement;
    }

    public void compile(InstructionSequence seq) {
        seq.append(new Instruction(Opcode.LABEL, seq.createLineLabel(number)));
        statement.compile(seq);
    }

}
