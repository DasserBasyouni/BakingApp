package com.example.dasser.bakingapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.widget.LinearLayout;

import com.example.dasser.bakingapp.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRecipe(List<Recipe> recipe);

    @Query("SELECT name from recipe_db ORDER BY id ASC")
    List<String> getRecipesNames();

    @Query("SELECT servings FROM recipe_db ORDER BY id ASC")
    List<Integer> getRecipesServings();

    // method one queries
    @Query("SELECT ingredients FROM recipe_db WHERE id LIKE :id LIMIT 1")
    String getRecipeIngredients(int id);

    @Query("SELECT steps FROM recipe_db WHERE id = :id LIMIT 1")
    String getRecipeSteps(Integer id);


    // method two queries
    @Query("SELECT ingredients FROM recipe_db")
    List<String> getAllRecipesIngredients();

    @Query("SELECT steps FROM recipe_db")
    List<String> getAllRecipesSteps();


    @Query("SELECT servings FROM recipe_db WHERE id LIKE :name")
    int getTest(String name);
}
