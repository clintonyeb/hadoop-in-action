package com.clinton;

import java.io.IOException;
import java.util.*;

public class Mapper {
    private List<Pair<String, Integer>> pairs;
    private String fileName;
    private final Map<String, Integer> cache;


    public Mapper(String fileName) {
        pairs = new ArrayList<>();
        this.fileName = fileName;
        this.cache = new HashMap<>();
    }

    public void map() throws IOException {
        List<String> texts = Util.getWordsFromFile(fileName);
        for (String word : texts) {
            int count = cache.getOrDefault(word, 0);
            count += 1;
            cache.put(word, count);
        };
        createPairs();
    }

    private void createPairs() {
        for (String key : cache.keySet()) {
            pairs.add(new Pair<>(key, cache.get(key)));
        }
//        pairs.sort(Comparator.comparing(p -> p.key));
    }


    public List<Pair<String, Integer>> getPairs() {
        return pairs;
    }
}
