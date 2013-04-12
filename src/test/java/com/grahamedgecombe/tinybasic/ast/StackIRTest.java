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
            new Instruction(Opcode.PUSHI, 3),
            new Instruction(Opcode.PUSHI, 4),
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
            new Instruction(Opcode.PUSHI, 3),
            new Instruction(Opcode.PUSHI, 4),
            new Instruction(Opcode.ADD),
            new Instruction(Opcode.PUSHI, 5),
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
            new Instruction(Opcode.PUSHI, 0),
            new Instruction(Opcode.PUSHI, 42),
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

    @Test
    public void testEndStatement() {
        Statement stmt = new EndStatement();

        InstructionSequence seq = new InstructionSequence();
        stmt.compile(seq);

        List<Instruction> expected = Arrays.asList(new Instruction(Opcode.HLT));

        assertEquals(expected, seq.getInstructions());
    }

    @Test
    public void testReturnStatement() {
        Statement stmt = new ReturnStatement();

        InstructionSequence seq = new InstructionSequence();
        stmt.compile(seq);

        List<Instruction> expected = Arrays.asList(new Instruction(Opcode.RET));

        assertEquals(expected, seq.getInstructions());
    }

    @Test
    public void testLetStatement() {
        Statement stmt = new LetStatement("X", new ImmediateExpression(42));

        InstructionSequence seq = new InstructionSequence();
        stmt.compile(seq);

        List<Instruction> expected = Arrays.asList(
            new Instruction(Opcode.PUSHI, 42),
            new Instruction(Opcode.STORE, "X")
        );

        assertEquals(expected, seq.getInstructions());
    }

    @Test
    public void testBranchStatement() {
        Statement stmt = new BranchStatement(BranchType.GOTO, 10);

        InstructionSequence seq = new InstructionSequence();
        stmt.compile(seq);

        List<Instruction> expected = Arrays.asList(new Instruction(Opcode.JMP, "line_10"));

        assertEquals(expected, seq.getInstructions());
    }

    @Test
    public void testIfStatement() {
        Statement stmt = new IfStatement(
            RelationalOperator.EQ,
            new ImmediateExpression(13),
            new ImmediateExpression(37),
            new EndStatement()
        );

        InstructionSequence seq = new InstructionSequence();
        stmt.compile(seq);

        List<Instruction> expected = Arrays.asList(
            new Instruction(Opcode.PUSHI, 13),
            new Instruction(Opcode.PUSHI, 37),
            new Instruction(Opcode.JMPEQ, "generated_0"),
            new Instruction(Opcode.JMP, "generated_1"),
            new Instruction(Opcode.LABEL, "generated_0"),
            new Instruction(Opcode.HLT),
            new Instruction(Opcode.LABEL, "generated_1")
        );

        assertEquals(expected, seq.getInstructions());
    }

    @Test
    public void testInputStatement() {
        Statement stmt = new InputStatement("A", "B", "C");

        InstructionSequence seq = new InstructionSequence();
        stmt.compile(seq);

        List<Instruction> expected = Arrays.asList(
            new Instruction(Opcode.IN),
            new Instruction(Opcode.STORE, "A"),
            new Instruction(Opcode.IN),
            new Instruction(Opcode.STORE, "B"),
            new Instruction(Opcode.IN),
            new Instruction(Opcode.STORE, "C")
        );

        assertEquals(expected, seq.getInstructions());
    }

    @Test
    public void testPrintStatement() {
        Statement stmt = new PrintStatement(new ImmediateExpression(123), new ImmediateString("abc"));

        InstructionSequence seq = new InstructionSequence();
        stmt.compile(seq);

        List<Instruction> expected = Arrays.asList(
            new Instruction(Opcode.PUSHI, 123),
            new Instruction(Opcode.OUTI),
            new Instruction(Opcode.PUSHS, "abc"),
            new Instruction(Opcode.OUTS),
            new Instruction(Opcode.PUSHS, "\n"),
            new Instruction(Opcode.OUTS)
        );

        assertEquals(expected, seq.getInstructions());
    }

}
