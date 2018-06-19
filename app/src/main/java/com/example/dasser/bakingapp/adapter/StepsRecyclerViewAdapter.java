package com.example.dasser.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dasser.bakingapp.R;
import com.example.dasser.bakingapp.model.Recipe;
import com.example.dasser.bakingapp.ui.StepsFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.example.dasser.bakingapp.Constants.BUNDLE_STEP_DESCRIPTION;
import static com.example.dasser.bakingapp.Constants.BUNDLE_STEP_VIDEO_URL;


public class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter.ViewHolder> {

    private List<String> mShortDescription, mDescription, mVideoUrl, mThumbnailUrl;

    public StepsRecyclerViewAdapter(List<Recipe.Steps> steps) {
        for (int i = 0; i < steps.size(); i++) {
            Recipe.Steps step = steps.get(i);

            mShortDescription = new ArrayList<>();
            mDescription = new ArrayList<>();
            mVideoUrl = new ArrayList<>();
            mThumbnailUrl = new ArrayList<>();

            mShortDescription.add(step.getShortdescription());
            mDescription.add(step.getShortdescription());
            mVideoUrl.add(step.getShortdescription());
            mThumbnailUrl.add(step.getShortdescription());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView shortDescriptionTV;
        final ImageView thumbnailIV;
        final View view;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            shortDescriptionTV = view.findViewById(R.id.textView_recipe_name);
            thumbnailIV = view.findViewById(R.id.imageView_random_design);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredients, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.shortDescriptionTV.setText(mShortDescription.get(position));

        holder.view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new StepsFragment();
                fragment.setArguments(getBundle());

                FragmentActivity activity = (FragmentActivity) holder.view.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
            }

            Bundle getBundle() {
                Bundle bundle = new Bundle();

                String videoUrl = mVideoUrl.get(position);
                String description = mDescription.get(position);

                if (!videoUrl.isEmpty())
                    bundle.putString(BUNDLE_STEP_VIDEO_URL, mVideoUrl.get(position));
                if (!description.isEmpty())
                    bundle.putString(BUNDLE_STEP_DESCRIPTION, mDescription.get(position));

                return bundle;
            }
        });



        String thumbnailUrl = mThumbnailUrl.get(position);
        if (!thumbnailUrl.isEmpty()){
            holder.thumbnailIV.setVisibility(View.VISIBLE);
            Picasso.get().load(thumbnailUrl).into(holder.thumbnailIV, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Timber.d("thumbnailIV error placing image: %s", e.getLocalizedMessage());
                    holder.thumbnailIV.setVisibility(View.GONE);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mShortDescription.size();
    }

}
