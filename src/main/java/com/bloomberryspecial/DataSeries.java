package com.bloomberryspecial;

import lombok.Value;

import java.awt.*;
import java.util.List;

@Value
public class DataSeries {
    String name;
    List<DataPoint> data;
    Color color;
    Stroke stroke;
    DrawStyle drawStyle;
}
