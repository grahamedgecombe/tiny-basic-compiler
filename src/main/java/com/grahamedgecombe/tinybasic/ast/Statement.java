package com.grahamedgecombe.tinybasic.ast;

import com.grahamedgecombe.tinybasic.stackir.InstructionSequence;

public abstract class Statement {

    public abstract void compile(InstructionSequence seq);

}
