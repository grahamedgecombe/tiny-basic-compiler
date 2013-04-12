package com.grahamedgecombe.tinybasic.ast;

import com.grahamedgecombe.tinybasic.stackir.InstructionSequence;

public abstract class StringExpression {

    public abstract void compile(InstructionSequence seq);

}
