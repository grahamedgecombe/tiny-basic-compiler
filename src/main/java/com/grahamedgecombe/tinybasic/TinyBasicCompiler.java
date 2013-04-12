package com.grahamedgecombe.tinybasic;

import com.grahamedgecombe.tinybasic.ast.Program;
import com.grahamedgecombe.tinybasic.parser.Parser;
import com.grahamedgecombe.tinybasic.tokenizer.Tokenizer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class TinyBasicCompiler {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get(args[0]);
        try (Tokenizer tokenizer = new Tokenizer(Files.newBufferedReader(path, StandardCharsets.UTF_8))) {
            try (Parser parser = new Parser(tokenizer)) {
                Program program = parser.parse();
                System.out.println(program.toString().trim());
            }
        }
    }

}
