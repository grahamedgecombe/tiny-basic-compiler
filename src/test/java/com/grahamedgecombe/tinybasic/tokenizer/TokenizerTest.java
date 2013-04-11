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
            assertEquals(new Token(Type.OP_EQ), tokenizer.nextToken());
            assertEquals(new Token(Type.OP_LTE), tokenizer.nextToken());
            assertEquals(new Token(Type.OP_LT), tokenizer.nextToken());
            assertEquals(new Token(Type.OP_NE), tokenizer.nextToken());
            assertEquals(new Token(Type.OP_NE), tokenizer.nextToken());
            assertEquals(new Token(Type.OP_GT), tokenizer.nextToken());
            assertEquals(new Token(Type.OP_GTE), tokenizer.nextToken());
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
            assertEquals(new Token(Type.OP_PLUS), tokenizer.nextToken());
            assertEquals(new Token(Type.OP_MINUS), tokenizer.nextToken());
            assertEquals(new Token(Type.OP_MULT), tokenizer.nextToken());
            assertEquals(new Token(Type.OP_DIV), tokenizer.nextToken());
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

}
