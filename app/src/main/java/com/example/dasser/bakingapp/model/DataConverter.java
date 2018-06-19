package com.example.dasser.bakingapp.model;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {

    @TypeConverter
    public String fromRecipeSteps(List<Recipe.Steps> countryLang) {
        if (countryLang == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Object>>() {
        }.getType();
        return gson.toJson(countryLang, type);
    }

    @TypeConverter
    public List<Recipe.Steps> toRecipeSteps(String countryLangString) {
        if (countryLangString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Recipe.Steps>>() {
        }.getType();
        return gson.fromJson(countryLangString, type);
    }


    @TypeConverter
    public String fromRecipeIngredients(List<Recipe.Ingredient> countryLang) {
        if (countryLang == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Recipe.Ingredient>>() {
        }.getType();
        return gson.toJson(countryLang, type);
    }

    @TypeConverter
    public List<Recipe.Ingredient> toRecipeIngredients(String countryLangString) {
        if (countryLangString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Recipe.Ingredient>>() {
        }.getType();
        return gson.fromJson(countryLangString, type);
    }
}
