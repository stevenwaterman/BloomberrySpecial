package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataPoint;
import com.bloomberryspecial.DataSeries;
import com.bloomberryspecial.DrawStyle;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Add extends BinaryInputTransformer {
    @Override
    protected DataSeries internalTransform(DataSeries line1, DataSeries line2) {
        List<List<DataPoint>> interpolated = interpolate(Lists.newArrayList(line1.getData(), line2.getData()));
        List<DataPoint> interpolated1 = interpolated.get(0);
        List<DataPoint> interpolated2 = interpolated.get(1);

        List<DataPoint> data = new ArrayList<>();
        for (int i = 0; i < interpolated1.size(); i++) {
            DataPoint point1 = interpolated1.get(i);
            DataPoint point2 = interpolated2.get(i);
            data.add(point1.withY(point1.getY() + point2.getY()));
        }
        return new DataSeries("(" + line1.getName() + ") + (" + line2.getName() + ")", data, line1.getColor(), line1.getStroke(), DrawStyle.POINTS);
    }


}
