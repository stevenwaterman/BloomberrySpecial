package com.bloomberryspecial;

import lombok.Value;

@Value
public class DataPoint {
    Integer x;
    Double y;

    public DataPoint withX(Integer newX) {
        return new DataPoint(newX, y);
    }

    public DataPoint withY(Double newY) {
        return new DataPoint(x, newY);
    }
}
