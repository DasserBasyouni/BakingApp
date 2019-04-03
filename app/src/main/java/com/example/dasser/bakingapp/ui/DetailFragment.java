package com.example.dasser.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dasser.bakingapp.Constants.BUNDLE_RECIPE_CLICKED_POSITION;
import static com.example.dasser.bakingapp.Constants.LOADER_ID_DETAIL_ACTIVITY;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks{

    @BindView(R.id.recyclerView_ingredients) RecyclerView mIngredients_RV;
    @BindView(R.id.recyclerView_steps) RecyclerView mSteps_RV;
    @BindView(R.id.textView_ingredients) TextView mIngredients_TV;
    @BindView(R.id.textView_steps) TextView mSteps_TV;
    @BindView(R.id.progressBar_detail_fragment) ProgressBar mProgressBar;

    private boolean mTwoPane;


    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    public DetailFragment() { }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        getLoaderManager().initLoader(LOADER_ID_DETAIL_ACTIVITY, getArguments(), this);
        return view;
    }

    private void setupRecyclerViews(Object data) {
        Combinations.RecipeIngredientsAndStepsCombination recipeIngredientsAndStepsCombination =
                (Combinations.RecipeIngredientsAndStepsCombination) data;
        initialiseTwoPaneBoolean();

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
                recipeIngredientsAndStepsCombination.getSteps()));
        mSteps_RV.setHasFixedSize(true);
    }

    private void initialiseTwoPaneBoolean() {
        if (getActivity() != null)
            mTwoPane = getActivity().findViewById(R.id.main_fragment) == null;
        else
            throw new NullPointerException("getActivity() is null");
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        if (getContext() != null)
            return new GetRecipesDetails(getContext(), args);
        else
            throw new NullPointerException("onCreateLoader null context");
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
    public void onLoaderReset(@NonNull Loader loader) { }


    public static class GetRecipesDetails extends AsyncTaskLoader<Combinations.RecipeIngredientsAndStepsCombination> {

        Bundle bundle;

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
                throw new NullPointerException("Bundle is null");

            // TODO (3) getRecipesIngredients_MethodOne() is returning null, what is wrong with it?
            return new Combinations.RecipeIngredientsAndStepsCombination(
            AppDatabaseUtils.getRecipesIngredients_MethodTwo(getContext(), id),
            AppDatabaseUtils.getRecipeSteps_MethodTwo(getContext(), id));
        }
    }
}