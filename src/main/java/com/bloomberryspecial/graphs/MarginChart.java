package com.bloomberryspecial.graphs;

import com.bloomberryspecial.*;
import com.bloomberryspecial.transformers.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MarginChart extends BloomberrySpecialChartOverlay {
    public MarginChart(BloomberrySpecialConfig config) {
        super(config);
    }

    @Override
    protected String getTitle() {
        return "Margin";
    }

    @Override
    protected List<DataSeries> getSeries(ItemModel itemModel) {
        DataSeries buyPrice = buyPriceSeries(itemModel);
        DataSeries sellPrice = sellPriceSeries(itemModel);
        List<DataSeries> margin = new Subtract().getData(buyPrice, sellPrice);
        List<DataSeries> movingAvg = new MovingAvg(25).getData(margin);
        List<DataSeries> emphasise = new Emphasise().getData(movingAvg);

        List<DataSeries> display = new ArrayList<>();
        display.addAll(margin);
        display.addAll(emphasise);
        return display;
    }
}
