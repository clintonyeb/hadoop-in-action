package com.clinton;

public class WordCount {
    private final int m;
    private final int r;
    Mapper[] mappers;
    Reducer[] reducers;

    public WordCount(int m, int r) {
        this.m = m;
        this.r = r;
        this.mappers = new Mapper[m];
        this.reducers = new Reducer[r];
    }

    public void start(String fileName) {

    }

    private void runMappers() {
        // split  input data into m
        // create a mapper for each
    }
}
