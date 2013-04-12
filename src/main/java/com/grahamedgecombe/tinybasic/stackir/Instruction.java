package com.grahamedgecombe.tinybasic.stackir;

import java.util.Objects;
import java.util.Optional;

public final class Instruction {

    private final Opcode opcode;
    private final Optional<String> stringOperand;
    private final Optional<Integer> integerOperand;

    public Instruction(Opcode opcode) {
        this.opcode = opcode;
        this.stringOperand = Optional.empty();
        this.integerOperand = Optional.empty();
    }

    public Instruction(Opcode opcode, String operand) {
        this.opcode = opcode;
        this.stringOperand = Optional.of(operand);
        this.integerOperand = Optional.empty();
    }

    public Instruction(Opcode opcode, int operand) {
        this.opcode = opcode;
        this.stringOperand = Optional.empty();
        this.integerOperand = Optional.of(operand);
    }

    public Opcode getOpcode() {
        return opcode;
    }

    public Optional<String> getStringOperand() {
        return stringOperand;
    }

    public Optional<Integer> getIntegerOperand() {
        return integerOperand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Instruction that = (Instruction) o;

        if (!integerOperand.equals(that.integerOperand)) return false;
        if (opcode != that.opcode) return false;
        if (!stringOperand.equals(that.stringOperand)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(opcode, stringOperand, integerOperand);
    }

    @Override
    public String toString() {
        if (stringOperand.isPresent())
            return opcode + " " + stringOperand.get();
        else if (integerOperand.isPresent())
            return opcode + " " + integerOperand.get();
        else
            return opcode.toString();
    }

}
