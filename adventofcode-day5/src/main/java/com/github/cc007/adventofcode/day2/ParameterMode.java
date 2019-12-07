package com.github.cc007.adventofcode.day2;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ParameterMode {

    public ParameterMode(int parameterModeValue) {
        this.parameter1Mode = Mode.valueOf(parameterModeValue % 10);
        parameterModeValue /= 10;
        this.parameter2Mode = Mode.valueOf(parameterModeValue % 10);
        parameterModeValue /= 10;
        this.parameter3Mode = Mode.valueOf(parameterModeValue % 10);
    }

    private final Mode parameter1Mode;
    private final Mode parameter2Mode;
    private final Mode parameter3Mode;

    public enum Mode {
        POSITION(0),
        IMMEDIATE(1);

        public int parameterModeValue;

        Mode(int parameterModeValue) {
            this.parameterModeValue = parameterModeValue;
        }

        public static Mode valueOf(int parameterModeValue) {
            Set<Integer> parameterModeValues = Arrays.stream(Mode.values())
                    .map((mode -> mode.parameterModeValue))
                    .collect(Collectors.toSet());

            return Arrays.stream(Mode.values())
                    .filter(mode -> mode.parameterModeValue == parameterModeValue)
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Invalid parameterModeValue! Given: " + parameterModeValue + ", available: " + parameterModeValues));
        }
    }
}
