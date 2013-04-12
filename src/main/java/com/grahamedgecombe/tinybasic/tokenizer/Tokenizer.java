package com.grahamedgecombe.tinybasic.tokenizer;

import com.grahamedgecombe.tinybasic.tokenizer.Token.Type;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public final class Tokenizer implements Closeable {

    private static boolean isWhitespace(int ch) {
        return Character.isWhitespace(ch);
    }

    private static boolean isDigit(int ch) {
        return ch >= '0' && ch <= '9';
    }

    private static boolean isAlpha(int ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    private final Reader reader;

    public Tokenizer(String str) {
        this.reader = new StringReader(str);
    }

    public Tokenizer(Reader reader) {
        this.reader = reader;
    }

    private int peek() throws IOException {
        reader.mark(1);
        try {
            return reader.read();
        } finally {
            reader.reset();
        }
    }

    public Token nextToken() throws IOException {
        for (;;) {
            int ch = reader.read();
            if (ch == -1)
                return new Token(Type.EOF);
            else if (ch == '\n')
                return new Token(Type.LF);
            else if (ch == '+')
                return new Token(Type.PLUS);
            else if (ch == '-')
                return new Token(Type.MINUS);
            else if (ch == '*')
                return new Token(Type.MULT);
            else if (ch == '/')
                return new Token(Type.DIV);
            else if (ch == '(')
                return new Token(Type.LPAREN);
            else if (ch == ')')
                return new Token(Type.RPAREN);
            else if (ch == ',')
                return new Token(Type.COMMA);
            else if (ch == '"')
                return nextStringToken();
            else if (ch == '=')
                return new Token(Type.EQ);
            else if (ch == '>' || ch == '<')
                return nextRelationalOperatorToken(ch);
            else if (isAlpha(ch) && !isAlpha(peek()))
                return new Token(Type.VAR, new String(new char[] { (char) ch }));
            else if (isAlpha(ch))
                return nextKeywordToken(ch);
            else if (isDigit(ch))
                return nextNumberToken(ch);
            else if (!isWhitespace(ch))
                throw new IOException("Unexpected character: " + ch);
        }
    }

    private Token nextRelationalOperatorToken(int first) throws IOException {
        int second = peek();

        if (first == '>') {
            if (second == '<') {
                reader.skip(1);
                return new Token(Type.NE);
            } else if (second == '=') {
                reader.skip(1);
                return new Token(Type.GTE);
            } else {
                return new Token(Type.GT);
            }
        } else {
            assert first == '<';

            if (second == '>') {
                reader.skip(1);
                return new Token(Type.NE);
            } else if (second == '=') {
                reader.skip(1);
                return new Token(Type.LTE);
            } else {
                return new Token(Type.LT);
            }
        }
    }

    private Token nextStringToken() throws IOException {
        StringBuilder buf = new StringBuilder();
        for (;;) {
            int ch = reader.read();
            if (ch == -1)
                throw new IOException("Unexpected EOF within string");
            else if (ch == '"')
                break;

            buf.append((char) ch);
        }
        return new Token(Type.STRING, buf.toString());
    }

    private Token nextKeywordToken(int first) throws IOException {
        StringBuilder buf = new StringBuilder();
        buf.append((char) first);
        for (;;) {
            int ch = peek();
            if (!isAlpha(ch))
                break;

            reader.skip(1);
            buf.append((char) ch);
        }
        return new Token(Type.KEYWORD, buf.toString());
    }

    private Token nextNumberToken(int first) throws IOException {
        StringBuilder buf = new StringBuilder();
        buf.append((char) first);
        for (;;) {
            int ch = peek();
            if (!isDigit(ch))
                break;

            reader.skip(1);
            buf.append((char) ch);
        }
        return new Token(Type.NUMBER, buf.toString());
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

}
