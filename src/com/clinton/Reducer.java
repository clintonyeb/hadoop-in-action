package com.clinton;

import java.util.*;

public class Reducer {
    private List<Pair<String, Integer>> pairs;

    private Reducer(List<Pair<String, Integer>> pairs) {
        this.pairs = pairs;
    }

    public static Reducer getInstance(List<Pair<String, Integer>> pairs) {
        Reducer reducer = new Reducer(pairs);

        List<GroupByPair<String, Integer>> groupedPairs = reducer.groupPairs();
        System.out.println("\n\n===== Reducer Input =====");
        reducer.printGroupPairList(groupedPairs);

        List<Pair<String, Integer>> sumPairs = reducer.sumGroupPairs(groupedPairs);
        System.out.println("\n\n===== Reducer Output =====");
        reducer.printGroupPairSum(sumPairs);

        return reducer;
    }

    private List<GroupByPair<String, Integer>> groupPairs() {
        Map<String, GroupByPair<String, Integer>> cache = new HashMap<>();
        Set<String> keys = new HashSet<>();
        for (Pair<String, Integer> pair : pairs) {
            GroupByPair<String, Integer> current;

            if (cache.containsKey(pair.key)) {
                current = cache.get(pair.key);
            } else {
                current = new GroupByPair<>(pair.key);
            }

            current.addValue(pair.value);
            cache.put(pair.key, current);
            keys.add(pair.key);
        }
        List<GroupByPair<String, Integer>> groups = new ArrayList<>();
        for (String key : keys) {
            groups.add(cache.get(key));
        }

        groups.sort(Comparator.comparing(GroupByPair::getKey));
        return groups;
    }

    private List<Pair<String, Integer>> sumGroupPairs(List<GroupByPair<String, Integer>> groupPairs) {
        List<Pair<String, Integer>> pairs = new ArrayList<>();

        for (GroupByPair<String, Integer> groupPair : groupPairs) {
            GroupByPair<String, Integer> group = new GroupByPair<>(groupPair.getKey());
            int sum = 0;
            for (int value : groupPair.getValues()) {
                sum += value;
            }
            pairs.add(new Pair<String, Integer>(group.getKey(), sum));
        }

        pairs.sort(Comparator.comparing(p -> p.key));
        return pairs;
    }

    private void printGroupPairList(List<GroupByPair<String, Integer>> groups) {
        for (GroupByPair<String, Integer> group : groups) {
            System.out.println(group);
        }
    }

    private void printGroupPairSum(List<Pair<String, Integer>> pairs) {
        for (Pair<String, Integer> pair : pairs) {
            System.out.println(pair);
        }
    }
}
