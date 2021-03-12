package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataSeries;
import com.google.common.collect.Lists;

import java.util.List;

public abstract class OneToManyTransformer extends Transformer{
    public OneToManyTransformer() {
        super(1, null);
    }

    protected abstract List<DataSeries> internalTransform(DataSeries line);

    @Override
    protected final List<DataSeries> transform(List<DataSeries> lines) {
        DataSeries input = lines.get(0);
        return internalTransform(input);
    }
}
