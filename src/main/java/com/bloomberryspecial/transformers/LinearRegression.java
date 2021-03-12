package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataPoint;
import com.bloomberryspecial.DataSeries;
import com.bloomberryspecial.DrawStyle;
import com.google.common.collect.Lists;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.List;

public class LinearRegression extends UnaryTransformer {
    @Override
    protected DataSeries internalTransform(DataSeries line) {
        SimpleRegression regression = new SimpleRegression();
        double[][] data = line.getData().stream().map(datapoint -> new double[]{ datapoint.getX().doubleValue(), datapoint.getY() }).toArray(double[][]::new);
        regression.addData(data);
        double slope = regression.getSlope();
        double intercept = regression.getIntercept();

        DataPoint first = line.getData().get(0);
        DataPoint last = line.getData().get(line.getData().size() - 1);
        List<DataPoint> outputData = Lists.newArrayList(
                first.withY(slope * first.getX() + intercept),
                last.withY(slope * last.getX() + intercept)
        );
        return new DataSeries(line.getName() + " -> linear regression", outputData, line.getColor(), line.getStroke(), DrawStyle.LINE);
    }
}
