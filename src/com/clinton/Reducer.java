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
       sumPairs = sumGroupPairs(groupedPairs);
   }

    private List<GroupByPair<String, Integer>> groupPairs() {
        Map<String, GroupByPair<String, Integer>> cache = new HashMap<>();
        Set<String> keys = new HashSet<>();
        for (Pair<String, Integer> pair : pairs) {
            GroupByPair<String, Integer> current;

            if(cache.containsKey(pair.key)) {
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
