package com.example.dasser.bakingapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        mName = new ArrayList<>();
        mQuantity = new ArrayList<>();

        for (int i = 0; i < ingredient.size(); i++) {
            Recipe.Ingredient ingredient1 = ingredient.get(i);

            mName.add(ingredient1.getIngredient());
            mQuantity.add(ingredient1.getQuantity() + " " + ingredient1.getMeasure());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mIngredientTV;
        private final TextView mQuantityTV;

        ViewHolder(View view) {
            super(view);
            mIngredientTV = view.findViewById(R.id.textView_name);
            mQuantityTV = view.findViewById(R.id.textView_quantity);
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
