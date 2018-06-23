package com.example.dasser.bakingapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dasser.bakingapp.database.AppDatabaseUtils;
import com.example.dasser.bakingapp.model.Recipe;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.dasser.bakingapp.Constants.PREF_RECIPE_OF_THE_DAY_ID;
import static com.example.dasser.bakingapp.Constants.WIDGET_PREF;

public class Utils {

    public static int getRecipeOfTheDayIdAndPlusOneId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);

        int id = sharedPreferences.getInt(PREF_RECIPE_OF_THE_DAY_ID, 0);

        int newId = 0;
        if (id != 3)
            newId = id+1;

        sharedPreferences.edit().putInt(PREF_RECIPE_OF_THE_DAY_ID, newId).apply();

        return id;
    }

    public static String getWidgetIngredientsFormat(Context context, int id) {
        List<Recipe.Ingredient> ingredients = AppDatabaseUtils.getRecipesIngredients_MethodTwo(context, id);
        StringBuilder widgetIngredients = new StringBuilder();

        for (int i=0 ; i<3 ; i++){
            widgetIngredients.append(ingredients.get(i).getIngredient()).append("\n");

            if (i == 2)
                widgetIngredients.append(context.getString(R.string.dots));
        }
        return widgetIngredients.toString();
    }

}
