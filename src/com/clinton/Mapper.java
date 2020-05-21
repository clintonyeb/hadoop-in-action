package com.clinton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Mapper {
    private List<Pair<String, Integer>> pairs;
    private String fileName;

    public Mapper(String fileName) {
        pairs = new ArrayList<>();
        this.fileName = fileName;
    }

    public void map() throws IOException {
        List<String> texts = Util.getWordsFromFile(fileName);
        for (String word : texts) pairs.add(new Pair<>(word, 1));
        pairs.sort(Comparator.comparing(p -> p.key));
    }


    public List<Pair<String, Integer>> getPairs() {
        return pairs;
    }
}
