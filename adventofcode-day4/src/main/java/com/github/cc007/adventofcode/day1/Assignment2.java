package com.github.cc007.adventofcode.day1;

import java.io.IOException;

public class Assignment2 {
    public static void main(String[] args) throws IOException {
        int lowerBound = 234208;
        int upperBound = 765869;

        int counter = 0;

        System.out.println(containsAdjacentDigits(112233));
        System.out.println(containsAdjacentDigits(123444));
        System.out.println(containsAdjacentDigits(111122));
        System.out.println(containsAdjacentDigits(111123));
        System.out.println(containsAdjacentDigits(112222));

        for (int i = lowerBound; i <= upperBound; i++) {
            if (containsAdjacentDigits(i) && hasIncreasingDigits(i)) {
                counter++;
            }
        }
        System.out.println("Number of passwords with criteria: " + counter);
    }

    private static boolean containsAdjacentDigits(int number) {
        Integer prev = null;
        boolean candidateFound = false;
        boolean sameNumber = false;
        for (; number > 0; number /= 10) {
            int curr = number % 10;
            if (prev != null)
                if (!sameNumber) {
                    if (prev == curr) {
                        candidateFound = true;
                        sameNumber = true;
                    }
                } else if (candidateFound) {
                    if (prev == curr) {
                        candidateFound = false;
                    } else {
                        return true;
                    }
                } else if (prev != curr) {
                    sameNumber = false;
                }
            prev = curr;
        }
        return candidateFound;
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
