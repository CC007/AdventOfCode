package com.github.cc007.adventofcode.day3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class LineSection {
    private final Point p1;
    private final Point p2;

    public static List<LineSection> fromTrace(String trace) {
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

    public static List<Point> getIntersectionPoints(List<LineSection> wire1LineSections, List<LineSection> wire2LineSections) {
        List<LineSectionTuple> intersectionLineSections = getIntersectionLineSections(wire1LineSections, wire2LineSections);
        return intersectionLineSections.stream().map(LineSectionTuple::getIntersection).collect(Collectors.toList());
    }

    public static List<LineSectionTuple> getIntersectionLineSections(List<LineSection> wire1LineSections, List<LineSection> wire2LineSections) {
        List<LineSectionTuple> intersections = new ArrayList<>();
        for (LineSection wire1LineSection : wire1LineSections) {
            for (LineSection wire2LineSection : wire2LineSections) {
                LineSection.getIntersectionLineSections(wire1LineSection, wire2LineSection)
                        .ifPresent(intersections::add);
            }
        }
        return intersections;
    }

    public static Optional<Point> getIntersectionPoint(LineSection lineSection1, LineSection lineSection2) {
        Optional<LineSectionTuple> intersectionLineSections = getIntersectionLineSections(lineSection1, lineSection2);
        return intersectionLineSections.map(LineSectionTuple::getIntersection);
    }

    public static Optional<LineSectionTuple> getIntersectionLineSections(LineSection lineSection1, LineSection lineSection2) {
        if (lineSection1.isVertical() && lineSection2.isVertical()
                || lineSection1.isHorizontal() && lineSection2.isHorizontal()) {
            //System.out.println(lineSection1 + " and " + lineSection2 + " are parallel");
            return Optional.empty();
        }
        if (!lineSection1.isWithinBounds(lineSection2.getAsymptote())
                || !lineSection2.isWithinBounds(lineSection1.getAsymptote())) {
            //System.out.println(lineSection1 + " and " + lineSection2 + " are not intersecting");
            return Optional.empty();
        }
        //System.out.println("Found intersection: " + lineSection1 + " and " + lineSection2);
        return Optional.of(new LineSectionTuple(lineSection1, lineSection2));
    }

    public boolean isWithinBounds(int asymptote) {
        return asymptote > this.getLowerBound() && asymptote < this.getUpperBound();
    }

    public boolean isVertical() {
        return p1.getX() == p2.getX();
    }

    public boolean isHorizontal() {
        return !isVertical();
    }

    public int getAsymptote() {
        return isVertical() ? p1.getX() : p1.getY();
    }

    public int getLowerBound() {
        return isVertical() ? Math.min(p1.getY(), p2.getY()) : Math.min(p1.getX(), p2.getX());
    }

    public int getUpperBound() {
        return isVertical() ? Math.max(p1.getY(), p2.getY()) : Math.max(p1.getX(), p2.getX());
    }

    public int getLength() {
        return isVertical() ? Math.abs(p1.getY() - p2.getY()) : Math.abs(p1.getX() - p2.getX());
    }

    @Override
    public String toString() {
        return p1 + " -> " + p2;
    }
}
