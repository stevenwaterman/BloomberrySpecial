package com.bloomberryspecial.graphs;

import com.bloomberryspecial.*;
import com.bloomberryspecial.transformers.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AbnormalityChart extends BloomberrySpecialChartOverlay {
    public AbnormalityChart(BloomberrySpecialConfig config) {
        super(config);
    }

    @Override
    protected String getTitle() {
        return "Abnormality";
    }

    @Override
    protected List<DataSeries> getSeries(ItemModel itemModel) {
        DataSeries buyPrice = new DataSeries("Buy Price", DataSelector.BUY_PRICE.getData(itemModel), buyColor, new BasicStroke(1f), DrawStyle.POINTS);
        List<DataSeries> removedTrend = new NormaliseTrend().getData(buyPrice);
        List<DataSeries> normalised = new NormalisePercentage().getData(removedTrend);
        List<DataSeries> standardError = new StandardError().getData(normalised);
        List<DataSeries> emphasise = new Every(new Emphasise()).getData(standardError);

        List<DataSeries> display = new ArrayList<>();
        display.addAll(normalised);
        display.addAll(emphasise);
        return display;
    }
}
