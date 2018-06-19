package com.example.dasser.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dasser.bakingapp.R;
import com.example.dasser.bakingapp.adapter.IngredientRecyclerViewAdapter;
import com.example.dasser.bakingapp.adapter.RecipeIngredientsAndStepsCombination;
import com.example.dasser.bakingapp.adapter.StepsRecyclerViewAdapter;
import com.example.dasser.bakingapp.database.AppDatabaseUtils;
import com.example.dasser.bakingapp.database.RecipeRoomDatabase;
import com.example.dasser.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dasser.bakingapp.Constants.BUNDLE_RECIPE_CLICKED_POSITION;
import static com.example.dasser.bakingapp.Constants.LOADER_ID_DETAIL_ACTIVITY;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks{

    @BindView(R.id.recyclerView_ingredients) RecyclerView ingredientsRV;
    @BindView(R.id.recyclerView_steps) RecyclerView stepsRV;

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
        RecipeIngredientsAndStepsCombination recipeIngredientsAndStepsCombination =
                (RecipeIngredientsAndStepsCombination) data;
        initialiseTwoPaneBoolean();

        int ingredientsNumberOfColumns;
        if (mTwoPane)
            ingredientsNumberOfColumns = 2;
        else
            ingredientsNumberOfColumns = 3;

        String  testCase1 = ((RecipeIngredientsAndStepsCombination) data).getIngredients().get(1).getMeasure();
        String testCase2 = ((RecipeIngredientsAndStepsCombination) data).getSteps().get(1).getDescription();


        ingredientsRV.setLayoutManager(new GridLayoutManager(getContext(), ingredientsNumberOfColumns));
        ingredientsRV.setAdapter(new IngredientRecyclerViewAdapter(
                recipeIngredientsAndStepsCombination.getIngredients()));

        stepsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        stepsRV.setAdapter(new StepsRecyclerViewAdapter(
                recipeIngredientsAndStepsCombination.getSteps()));
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
        String  testCase3 = ((RecipeIngredientsAndStepsCombination) data).getIngredients().get(1).getMeasure();
        setupRecyclerViews(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) { }


    public static class GetRecipesDetails extends AsyncTaskLoader<RecipeIngredientsAndStepsCombination> {

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
        public RecipeIngredientsAndStepsCombination loadInBackground() {
            int id;
            if (bundle != null)
                id = bundle.getInt(BUNDLE_RECIPE_CLICKED_POSITION);
            else
                throw new NullPointerException("Bundle is null");

            List<Integer> ids = new ArrayList<>();
            ids.add(id);

            RecipeIngredientsAndStepsCombination recipeIngredientsAndStepsCombination
                    = new RecipeIngredientsAndStepsCombination(
                    AppDatabaseUtils.getRecipeIngredients(getContext(), ids),
                    AppDatabaseUtils.getRecipeSteps(getContext(), id));

            List<Recipe.Ingredient> testCase4 = AppDatabaseUtils.getRecipeIngredients(getContext(), ids);

            int testCase5 = RecipeRoomDatabase.getDatabase(getContext())
                    .recipeDao().getTest("Brownies");

            String testCase6 = recipeIngredientsAndStepsCombination.getIngredients().get(1).getMeasure();

            return recipeIngredientsAndStepsCombination;
        }
    }
}