package com.bloomberryspecial;

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
}
