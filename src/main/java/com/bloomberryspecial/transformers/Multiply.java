package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataPoint;
import com.bloomberryspecial.DataSeries;

import java.util.List;
import java.util.stream.Collectors;

public class Multiply extends UnaryTransformer {
    private final double multiplicand;

    public Multiply(double multiplicand) {
        this.multiplicand = multiplicand;
    }

    @Override
    protected DataSeries internalTransform(DataSeries line) {
        List<DataPoint> output = line.getData().stream().map(datapoint -> datapoint.withY(datapoint.getY() * multiplicand)).collect(Collectors.toList());
        return new DataSeries(line.getName() + " * " + multiplicand, output, line.getColor(), line.getStroke(), line.getDrawStyle());
    }
}
