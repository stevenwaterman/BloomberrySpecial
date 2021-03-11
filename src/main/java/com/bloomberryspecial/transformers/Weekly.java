package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataSelector;
import com.bloomberryspecial.ItemModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Weekly extends Transformer {
    private final DataSelector selector;

    @Override
    protected List<Pair<Integer, Double>> transform(ItemModel itemModel) {
        final List<Pair<Integer, Double>> data = selector.getData(itemModel);

        final List<Pair<Integer, Double>> output = new ArrayList<>();
        for (Pair<Integer, Double> datapoint : data) {
            OffsetDateTime time = Instant.ofEpochSecond(datapoint.getLeft()).atOffset(ZoneOffset.UTC);
            output.add(Pair.of(
                    (time.getDayOfWeek().getValue() - 1) * 86400 + time.getHour() * 3600 + time.getMinute() * 60 + time.getSecond(),
                    datapoint.getRight()
            ));
        }

        return output;
    }
}
