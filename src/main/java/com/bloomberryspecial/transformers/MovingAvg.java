package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataPoint;
import com.bloomberryspecial.DataSeries;
import com.bloomberryspecial.DrawStyle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MovingAvg extends UnaryTransformer {
    private final int windowSize;

    public MovingAvg(int windowSize) {
        super();
        this.windowSize = windowSize;
    }

    @Override
    protected DataSeries internalTransform(DataSeries line) {
        double sum = 0;
        long count = 0;

        final List<DataPoint> data = line.getData();
        final List<DataPoint> output = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            final DataPoint datapoint = data.get(i);
            sum += datapoint.getY();
            count++;
            if (count > windowSize) {
                count--;
                sum -= data.get(i - windowSize).getY();
            }
            output.add(datapoint.withY(sum / count));
        }

        return new DataSeries(
                line.getName() + " -> " + windowSize + "-point moving avg",
                output,
                line.getColor(),
                line.getStroke(),
                line.getDrawStyle()
        );
    }
}
