package com.example.dasser.bakingapp.retrofit;

import com.example.dasser.bakingapp.model.Recipe;
import com.example.dasser.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeAPI {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipeList();

}
