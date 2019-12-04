package com.github.cc007.adventofcode.day1;

import java.io.IOException;

public class Assignment1 {
    public static void main(String[] args) throws IOException {
        int lowerBound = 234208;
        int upperBound = 765869;

        int counter = 0;

        for (int i = lowerBound; i <= upperBound; i++) {
            if(containsAdjacentDigits(i) && hasIncreasingDigits(i)) {
                counter++;
            }
        }
        System.out.println("Number of passwords with criteria: " + counter);
    }

    private static boolean containsAdjacentDigits(int number) {
        Integer prev = null;
        for (; number > 0; number /= 10) {
            int curr = number % 10;
            if (prev != null && prev == curr) {
                return true;
            }
            prev = curr;
        }
        return false;
    }

    private static boolean hasIncreasingDigits(int number) {
        Integer prev = null;
        for (; number > 0; number /= 10) {
            int curr = number % 10;
            if (prev != null && prev < curr) {
                return false;
            }
            prev = curr;
        }
        return true;
    }
}
