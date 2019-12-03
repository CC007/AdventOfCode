package com.github.cc007.adventofcode.day3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Point {
    private final int x;
    private final int y;

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
