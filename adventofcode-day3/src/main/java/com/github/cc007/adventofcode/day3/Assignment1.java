package com.github.cc007.adventofcode.day3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
            List<LineSection> wire1LineSections = getLineSections(wire1Trace);
            List<LineSection> wire2LineSections = getLineSections(wire2Trace);
            System.out.println(wire1LineSections);
            System.out.println(wire2LineSections);
            List<Point> intersections = getIntersections(wire1LineSections, wire2LineSections);
            System.out.println(intersections);
            System.out.println(getLowestManhattenDistance(intersections));
        }
    }

    private static List<LineSection> getLineSections(String trace) {
        return Arrays.stream(trace.split(","))
                .reduce(new LinkedList<>(), (lineSections, traceSegment) -> {
                    TraceDirection direction = TraceDirection.valueOf(traceSegment.charAt(0));
                    int distance = Integer.parseInt(traceSegment.substring(1));
                    Point p1 = lineSections.isEmpty() ? new Point(0, 0) : lineSections.getLast().getP2();
                    lineSections.add(direction.getLineSection(p1, distance));
                    return lineSections;
                }, ((lineSections1, lineSections2) -> {
                    lineSections1.addAll(lineSections2);
                    return lineSections1;
                }));
    }

    private static List<Point> getIntersections(List<LineSection> wire1LineSections, List<LineSection> wire2LineSections) {
        List<Point> intersections = new ArrayList<>();
        for (LineSection wire1LineSection : wire1LineSections) {
            for (LineSection wire2LineSection : wire2LineSections) {
                LineSection.getIntersection(wire1LineSection, wire2LineSection)
                        .ifPresent(intersections::add);
            }
        }
        return intersections;
    }

    private static int getLowestManhattenDistance(List<Point> intersections) {
        return intersections.stream()
                .map((point -> Math.abs(point.getX()) + Math.abs(point.getY())))
                .reduce(Integer.MAX_VALUE, Math::min);
    }
}
