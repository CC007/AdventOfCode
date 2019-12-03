package com.github.cc007.adventofcode.day3;

import lombok.RequiredArgsConstructor;

import java.util.function.BiFunction;

@RequiredArgsConstructor
public enum TraceDirection {

    UP('U', ((origin, distance) -> {
        Point p2 = new Point(origin.getX(), origin.getY() - distance);
        return new LineSection(origin, p2);
    })),
    DOWN('D', (origin, distance) -> {
        Point p2 = new Point(origin.getX(), origin.getY() + distance);
        return new LineSection(origin, p2);
    }),
    LEFT('L', (origin, distance) -> {
        Point p2 = new Point(origin.getX() - distance, origin.getY());
        return new LineSection(origin, p2);
    }),
    RIGHT('R', (origin, distance) -> {
        Point p2 = new Point(origin.getX() + distance, origin.getY());
        return new LineSection(origin, p2);
    });

    final char directionChar;
    private final BiFunction<Point, Integer, LineSection> lineSectionParser;

    public static TraceDirection valueOf(char directionChar) {
        if (directionChar == TraceDirection.UP.directionChar) {
            return TraceDirection.UP;
        } else if (directionChar == TraceDirection.DOWN.directionChar) {
            return TraceDirection.DOWN;
        } else if (directionChar == TraceDirection.LEFT.directionChar) {
            return TraceDirection.LEFT;
        } else if (directionChar == TraceDirection.RIGHT.directionChar) {
            return TraceDirection.RIGHT;
        }
        throw new RuntimeException("Unknown trace direction! Use either U, D, L or R.");
    }

    public LineSection getLineSection(Point origin, int distance) {
        return lineSectionParser.apply(origin, distance);
    }
}
