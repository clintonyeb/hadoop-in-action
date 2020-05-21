package com.clinton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCount {
    private final int numberOfMappers;
    private final int numberOfReducers;
    private final String FILENAME = "src/com/clinton/mapper-%d.txt";

    public WordCount(int numberOfMappers, int numberOfReducers) {
        this.numberOfMappers = numberOfMappers;
        this.numberOfReducers = numberOfReducers;
    }

    public void count() throws IOException {
        run();
    }

    private void run() throws IOException {
        Mapper[] mappers = new Mapper[numberOfMappers];

        for (int i = 0; i < numberOfMappers; i++) {
            // create mappers
            Mapper mapper = new Mapper(String.format(FILENAME, i));
            mappers[i] = mapper;

            // perform mapping and print
            mapper.map();
            System.out.println(String.format("\n===== Mapper %d Output =====", i));
            System.out.println(mapper);
        }


        Map<Integer, List<Pair<String, Integer>>> partitionedPairs = new HashMap<>();

        for (int i = 0; i < mappers.length; i++) {
            Mapper mapper = mappers[i];

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

            for (int j = 0; j < numberOfReducers; j++) {
                if(!partitionedPairs.containsKey(j)) continue;
                System.out.println(String.format("\n===== Pairs send from Mapper %d Reducer %d =====", i, j));
                for (Pair<String, Integer> pair : partitionedPairs.get(j)) {
                    System.out.println(pair);
                }
            }
        }

        // Perform reduction for partitions
        Reducer[] reducers = new Reducer[numberOfReducers];

        for (int j = 0; j < numberOfReducers; j++) {
            if(!partitionedPairs.containsKey(j)) continue;
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
        return key.hashCode() % numberOfReducers;
    }
}
