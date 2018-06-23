package com.example.dasser.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;

public class Combinations {

    public static class RecipeIngredientsAndStepsCombination {

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

    public static class RecipeNameAndServingCombination {

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

    public static class RecipeDescriptionsAndUrlsCombination implements Parcelable {

        private List<String> descriptions;
        private List<String> urls;

        public RecipeDescriptionsAndUrlsCombination(List<String> descriptions, List<String> urls) {
            this.descriptions = descriptions;
            this.urls = urls;
        }

        public List<String> getDescriptions() {
            return descriptions;
        }

        public List<String> getUrls() {
            return urls;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringList(this.descriptions);
            dest.writeStringList(this.urls);
        }

        RecipeDescriptionsAndUrlsCombination(Parcel in) {
            this.descriptions = in.createStringArrayList();
            this.urls = in.createStringArrayList();
        }

        public static final Parcelable.Creator<RecipeDescriptionsAndUrlsCombination> CREATOR = new Parcelable.Creator<RecipeDescriptionsAndUrlsCombination>() {
            @Override
            public RecipeDescriptionsAndUrlsCombination createFromParcel(Parcel source) {
                return new RecipeDescriptionsAndUrlsCombination(source);
            }

            @Override
            public RecipeDescriptionsAndUrlsCombination[] newArray(int size) {
                return new RecipeDescriptionsAndUrlsCombination[size];
            }
        };
    }

    public static class RecipeNameAndIngredients implements Parcelable {

        @NonNull private String name;
        @NonNull private String ingredients;

        public RecipeNameAndIngredients(@NonNull String name, @NonNull String ingredients) {
            this.name = name;
            this.ingredients = ingredients;
        }

        public String getIngredients() {
            return ingredients;
        }

        public String getName() {
            return name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.ingredients);
        }

        RecipeNameAndIngredients(Parcel in) {
            this.name = in.readString();
            this.ingredients = in.readString();
        }

        public static final Parcelable.Creator<RecipeNameAndIngredients> CREATOR = new Parcelable.Creator<RecipeNameAndIngredients>() {
            @Override
            public RecipeNameAndIngredients createFromParcel(Parcel source) {
                return new RecipeNameAndIngredients(source);
            }

            @Override
            public RecipeNameAndIngredients[] newArray(int size) {
                return new RecipeNameAndIngredients[size];
            }
        };
    }
}