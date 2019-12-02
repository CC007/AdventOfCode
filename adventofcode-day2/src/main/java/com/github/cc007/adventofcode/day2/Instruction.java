package com.github.cc007.adventofcode.day2;

import java.util.List;
import java.util.function.BiConsumer;

public enum Instruction {

    ADD(1, 3, (address, memory) ->{
        int parameter1 = memory.get(address++);
        int parameter2 = memory.get(address++);
        int parameter3 = memory.get(address);
        int a = memory.get(parameter1);
        int b = memory.get(parameter2);
        System.out.println("address " + parameter3 + " <= address " + parameter1 + " (" + a + ") + address " + parameter2 + " (" + b + ")");

        memory.set(parameter3, a + b);
    }),

    MULTIPLY(2, 3, (address, memory) ->{
        int parameter1 = memory.get(address++);
        int parameter2 = memory.get(address++);
        int parameter3 = memory.get(address);
        int a = memory.get(parameter1);
        int b = memory.get(parameter2);
        System.out.println("address " + parameter3 + " <= address " + parameter1 + " (" + a + ") * address " + parameter2 + " (" + b + ")");

        memory.set(parameter3, a * b);
    }),

    HALT(99, 0, (address, memory) -> {});

    public int opCode;
    public int parameterCount;
    private BiConsumer<Integer, List<Integer>> instruction;

    Instruction(int opCode, int parameterCount, BiConsumer<Integer, List<Integer>> instruction) {
        this.opCode = opCode;
        this.instruction = instruction;
        this.parameterCount = parameterCount;
    }

    public void execute(int address, List<Integer> memory) {
        instruction.accept(address, memory);
    }

    public static Instruction valueOf(int opCode) {
        if (opCode == ADD.opCode) {
            return ADD;
        } else if (opCode == MULTIPLY.opCode) {
            return MULTIPLY;
        }
        throw new RuntimeException("Invalid opCode! Only 1 (add) or 2 (multiply) is valid");
    }
}
