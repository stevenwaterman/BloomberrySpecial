package com.bloomberryspecial.graphs;

import com.bloomberryspecial.BloomberrySpecialConfig;
import com.bloomberryspecial.DataSeries;
import com.bloomberryspecial.ItemModel;
import com.bloomberryspecial.transformers.Emphasise;
import com.bloomberryspecial.transformers.MovingAvg;
import com.bloomberryspecial.transformers.Subtract;

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
