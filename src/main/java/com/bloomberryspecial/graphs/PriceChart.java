package com.bloomberryspecial.graphs;

import com.bloomberryspecial.BloomberrySpecialConfig;
import com.bloomberryspecial.DataSeries;
import com.bloomberryspecial.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class PriceChart extends BloomberrySpecialChartOverlay {
    public PriceChart(BloomberrySpecialConfig config) {
        super(config);
    }

    @Override
    protected String getTitle() {
        return "Price";
    }

    @Override
    protected List<DataSeries> getSeries(ItemModel itemModel) {
        List<DataSeries> display = new ArrayList<>();
        display.add(buyPriceSeries(itemModel));
        display.add(sellPriceSeries(itemModel));
        return display;
    }
}
