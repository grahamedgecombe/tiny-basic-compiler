package com.grahamedgecombe.tinybasic.codegen.x86_64;

import com.grahamedgecombe.tinybasic.codegen.CodeGenerator;
import com.grahamedgecombe.tinybasic.stackir.Instruction;
import com.grahamedgecombe.tinybasic.stackir.InstructionSequence;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public final class X86_64CodeGenerator extends CodeGenerator {

    private final Writer writer;

    public X86_64CodeGenerator(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void generate(InstructionSequence seq) throws IOException {
        writer.append("[extern exit]\n");
        writer.append("[extern printf]\n");
        writer.append("[extern scanf]\n");
        writer.append("[section .code]\n");
        writer.append("[global main]\n");
        writer.append("main:\n");
        writer.append("  push rbp\n");
        writer.append("  mov rbp, rsp\n");
        writer.append("  sub rsp, " + (8 * 27) + "\n");

        Map<String, String> strings = new HashMap<>();
        for (Instruction instruction : seq.getInstructions()) {
            switch (instruction.getOpcode()) {
                case LABEL:
                    writer.append(instruction.getStringOperand().get() + ":\n");
                    break;

                case PUSHI:
                    writer.append("  push 0x" + Integer.toHexString(instruction.getIntegerOperand().get()) + "\n");
                    break;

                case PUSHS:
                    String label = seq.createGeneratedLabel();
                    strings.put(label, instruction.getStringOperand().get());
                    writer.append("  push " + label + "\n");
                    break;

                case LOAD:
                    writer.append("  mov rax, [rbp - " + varIndex(instruction) + "]\n");
                    writer.append("  push rax\n");
                    break;

                case STORE:
                    writer.append("  pop rax\n");
                    writer.append("  mov [rbp - " + varIndex(instruction) + "], rax\n");
                    break;

                case ADD:
                    writer.append("  pop rax\n");
                    writer.append("  pop rbx\n");
                    writer.append("  add rax, rbx\n");
                    writer.append("  push rax\n");
                    break;

                case SUB:
                    writer.append("  pop rax\n");
                    writer.append("  pop rbx\n");
                    writer.append("  sub rax, rbx\n");
                    writer.append("  push rax\n");
                    break;

                case MUL:
                    writer.append("  pop rax\n");
                    writer.append("  pop rbx\n");
                    writer.append("  imul rax, rbx\n");
                    writer.append("  push rax\n");
                    break;

                case DIV:
                    writer.append("  xor rdx, rdx\n");
                    writer.append("  pop rax\n");
                    writer.append("  pop rbx\n");
                    writer.append("  idiv rbx\n");
                    writer.append("  push rax\n");
                    break;

                case CALL:
                    writer.append("  call " + instruction.getStringOperand().get() + "\n");
                    break;

                case RET:
                    writer.append("  ret\n");
                    break;

                case JMP:
                    writer.append("  jmp " + instruction.getStringOperand().get() + "\n");
                    break;

                case JMPGT:
                    writer.append("  pop rbx\n");
                    writer.append("  pop rax\n");
                    writer.append("  cmp rax, rbx\n");
                    writer.append("  jg " + instruction.getStringOperand().get() + "\n");
                    break;

                case JMPGTE:
                    writer.append("  pop rbx\n");
                    writer.append("  pop rax\n");
                    writer.append("  cmp rax, rbx\n");
                    writer.append("  jge " + instruction.getStringOperand().get() + "\n");
                    break;

                case JMPLT:
                    writer.append("  pop rbx\n");
                    writer.append("  pop rax\n");
                    writer.append("  cmp rax, rbx\n");
                    writer.append("  jl " + instruction.getStringOperand().get() + "\n");
                    break;

                case JMPLTE:
                    writer.append("  pop rbx\n");
                    writer.append("  pop rax\n");
                    writer.append("  cmp rax, rbx\n");
                    writer.append("  jle " + instruction.getStringOperand().get() + "\n");
                    break;

                case JMPEQ:
                    writer.append("  pop rbx\n");
                    writer.append("  pop rax\n");
                    writer.append("  cmp rax, rbx\n");
                    writer.append("  je " + instruction.getStringOperand().get() + "\n");
                    break;

                case JMPNE:
                    writer.append("  pop rbx\n");
                    writer.append("  pop rax\n");
                    writer.append("  cmp rax, rbx\n");
                    writer.append("  jne " + instruction.getStringOperand().get() + "\n");
                    break;

                case HLT:
                    writer.append("  mov rdi, 0\n");
                    writer.append("  call exit\n");
                    break;

                case IN:
                    strings.put("num_fmt", "%d");
                    writer.append("  lea rsi, [rbp - " + (8 * 26) + "]\n");
                    writer.append("  mov rdi, num_fmt\n");
                    writer.append("  mov al, 0\n");
                    writer.append("  call scanf\n");
                    writer.append("  xor rax, rax\n");
                    writer.append("  mov eax, [rbp - " + (8 * 26) + "]\n");
                    writer.append("  push rax\n");
                    break;

                case OUTS:
                    strings.put("str_fmt", "%s");
                    writer.append("  pop rsi\n");
                    writer.append("  mov rdi, str_fmt\n");
                    writer.append("  mov al, 0\n");
                    writer.append("  call printf\n");
                    break;

                case OUTI:
                    strings.put("num_fmt", "%d");
                    writer.append("  pop rsi\n");
                    writer.append("  mov rdi, num_fmt\n");
                    writer.append("  mov al, 0\n");
                    writer.append("  call printf\n");
                    break;
            }
        }

        writer.append("  mov rax, 0\n");
        writer.append("  mov rsp, rbp\n");
        writer.append("  pop rbp\n");
        writer.append("  ret\n");

        writer.append("[section .rodata]\n");
        for (Map.Entry<String, String> string : strings.entrySet()) {
            writer.append(string.getKey() + ":\n");
            writer.append("  db \"" + escape(string.getValue()) + "\", 0\n");
        }
    }

    private String escape(String value) {
        value = value.replace("\n", "\", 10, \"");
        return value;
    }

    private int varIndex(Instruction instruction) {
        return (instruction.getStringOperand().get().charAt(0) - 'A') * 8;
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

}
