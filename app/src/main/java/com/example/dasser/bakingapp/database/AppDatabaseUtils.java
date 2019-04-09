package com.example.dasser.bakingapp.database;

import android.content.Context;

import com.example.dasser.bakingapp.model.DataConverter;
import com.example.dasser.bakingapp.model.Recipe;

import java.util.List;

public class AppDatabaseUtils {

    public static void insertRecipesNames(Context context, List<Recipe> recipe) {
        getRecipeDB(context).recipeDao().insertRecipe(recipe);
    }

    public static List<String> getRecipesNames(Context context) {
        return getRecipeDB(context).recipeDao().getRecipesNames();
    }

    public static List<Integer> getRecipesServings (Context context) {
        return getRecipeDB(context).recipeDao().getRecipesServings();
    }

    private static RecipeRoomDatabase getRecipeDB(Context context) {
        return RecipeRoomDatabase.getDatabase(context);
    }


    // Method One
    public static List<Recipe.Ingredient> getRecipeIngredientsMethodOne(Context context, Integer id) {
        return new DataConverter().toRecipeIngredients(getRecipeDB(context).recipeDao().getRecipeIngredients(id));
    }

    public static List<Recipe.Steps> getRecipeStepsMethodOne(Context context, Integer id) {
        return new DataConverter().toRecipeSteps(getRecipeDB(context).recipeDao().getRecipeSteps(id));
    }


    // Method Two
    public static List<Recipe.Ingredient> getRecipesIngredientsMethodTwo(Context context, int id) {
        return new DataConverter().toRecipeIngredients(getRecipeDB(context).recipeDao().getAllRecipesIngredients().get(id));
    }

    public static List<Recipe.Steps> getRecipeStepsMethodTwo(Context context, int id) {
        return new DataConverter().toRecipeSteps(getRecipeDB(context).recipeDao().getAllRecipesSteps().get(id));
    }

}
