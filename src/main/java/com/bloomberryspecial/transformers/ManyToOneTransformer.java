package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataSeries;
import com.google.common.collect.Lists;

import java.util.List;

public abstract class ManyToOneTransformer extends Transformer{
    public ManyToOneTransformer() {
        super(null, 1);
    }

    protected abstract DataSeries internalTransform(List<DataSeries> lines);

    @Override
    protected final List<DataSeries> transform(List<DataSeries> lines) {
        DataSeries output = internalTransform(lines);
        return Lists.newArrayList(output);
    }
}
