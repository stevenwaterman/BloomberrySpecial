package com.bloomberryspecial;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemComposition;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemModel {
    ItemComposition item;
    List<RLHistoricalDatapoint> historicalData;
    EnumMap<DataSelector, List<Pair<Integer, Double>>> data;

    public ItemModel(ItemComposition item, List<RLHistoricalDatapoint> historicalData) {
        this.item = item;
        this.historicalData = historicalData;

        data = new EnumMap<>(DataSelector.class);
        for (DataSelector selector : DataSelector.values()) {
            data.put(selector, createData(historicalData, selector));
        }
    }

    private static List<Pair<Integer, Double>> createData(List<RLHistoricalDatapoint> historicalData, DataSelector selector) {
        Stream<Pair<Integer, Double>> stream = historicalData.stream()
                .map(datapoint -> Pair.of( datapoint.getTimestamp(), selector.getY(datapoint)));
        if(!selector.isZeroValid()) stream = stream.filter(e -> e.getRight().doubleValue() > 0);
        return stream.collect(Collectors.toList());
    }
}
