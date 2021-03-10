package com.bloomberryspecial;

import com.google.common.collect.Maps;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemComposition;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemModel {
    ItemComposition item;
    List<RLHistoricalDatapoint> historicalData;
    EnumMap<DataSelector, List<Pair<Integer, Integer>>> data;

    Range priceBounds;
    Range volumeBounds;
    Range timeBounds;

    List<Integer> priceMarks;
    List<Integer> volumeMarks;
    List<Integer> timeMarks;

    public ItemModel(ItemComposition item, List<RLHistoricalDatapoint> historicalData, BloomberrySpecialConfig config) {
        this.item = item;
        this.historicalData = historicalData;

        priceBounds = historicalData
                .stream()
                .flatMap(e -> Stream.of(e.getAvgLowPrice(), e.getAvgHighPrice()))
                .filter(e -> e > 0)
                .reduce(initialPair, ItemModel::applyRange, ItemModel::combineRange);
        priceMarks = createMarks(priceBounds, config.graphHeight() / 30);

        volumeBounds = historicalData
                .stream()
                .flatMap(e -> Stream.of(e.getLowPriceVolume(), e.getHighPriceVolume()))
                .reduce(initialPair, ItemModel::applyRange, ItemModel::combineRange);
        volumeMarks = createMarks(volumeBounds, config.graphHeight() / 30);

        timeBounds = historicalData
                .stream()
                .map(RLHistoricalDatapoint::getTimestamp)
                .reduce(initialPair, ItemModel::applyRange, ItemModel::combineRange);
        timeMarks = createMarks(timeBounds, config.graphWidth() / 100);

        data = new EnumMap<>(DataSelector.class);
        for (DataSelector selector : DataSelector.values()) {
            data.put(selector, createData(historicalData, selector));
        }
    }

    private static final Range initialPair = new Range(Integer.MAX_VALUE, Integer.MIN_VALUE);

    private static Range combineRange(Range pair1, Range pair2) {
        return new Range(Math.min(pair1.getMin(), pair2.getMin()), Math.max(pair1.getMax(), pair2.getMax()));
    }

    private static Range applyRange(Range acc, Integer elem) {
        return new Range(Math.min(acc.getMin(), elem), Math.max(acc.getMax(), elem));
    }

    private static List<Integer> createMarks(Range bounds, int desiredMarkCount) {
        int min = bounds.getMin(); // 6500
        int max = bounds.getMax(); // 10000
        if (min == max) return Collections.singletonList(min);

        int range = max - min; // 3500
        double desiredRangePerMark = (double) range / desiredMarkCount; // 350.0

        double rangeMagnitude = Math.round(Math.pow(10, Math.floor(Math.log10(desiredRangePerMark)))); // 100.0
        double rangeMagnitudeMultiple = desiredRangePerMark / rangeMagnitude ; // 3.5
        long adjustedMultiple = Math.round(rangeMagnitudeMultiple); // 4
        long rangePerMark = Math.max(1, Math.round(rangeMagnitude * adjustedMultiple)); // 400

        double markStart = Math.ceil((double) min / rangePerMark) * rangePerMark; // 6800.0
        List<Integer> marks = new ArrayList<>();
        for (int i = (int) markStart; i <= max; i += rangePerMark) {
            marks.add(i); // 6800, 7200, ..., 9600, 10000
        }
        return marks;
    }

    @Value
    public static class Range {
        int min;
        int max;

        public int getRange() {
            return max - min;
        }

        public double getFraction(int value) {
            int delta = value - min;
            return (double) delta / getRange();
        }
    }

    private static List<Pair<Integer, Integer>> createData(List<RLHistoricalDatapoint> historicalData, DataSelector selector) {
        Stream<Pair<Integer, Integer>> stream = historicalData .stream()
                .map(datapoint -> Pair.of(
                        datapoint.getTimestamp(),
                        selector.getY(datapoint)));
        if(!selector.isZeroValid()) stream = stream.filter(e -> e.getRight() > 0);
        return stream.collect(Collectors.toList());
    }
}
