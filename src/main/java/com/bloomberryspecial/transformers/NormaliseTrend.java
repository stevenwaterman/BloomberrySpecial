package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataPoint;
import com.bloomberryspecial.DataSeries;
import com.bloomberryspecial.DrawStyle;
import com.google.common.collect.Lists;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.List;
import java.util.stream.Collectors;

public class NormaliseTrend extends UnaryTransformer {
    @Override
    protected DataSeries internalTransform(DataSeries line) {
        SimpleRegression regression = new SimpleRegression();
        double[][] data = line.getData().stream().map(datapoint -> new double[]{ datapoint.getX().doubleValue(), datapoint.getY() }).toArray(double[][]::new);
        regression.addData(data);
        double slope = regression.getSlope();

        int firstX = line.getData().get(0).getX();
        List<DataPoint> outputData = line.getData().stream().map(dataPoint -> {
            double trendChange = slope * (dataPoint.getX() - firstX);
            return dataPoint.withY(dataPoint.getY() - trendChange);
        }).collect(Collectors.toList());
        return new DataSeries(line.getName() + " -> remove trend", outputData, line.getColor(), line.getStroke(), line.getDrawStyle());
    }
}
