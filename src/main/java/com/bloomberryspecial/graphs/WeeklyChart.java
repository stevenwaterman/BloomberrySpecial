package com.bloomberryspecial.graphs;

import com.bloomberryspecial.BloomberrySpecialConfig;
import com.bloomberryspecial.DataSeries;
import com.bloomberryspecial.DrawStyle;
import com.bloomberryspecial.ItemModel;
import com.bloomberryspecial.transformers.*;

import java.util.ArrayList;
import java.util.List;

public class WeeklyChart extends BloomberrySpecialChartOverlay {
    public WeeklyChart(BloomberrySpecialConfig config) {
        super(config);
    }

    @Override
    protected String getTitle() {
        return "Weekly Price";
    }

    @Override
    protected List<DataSeries> getSeries(ItemModel itemModel) {
        DataSeries buyPrice = buyPriceSeries(itemModel);
        List<DataSeries> removedTrend = new NormaliseTrend().getData(buyPrice);
        List<DataSeries> daily = new Modulo(Modulo.ModuloPeriod.WEEK).getData(removedTrend);
        List<DataSeries> dots = new Every(new SetDrawStyle(DrawStyle.POINTS)).getData(daily);
        List<DataSeries> normalised = new Every(new NormalisePercentage()).getData(dots);
        List<DataSeries> movingAvg = new Every(new MovingAvg(25)).getData(normalised);
        List<DataSeries> combine = new CombineX().getData(movingAvg);
        List<DataSeries> emphasise = new Emphasise().getData(combine);

        List<DataSeries> display = new ArrayList<>();
        display.addAll(normalised);
        display.addAll(emphasise);
        return display;
    }
}
