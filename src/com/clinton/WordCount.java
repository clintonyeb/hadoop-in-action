package com.clinton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCount {
    private final int m;
    private final int r;

    public WordCount(int m, int r) {
        this.m = m;
        this.r = r;
    }

    public void count() throws IOException {
        String[] texts = new String[3];
        texts[0] = "\"cat bat\" mat-pat mum.edu sat fat 'rat eat cat' mum_cs mat";
        texts[1] = "bat-hat mat pat \"oat hat rat mum_cs eat oat-pat";
        texts[2] = "zat lat-cat pat jat. hat rat. kat sat wat";
        run(texts);
    }

    private void run(String[] inputTexts) {
        Mapper[] mappers = new Mapper[m];

        // create mappers
        for (int i = 0; i < m; i++) {
            mappers[i] = new Mapper(Util.processLine(inputTexts[i]));
        }

        // perform mapping and print
        for (int i = 0; i < mappers.length; i++) {
            Mapper mapper = mappers[i];
            mapper.map();

            System.out.println(String.format("\n===== Mapper %d Output =====", i));
            System.out.println(mapper);
        }

        Map<Integer, List<Pair<String, Integer>>> partitionedPairs = new HashMap<>();
        // populate initial map with reducers
        for (int i = 0; i < mappers.length; i++) {
            partitionedPairs.put(i, new ArrayList<>());
        }

        for (int i = 0; i < mappers.length; i++) {
            Mapper mapper = mappers[i];

            // Create reducer splits
            for (Pair<String, Integer> pair : mapper.getPairs()) {
                int partition = getPartition(pair.key);
                List<Pair<String, Integer>> partitionPairs = partitionedPairs.get(partition);
                partitionPairs.add(pair);
                partitionedPairs.put(partition, partitionPairs);
            }

            for (int j = 0; j < r; j++) {
                System.out.println(String.format("\n===== Pairs send from Mapper %d Reducer %d =====", i, j));
                for (Pair<String, Integer> pair : partitionedPairs.get(j)) {
                    System.out.println(pair);
                }
            }
        }

        // Perform reduction for partitions
        Reducer[] reducers = new Reducer[r];

        for (int j = 0; j < r; j++) {
            List<Pair<String, Integer>> pairs = partitionedPairs.get(j);
            Reducer reducer = new Reducer(pairs);
            reducer.reduce();
            reducers[j] = reducer;
        }

        // print reduction results - input
        for (int j = 0; j < reducers.length; j++) {
            Reducer reducer = reducers[j];
            if (reducer == null) continue;
            System.out.println(String.format("\n===== Reducer %d input =====", j));
            reducer.printGroupPairList();
        }

        // print reduction results - output
        for (int j = 0; j < reducers.length; j++) {
            Reducer reducer = reducers[j];
            if (reducer == null) continue;
            System.out.println(String.format("\n===== Reducer %d output =====", j));
            reducer.printGroupPairSum();
        }
    }

    private int getPartition(String key) {
        return key.hashCode() % r;
    }
}
