package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataSeries;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public abstract class BinaryOutputTransformer extends Transformer{
    public BinaryOutputTransformer() {
        super(2, 1);
    }

    protected abstract Pair<DataSeries, DataSeries> internalTransform(DataSeries line);

    @Override
    protected final List<DataSeries> transform(List<DataSeries> lines) {
        DataSeries input = lines.get(0);
        Pair<DataSeries, DataSeries> outputs = internalTransform(input);
        return Lists.newArrayList(outputs.getLeft(), outputs.getRight());
    }
}
