package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataSeries;
import com.bloomberryspecial.DrawStyle;

import java.awt.*;

public class SetDrawStyle extends UnaryTransformer {
    private final DrawStyle drawStyle;

    public SetDrawStyle(DrawStyle drawStyle) {
        this.drawStyle = drawStyle;
    }

    @Override
    protected DataSeries internalTransform(DataSeries line) {
        return new DataSeries(
                line.getName(),
                line.getData(),
                line.getColor(),
                line.getStroke(),
                drawStyle
        );
    }
}
