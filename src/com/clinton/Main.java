package com.clinton;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final String FILE_NAME = "src/com/clinton/testDataForW1D1.txt";

    public static void main(String[] args) throws IOException {
        WordCount wordCount = new WordCount(3, 4);
        wordCount.start();
    }
}
