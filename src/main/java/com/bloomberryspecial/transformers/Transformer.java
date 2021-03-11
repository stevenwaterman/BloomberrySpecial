package com.bloomberryspecial.transformers;

import com.bloomberryspecial.DataSelector;
import com.bloomberryspecial.ItemModel;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public abstract class Transformer {
    private final DataSelector selector;
    private List<Pair<Integer, Integer>> cache = null;

    public Transformer(DataSelector selector){
        this.selector = selector;
    }

    protected abstract List<Pair<Integer, Integer>> transform(List<Pair<Integer, Integer>> data);

    public List<Pair<Integer, Integer>> getData(ItemModel itemModel) {
        if (cache == null) {
            cache = transform(selector.getData(itemModel));
        }
        return cache;
    }
}
