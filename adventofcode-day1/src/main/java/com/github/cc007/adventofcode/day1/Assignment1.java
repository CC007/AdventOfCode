package com.github.cc007.adventofcode.day1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Assignment1 {
    public static void main(String[] args) throws IOException {
        try (InputStream is = Assignment1.class.getResourceAsStream("/input.txt");
             Reader reader = new InputStreamReader(is);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            int totalFuelRequirements = 0;

            for (Integer mass = getNextMass(bufferedReader);
                 mass != null;
                 mass = getNextMass(bufferedReader)) {
                int moduleFuelRequirements = getFuelRequirements(mass);

                System.out.println("Required fuel for mass " + mass + ": " + moduleFuelRequirements);
                totalFuelRequirements += moduleFuelRequirements;
            }
            System.out.println("Total required fuel: " + totalFuelRequirements);
        }
    }

    private static Integer getNextMass(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return null;
        }
        return Integer.parseInt(line);
    }

    private static int getFuelRequirements(int mass) {
        return mass / 3 - 2;
    }
}
