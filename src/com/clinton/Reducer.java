package com.clinton;

import java.util.*;

public class Reducer {
    private final List<Pair<String, Integer>> pairs;
    private List<GroupByPair<String, Integer>> groupedPairs;
    private List<Pair<String, Integer>> sumPairs;

    public Reducer(List<Pair<String, Integer>> pairs) {
        this.pairs = pairs;
    }

    public void reduce() {
        groupedPairs = groupPairs();
        sumPairs = sumGroupPairs();
    }

    private List<GroupByPair<String, Integer>> groupPairs() {
        Map<String, GroupByPair<String, Integer>> cache = new HashMap<>();
        for (Pair<String, Integer> pair : pairs) {
            GroupByPair<String, Integer> current;
            if (cache.containsKey(pair.key)) {
                current = cache.get(pair.key);
            } else {
                current = new GroupByPair<>(pair.key);
            }
            current.addValue(pair.value);
            cache.put(pair.key, current);
        }
        List<GroupByPair<String, Integer>> groups = new ArrayList<>();
        for (String key : cache.keySet()) {
            groups.add(cache.get(key));
        }
        groups.sort(Comparator.comparing(GroupByPair::getKey));
        return groups;
    }

    private List<Pair<String, Integer>> sumGroupPairs() {
        List<Pair<String, Integer>> pairs = new ArrayList<>();
        for (GroupByPair<String, Integer> groupPair : groupedPairs) {
            GroupByPair<String, Integer> group = new GroupByPair<>(groupPair.getKey());
            int sum = groupPair.getValues().stream().reduce(0, Integer::sum);
            pairs.add(new Pair<>(group.getKey(), sum));
        }
        pairs.sort(Comparator.comparing(p -> p.key));
        return pairs;
    }

    public void printGroupPairList() {
        for (GroupByPair<String, Integer> group : groupedPairs) {
            System.out.println(group);
        }
    }

    public void printGroupPairSum() {
        for (Pair<String, Integer> pair : sumPairs) {
            System.out.println(pair);
        }
    }
}
