package com.example.dasser.bakingapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.dasser.bakingapp.model.DataConverter;
import com.example.dasser.bakingapp.model.Recipe;

import static com.example.dasser.bakingapp.Constants.DATABASE_NAME;

@Database(entities = {Recipe.class}, version = 1,  exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class RecipeRoomDatabase extends RoomDatabase{

    private static RecipeRoomDatabase INSTANCE = null;

    public abstract RecipeDAO recipeDao();

    public static RecipeRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            RecipeRoomDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
