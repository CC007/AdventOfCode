package com.github.cc007.adventofcode.day2;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@Slf4j
public class Assignment2 {
    public static void main(String[] args) throws IOException {
        try (InputStream is = Assignment1.class.getResourceAsStream("/input.txt");
             Reader reader = new InputStreamReader(is);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String memoryString = bufferedReader.readLine();
            //String memoryString = "3,9,8,9,10,9,4,9,99,-1,8";
            //String memoryString = "3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9";
            //String memoryString = "3,3,1105,-1,9,1101,0,0,12,4,12,99,1";
            //String memoryString = "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99";
            Computer computer = new Computer(memoryString);
            computer.run();
            log.info("Final intCode: " + computer);
            log.info("Final result: " + computer.toString().split(",")[0]);
        }
    }
}