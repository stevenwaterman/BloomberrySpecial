package com.bloomberryspecial.transformers;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class MovingAvg extends Transformer {
    private final int windowSize;

    public MovingAvg(List<Pair<Integer, Integer>> input, int windowSize) {
        super(input);
        this.windowSize = windowSize;
    }

    @Override
    protected List<Pair<Integer, Integer>> transform(List<Pair<Integer, Integer>> data) {
        int sum = 0;
        int count = 0;

        final List<Pair<Integer, Integer>> output = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            final Pair<Integer, Integer> datapoint = data.get(i);
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
