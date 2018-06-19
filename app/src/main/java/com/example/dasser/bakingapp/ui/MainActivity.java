package com.example.dasser.bakingapp.ui;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.dasser.bakingapp.R;
import com.example.dasser.bakingapp.database.AppDatabaseUtils;
import com.example.dasser.bakingapp.database.RecipeDAO;
import com.example.dasser.bakingapp.database.RecipeRoomDatabase;
import com.example.dasser.bakingapp.model.Recipe;
import com.example.dasser.bakingapp.retrofit.RecipeAPI;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static com.example.dasser.bakingapp.Constants.BUNDLE_RECIPE_NAMES;
import static com.example.dasser.bakingapp.Constants.LOADER_ID_MAIN_ACTIVITY;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    @BindView(R.id.fragment_container) FrameLayout fragmentContainerLayout;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initialiseRetrofitConnection();
        initialiseTwoPaneBoolean();

    }

    private void initialiseTwoPaneBoolean() {
        mTwoPane = findViewById(R.id.main_fragment) == null;
    }

    private void initialiseRetrofitConnection() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation().create()))
                .build();

        retrofit.create(RecipeAPI.class).getRecipeList().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                List<Recipe> body = response.body();

                if (body != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(BUNDLE_RECIPE_NAMES, (ArrayList<? extends Parcelable>) body);

                    getSupportLoaderManager().initLoader(LOADER_ID_MAIN_ACTIVITY, bundle, MainActivity.this);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Timber.d("initialiseRetrofitConnection.getRecipeList.onFailure: %s",
                        t.getLocalizedMessage());
            }
        });
    }


    private void launchUserInterfaceFragments() {
        if (mTwoPane)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new MainFragment()).commit();
        else
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new DetailFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else
            getFragmentManager().popBackStack();
    }


    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        List<Recipe> body = null;

        if (args != null) {
            body = args.getParcelableArrayList(BUNDLE_RECIPE_NAMES);
        }

        return new InsertToDatabase(this,
                AppDatabaseUtils.getRecipeDB(MainActivity.this),
                body);
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        progressBar.setVisibility(View.GONE);
        launchUserInterfaceFragments();
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        Timber.d("Loader Reset");
    }

    public static class InsertToDatabase extends AsyncTaskLoader<List<Recipe>> {
        private RecipeRoomDatabase recipeRoomDatabase;
        private List<Recipe> recipesName;

        InsertToDatabase(@NonNull Context context, RecipeRoomDatabase recipeRoomDatabase, List<Recipe> recipesName) {
            super(context);
            this.recipeRoomDatabase = recipeRoomDatabase;
            this.recipesName = recipesName;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Nullable
        @Override
        public List<Recipe> loadInBackground() {
            RecipeDAO recipeDAO = recipeRoomDatabase.recipeDao();

            for (int i = 0; i < recipesName.size(); i++)
                recipeDAO.insertRecipe(recipesName.get(i));

            return null;
        }
    }
}