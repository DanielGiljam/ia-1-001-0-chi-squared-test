package com.giljam.daniel.chisquaredtest.chi;

import java.util.ArrayList;
import java.util.List;

public class Misc {
    
    public static int add(List<Integer> values) {
        int sum = 0;
        
        for (int x = 0; x < values.size(); x++) {
            sum = sum + values.get(x);
        }
        
        return sum;
    }

    public static List<Integer> valuesProjectionGenerator(List<List<Integer>> twoDimenValues) {
        List<Integer> oneDimenValues = new ArrayList<>();
        for (List<Integer> row : twoDimenValues) oneDimenValues.addAll(row);
        return oneDimenValues;
    }
}
