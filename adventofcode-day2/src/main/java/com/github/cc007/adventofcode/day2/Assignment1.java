package com.github.cc007.adventofcode.day2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class Assignment1 {
    public static void main(String[] args) throws IOException {
        try (InputStream is = Assignment1.class.getResourceAsStream("/input.txt");
             Reader reader = new InputStreamReader(is);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String memoryString = bufferedReader.readLine();
            //String memoryString = "1,0,0,0,99";
            //String memoryString = "1,9,10,3,2,3,11,0,99,30,40,50";
            Computer computer = new Computer(memoryString);
            computer.fixMemory(Assignment1::twelveOhTwoProgramAlarmFix);
            computer.run();
            System.out.println("Final intCode: " + computer);
            System.out.println("Final result: " + computer.toString().split(",")[0]);
        }
    }

    private static void twelveOhTwoProgramAlarmFix(List<Integer> memory) {
        memory.set(1, 12);
        memory.set(2, 2);
    }
}
