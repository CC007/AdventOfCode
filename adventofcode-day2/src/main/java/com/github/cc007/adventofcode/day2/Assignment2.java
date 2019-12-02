package com.github.cc007.adventofcode.day2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class Assignment2 {
    public static void main(String[] args) throws IOException {
        try (InputStream is = Assignment1.class.getResourceAsStream("/input.txt");
             Reader reader = new InputStreamReader(is);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String memoryString = bufferedReader.readLine();

            System.out.println("100 * noun + verb = " + findNounAndVerb(memoryString));

        }
    }

    private static int findNounAndVerb(String memoryString) {
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                Computer computer = new Computer(memoryString);
                int finalNoun = noun;
                int finalVerb = verb;
                computer.fixMemory((memory) -> Assignment2.customFix(memory, finalNoun, finalVerb));
                computer.run();
                System.out.println("Final intCode: " + computer);
                String result = computer.toString().split(",")[0];
                System.out.println("Final result: " + result);
                if (result.equals("19690720")) {
                    return 100 * noun + verb;
                }
            }
        }
        return -1;
    }

    private static void customFix(List<Integer> memory, int noun, int verb) {
        memory.set(1, noun);
        memory.set(2, verb);
    }
}
