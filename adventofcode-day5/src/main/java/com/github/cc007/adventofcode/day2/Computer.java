package com.github.cc007.adventofcode.day2;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class Computer implements Runnable {

    private List<Integer> memory;
    private AtomicInteger instructionPointer = new AtomicInteger(0);
    private Instruction instruction;
    private ParameterMode parameterMode;

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
        while (memory.get(instructionPointer.get()) != Instruction.HALT.opCode) {
            log.info("Current memory: " + this);

            int intCode = fetch();

            parse(intCode);
            log.info("Found instruction: " + intCode + " (" + instruction.name() + ")");
            log.info("Parameter mode: " + parameterMode.getParameter1Mode() + ", " + parameterMode.getParameter2Mode() + ", " + parameterMode.getParameter3Mode());

            execute();
        }
    }

    private int fetch() {
        return memory.get(instructionPointer.getAndIncrement());
    }

    private void parse(int intCode) {
        this.instruction = Instruction.valueOf(intCode % 100);
        this.parameterMode = new ParameterMode(intCode / 100);
    }

    private void execute() {
        instruction.execute(parameterMode, instructionPointer, memory);
    }

    @Override
    public String toString() {
        return memory.stream()
                .map((Object::toString))
                .collect(Collectors.joining(","));
    }
}
