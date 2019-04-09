package com.example.dasser.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dasser.bakingapp.R;
import com.example.dasser.bakingapp.adapter.IngredientRecyclerViewAdapter;
import com.example.dasser.bakingapp.adapter.StepsRecyclerViewAdapter;
import com.example.dasser.bakingapp.database.AppDatabaseUtils;
import com.example.dasser.bakingapp.model.Combinations;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dasser.bakingapp.Constants.BUNDLE_RECIPE_CLICKED_POSITION;
import static com.example.dasser.bakingapp.Constants.LOADER_ID_DETAIL_ACTIVITY;

public class RecipeDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks{

    @BindView(R.id.recyclerView_ingredients) RecyclerView mIngredients_RV;
    @BindView(R.id.recyclerView_steps) RecyclerView mSteps_RV;
    @BindView(R.id.textView_ingredients) TextView mIngredients_TV;
    @BindView(R.id.textView_steps) TextView mSteps_TV;
    @BindView(R.id.progressBar_detail_fragment) ProgressBar mProgressBar;

    private int previousPosition;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("previousPosition", ((StepsRecyclerViewAdapter) Objects
                .requireNonNull(mSteps_RV.getAdapter())).previousPosition);
    }

    @Override
    public void onActivityCreated(@Nullable  Bundle outState) {
        super.onActivityCreated(outState);
        if (outState != null)
            previousPosition = Objects.requireNonNull(outState)
                    .getInt("previousPosition", previousPosition);
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null)
            getLoaderManager().initLoader(LOADER_ID_DETAIL_ACTIVITY, getArguments(), this);

        return view;
    }

    private void setupRecyclerViews(Object data) {
        Combinations.RecipeIngredientsAndStepsCombination recipeIngredientsAndStepsCombination =
                (Combinations.RecipeIngredientsAndStepsCombination) data;
        boolean mTwoPane = getResources().getConfiguration().smallestScreenWidthDp >= 600;

        int ingredientsNumberOfColumns;
        if (mTwoPane)
            ingredientsNumberOfColumns = 2;
        else
            ingredientsNumberOfColumns = 3;

        mIngredients_RV.setLayoutManager(new GridLayoutManager(getContext(), ingredientsNumberOfColumns,
                GridLayoutManager.HORIZONTAL, false));
        mIngredients_RV.setAdapter(new IngredientRecyclerViewAdapter(
                recipeIngredientsAndStepsCombination.getIngredients()));
        mIngredients_RV.setHasFixedSize(true);

        mSteps_RV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSteps_RV.setAdapter(new StepsRecyclerViewAdapter(
                recipeIngredientsAndStepsCombination.getSteps(), mTwoPane,
                Objects.requireNonNull(this.getActivity()).getSupportFragmentManager()));
        mSteps_RV.setHasFixedSize(true);

        if (mTwoPane)
            mSteps_RV.post(() -> {
                RecyclerView.ViewHolder viewHolder = mSteps_RV.findViewHolderForAdapterPosition(previousPosition);
                if (viewHolder != null)
                    viewHolder.itemView.performClick();
            });
    }

    void setArgumentsThenStartLoader(Bundle arg) {
        setArguments(arg);
        getLoaderManager().initLoader(LOADER_ID_DETAIL_ACTIVITY, arg, this);
    }


    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        if (getContext() != null)
            return new GetRecipesDetails(getContext(), args);
        else
            throw new IllegalArgumentException("onCreateLoader - null context");
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        setupRecyclerViews(data);
        mIngredients_TV.setVisibility(View.VISIBLE);
        mIngredients_RV.setVisibility(View.VISIBLE);
        mSteps_TV.setVisibility(View.VISIBLE);
        mSteps_RV.setVisibility(View.VISIBLE);
        mSteps_RV.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) { /* do nothing */ }


    public static class GetRecipesDetails extends AsyncTaskLoader<Combinations.RecipeIngredientsAndStepsCombination> {

        private Bundle bundle;

        GetRecipesDetails(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }


        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Nullable
        @Override
        public Combinations.RecipeIngredientsAndStepsCombination loadInBackground() {
            int id;
            if (bundle != null)
                id = bundle.getInt(BUNDLE_RECIPE_CLICKED_POSITION);
            else
                throw new IllegalArgumentException("Bundle is equal to null");

            // TODO (3) getRecipesIngredients_MethodOne() is returning null, what is wrong with it?
            return new Combinations.RecipeIngredientsAndStepsCombination(
            AppDatabaseUtils.getRecipesIngredientsMethodTwo(getContext(), id),
            AppDatabaseUtils.getRecipeStepsMethodTwo(getContext(), id));
        }
    }
}