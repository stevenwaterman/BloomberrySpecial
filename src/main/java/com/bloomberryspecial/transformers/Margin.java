package com.bloomberryspecial.transformers;

import com.bloomberryspecial.ItemModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Margin extends Transformer {
    @Override
    protected List<Pair<Integer, Double>> transform(ItemModel itemModel) {
        return itemModel.getHistoricalData()
                .stream()
                .filter(e -> e.getAvgHighPrice() > 0 && e.getAvgLowPrice() > 0)
                .map(e -> Pair.of(e.getTimestamp(), (double) (e.getAvgHighPrice() - e.getAvgLowPrice())))
                .collect(Collectors.toList());
    }
}
