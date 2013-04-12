package com.grahamedgecombe.tinybasic.tokenizer;

import com.grahamedgecombe.tinybasic.tokenizer.Token.Type;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public final class TokenizerTest {

    @Test
    public void testEof() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("")) {
            assertEquals(new Token(Type.EOF), tokenizer.nextToken());
        }
    }

    @Test
    public void testNewLine() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("\n")) {
            assertEquals(new Token(Type.LF), tokenizer.nextToken());
        }
    }

    @Test
    public void testString() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("\"hello\"")) {
            assertEquals(new Token(Type.STRING, "hello"), tokenizer.nextToken());
        }
    }

    @Test
    public void testNumber() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("123")) {
            assertEquals(new Token(Type.NUMBER, "123"), tokenizer.nextToken());
        }
    }

    @Test
    public void testRelationalOperators() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("= <= < <> >< > >=")) {
            assertEquals(new Token(Type.EQ), tokenizer.nextToken());
            assertEquals(new Token(Type.LTE), tokenizer.nextToken());
            assertEquals(new Token(Type.LT), tokenizer.nextToken());
            assertEquals(new Token(Type.NE), tokenizer.nextToken());
            assertEquals(new Token(Type.NE), tokenizer.nextToken());
            assertEquals(new Token(Type.GT), tokenizer.nextToken());
            assertEquals(new Token(Type.GTE), tokenizer.nextToken());
        }
    }

    @Test
    public void testVariable() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("G")) {
            assertEquals(new Token(Type.VAR, "G"), tokenizer.nextToken());
        }
    }

    @Test
    public void testKeyword() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("GOTO")) {
            assertEquals(new Token(Type.KEYWORD, "GOTO"), tokenizer.nextToken());
        }
    }

    @Test
    public void testArithmeticOperators() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("+ - * /")) {
            assertEquals(new Token(Type.PLUS), tokenizer.nextToken());
            assertEquals(new Token(Type.MINUS), tokenizer.nextToken());
            assertEquals(new Token(Type.MULT), tokenizer.nextToken());
            assertEquals(new Token(Type.DIV), tokenizer.nextToken());
        }
    }

    @Test
    public void testMisc() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("( ) ,")) {
            assertEquals(new Token(Type.LPAREN), tokenizer.nextToken());
            assertEquals(new Token(Type.RPAREN), tokenizer.nextToken());
            assertEquals(new Token(Type.COMMA), tokenizer.nextToken());
        }
    }

    @Test
    public void testWithoutWhitespace() throws IOException {
        try (Tokenizer tokenizer = new Tokenizer("PRINT13+37")) {
            assertEquals(new Token(Type.KEYWORD, "PRINT"), tokenizer.nextToken());
            assertEquals(new Token(Type.NUMBER, "13"), tokenizer.nextToken());
            assertEquals(new Token(Type.PLUS), tokenizer.nextToken());
            assertEquals(new Token(Type.NUMBER, "37"), tokenizer.nextToken());
        }
    }

}
