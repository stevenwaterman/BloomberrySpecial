package com.bloomberryspecial;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemComposition;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemModel {
    ItemComposition item;
    List<RLHistoricalDatapoint> historicalData;
    EnumMap<DataSelector, List<DataPoint>> data;

    public ItemModel(ItemComposition item, List<RLHistoricalDatapoint> historicalData) {
        this.item = item;
        this.historicalData = historicalData;

        data = new EnumMap<>(DataSelector.class);
        for (DataSelector selector : DataSelector.values()) {
            data.put(selector, createData(historicalData, selector));
        }
    }

    private static List<DataPoint> createData(List<RLHistoricalDatapoint> historicalData, DataSelector selector) {
        Stream<DataPoint> stream = historicalData.stream()
                .map(datapoint -> new DataPoint(datapoint.getTimestamp(), selector.getY(datapoint)));
        if(!selector.isZeroValid()) stream = stream.filter(e -> e.getY() > 0);
        return stream.collect(Collectors.toList());
    }
}
