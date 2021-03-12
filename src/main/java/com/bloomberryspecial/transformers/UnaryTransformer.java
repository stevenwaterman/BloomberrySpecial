package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataSeries;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class UnaryTransformer extends Transformer{
    public UnaryTransformer() {
        super(1, 1);
    }

    protected abstract DataSeries internalTransform(DataSeries line);

    @Override
    protected final List<DataSeries> transform(List<DataSeries> lines) {
        DataSeries input = lines.get(0);
        DataSeries output = internalTransform(input);
        return Lists.newArrayList(output);
    }
}
