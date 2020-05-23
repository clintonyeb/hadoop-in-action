package com.clinton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCount {
    private final int m;
    private final int r;
    private final String FILENAME = "src/com/clinton/resources/mapper-%d.txt";

    public WordCount(int m, int r) {
        this.m = m;
        this.r = r;
    }

    public void count() throws IOException {
        run();
    }

    private void run() throws IOException {
        Mapper[] mappers = new Mapper[m];

        for (int i = 0; i < m; i++) {
            // create mappers
            Mapper mapper = new Mapper(String.format(FILENAME, i));
            mappers[i] = mapper;

            // perform mapping and print
            mapper.map();
            System.out.println(String.format("\n===== Mapper %d Output =====", i));
            Util.printAll(mapper.getPairs());
        }



        Map<Integer, List<Pair<String, Integer>>> allPartitionedPairs = new HashMap<>();

        for (int i = 0; i < mappers.length; i++) {
            Mapper mapper = mappers[i];

            Map<Integer, List<Pair<String, Integer>>> partitionedPairs = new HashMap<>();

            // Create reducer splits
            for (Pair<String, Integer> pair : mapper.getPairs()) {
                int partition = getPartition(pair.key);
                List<Pair<String, Integer>> partitionPairs;
                if(partitionedPairs.containsKey(partition)) {
                    partitionPairs = partitionedPairs.get(partition);
                } else {
                    partitionPairs = new ArrayList<>();
                }
                partitionPairs.add(pair);
                partitionedPairs.put(partition, partitionPairs);
            }

            for (int j = 0; j < r; j++) {
                if(!partitionedPairs.containsKey(j)) continue;
                System.out.println(String.format("\n===== Pairs send from Mapper %d Reducer %d =====", i, j));
                Util.printAll(partitionedPairs.get(j));
            }

            Util.mergeMap(allPartitionedPairs, partitionedPairs);
        }

        // Perform reduction for partitions
        Reducer[] reducers = new Reducer[r];

        for (int j = 0; j < r; j++) {
            if(!allPartitionedPairs.containsKey(j)) continue;
            List<Pair<String, Integer>> pairs = allPartitionedPairs.get(j);
            Reducer reducer = new Reducer(pairs);
            reducer.reduce();
            reducers[j] = reducer;
        }

        // print reduction results - input
        for (int j = 0; j < reducers.length; j++) {
            Reducer reducer = reducers[j];
            if (reducer == null) continue;
            System.out.println(String.format("\n===== Reducer %d input =====", j));
            Util.printAll(reducer.getGroupedPairs());
        }

        // print reduction results - output
        for (int j = 0; j < reducers.length; j++) {
            Reducer reducer = reducers[j];
            if (reducer == null) continue;
            System.out.println(String.format("\n===== Reducer %d output =====", j));
            Util.printAll(reducer.getSumPairs());
        }
    }

    private int getPartition(String key) {
        return key.hashCode() % r;
    }
}
