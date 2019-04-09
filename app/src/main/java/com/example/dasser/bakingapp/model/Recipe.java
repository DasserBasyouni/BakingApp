package com.example.dasser.bakingapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.dasser.bakingapp.Constants.DATABASE_NAME;

@Entity(tableName = DATABASE_NAME)
public class Recipe implements Parcelable {

    @ColumnInfo(name = "image_url")
    private String image;

    @ColumnInfo(name = "servings")
    private int servings;

    @ColumnInfo(name = "steps")
    private List<Steps> steps;

    @ColumnInfo(name = "ingredients")
    private List<Ingredient> ingredients;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    public Recipe(@NonNull String image, int servings, List<Steps> steps
            , List<Ingredient> ingredients, @NonNull String name, int id) {
        this.image = image;
        this.servings = servings;
        this.steps = steps;
        this.ingredients = ingredients;
        this.name = name;
        this.id = id;
    }


    public String getImage() {
        return image;
    }

    public int getServings() {
        return servings;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static class Steps {
        @SerializedName("thumbnailURL")
        private String thumbnailurl;
        @SerializedName("videoURL")
        private String videourl;
        @SerializedName("shortDescription")
        private String shortdescription;
        private String description;
        private int id;

        public String getThumbnailurl() {
            return thumbnailurl;
        }

        public String getVideourl() {
            return videourl;
        }

        public String getDescription() {
            return description;
        }

        public String getShortdescription() {
            return shortdescription;
        }

        public int getId() {
            return id;
        }

        public void setThumbnailurl(String thumbnailurl) {
            this.thumbnailurl = thumbnailurl;
        }

        public void setVideourl(String videourl) {
            this.videourl = videourl;
        }

        public void setShortdescription(String shortdescription) {
            this.shortdescription = shortdescription;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Ingredient {
        private String ingredient;
        private String measure;

        @Ignore
        private float quantity;

        private String str_quantity;

        public String getIngredient() {
            return ingredient;
        }

        public String getMeasure() {
            return measure;
        }

        public float getQuantity() {
            return quantity;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }

        public void setQuantity(float quantity) {
            this.quantity = quantity;
        }

        public String getStr_quantity() {
            return String.valueOf(quantity);
        }

        public void setStr_quantity(String str_quantity) {
            this.str_quantity = str_quantity;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image);
        dest.writeInt(this.servings);
        dest.writeList(this.steps);
        dest.writeList(this.ingredients);
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }

    protected Recipe(Parcel in) {
        this.image = in.readString();
        this.servings = in.readInt();
        this.steps = new ArrayList<Steps>();
        in.readList(this.steps, Steps.class.getClassLoader());
        this.ingredients = new ArrayList<Ingredient>();
        in.readList(this.ingredients, Ingredient.class.getClassLoader());
        this.name = Objects.requireNonNull(in.readString());
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

}