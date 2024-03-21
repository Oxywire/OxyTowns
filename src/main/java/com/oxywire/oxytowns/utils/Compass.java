package com.oxywire.oxytowns.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Compass {

    public static Compass.Point getCompassPointForDirection(final double inDegrees) {
        double degrees = (inDegrees - 90) % 360;
        if (degrees < 0) degrees += 360;

        int index = (int) Math.floor((degrees + 22.5) / 45) % 8;
        return Compass.Point.values()[index];
    }

    public enum Point {
        W, NW, N, NE, E, SE, S, SW
    }
}
