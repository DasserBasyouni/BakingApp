package com.example.dasser.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dasser.bakingapp.R;
import com.example.dasser.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;


public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.ViewHolder> {

    private List<String> mQuantity, mName;

    public IngredientRecyclerViewAdapter(List<Recipe.Ingredient> ingredient) {
        for (int i = 0; i < ingredient.size(); i++) {
            Recipe.Ingredient ingredient1 = ingredient.get(i);

            mName = new ArrayList<>();
            mQuantity = new ArrayList<>();

            mName.add(ingredient1.getIngredient());
            mQuantity.add(ingredient1.getQuantity() + " " + ingredient1.getMeasure());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIngredientTV;
        final TextView mQuantityTV;

        ViewHolder(View view) {
            super(view);
            mIngredientTV = view.findViewById(R.id.textView_recipe_name);
            mQuantityTV = view.findViewById(R.id.imageView_random_design);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredients, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mIngredientTV.setText(mName.get(position));
        holder.mQuantityTV.setText(mQuantity.get(position));
    }

    @Override
    public int getItemCount() {
        return mQuantity.size();
    }

}
