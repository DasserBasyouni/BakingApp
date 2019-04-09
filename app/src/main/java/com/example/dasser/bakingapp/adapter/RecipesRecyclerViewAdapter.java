package com.example.dasser.bakingapp.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dasser.bakingapp.R;
import com.example.dasser.bakingapp.model.Combinations;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class RecipesRecyclerViewAdapter extends RecyclerView.Adapter<RecipesRecyclerViewAdapter.ViewHolder> {

    private final List<String> mRecipes;
    private final List<Integer> mRecipesServings;
    private final List<Integer> mImagesRes;
    private final Context context;

    public RecipesRecyclerViewAdapter(Context context, Combinations.RecipeNameAndServingCombination recipeNameAndServingCombination) {
        mRecipes = recipeNameAndServingCombination.getNames();
        mRecipesServings = recipeNameAndServingCombination.getServing();
        this.context = context;

        mImagesRes = new ArrayList<>();
        mImagesRes.add(0, R.drawable.background_1);
        mImagesRes.add(1, R.drawable.background_2);
        mImagesRes.add(2, R.drawable.background_3);
        mImagesRes.add(3, R.drawable.background_4);
    }

    @Override
    public int getItemCount() {
        if(mRecipes == null)
            return 0;
        return mRecipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mRecipeName, mRecipeServing;
        private final ImageView mRandomDesign;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mRecipeName = view.findViewById(R.id.textView_recipe_name);
            mRecipeServing = view.findViewById(R.id.textView_recipe_serving);
            mRandomDesign = view.findViewById(R.id.imageView_random_design);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mRecipeName.setText(mRecipes.get(position));
        holder.mRandomDesign.setImageResource(mImagesRes.get(position));
        holder.mRecipeServing.setText(
                context.getString(R.string.serving,
                        mRecipesServings.get(position)));
    }

}
