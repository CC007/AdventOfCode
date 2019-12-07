package com.github.cc007.adventofcode.day2;

import com.github.cc007.currying.function.TriConsumer;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public enum Instruction {

    ADD(1, 3, (parameterMode, address, memory) -> {
        int parameter1 = memory.get(address.getAndIncrement());
        int parameter2 = memory.get(address.getAndIncrement());
        int parameter3 = memory.get(address.getAndIncrement());
        int a = get(parameter1, memory, parameterMode.getParameter1Mode());
        int b = get(parameter2, memory, parameterMode.getParameter2Mode());
        log.info("address " + parameter3 + " <= address " + parameter1 + " (" + a + ") + address " + parameter2 + " (" + b + ")");

        memory.set(parameter3, a + b);
    }),

    MULTIPLY(2, 3, (parameterMode, address, memory) -> {
        int parameter1 = memory.get(address.getAndIncrement());
        int parameter2 = memory.get(address.getAndIncrement());
        int parameter3 = memory.get(address.getAndIncrement());
        int a = get(parameter1, memory, parameterMode.getParameter1Mode());
        int b = get(parameter2, memory, parameterMode.getParameter2Mode());
        log.info("address " + parameter3 + " <= address " + parameter1 + " (" + a + ") * address " + parameter2 + " (" + b + ")");

        memory.set(parameter3, a * b);
    }),

    SCAN(3, 1, (parameterMode, address, memory) -> {
        int parameter1 = memory.get(address.getAndIncrement());
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Input a number: ");
            memory.set(parameter1, Integer.parseInt(bufferedReader.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }),

    PRINT(4, 1, (parameterMode, address, memory) -> {
        int parameter1 = memory.get(address.getAndIncrement());
        int a = get(parameter1, memory, parameterMode.getParameter1Mode());
        System.out.println("Output is " + a);
    }),

    JUMP_IF_TRUE(5, 2, (parameterMode, address, memory) -> {
        int parameter1 = memory.get(address.getAndIncrement());
        int parameter2 = memory.get(address.getAndIncrement());
        int a = get(parameter1, memory, parameterMode.getParameter1Mode());

        if (a != 0) {
            address.set(get(parameter2, memory, parameterMode.getParameter2Mode()));
        }
    }),

    JUMP_IF_FALSE(6, 2, (parameterMode, address, memory) -> {
        int parameter1 = memory.get(address.getAndIncrement());
        int parameter2 = memory.get(address.getAndIncrement());
        int a = get(parameter1, memory, parameterMode.getParameter1Mode());

        if (a == 0) {
            address.set(get(parameter2, memory, parameterMode.getParameter2Mode()));
        }
    }),

    LESS_THAN(7, 3, (parameterMode, address, memory) -> {
        int parameter1 = memory.get(address.getAndIncrement());
        int parameter2 = memory.get(address.getAndIncrement());
        int parameter3 = memory.get(address.getAndIncrement());
        int a = get(parameter1, memory, parameterMode.getParameter1Mode());
        int b = get(parameter2, memory, parameterMode.getParameter2Mode());

        memory.set(parameter3, a < b ? 1 : 0);
    }),

    EQUALS(8, 3, (parameterMode, address, memory) -> {
        int parameter1 = memory.get(address.getAndIncrement());
        int parameter2 = memory.get(address.getAndIncrement());
        int parameter3 = memory.get(address.getAndIncrement());
        int a = get(parameter1, memory, parameterMode.getParameter1Mode());
        int b = get(parameter2, memory, parameterMode.getParameter2Mode());

        memory.set(parameter3, a == b ? 1 : 0);
    }),

    HALT(99, 0, (parameterMode, address, memory) -> {
    });

    public int opCode;
    public int parameterCount;
    private TriConsumer<ParameterMode, AtomicInteger, List<Integer>> instruction;

    Instruction(int opCode, int parameterCount, TriConsumer<ParameterMode, AtomicInteger, List<Integer>> instruction) {
        this.opCode = opCode;
        this.instruction = instruction;
        this.parameterCount = parameterCount;
    }

    public static Instruction valueOf(int opCode) {
        Set<Integer> opCodes = Arrays.stream(Instruction.values())
                .map((instruction -> instruction.opCode))
                .collect(Collectors.toSet());

        return Arrays.stream(Instruction.values())
                .filter(instruction -> instruction.opCode == opCode)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Invalid opCode! Given: " + opCode + ", available: " + opCodes));
    }

    public void execute(ParameterMode parameterMode, AtomicInteger address, List<Integer> memory) {
        instruction.accept(parameterMode, address, memory);
    }

    private static int get(int parameter, List<Integer> memory, ParameterMode.Mode mode) {
        switch (mode) {
            case POSITION:
                return memory.get(parameter);
            case IMMEDIATE:
                return parameter;
            default:
                throw new RuntimeException("Unknown parameter mode!");
        }
    }
}
