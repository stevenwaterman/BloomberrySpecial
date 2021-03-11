package com.bloomberryspecial.transformers;

import com.bloomberryspecial.ItemModel;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public abstract class Transformer {
    private List<Pair<Integer, Double>> cache = null;

    protected abstract List<Pair<Integer, Double>> transform(ItemModel itemModel);

    public List<Pair<Integer, Double>> getData(ItemModel itemModel) {
        if (cache == null) {
            cache = transform(itemModel);
        }
        return cache;
    }
}
