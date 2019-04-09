package com.example.dasser.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dasser.bakingapp.R;
import com.example.dasser.bakingapp.model.Combinations;
import com.example.dasser.bakingapp.model.Recipe;
import com.example.dasser.bakingapp.ui.OneStepDetailActivity;
import com.example.dasser.bakingapp.ui.OneStepDetailFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

import static com.example.dasser.bakingapp.Constants.BUNDLE_STEPS_COMBINATION;
import static com.example.dasser.bakingapp.Constants.BUNDLE_STEPS_NUMBER;


public class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter.ViewHolder> {

    private List<String> mShortDescription, mDescription, mVideoUrl, mThumbnailUrl;
    private boolean mTwoPane;
    private FragmentManager mFragmentManager;
    private CardView previousCard;
    public int previousPosition;

    public StepsRecyclerViewAdapter(List<Recipe.Steps> steps, boolean twoPane, FragmentManager fragmentManager) {
        mShortDescription = new ArrayList<>();
        mDescription = new ArrayList<>();
        mVideoUrl = new ArrayList<>();
        mThumbnailUrl = new ArrayList<>();
        mTwoPane = twoPane;
        mFragmentManager = fragmentManager;

        for (int i = 0; i < steps.size(); i++) {
            Recipe.Steps step = steps.get(i);

            mShortDescription.add(step.getShortdescription());
            mDescription.add(step.getDescription());
            mVideoUrl.add(step.getVideourl());
            mThumbnailUrl.add(step.getThumbnailurl());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView shortDescriptionTV;
        private final ImageView thumbnailIV;
        private final View view;
        private final CardView cardView;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            shortDescriptionTV = view.findViewById(R.id.textView_short_description);
            thumbnailIV = view.findViewById(R.id.imageView_thumbnail);
            cardView = view.findViewById(R.id.item_step_cardView);
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

                if (mTwoPane) {
                    if (previousCard != null)
                        previousCard.setCardBackgroundColor(Color.WHITE);
                    previousCard = holder.cardView;
                    previousPosition = position;
                    holder.cardView.setCardBackgroundColor(Color.GREEN);

                    Fragment fragment = new OneStepDetailFragment();
                    fragment.setArguments(getBundle());

                    mFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } else {
                    Intent intent = new Intent(context, OneStepDetailActivity.class);
                    intent.putExtras(getBundle());
                    context.startActivity(intent);
                }
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
                public void onSuccess() { /* do nothing */ }

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
