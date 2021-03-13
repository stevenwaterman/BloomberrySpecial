package com.bloomberryspecial;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public enum DataSelector {
    BUY_PRICE(RLHistoricalDatapoint::getAvgHighPrice, false),
    SELL_PRICE(RLHistoricalDatapoint::getAvgLowPrice, false),
    BUY_VOLUME(RLHistoricalDatapoint::getHighPriceVolume, true),
    SELL_VOLUME(RLHistoricalDatapoint::getLowPriceVolume, true);

    private final Function<RLHistoricalDatapoint, Integer> yValueExtractor;
    @Getter
    private final boolean isZeroValid;

    public Double getY(RLHistoricalDatapoint datapoint) {
        return yValueExtractor.apply(datapoint).doubleValue();
    }

    public List<DataPoint> getData(ItemModel itemModel) {
        return itemModel.getData().get(this);
    }
}
