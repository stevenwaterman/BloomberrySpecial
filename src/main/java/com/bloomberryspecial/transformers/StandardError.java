package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataPoint;
import com.bloomberryspecial.DataSeries;
import com.bloomberryspecial.DrawStyle;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class StandardError extends Transformer {
    public StandardError() {
        super(1, 3);
    }

    @Override
    protected List<DataSeries> transform(List<DataSeries> lines) {
        DataSeries line = lines.get(0);
        List<Double> yValues = line.getData().stream().map(DataPoint::getY).collect(Collectors.toList());
        double mean = yValues.stream().reduce(Double::sum).orElse(0.0) / yValues.size();
        double sd = Math.sqrt(yValues.stream().map(value -> Math.pow(value - mean, 2)).reduce(Double::sum).orElse(1.0) / yValues.size());

        int minX = line.getData().get(0).getX();
        int maxX = line.getData().get(line.getData().size() - 1).getX();
        List<DataPoint> upper = Lists.newArrayList(new DataPoint(minX, mean + sd), new DataPoint(maxX, mean + sd));
        List<DataPoint> mid = Lists.newArrayList(new DataPoint(minX, mean), new DataPoint(maxX, mean));
        List<DataPoint> lower = Lists.newArrayList(new DataPoint(minX, mean - sd), new DataPoint(maxX, mean - sd));

        return Lists.newArrayList(
                new DataSeries(line.getName() + " -> upper", upper, line.getColor(), line.getStroke(), DrawStyle.LINE),
                new DataSeries(line.getName() + " -> mid", mid, line.getColor(), line.getStroke(), DrawStyle.LINE),
                new DataSeries(line.getName() + " -> lower", lower, line.getColor(), line.getStroke(), DrawStyle.LINE)
        );
    }
}
