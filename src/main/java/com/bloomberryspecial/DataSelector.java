package com.bloomberryspecial;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public enum DataSelector {
    BUY_PRICE(RLHistoricalDatapoint::getAvgHighPrice, BloomberrySpecialConfig::buyColor, false),
    SELL_PRICE(RLHistoricalDatapoint::getAvgLowPrice, BloomberrySpecialConfig::sellColor, false),
    BUY_VOLUME(RLHistoricalDatapoint::getHighPriceVolume, BloomberrySpecialConfig::buyColor, true),
    SELL_VOLUME(RLHistoricalDatapoint::getLowPriceVolume, BloomberrySpecialConfig::sellColor, true);

    private final Function<RLHistoricalDatapoint, Integer> yValueExtractor;
    private final Function<BloomberrySpecialConfig, Color> colorExtractor;
    @Getter
    private final boolean isZeroValid;

    public Double getY(RLHistoricalDatapoint datapoint) {
        return yValueExtractor.apply(datapoint).doubleValue();
    }

    public List<Pair<Integer, Double>> getData(ItemModel itemModel) {
        return itemModel.getData().get(this);
    }

    public Color getColor(BloomberrySpecialConfig config) {
        return colorExtractor.apply(config);
    }
}
