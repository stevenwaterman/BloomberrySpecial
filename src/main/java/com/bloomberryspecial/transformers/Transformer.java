package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataPoint;
import com.bloomberryspecial.DataSeries;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Transformer {
    private List<DataSeries> cache = null;
    @Getter
    private final Integer inputCount;
    @Getter
    private final Integer outputCount;

    public Transformer(Integer inputCount, Integer outputCount) {
        this.inputCount = inputCount;
        this.outputCount = outputCount;
    }

    protected abstract List<DataSeries> transform(List<DataSeries> lines);

    public final List<DataSeries> getData(DataSeries ...lines) {
        return getData(Lists.newArrayList(lines));
    }

    public final List<DataSeries> getData(List<DataSeries> lines) {
        assert inputCount == null || lines.size() == inputCount;
//        if (cache == null)
            cache = transform(lines);
        assert outputCount == null || cache.size() == outputCount;
        return cache;
    }

    public static List<List<DataPoint>> interpolate(List<List<DataPoint>> lines) {
        lines.forEach(line -> line.sort(Comparator.comparing(DataPoint::getX)));
        List<Integer> overlap = getOverlapPoints(lines);
        return lines.stream().map(line -> interpolate(line, overlap)).collect(Collectors.toList());
    }

    private static List<Integer> getOverlapPoints(List<List<DataPoint>> lines) {
        Integer highestMin = lines.stream().map(line -> line.get(0).getX()).max(Comparator.comparingInt(a -> a)).orElse(0);
        Integer lowestMax = lines.stream().map(line -> line.get(line.size() - 1).getX()).min(Comparator.comparingInt(a -> a)).orElse(0);
        Set<Integer> xVales = lines.stream().flatMap(Collection::stream).map(DataPoint::getX).filter(x -> x >= highestMin && x <= lowestMax).collect(Collectors.toSet());
        return xVales.stream().sorted().collect(Collectors.toList());
    }

    public static List<DataPoint> interpolate(List<DataPoint> data, List<Integer> desiredPoints) {
        List<DataPoint> output = new ArrayList<>();

        int dataPointer = 0;
        int desiredPointer = 0;

        while (desiredPointer < desiredPoints.size()) {
            DataPoint dataPoint = data.get(dataPointer);
            Integer desired = desiredPoints.get(desiredPointer);

            if (dataPoint.getX().equals(desired)) {
                output.add(dataPoint);
                desiredPointer++;
            } else if (dataPoint.getX() < desired) {
                dataPointer++;
            } else {
                DataPoint lastDataPoint = data.get(dataPointer - 1);
                int delta = desired - lastDataPoint.getX();
                int xRange = dataPoint.getX() - lastDataPoint.getX();
                double fraction = (double) delta / xRange;
                double yRange = dataPoint.getY() - lastDataPoint.getY();
                double desiredY = lastDataPoint.getY() + fraction * yRange;
                output.add(new DataPoint(desired, desiredY));
                desiredPointer++;
            }
        }

        return output;
    }
}
