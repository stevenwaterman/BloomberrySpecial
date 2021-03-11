package com.bloomberryspecial;

import lombok.Value;

@Value
public class Range {
    int min;
    int max;

    public int getRange() {
        return max - min;
    }

    public double getFraction(int value) {
        int delta = value - min;
        return (double) delta / getRange();
    }
}
