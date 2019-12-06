package com.github.cc007.adventofcode.day3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Point {
    private final int x;
    private final int y;

    public int getManhattanDistance(Point that) {
        return Math.abs(this.getX() - that.getX()) + Math.abs(this.getY() - that.getY());
    }

    public int getManhattanDistance() {
        return Math.abs(this.getX()) + Math.abs(this.getY());
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
