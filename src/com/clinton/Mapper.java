package com.clinton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Mapper {
    private List<Pair<String, Integer>> pairs;
    private List<String> texts;

    public Mapper(List<String> texts){
        pairs = new ArrayList<>();
        this.texts = texts;
    }

    public void map(){
        for (String word : texts) pairs.add(new Pair<>(word, 1));
        pairs.sort(Comparator.comparing(p -> p.key));
    }

    public List<Pair<String, Integer>> getPairs() {
        return pairs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Pair<String, Integer> pair : pairs) {
            builder.append(pair).append("\n");
        }
        return builder.toString();
    }
}
