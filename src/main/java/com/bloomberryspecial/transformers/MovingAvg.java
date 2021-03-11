package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataSelector;
import com.bloomberryspecial.ItemModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MovingAvg extends Transformer {
    private final DataSelector selector;
    private final int windowSize;

    @Override
    protected List<Pair<Integer, Double>> transform(ItemModel itemModel) {
        final List<Pair<Integer, Double>> data = selector.getData(itemModel);

        double sum = 0;
        long count = 0;

        final List<Pair<Integer, Double>> output = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            final Pair<Integer, Double> datapoint = data.get(i);
            sum += datapoint.getRight();
            count++;
            if (count > windowSize) {
                count--;
                sum -= data.get(i - windowSize).getRight();
            }
            output.add(Pair.of(datapoint.getLeft(), sum / count));
        }

        return output;
    }
}
