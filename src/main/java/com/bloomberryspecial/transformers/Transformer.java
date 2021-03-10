package com.bloomberryspecial.transformers;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public abstract class Transformer {
    private final List<Pair<Integer, Integer>> input;
    private List<Pair<Integer, Integer>> cache = null;

    public Transformer(List<Pair<Integer, Integer>> input){
        this.input = input;
    }

    protected abstract List<Pair<Integer, Integer>> transform(List<Pair<Integer, Integer>> data);

    public List<Pair<Integer, Integer>> getData() {
        if (cache == null) {
            cache = transform(input);
        }
        return cache;
    }
}
