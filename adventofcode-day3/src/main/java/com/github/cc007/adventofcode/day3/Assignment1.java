package com.github.cc007.adventofcode.day3;

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
            String wire1Trace = bufferedReader.readLine();
            String wire2Trace = bufferedReader.readLine();
//            String wire1Trace = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51";
//            String wire2Trace = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";
            List<LineSection> wire1LineSections = LineSection.fromTrace(wire1Trace);
            List<LineSection> wire2LineSections = LineSection.fromTrace(wire2Trace);
            System.out.println(wire1LineSections);
            System.out.println(wire2LineSections);
            List<Point> intersections = LineSection.getIntersections(wire1LineSections, wire2LineSections);
            System.out.println(intersections);
            System.out.println(getLowestManhattenDistance(intersections));
        }
    }

    private static int getLowestManhattenDistance(List<Point> intersections) {
        return intersections.stream()
                .map((point -> Math.abs(point.getX()) + Math.abs(point.getY())))
                .reduce(Integer.MAX_VALUE, Math::min);
    }
}
