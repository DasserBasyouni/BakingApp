package com.example.dasser.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dasser.bakingapp.R;
import com.example.dasser.bakingapp.adapter.RecipeNameAndServingCombination;
import com.example.dasser.bakingapp.adapter.RecipesRecyclerItemClickListener;
import com.example.dasser.bakingapp.adapter.RecipesRecyclerViewAdapter;
import com.example.dasser.bakingapp.database.AppDatabaseUtils;
import com.example.dasser.bakingapp.model.Recipe;

import java.util.List;

import static com.example.dasser.bakingapp.Constants.BUNDLE_RECIPE_CLICKED_POSITION;
import static com.example.dasser.bakingapp.Constants.LOADER_ID_DETAIL_ACTIVITY;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks{

    private RecyclerView recyclerView;

    public MainFragment() { }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (view instanceof RecyclerView) {
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(null);
        }

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
                (RecipeNameAndServingCombination) data));
        recyclerView.addOnItemTouchListener(new RecipesRecyclerItemClickListener(getContext()
                , recyclerView, new RecipesRecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (getFragmentManager() != null) {
                Bundle bundle = new Bundle();
                bundle.putInt(BUNDLE_RECIPE_CLICKED_POSITION, position);

                Fragment fragment = new DetailFragment();
                fragment.setArguments(bundle);

                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                } else
                    throw new NullPointerException("beginTransaction() returns null");
            }
        }));

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) { }


    public static class GetRecipesNames extends AsyncTaskLoader<RecipeNameAndServingCombination> {

        GetRecipesNames(@NonNull Context context) {
            super(context);
        }


        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Nullable
        @Override
        public RecipeNameAndServingCombination loadInBackground() {
            return new RecipeNameAndServingCombination(
                    AppDatabaseUtils.getRecipesNames(getContext()),
                    AppDatabaseUtils.getRecipesServings(getContext()));
        }
    }
}