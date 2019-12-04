package com.github.cc007.adventofcode.day3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class LineSectionTuple {
    private final LineSection first;
    private final LineSection second;

    public Intersection getIntersectionPoint(List<LineSection> wire1LineSections, List<LineSection> wire2LineSections) {
        return new Intersection(wire1LineSections.indexOf(getFirst()), wire2LineSections.indexOf(getSecond()));
    }

    public Intersection getLoopIntersection(List<LineSection> wireLineSections) {
        return new Intersection(wireLineSections.indexOf(getFirst()), wireLineSections.indexOf(getSecond()));
    }

    public Point getIntersectionPoint(){
        if(this.getFirst().isVertical()) {
            return new Point(this.getFirst().getAsymptote(), this.getSecond().getAsymptote());
        } else {
            return new Point(this.getSecond().getAsymptote(), this.getFirst().getAsymptote());
        }
    }

    @Override
    public String toString() {
        return getIntersectionPoint().toString();
    }
}
