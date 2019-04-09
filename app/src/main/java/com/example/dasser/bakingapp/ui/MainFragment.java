package com.example.dasser.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.dasser.bakingapp.R;
import com.example.dasser.bakingapp.adapter.RecipesRecyclerItemClickListener;
import com.example.dasser.bakingapp.adapter.RecipesRecyclerViewAdapter;
import com.example.dasser.bakingapp.database.AppDatabaseUtils;
import com.example.dasser.bakingapp.model.Combinations;

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

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks{

    @BindView(R.id.progressBar_main_fragment) ProgressBar progressBar;
    @BindView(R.id.recyclerView_recipes) RecyclerView recyclerView;

    public MainFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        boolean mTwoPane = getResources().getConfiguration().smallestScreenWidthDp >= 600;
        boolean mLandscapeMode = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (mTwoPane && mLandscapeMode)
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        else if (mTwoPane)
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(null);

        getLoaderManager().initLoader(LOADER_ID_DETAIL_ACTIVITY, null, this);
        return view;
    }


    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        if (getContext() != null)
            return new GetRecipesNames(getContext());
        else
            throw new NullPointerException("onCreateLoader null context");
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        recyclerView.setAdapter(new RecipesRecyclerViewAdapter(getContext(),
                (Combinations.RecipeNameAndServingCombination) data));
        recyclerView.addOnItemTouchListener(new RecipesRecyclerItemClickListener(getContext()
                , position -> {
                    if (getFragmentManager() != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(BUNDLE_RECIPE_CLICKED_POSITION, position);
                        Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } else
                        throw new NullPointerException("beginTransaction() returns null");
                }));
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) { }


    public static class GetRecipesNames extends AsyncTaskLoader<Combinations.RecipeNameAndServingCombination> {

        GetRecipesNames(@NonNull Context context) {
            super(context);
        }


        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Nullable
        @Override
        public Combinations.RecipeNameAndServingCombination loadInBackground() {
            return new Combinations.RecipeNameAndServingCombination(
                    AppDatabaseUtils.getRecipesNames(getContext()),
                    AppDatabaseUtils.getRecipesServings(getContext()));
        }
    }
}