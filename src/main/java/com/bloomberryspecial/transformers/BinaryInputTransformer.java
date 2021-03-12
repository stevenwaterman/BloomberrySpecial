package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataSeries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class BinaryInputTransformer extends Transformer{
    public BinaryInputTransformer() {
        super(2, 1);
    }

    protected abstract DataSeries internalTransform(DataSeries line1, DataSeries line2);

    @Override
    protected final List<DataSeries> transform(List<DataSeries> lines) {
        DataSeries input1 = lines.get(0);
        DataSeries input2 = lines.get(1);
        DataSeries output = internalTransform(input1, input2);
        List<DataSeries> wrapped = new ArrayList<>();
        wrapped.add(output);
        return wrapped;
    }
}
