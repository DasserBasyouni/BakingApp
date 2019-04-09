package com.example.dasser.bakingapp.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dasser.bakingapp.R;

public class OneStepDetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_container);

        if (savedInstanceState == null) {
            Fragment fragment = new OneStepDetailFragment();
            fragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameLayout_activities_container, fragment).commit();
        }
    }
}