package com.github.cc007.adventofcode.day2;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Slf4j
public class Assignment1 {
    public static void main(String[] args) throws IOException {
        try (InputStream is = Assignment1.class.getResourceAsStream("/input.txt");
             Reader reader = new InputStreamReader(is);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String memoryString = bufferedReader.readLine();
            //String memoryString = "3,0,4,0,99";
            Computer computer = new Computer(memoryString);
            computer.run();
            log.info("Final intCode: " + computer);
            log.info("Final result: " + computer.toString().split(",")[0]);
        }
    }
}
