package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataSelector;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class MovingAvg extends Transformer {
    private final int windowSize;

    public MovingAvg(DataSelector selector, int windowSize) {
        super(selector);
        this.windowSize = windowSize;
    }

    @Override
    protected List<Pair<Integer, Integer>> transform(List<Pair<Integer, Integer>> data) {
        long sum = 0;
        long count = 0;

        final List<Pair<Integer, Integer>> output = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            final Pair<Integer, Integer> datapoint = data.get(i);
            sum += datapoint.getRight();
            count++;
            if (count > windowSize) {
                count--;
                sum -= data.get(i - windowSize).getRight();
            }
            int avg = (int) (sum / count);
            output.add(Pair.of(datapoint.getLeft(), avg));
        }

        return output;
    }
}
