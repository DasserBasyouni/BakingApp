package com.example.dasser.bakingapp.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import com.example.dasser.bakingapp.R;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class RecipeDetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        boolean mTwoPane = getResources().getConfiguration().smallestScreenWidthDp >= 600;

        if (savedInstanceState == null) {
            Fragment fragment = new RecipeDetailFragment();

            if (mTwoPane){
                ((RecipeDetailFragment) Objects.requireNonNull(getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_recipe_detail)))
                        .setArgumentsThenStartLoader(getIntent().getExtras());

            } else {
                fragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
            }
        }
    }
}