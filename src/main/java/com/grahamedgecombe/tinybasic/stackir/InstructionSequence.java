package com.grahamedgecombe.tinybasic.stackir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InstructionSequence {

    private final List<Instruction> instructions = new ArrayList<>();
    private int labelCounter = 0;

    public String createLineLabel(int line) {
        return "line_" + line;
    }

    public String createGeneratedLabel() {
        return "generated_" + (labelCounter++);
    }

    public void append(Instruction... instructions) {
        for (Instruction instruction : instructions)
            this.instructions.add(instruction);
    }

    public List<Instruction> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

}
