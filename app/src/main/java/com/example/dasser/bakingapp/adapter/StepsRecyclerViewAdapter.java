package com.example.dasser.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dasser.bakingapp.R;
import com.example.dasser.bakingapp.model.Combinations;
import com.example.dasser.bakingapp.model.Recipe;
import com.example.dasser.bakingapp.ui.StepsActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.example.dasser.bakingapp.Constants.BUNDLE_STEPS_COMBINATION;
import static com.example.dasser.bakingapp.Constants.BUNDLE_STEPS_NUMBER;


public class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter.ViewHolder> {

    private List<String> mShortDescription, mDescription, mVideoUrl, mThumbnailUrl;

    public StepsRecyclerViewAdapter(List<Recipe.Steps> steps) {
        mShortDescription = new ArrayList<>();
        mDescription = new ArrayList<>();
        mVideoUrl = new ArrayList<>();
        mThumbnailUrl = new ArrayList<>();

        for (int i = 0; i < steps.size(); i++) {
            Recipe.Steps step = steps.get(i);

            mShortDescription.add(step.getShortdescription());
            mDescription.add(step.getDescription());
            mVideoUrl.add(step.getVideourl());
            mThumbnailUrl.add(step.getThumbnailurl());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView shortDescriptionTV;
        final ImageView thumbnailIV;
        final View view;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            shortDescriptionTV = view.findViewById(R.id.textView_short_description);
            thumbnailIV = view.findViewById(R.id.imageView_thumbnail);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.shortDescriptionTV.setText(mShortDescription.get(position));

        holder.view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                Intent intent = new Intent(context, StepsActivity.class);
                intent.putExtras(getBundle());
                context.startActivity(intent);
            }

            Bundle getBundle() {
                Bundle bundle = new Bundle();

                bundle.putParcelable(BUNDLE_STEPS_COMBINATION,
                        new Combinations.RecipeDescriptionsAndUrlsCombination(mDescription,
                                mVideoUrl));
                bundle.putInt(BUNDLE_STEPS_NUMBER, position);

                return bundle;
            }
        });


        String thumbnailUrl = mThumbnailUrl.get(position);
        if (!thumbnailUrl.isEmpty()){
            holder.thumbnailIV.setVisibility(View.VISIBLE);
            Picasso.get().load(thumbnailUrl).into(holder.thumbnailIV, new Callback() {
                @Override
                public void onSuccess() {}

                @Override
                public void onError(Exception e) {
                    Timber.d("thumbnailIV error placing image: %s", e.getLocalizedMessage());
                    holder.thumbnailIV.setVisibility(View.GONE);
                }
            });
        }else
            holder.thumbnailIV.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return mShortDescription.size();
    }

}
