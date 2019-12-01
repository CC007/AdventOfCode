package com.github.cc007.adventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Day1Assignment2 {

    public static void main(String[] args) throws IOException {
        try (InputStream is = Day1Assignment2.class.getResourceAsStream("/input.txt");
             Reader reader = new InputStreamReader(is);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            int totalFuelRequirements = 0;

            for (Integer mass = getNextMass(bufferedReader);
                 mass != null;
                 mass = getNextMass(bufferedReader)) {
                int moduleFuelRequirements = getFuelRequirements(mass);
                int totalModuleFuelRequirements = getTotalModuleFuelRequirements(moduleFuelRequirements);
                System.out.println("Required fuel for mass " + mass + ": " + totalModuleFuelRequirements);
                System.out.println();
                totalFuelRequirements += totalModuleFuelRequirements;
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

    private static int getTotalModuleFuelRequirements(int fuelMass){
        int totalModuleFuelRequirements = fuelMass;
        System.out.print(fuelMass);
        for (int currentExtraFuelModule = getFuelRequirements(fuelMass);
             currentExtraFuelModule >= 0;
             currentExtraFuelModule = getFuelRequirements(currentExtraFuelModule)) {
            System.out.print(" + " + currentExtraFuelModule);
            totalModuleFuelRequirements += currentExtraFuelModule;
        }
        System.out.println();
        return totalModuleFuelRequirements;
    }
}
