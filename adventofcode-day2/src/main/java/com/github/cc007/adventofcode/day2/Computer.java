package com.github.cc007.adventofcode.day2;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Computer implements Runnable {

    private List<Integer> memory;
    private int instructionPointer = 0;

    public Computer(String memoryString) {
        this.memory = parseIntCode(memoryString);
    }

    private static List<Integer> parseIntCode(String input) {
        return Arrays.stream(input.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public void fixMemory(Consumer<List<Integer>> memoryFixer) {
        memoryFixer.accept(memory);
    }

    @Override
    public void run() {
        while (memory.get(instructionPointer) != Instruction.HALT.opCode) {
            System.out.println("Current memory: " + this);

            int opCode = fetch();
            System.out.print("Found opCode: " + opCode);

            Instruction instruction = parse(opCode);
            System.out.println(" (" + instruction.name() + ")");

            execute(instruction);
        }
    }

    private int fetch() {
        return memory.get(instructionPointer++);
    }

    private Instruction parse(int opCode) {
        return Instruction.valueOf(opCode);
    }

    private void execute(Instruction instruction) {
        instruction.execute(instructionPointer, memory);
        instructionPointer += instruction.parameterCount;
    }

    @Override
    public String toString() {
        return memory.stream()
                .map((Object::toString))
                .collect(Collectors.joining(","));
    }
}
