package com.github.cc007.adventofcode.day3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Assignment2 {

    public static void main(String[] args) throws IOException {
        try (InputStream is = Assignment1.class.getResourceAsStream("/input.txt");
             Reader reader = new InputStreamReader(is);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
//            String wire1Trace = bufferedReader.readLine();
//            String wire2Trace = bufferedReader.readLine();
            String wire1Trace = "R8,U5,L5,D3";
            String wire2Trace = "U7,R6,D4,L4";
//            String wire1Trace = "R75,D30,R83,U83,L12,D49,R71,U7,L72";
//            String wire2Trace = "U62,R66,U55,R34,D71,R55,D58,R83";
//            String wire1Trace = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51";
//            String wire2Trace = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";
            List<LineSection> wire1LineSections = LineSection.fromTrace(wire1Trace);
            List<LineSection> wire2LineSections = LineSection.fromTrace(wire2Trace);
            System.out.println(wire1LineSections);
            System.out.println(wire2LineSections);
            List<LineSectionTuple> intersectionLineSections = LineSection.getIntersectionLineSections(wire1LineSections, wire2LineSections);
            System.out.println(intersectionLineSections);
            System.out.println("Fewest combined steps: " + getFewestCombinedSteps(intersectionLineSections, wire1LineSections, wire2LineSections));
        }
    }

    private static int getFewestCombinedSteps(List<LineSectionTuple> intersectionLineSections, List<LineSection> wire1LineSections, List<LineSection> wire2LineSections) {
        return intersectionLineSections.stream()
                .map(intersection -> getCombinedSteps(intersection, wire1LineSections, wire2LineSections))
                .reduce(Math::min)
                .orElseThrow(RuntimeException::new);
    }

    private static int getCombinedSteps(LineSectionTuple intersectionLineSection, List<LineSection> wire1LineSections, List<LineSection> wire2LineSections) {
        Point intersectionPoint = intersectionLineSection.getIntersection();
        Intersection intersection = intersectionLineSection.getIntersection(wire1LineSections, wire2LineSections);
        System.out.println("Intersection point: " + intersectionPoint);
        System.out.println("Intersection indices: " + intersection);

        List<LineSectionTuple> wire1Loops = LineSection.getIntersectionLineSections(wire1LineSections, wire1LineSections);
        int wire1Steps = getSteps(intersection.getStart(), wire1Loops, wire1LineSections, intersectionPoint, 0, "");

        List<LineSectionTuple> wire2Loops = LineSection.getIntersectionLineSections(wire2LineSections, wire2LineSections);
        int wire2Steps = getSteps(intersection.getEnd(), wire2Loops, wire2LineSections, intersectionPoint, 0, "");

        System.out.println("Wire 1 steps (index " + intersection.getStart() + "): " + wire1Steps);
        System.out.println("Wire 2 steps (index " + intersection.getEnd() + "): " + wire2Steps);
        System.out.println("Combined steps (" + intersectionPoint + "): " + (wire1Steps + wire2Steps));
        return wire1Steps + wire2Steps;
    }

    private static int getSteps(int intersectionIndex, List<LineSectionTuple> wireLoops, List<LineSection> wireLineSections, Point intersectionPoint, int index, String indent) {
        List<Intersection> wireIntersections = wireLoops.stream()
                .map((wireLoop) -> wireLoop.getLoopIntersection(wireLineSections))
                .filter(shortcut -> shortcut.getStart() < shortcut.getEnd())
                .collect(Collectors.toList());
        if (index == 0) {
            printWireLoops(wireLoops, wireIntersections);
        }
        if (index >= intersectionIndex) {
            int steps = wireLineSections.get(intersectionIndex).getP1().getManhattanDistance(intersectionPoint);
            System.out.println(indent + "| " + index + ": steps = " + steps);
            System.out.println(indent + "+-Subtotal: " + steps);
            return steps;
        }
        Optional<Intersection> loopIntersection = wireIntersections.stream()
                .filter(wire1Intersection -> wire1Intersection.getStart() == index)
                .filter(wire1Intersection -> wire1Intersection.getEnd() < intersectionIndex)
                .findAny();

        System.out.println(indent + "| " + index + ": steps += " + wireLineSections.get(index).getLength());
        int totalSteps = wireLineSections.get(index).getLength() + getSteps(intersectionIndex, wireLoops, wireLineSections, intersectionPoint, index + 1, indent + "| ");

        //System.out.println(index + ": steps += " + wireLineSections.get(index).getLength() + " (" + totalSteps + ")");

        System.out.println(indent + "+-Subtotal: " + totalSteps);

        if (loopIntersection.isPresent()) {
            LineSection lineSectionStart = wireLineSections.get(loopIntersection.get().getStart());
            LineSection lineSectionEnd = wireLineSections.get(loopIntersection.get().getEnd());
            int shortcutSteps = lineSectionStart.getP1().getManhattanDistance(lineSectionEnd.getP2());
            System.out.println(indent + "| OR steps += " + shortcutSteps + " (using " + loopIntersection.get() + ")");
            int totalStepsIfShortcut = shortcutSteps + getSteps(intersectionIndex, wireLoops, wireLineSections, intersectionPoint, loopIntersection.get().getEnd() + 1, indent + "| ");
            totalSteps = Math.min(totalSteps, totalStepsIfShortcut);

            System.out.println(indent + "+-Subtotal: " + totalStepsIfShortcut);
        }
        return totalSteps;
    }

    private static void printWireLoops(List<LineSectionTuple> wireLoops, List<Intersection> wireIntersections) {
        System.out.println(wireLoops);
        System.out.println("[");
        for (Intersection wire1Intersection : wireIntersections) {
            System.out.println(" " + wire1Intersection);
        }
        System.out.println("]");
    }

}
