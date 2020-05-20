package com.clinton;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        WordCount wordCount = new WordCount(3, 4);
        wordCount.start();
    }
}
