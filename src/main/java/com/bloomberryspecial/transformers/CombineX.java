package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataPoint;
import com.bloomberryspecial.DataSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CombineX extends ManyToOneTransformer {
    @Override
    protected DataSeries internalTransform(List<DataSeries> lines) {
        List<List<DataPoint>> interpolated = interpolate(lines.stream().map(DataSeries::getData).collect(Collectors.toList()));
        int lineCount = interpolated.size();
        int size = interpolated.get(0).size();

        List<DataPoint> output = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int x = interpolated.get(0).get(i).getX();
            double sum = 0.0;
            for (List<DataPoint> line : interpolated) {
                sum += line.get(i).getY();
            }
            output.add(new DataPoint(x, sum / lineCount));
        }

        DataSeries firstLine = lines.get(0);
        return new DataSeries(firstLine.getName() + " -> combine", output, firstLine.getColor(), firstLine.getStroke(), firstLine.getDrawStyle());
    }
}
