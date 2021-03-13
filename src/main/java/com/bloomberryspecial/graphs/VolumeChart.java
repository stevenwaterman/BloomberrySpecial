package com.bloomberryspecial.graphs;

import com.bloomberryspecial.BloomberrySpecialConfig;
import com.bloomberryspecial.DataSeries;
import com.bloomberryspecial.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class VolumeChart extends BloomberrySpecialChartOverlay {
    public VolumeChart(BloomberrySpecialConfig config) {
        super(config);
    }

    @Override
    protected String getTitle() {
        return "Volume";
    }

    @Override
    protected List<DataSeries> getSeries(ItemModel itemModel) {
        List<DataSeries> display = new ArrayList<>();
        display.add(buyVolumeSeries(itemModel));
        display.add(sellVolumeSeries(itemModel));
        return display;
    }
}
