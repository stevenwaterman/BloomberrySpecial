package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataSeries;
import com.bloomberryspecial.DrawStyle;

import java.awt.*;

public class Emphasise extends UnaryTransformer {
    @Override
    protected DataSeries internalTransform(DataSeries line) {
        return new DataSeries(
                line.getName(),
                line.getData(),
                Color.WHITE,
                new BasicStroke(3f),
                DrawStyle.LINE
        );
    }
}
