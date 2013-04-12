package com.grahamedgecombe.tinybasic.parser;

import com.grahamedgecombe.tinybasic.ast.*;
import com.grahamedgecombe.tinybasic.tokenizer.Tokenizer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public final class ParserTest {

    @Test
    public void testUnaryOperator() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("-7")) {
            try (Parser parser = new Parser(tokenizer)) {
                Expression expr = parser.nextExpression();
                assertEquals(new UnaryExpression(UnaryOperator.MINUS, new ImmediateExpression(7)), expr);
            }
        }
    }

    @Test
    public void testBinaryOperator() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("3 + 4")) {
            try (Parser parser = new Parser(tokenizer)) {
                Expression expr = parser.nextExpression();
                assertEquals(new BinaryExpression(
                    BinaryOperator.PLUS,
                    new ImmediateExpression(3),
                    new ImmediateExpression(4)
                ), expr);
            }
        }
    }

    @Test
    public void testAssociativity() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("3 + 4 + 5")) {
            try (Parser parser = new Parser(tokenizer)) {
                Expression expr = parser.nextExpression();
                assertEquals(new BinaryExpression(
                    BinaryOperator.PLUS,
                    new BinaryExpression(
                        BinaryOperator.PLUS,
                        new ImmediateExpression(3),
                        new ImmediateExpression(4)
                    ),
                    new ImmediateExpression(5)
                ), expr);
            }
        }
    }

    @Test
    public void testPrecedence() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("3 + 4 * 5")) {
            try (Parser parser = new Parser(tokenizer)) {
                Expression expr = parser.nextExpression();
                assertEquals(new BinaryExpression(
                    BinaryOperator.PLUS,
                    new ImmediateExpression(3),
                    new BinaryExpression(
                        BinaryOperator.MULT,
                        new ImmediateExpression(4),
                        new ImmediateExpression(5)
                    )
                ), expr);
            }
        }
    }

    @Test
    public void testParentheses() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("(3 + 4) * 5")) {
            try (Parser parser = new Parser(tokenizer)) {
                Expression expr = parser.nextExpression();
                assertEquals(new BinaryExpression(
                    BinaryOperator.MULT,
                    new BinaryExpression(
                        BinaryOperator.PLUS,
                        new ImmediateExpression(3),
                        new ImmediateExpression(4)
                    ),
                    new ImmediateExpression(5)
                ), expr);
            }
        }
    }

    @Test
    public void testEndStatement() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("END")) {
            try (Parser parser = new Parser(tokenizer)) {
                Statement stmt = parser.nextStatement();
                assertEquals(new EndStatement(), stmt);
            }
        }
    }

    @Test
    public void testReturnStatement() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("RETURN")) {
            try (Parser parser = new Parser(tokenizer)) {
                Statement stmt = parser.nextStatement();
                assertEquals(new ReturnStatement(), stmt);
            }
        }
    }

    @Test
    public void testLetStatement() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("LET X = 3")) {
            try (Parser parser = new Parser(tokenizer)) {
                Statement stmt = parser.nextStatement();
                assertEquals(new LetStatement("X", new ImmediateExpression(3)), stmt);
            }
        }
    }

    @Test
    public void testInputStatement() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("INPUT A, B, C")) {
            try (Parser parser = new Parser(tokenizer)) {
                Statement stmt = parser.nextStatement();
                assertEquals(new InputStatement("A", "B", "C"), stmt);
            }
        }
    }

    @Test
    public void testBranchStatement() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("GOTO 400")) {
            try (Parser parser = new Parser(tokenizer)) {
                Statement stmt = parser.nextStatement();
                assertEquals(new BranchStatement(BranchType.GOTO, 400), stmt);
            }
        }
    }

    @Test
    public void testIfStatement() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("IF X = Y THEN END")) {
            try (Parser parser = new Parser(tokenizer)) {
                Statement stmt = parser.nextStatement();
                assertEquals(new IfStatement(
                    RelationalOperator.EQ,
                    new VariableExpression("X"),
                    new VariableExpression("Y"),
                    new EndStatement()
                ), stmt);
            }
        }
    }

    @Test
    public void testPrintStatement() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("PRINT \"The meaning of life is \", 42, \".\"")) {
            try (Parser parser = new Parser(tokenizer)) {
                Statement stmt = parser.nextStatement();
                assertEquals(new PrintStatement(
                    new ImmediateString("The meaning of life is "),
                    new ImmediateExpression(42),
                    new ImmediateString(".")
                ), stmt);
            }
        }
    }

    @Test
    public void testLine() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("10 PRINT \"Hello, world!\"")) {
            try (Parser parser = new Parser(tokenizer)) {
                Line line = parser.nextLine();
                assertEquals(new Line(10, new PrintStatement(new ImmediateString("Hello, world!"))), line);
            }
        }
    }

    @Test
    public void testProgram() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("10 LET X = X + 1\n20 PRINT X\n30 GOTO 10")) {
            try (Parser parser = new Parser(tokenizer)) {
                Program program = parser.parse();
                Program expected = new Program(
                    new Line(10, new LetStatement("X", new BinaryExpression(
                        BinaryOperator.PLUS,
                        new VariableExpression("X"),
                        new ImmediateExpression(1)
                    ))),
                    new Line(20, new PrintStatement(new VariableExpression("X"))),
                    new Line(30, new BranchStatement(BranchType.GOTO, 10)));
                assertEquals(expected, program);
            }
        }
    }

}
