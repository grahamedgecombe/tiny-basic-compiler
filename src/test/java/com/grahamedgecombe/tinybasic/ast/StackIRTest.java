package com.grahamedgecombe.tinybasic.ast;

import com.grahamedgecombe.tinybasic.stackir.Instruction;
import com.grahamedgecombe.tinybasic.stackir.InstructionSequence;
import com.grahamedgecombe.tinybasic.stackir.Opcode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public final class StackIRTest {

    @Test
    public void testSimpleArithmetic() {
        Expression expr = new BinaryExpression(
            BinaryOperator.PLUS,
            new ImmediateExpression(3),
            new ImmediateExpression(4));

        InstructionSequence seq = new InstructionSequence();
        expr.compile(seq);

        List<Instruction> expected = Arrays.asList(
            new Instruction(Opcode.PUSH, 3),
            new Instruction(Opcode.PUSH, 4),
            new Instruction(Opcode.ADD)
        );

        assertEquals(expected, seq.getInstructions());
    }

    @Test
    public void testNestedArithmetic() {
        Expression expr = new BinaryExpression(
            BinaryOperator.MULT,
            new BinaryExpression(
                BinaryOperator.PLUS,
                new ImmediateExpression(3),
                new ImmediateExpression(4)
            ),
            new ImmediateExpression(5)
        );

        InstructionSequence seq = new InstructionSequence();
        expr.compile(seq);

        List<Instruction> expected = Arrays.asList(
            new Instruction(Opcode.PUSH, 3),
            new Instruction(Opcode.PUSH, 4),
            new Instruction(Opcode.ADD),
            new Instruction(Opcode.PUSH, 5),
            new Instruction(Opcode.MUL)
        );

        assertEquals(expected, seq.getInstructions());
    }

    @Test
    public void testNegation() {
        Expression expr = new UnaryExpression(UnaryOperator.MINUS, new ImmediateExpression(42));

        InstructionSequence seq = new InstructionSequence();
        expr.compile(seq);

        List<Instruction> expected = Arrays.asList(
            new Instruction(Opcode.PUSH, 0),
            new Instruction(Opcode.PUSH, 42),
            new Instruction(Opcode.SUB)
        );

        assertEquals(expected, seq.getInstructions());
    }

    @Test
    public void testVar() {
        Expression expr = new VariableExpression("X");

        InstructionSequence seq = new InstructionSequence();
        expr.compile(seq);

        List<Instruction> expected = Arrays.asList(new Instruction(Opcode.LOAD, "X"));

        assertEquals(expected, seq.getInstructions());
    }

}
