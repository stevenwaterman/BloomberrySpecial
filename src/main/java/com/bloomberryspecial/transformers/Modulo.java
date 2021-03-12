package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataPoint;
import com.bloomberryspecial.DataSeries;
import com.bloomberryspecial.DrawStyle;

import javax.xml.crypto.Data;
import java.awt.*;
import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Modulo extends OneToManyTransformer {
    private final ModuloPeriod period;

    public Modulo(ModuloPeriod period) {
        super();
        this.period = period;
    }

    @Override
    protected List<DataSeries> internalTransform(DataSeries line) {
        List<Integer> desiredX = line.getData().stream().map(DataPoint::getX).collect(Collectors.toList());
        OffsetDateTime start = Instant.ofEpochSecond(desiredX.get(0)).atOffset(ZoneOffset.UTC).toLocalDate().atStartOfDay().plusDays(1).atOffset(ZoneOffset.UTC);
        OffsetDateTime end = Instant.ofEpochSecond(desiredX.get(desiredX.size() - 1)).atOffset(ZoneOffset.UTC).toLocalDate().atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime pointer = start;
        while (!pointer.isAfter(end)) {
            desiredX.add((int) pointer.toEpochSecond() - 1);
            desiredX.add((int) pointer.toEpochSecond());
            pointer = pointer.plusDays(1);
        }
        List<DataPoint> inputData = interpolate(line.getData(), desiredX);

        int periodStartX = 0;
        Integer periodEndX = null;

        List<List<DataPoint>> output = new ArrayList<>();
        List<DataPoint> data = null;
        for (DataPoint dataPoint : inputData) {
            if (periodEndX == null || dataPoint.getX() >= periodEndX) {
                if (data != null) {
                    data.sort(Comparator.comparing(DataPoint::getX));
                    DataPoint first = data.get(0);
                    if (first.getX() < 300) {
                        output.add(data);
                    }
                }
                data = new ArrayList<>();

                LocalDate periodStartDate = Instant.ofEpochSecond(dataPoint.getX()).atOffset(ZoneOffset.UTC).toLocalDate();
                if (period == ModuloPeriod.WEEK) {
                    periodStartDate = periodStartDate.minusDays(periodStartDate.getDayOfWeek().getValue() - 1);
                }
                LocalDate periodEndDate = periodStartDate.plusDays(period == ModuloPeriod.DAY ? 1 : 7);
                periodStartX = (int) OffsetDateTime.of(periodStartDate.atStartOfDay(), ZoneOffset.UTC).toEpochSecond();
                periodEndX = (int) OffsetDateTime.of(periodEndDate.atStartOfDay(), ZoneOffset.UTC).toEpochSecond();
            }

            data.add(dataPoint.withX(dataPoint.getX() - periodStartX));
        }

        List<DataSeries> series = new ArrayList<>();
        for (int i = 0; i < output.size(); i++) {
            List<DataPoint> dataPoints = output.get(i);
            series.add(new DataSeries(
                    line.getName() + " -> modulo " + period.name() + " (" + i + ")",
                    dataPoints,
                    Color.getHSBColor((float) i / output.size(), 0.8f, 1f),
                    line.getStroke(),
                    line.getDrawStyle()
            ));
        }
        return series;
    }

    public enum ModuloPeriod {
        DAY,
        WEEK
    }
}
