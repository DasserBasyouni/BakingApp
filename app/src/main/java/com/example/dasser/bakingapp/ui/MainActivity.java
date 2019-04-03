package com.example.dasser.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.example.dasser.bakingapp.R;
import com.example.dasser.bakingapp.database.AppDatabaseUtils;
import com.example.dasser.bakingapp.model.Recipe;
import com.example.dasser.bakingapp.retrofit.RecipeAPI;

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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>{

    @BindView(R.id.progressBar_main_activity) ProgressBar progressBar;

    private boolean mTwoPane;


    // TODO (4): when the phone rotates the progressBar visibility goes to Visible again, how to disable that?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            ButterKnife.bind(this);

            initialiseRetrofitConnection();
            initialiseTwoPaneBoolean();
        }
    }

    private void initialiseTwoPaneBoolean() {
        mTwoPane = findViewById(R.id.main_fragment) != null;
    }

    private void initialiseRetrofitConnection() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net")
                .addConverterFactory(GsonConverterFactory.create())
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
                    .add(R.id.fragment_container, new DetailFragment()).commit();
        else
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new MainFragment()).commit();
    }


    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        List<Recipe> body = null;

        if (args != null) {
            body = args.getParcelableArrayList(BUNDLE_RECIPE_NAMES);
        }

        return new InsertToDatabase(this, body);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> data) {
        progressBar.setVisibility(View.GONE);
        launchUserInterfaceFragments();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {
        Timber.d("Loader Reset");
    }


    public static class InsertToDatabase extends AsyncTaskLoader<List<Recipe>> {
        private List<Recipe> recipesName;

        InsertToDatabase(@NonNull Context context, List<Recipe> recipesName) {
            super(context);
            this.recipesName = recipesName;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Nullable
        @Override
        public List<Recipe> loadInBackground() {
            AppDatabaseUtils.insertRecipesNames(getContext(), recipesName);
            return null;
        }
    }

    @Override
    public void onBackPressed(){
        this.getSupportFragmentManager().popBackStack();
    }
}