package com.grahamedgecombe.tinybasic.parser;

import com.grahamedgecombe.tinybasic.ast.*;
import com.grahamedgecombe.tinybasic.tokenizer.Token;
import com.grahamedgecombe.tinybasic.tokenizer.Token.Type;
import com.grahamedgecombe.tinybasic.tokenizer.Tokenizer;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Parser implements Closeable {

    private final Tokenizer tokenizer;
    private Token token;

    public Parser(Tokenizer tokenizer) throws IOException {
        this.tokenizer = tokenizer;
        this.token = tokenizer.nextToken();
    }

    private void consume() throws IOException {
        token = tokenizer.nextToken();
    }

    private boolean accept(Type type) throws IOException {
        if (token.getType() == type) {
            consume();
            return true;
        }

        return false;
    }

    private void expect(Type type) throws IOException {
        if (!accept(type))
            throw new IOException("Unexpected " + token.getType() + ", expecting " + type);
    }

    public Program parse() throws IOException {
        List<Line> lines = new ArrayList<>();
        while (!accept(Type.EOF)) {
            lines.add(nextLine());
        }
        return new Program(lines);
    }

    Line nextLine() throws IOException {
        if (token.getType() != Type.NUMBER)
            throw new IOException("Unexpected " + token.getType() + ", expecting NUMBER");

        int lineNumber = Integer.parseInt(token.getValue().get());
        consume();

        Statement stmt = nextStatement();

        if (token.getType() != Type.LF && token.getType() != Type.EOF)
            throw new IOException("Unexpected " + token.getType() + ", expecting LF or EOF");

        consume();
        return new Line(lineNumber, stmt);
    }

    Statement nextStatement() throws IOException {
        if (token.getType() != Type.KEYWORD)
            throw new IOException("Unexpected " + token.getType() + ", expecting KEYWORD");

        String keyword = token.getValue().get();
        switch (keyword) {
            case "PRINT":
                consume();

                List<StringExpression> values = new ArrayList<>();
                do {
                    if (token.getType() == Type.STRING) {
                        values.add(new ImmediateString(token.getValue().get()));
                        consume();
                    } else {
                        values.add(nextExpression());
                    }
                } while (accept(Type.COMMA));

                return new PrintStatement(values);

            case "IF":
                consume();

                Expression left = nextExpression();

                RelationalOperator operator;
                switch (token.getType()) {
                    case EQ:
                        operator = RelationalOperator.EQ;
                        break;
                    case NE:
                        operator = RelationalOperator.NE;
                        break;
                    case LT:
                        operator = RelationalOperator.LT;
                        break;
                    case LTE:
                        operator = RelationalOperator.LTE;
                        break;
                    case GT:
                        operator = RelationalOperator.GT;
                        break;
                    case GTE:
                        operator = RelationalOperator.GTE;
                        break;
                    default:
                        throw new IOException("Unexpected " + token.getType() + ", expecting EQ, NE, LT, LTE, GT or GTE");
                }
                consume();

                Expression right = nextExpression();

                if (token.getType() != Type.KEYWORD)
                    throw new IOException("Unexpected " + token.getType() + ", expecting KEYWORD");

                String thenKeyword = token.getValue().get();
                if (!thenKeyword.equals("THEN"))
                    throw new IOException("Unexpected keyword " + keyword + ", expecting THEN");

                consume();

                Statement statement = nextStatement();
                return new IfStatement(operator, left, right, statement);

            case "GOTO":
            case "GOSUB":
                consume();

                if (token.getType() != Type.NUMBER)
                    throw new IOException("Unexpected " + token.getType() + ", expecting NUMBER");

                int target = Integer.parseInt(token.getValue().get());
                consume();

                BranchType type = keyword.equals("GOTO") ? BranchType.GOTO : BranchType.GOSUB;
                return new BranchStatement(type, target);

            case "INPUT":
                consume();

                List<String> names = new ArrayList<>();
                do {
                    if (token.getType() != Type.VAR)
                        throw new IOException("Unexpected " + token.getType() + ", expecting VAR");

                    names.add(token.getValue().get());
                    consume();
                } while (accept(Type.COMMA));

                return new InputStatement(names);

            case "LET":
                consume();

                if (token.getType() != Type.VAR)
                    throw new IOException("Unexpected " + token.getType() + ", expecting VAR");

                String name = token.getValue().get();
                consume();

                expect(Type.EQ);

                return new LetStatement(name, nextExpression());

            case "RETURN":
                consume();
                return new ReturnStatement();

            case "END":
                consume();
                return new EndStatement();

            default:
                throw new IOException("Unknown keyword: " + keyword);
        }
    }

    Expression nextExpression() throws IOException {
        Expression left;

        if (token.getType() == Type.PLUS || token.getType() == Type.MINUS) {
            UnaryOperator operator = token.getType() == Type.PLUS ? UnaryOperator.PLUS : UnaryOperator.MINUS;
            consume();

            left = new UnaryExpression(operator, nextTerm());
        } else {
            left = nextTerm();
        }

        while (token.getType() == Type.PLUS || token.getType() == Type.MINUS) {
            BinaryOperator operator = token.getType() == Type.PLUS ? BinaryOperator.PLUS : BinaryOperator.MINUS;
            consume();

            Expression right = nextTerm();
            left = new BinaryExpression(operator, left, right);
        }

        return left;
    }

    private Expression nextTerm() throws IOException {
        Expression left = nextFactor();

        while (token.getType() == Type.MULT || token.getType() == Type.DIV) {
            BinaryOperator operator = token.getType() == Type.MULT ? BinaryOperator.MULT : BinaryOperator.DIV;
            consume();

            left = new BinaryExpression(operator, left, nextFactor());
        }

        return left;
    }

    private Expression nextFactor() throws IOException {
        switch (token.getType()) {
            case VAR:
                Expression expr = new VariableExpression(token.getValue().get());
                consume();
                return expr;

            case NUMBER:
                expr = new ImmediateExpression(Integer.parseInt(token.getValue().get()));
                consume();
                return expr;

            case LPAREN:
                consume();
                expr = nextExpression();
                expect(Type.RPAREN);
                return expr;

            default:
                throw new IOException("Unexpected " + token.getType() + ", expecting VAR, NUMBER or LPAREN");
        }
    }

    @Override
    public void close() throws IOException {
        tokenizer.close();
    }

}
