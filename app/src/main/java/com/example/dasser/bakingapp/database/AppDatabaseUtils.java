package com.example.dasser.bakingapp.database;

import android.content.Context;

import com.example.dasser.bakingapp.model.DataConverter;
import com.example.dasser.bakingapp.model.Recipe;

import java.util.List;

public class AppDatabaseUtils {

    public AppDatabaseUtils() {}

    public static List<String> getRecipesNames(Context context) {
        return getRecipeDB(context).recipeDao().getRecipesNames();
    }

    public static List<Integer> getRecipesServings (Context context) {
        return getRecipeDB(context).recipeDao().getRecipesServings();
    }

    public static List<Recipe.Ingredient> getRecipeIngredients(Context context, List<Integer> id) {
        return new DataConverter().toRecipeIngredients(getRecipeDB(context).recipeDao().getRecipeIngredients(id));
    }

    public static List<Recipe.Steps> getRecipeSteps(Context context, Integer id) {
        return new DataConverter().toRecipeSteps(getRecipeDB(context).recipeDao().getRecipeSteps(id));
    }

    public static RecipeRoomDatabase getRecipeDB(Context context) {
        return RecipeRoomDatabase.getDatabase(context);
    }

}
