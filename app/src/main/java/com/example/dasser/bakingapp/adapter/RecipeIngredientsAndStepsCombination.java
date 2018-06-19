package com.example.dasser.bakingapp.adapter;

import com.example.dasser.bakingapp.model.Recipe;

import java.util.List;

public class RecipeIngredientsAndStepsCombination {
    private List<Recipe.Ingredient> ingredients;
    private List<Recipe.Steps> steps;

    public RecipeIngredientsAndStepsCombination(List<Recipe.Ingredient> ingredients, List<Recipe.Steps> steps) {
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public List<Recipe.Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Recipe.Steps> getSteps() {
        return steps;
    }
}
