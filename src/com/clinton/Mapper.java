package com.clinton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Mapper {
    private final String DELIMITER = "[\\s-]+";
    private List<Pair<String, Integer>> pairs;

    private Mapper(){
        pairs = new ArrayList<>();
    }

    public static <K> Mapper getInstance(String fileName) throws IOException {
        Mapper mapper = new Mapper();
        mapper.generatePairsFromFile(fileName);

        System.out.println("\n\n===== Mapper Output =====");
        System.out.println(mapper);
        return mapper;
    }

    private void generatePairsFromFile(String fileName) throws IOException {
        Files.lines(Paths.get(fileName)).forEach(this::processLine);
        pairs.sort(Comparator.comparing(p -> p.key));
    }

    private void processLine(String line) {
        for (String word : line.split(DELIMITER)) processWord(word);
    }

    private void processWord(String w) {
        String word = w.trim().replaceAll("\"", "").toLowerCase();
        String format = formatText(word);
        if(format != null) pairs.add(new Pair<>(format, 1));
    }

    private String formatText(String text) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if(!Character.isLetter(c)) {
                if(i != 0 && i != text.length()-1) return null;
            } else {
                res.append(c);
            }
        }
        if(res.length() < 1) {
            return null;
        }
        return res.toString();
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
