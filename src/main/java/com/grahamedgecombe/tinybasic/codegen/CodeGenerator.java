package com.grahamedgecombe.tinybasic.codegen;

import com.grahamedgecombe.tinybasic.stackir.InstructionSequence;

import java.io.Closeable;
import java.io.IOException;

public abstract class CodeGenerator implements Closeable {

    public abstract void generate(InstructionSequence seq) throws IOException;

}
