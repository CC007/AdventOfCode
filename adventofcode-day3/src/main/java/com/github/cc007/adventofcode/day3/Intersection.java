package com.github.cc007.adventofcode.day3;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Intersection {
    private final int start;
    private final int end;

    @Override
    public String toString() {
        return getStart() + " -x-> " + getEnd();
    }
}
