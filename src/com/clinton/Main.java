package com.clinton;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
	    Mapper mapper = Mapper.getInstance("src/com/clinton/testDataForW1D1.txt");
        Reducer.getInstance(mapper.getPairs());
    }
}
