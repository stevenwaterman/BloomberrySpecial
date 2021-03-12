package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataSeries;

import java.util.List;
import java.util.stream.Collectors;

public class Every extends Transformer {
    private final UnaryTransformer transformer;

    public Every(UnaryTransformer transformer) {
        super(null, null);
        this.transformer = transformer;
    }

    @Override
    protected List<DataSeries> transform(List<DataSeries> lines) {
        return lines.stream().flatMap(line -> transformer.getData(line).stream()).collect(Collectors.toList());
    }
}
