package com.github.cc007.adventofcode.day3;

import lombok.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Assignment2 {

    public static void main(String[] args) throws IOException {
        try (InputStream is = Assignment1.class.getResourceAsStream("/input.txt");
             Reader reader = new InputStreamReader(is);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
//            String wire1Trace = bufferedReader.readLine();
//            String wire2Trace = bufferedReader.readLine();
//            String wire1Trace = "R8,U5,L5,D3";
//            String wire2Trace = "U7,R6,D4,L4";
            String wire1Trace = "R75,D30,R83,U83,L12,D49,R71,U7,L72";
            String wire2Trace = "U62,R66,U55,R34,D71,R55,D58,R83";
//            String wire1Trace = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51";
//            String wire2Trace = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";
            List<LineSection> wire1LineSections = LineSection.fromTrace(wire1Trace);
            List<LineSection> wire2LineSections = LineSection.fromTrace(wire2Trace);


            List<LineSectionTuple> intersectionLineSections = LineSection.getIntersectionLineSections(wire1LineSections, wire2LineSections);
            List<LineSectionTuple> wire1LoopLineSections = LineSection.getIntersectionLineSections(wire1LineSections, wire1LineSections);
            List<LineSectionTuple> wire2LoopLineSections = LineSection.getIntersectionLineSections(wire2LineSections, wire2LineSections);


            System.out.println(wire1LineSections);
            System.out.println(wire2LineSections);
            System.out.println("Fewest combined steps: " + getFewestCombinedSteps(wire1LineSections, wire2LineSections));
        }
    }

    private static Set<LineSection> buildLineSectionGraph(@NonNull Point startingPoint, LineSection previousLineSection, Collection<LineSection> wireLineSections, Collection<LineSection> otherWireLineSections) {
        LineSection startingLineSection = wireLineSections.stream()
                .filter(wireLineSection -> startingPoint.equals(wireLineSection.getP1()) || startingPoint.equals(wireLineSection.getP2()))
                .filter(wireLineSection -> previousLineSection == null || !(wireLineSection.equals(previousLineSection) || wireLineSection.equals(previousLineSection.getFlipped())))
                .map(wireLineSection -> startingPoint.equals(wireLineSection.getP2()) ? wireLineSection.getFlipped() : wireLineSection)
                .findAny()
                .orElseThrow(() -> new RuntimeException("No starting wire found"));

        return buildLineSectionGraph(startingLineSection, wireLineSections, otherWireLineSections);
    }

    private static Set<LineSection> buildLineSectionGraph(LineSection startingLineSection, Collection<LineSection> wireLineSections, Collection<LineSection> otherWireLineSections) {
        Set<LineSection> lineSectionGraph = new HashSet<>();

        Optional<LineSectionTuple> firstIntersectionLineSection = LineSection.getIntersectionLineSections(Collections.singleton(startingLineSection), otherWireLineSections).stream()
                .min(Comparator.comparingInt(intersectionLineSection -> startingLineSection.getP1().getManhattanDistance(intersectionLineSection.getIntersectionPoint())));

        Optional<LineSectionTuple> firstLoopIntersectionLineSection = LineSection.getIntersectionLineSections(Collections.singleton(startingLineSection), wireLineSections).stream()
                .min(Comparator.comparingInt(intersectionLineSection -> startingLineSection.getP1().getManhattanDistance(intersectionLineSection.getIntersectionPoint())));

        if(!firstIntersectionLineSection.isPresent() && !firstLoopIntersectionLineSection.isPresent()){
            lineSectionGraph.add(startingLineSection);
            lineSectionGraph.addAll(buildLineSectionGraph(startingLineSection.getP2(), startingLineSection, wireLineSections, otherWireLineSections));
            return lineSectionGraph;
        }

        if(firstIntersectionLineSection.isPresent()){
            LineSectionTuple intersectionLineSection = firstIntersectionLineSection.get();
            LineSection lineBeforeIntersection = new LineSection(startingLineSection.getP1(), intersectionLineSection.getIntersectionPoint());
            LineSection lineAfterIntersection = new LineSection(startingLineSection.getP1(), intersectionLineSection.getIntersectionPoint());
            lineSectionGraph.add(lineBeforeIntersection);
            lineSectionGraph.addAll(buildLineSectionGraph(lineAfterIntersection, wireLineSections, otherWireLineSections));
            return lineSectionGraph;
        }

        return lineSectionGraph;
    }

    private static int getFewestCombinedSteps(List<LineSection> wire1LineSections, List<LineSection> wire2LineSections) {
        List<LineSectionTuple> intersectionLineSections = LineSection.getIntersectionLineSections(wire1LineSections, wire2LineSections);
        System.out.println(intersectionLineSections);
        return intersectionLineSections.stream()
                .map(intersection -> getCombinedSteps(intersection, wire1LineSections, wire2LineSections))
                .reduce(Math::min)
                .orElseThrow(RuntimeException::new);
    }

    private static int getCombinedSteps(LineSectionTuple intersectionLineSection, List<LineSection> wire1LineSections, List<LineSection> wire2LineSections) {
        Point intersectionPoint = intersectionLineSection.getIntersectionPoint();
        Intersection intersection = intersectionLineSection.getIntersectionPoint(wire1LineSections, wire2LineSections);
        System.out.println("Intersection point: " + intersectionPoint);
        System.out.println("Intersection indices: " + intersection);

        List<LineSectionTuple> wire1Loops = LineSection.getIntersectionLineSections(wire1LineSections, wire1LineSections);
        int wire1Steps = getSteps(intersection.getStart(), wire1Loops, new Stack<>(), wire1LineSections, intersectionPoint, 0, "");

        List<LineSectionTuple> wire2Loops = LineSection.getIntersectionLineSections(wire2LineSections, wire2LineSections);
        int wire2Steps = getSteps(intersection.getEnd(), wire2Loops, new Stack<>(), wire2LineSections, intersectionPoint, 0, "");

        System.out.println("Wire 1 steps (index " + intersection.getStart() + "): " + wire1Steps);
        System.out.println("Wire 2 steps (index " + intersection.getEnd() + "): " + wire2Steps);
        System.out.println("Combined steps (" + intersectionPoint + "): " + (wire1Steps + wire2Steps));
        return wire1Steps + wire2Steps;
    }

    private static int getSteps(int intersectionIndex, List<LineSectionTuple> wireLoops, Stack<Intersection> visitedLoopIntersections, List<LineSection> wireLineSections, Point intersectionPoint, int index, String indent) {

        List<Intersection> wireIntersections = getLoopIntersections(wireLoops, visitedLoopIntersections, wireLineSections);

        if (index == 0) {
            System.out.println(wireLoops);
            printWireIntersections(wireIntersections);
        }

        if (index > intersectionIndex) {
            if (wireIntersections.stream().noneMatch(wireIntersection -> index <= wireIntersection.getStart())) {
                int steps = Integer.MAX_VALUE;
                System.out.println(indent + "| " + index + ": steps = " + steps + " (No result found)");
                System.out.println(indent + "+-Subtotal: " + steps);
                return steps;
            }
        }

        if (index == intersectionIndex) {
            int steps = wireLineSections.get(intersectionIndex).getP1().getManhattanDistance(intersectionPoint);
            System.out.println(indent + "| " + index + ": steps = " + steps);
            System.out.println(indent + "+-Subtotal: " + steps);
            return steps;
        }

        System.out.println(indent + "| " + index + ": steps += " + wireLineSections.get(index).getLength());
        int otherSteps = getSteps(intersectionIndex, wireLoops, visitedLoopIntersections, wireLineSections, intersectionPoint, index + 1, indent + "| ");
        int totalSteps = otherSteps == Integer.MAX_VALUE ? Integer.MAX_VALUE : wireLineSections.get(index).getLength() + otherSteps;

        System.out.println(indent + "+-Subtotal: " + totalSteps);

        totalSteps = takeShortcutIfPossible(index, wireIntersections, intersectionIndex, wireLoops, visitedLoopIntersections, wireLineSections, intersectionPoint, indent, totalSteps);
        return totalSteps;
    }


    private static int takeShortcutIfPossible(int index, List<Intersection> wireIntersections, int intersectionIndex, List<LineSectionTuple> wireLoops, Stack<Intersection> visitedLoopIntersections, List<LineSection> wireLineSections, Point intersectionPoint, String indent, int totalSteps) {
        Optional<Intersection> optionalLoopIntersection = getIntersectionAtCurrentIndex(index, wireIntersections, loopIntersection -> loopIntersection.getEnd() < intersectionIndex);
        if (optionalLoopIntersection.isPresent()) {
            Intersection loopIntersection = optionalLoopIntersection.get();
            LineSection lineSectionStart = wireLineSections.get(loopIntersection.getStart());
            LineSection lineSectionEnd = wireLineSections.get(loopIntersection.getEnd());

            int shortcutSteps;
            if (loopIntersection.getEnd() == intersectionIndex) {
                shortcutSteps = lineSectionStart.getP1().getManhattanDistance(intersectionPoint);
            } else {
                shortcutSteps = lineSectionStart.getP1().getManhattanDistance(lineSectionEnd.getP2());
            }
            System.out.println(indent + "| OR steps += " + shortcutSteps + " (using " + loopIntersection + ")");
            visitedLoopIntersections.push(loopIntersection);
            int otherStepsIfShortcut = getSteps(intersectionIndex, wireLoops, visitedLoopIntersections, wireLineSections, intersectionPoint, loopIntersection.getEnd() + 1, indent + "| ");
            int totalStepsIfShortcut = otherStepsIfShortcut == Integer.MAX_VALUE ? Integer.MAX_VALUE : shortcutSteps + otherStepsIfShortcut;
            visitedLoopIntersections.pop();
            totalSteps = Math.min(totalSteps, totalStepsIfShortcut);

            System.out.println(indent + "+-Subtotal: " + totalStepsIfShortcut);
        }
        return totalSteps;
    }

    private static List<Intersection> getLoopIntersections(Collection<LineSectionTuple> wireLoops, Stack<Intersection> visitedLoopIntersections, List<LineSection> wireLineSections) {
        return wireLoops.stream()
                .map((wireLoop) -> wireLoop.getLoopIntersection(wireLineSections))
                .filter(loopIntersection -> !visitedLoopIntersections.contains(loopIntersection))
                .collect(Collectors.toList());
    }

    private static Optional<Intersection> getIntersectionAtCurrentIndex(int index, Collection<Intersection> wireIntersections, Predicate<Intersection> intersectionPredicate) {
        return wireIntersections.stream()
                .filter(loopIntersection -> loopIntersection.getStart() == index)
                //.filter(intersectionPredicate)
                .findAny();
    }

    private static void printWireIntersections(Collection<Intersection> wireIntersections) {
        System.out.println("[");
        for (Intersection wire1Intersection : wireIntersections) {
            System.out.println(" " + wire1Intersection);
        }
        System.out.println("]");
    }

}
