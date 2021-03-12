package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataPoint;
import com.bloomberryspecial.DataSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NormalisePercentage extends UnaryTransformer {
    @Override
    protected DataSeries internalTransform(DataSeries line) {
        double initialY = line.getData().get(0).getY();
        List<DataPoint> output = line.getData().stream().map(datapoint -> datapoint.withY(100 * datapoint.getY() / initialY)).collect(Collectors.toList());
        return new DataSeries(line.getName() + " -> normalise percentage", output, line.getColor(), line.getStroke(), line.getDrawStyle());
    }
}
