package com.example.dasser.bakingapp.adapter;

import java.util.List;

public class RecipeNameAndServingCombination {
    private List<String> name;
    private List<Integer> serving;

    public RecipeNameAndServingCombination(List<String> name, List<Integer> serving) {
        this.name = name;
        this.serving = serving;
    }

    public List<String> getNames() {
        return name;
    }

    public List<Integer> getServing() {
        return serving;
    }
}
