package com.bloomberryspecial;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public enum DataSelector {
    BUY_PRICE(RLHistoricalDatapoint::getAvgHighPrice, ItemModel::getPriceBounds, ItemModel::getPriceMarks, BloomberrySpecialConfig::buyColor, false),
    SELL_PRICE(RLHistoricalDatapoint::getAvgLowPrice, ItemModel::getPriceBounds, ItemModel::getPriceMarks, BloomberrySpecialConfig::sellColor, false),
    BUY_VOLUME(RLHistoricalDatapoint::getHighPriceVolume, ItemModel::getVolumeBounds, ItemModel::getVolumeMarks, BloomberrySpecialConfig::buyColor, true),
    SELL_VOLUME(RLHistoricalDatapoint::getLowPriceVolume, ItemModel::getVolumeBounds, ItemModel::getVolumeMarks, BloomberrySpecialConfig::sellColor, true);

    private final Function<RLHistoricalDatapoint, Integer> yValueExtractor;
    private final Function<ItemModel, ItemModel.Range> yBoundsExtractor;
    private final Function<ItemModel, List<Integer>> yMarksExtractor;
    private final Function<BloomberrySpecialConfig, Color> colorExtractor;
    @Getter
    private final boolean isZeroValid;

    public Integer getY(RLHistoricalDatapoint datapoint) {
        return yValueExtractor.apply(datapoint);
    }

    public List<Pair<Integer, Integer>> getData(ItemModel itemModel) {
        return itemModel.getData().get(this);
    }

    public ItemModel.Range getBounds(ItemModel itemModel) {
        return yBoundsExtractor.apply(itemModel);
    }

    public List<Integer> getMarks(ItemModel itemModel) {
        return yMarksExtractor.apply(itemModel);
    }

    public Color getColor(BloomberrySpecialConfig config) {
        return colorExtractor.apply(config);
    }
}
