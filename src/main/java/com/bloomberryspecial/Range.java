package com.bloomberryspecial;

import lombok.Data;
import lombok.Value;

@Value
public class Range {
    int minX;
    int maxX;
    int minY;
    int maxY;

    public int getRangeX() {
        return maxX - minX;
    }

    public int getRangeY() {
        return maxY - minY;
    }

    public double getFractionX(double value) {
        final double delta = value - minX;
        return delta / getRangeX();
    }

    public double getFractionY(double value) {
        final double delta = value - minY;
        return delta / getRangeY();
    }

    public Range withDataPoint(DataPoint dataPoint) {
        return new Range(
                Math.min(minX, dataPoint.getX()),
                Math.max(maxX, dataPoint.getX()),
                (int) Math.floor(Math.min(minY, dataPoint.getY())),
                (int) Math.ceil(Math.max(maxY, dataPoint.getY()))
        );
    }

    public Range withRange(Range oth) {
        return new Range(
                Math.min(getMinX(), oth.getMinX()),
                Math.max(getMaxX(), oth.getMaxX()),
                Math.min(getMinY(), oth.getMinY()),
                Math.max(getMaxY(), oth.getMaxY()));
    }

    public static final Range SMALLEST = new Range(
            Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE
    );
}
