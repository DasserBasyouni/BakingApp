package com.example.dasser.bakingapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.dasser.bakingapp.R;
import com.example.dasser.bakingapp.model.Combinations;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.dasser.bakingapp.Constants.BUNDLE_STATE_RESUME_POSITION;
import static com.example.dasser.bakingapp.Constants.BUNDLE_STATE_RESUME_WINDOW;
import static com.example.dasser.bakingapp.Constants.BUNDLE_STEPS_COMBINATION;
import static com.example.dasser.bakingapp.Constants.BUNDLE_STEPS_NUMBER;

public class OneStepDetailFragment extends Fragment {

    @BindView(R.id.playerView_video) PlayerView recipeVideo_PV;
    @BindView(R.id.textView_description) TextView description_TV;
    @BindView(R.id.textView_label) TextView descriptionLabel_TV;
    @BindView(R.id.scrollview) ScrollView mScrollView;
    @BindView(R.id.button_previous) Button mPrevious_btn;
    @BindView(R.id.button_next) Button mNext_btn;
    @BindView(R.id.progressBar_step_fragment) ProgressBar mProgressBar;
    @BindView(R.id.textView_no_video) TextView mNoVideo_TV;

    private int mStepNumber;
    private SimpleExoPlayer player;
    private DefaultDataSourceFactory dataSourceFactory;
    private ExtractorMediaSource.EventListener eventListener;
    private List<String> urls, descriptions;

    private boolean mPhoneSceneIsInLandscape = false;
    private int mResumeWindow;
    private long mResumePosition;
    private Dialog mFullScreenDialog;
    private Combinations.RecipeDescriptionsAndUrlsCombination combination;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, view);

        mPhoneSceneIsInLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE
                && getResources().getConfiguration().smallestScreenWidthDp < 600 ;

        Bundle bundle = getArguments();
        if (bundle != null) {
            mStepNumber = bundle.getInt(BUNDLE_STEPS_NUMBER);
            combination = bundle.getParcelable(BUNDLE_STEPS_COMBINATION);
        }

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(BUNDLE_STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(BUNDLE_STATE_RESUME_POSITION);

            boolean containsSavedId = savedInstanceState.containsKey(BUNDLE_STEPS_NUMBER);
            if (containsSavedId) {
                mStepNumber = savedInstanceState.getInt(BUNDLE_STEPS_NUMBER);
            }
        }

        updateActivityTitle();

        if (combination != null) {
            initialiseViews(combination);
            checkIfButtonsNeedDisable();
        }

        return view;
    }

    private void updateActivityTitle(){
        Activity activity = getActivity();
        if (activity != null)
            activity.setTitle(getString(R.string.recipe_step_number, mStepNumber + 1));
    }

    private void initialiseViews(Combinations.RecipeDescriptionsAndUrlsCombination combination) {
        descriptions = combination.getDescriptions();
        urls = combination.getUrls();

        initialisePlayer();
        initialiseButtons();
    }

    private void setDescriptionText() {
        String description = descriptions.get(mStepNumber);
        if (description.isEmpty())
            description = getString(R.string.msg_no_description);
        description_TV.setText(description);

        mScrollView.setVisibility(View.VISIBLE);
        descriptionLabel_TV.setVisibility(View.VISIBLE);
    }

    private void initialiseButtons() {
        mNext_btn.setOnClickListener(v -> {
            if (mStepNumber != urls.size() - 1) {
                mStepNumber++;
                checkIfButtonsNeedDisable();
                updateActivityTitle();
                player.prepare(getMediaSourceAndInitialiseViews());
            }
        });

        mPrevious_btn.setOnClickListener(v -> {
            if (mStepNumber != 0) {
                mStepNumber--;
                checkIfButtonsNeedDisable();
                updateActivityTitle();
                player.prepare(getMediaSourceAndInitialiseViews());
            }

        });
    }

    private void checkIfButtonsNeedDisable() {
        if (mStepNumber == 0)
            mPrevious_btn.setEnabled(false);
        else
            mPrevious_btn.setEnabled(true);

        if (mStepNumber == urls.size() - 1)
            mNext_btn.setEnabled(false);
        else
            mNext_btn.setEnabled(true);
    }

    private void initialisePlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        recipeVideo_PV.setUseController(true);
        recipeVideo_PV.requestFocus();
        player.setPlayWhenReady(true);
        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                setDescriptionText();
                if (playbackState != Player.STATE_IDLE) {
                    if (!mPhoneSceneIsInLandscape)
                        restorePlayerHeight();
                    displayVideoAndDescriptionViewsAndHideLoading();
                } else {
                    recipeVideo_PV.setVisibility(View.INVISIBLE);
                    if (!mPhoneSceneIsInLandscape)
                        minimizePlayerHeight();
                }
            }
        });

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition)
            player.seekTo(mResumeWindow, mResumePosition);

        recipeVideo_PV.setPlayer(player);

        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
        dataSourceFactory = new DefaultDataSourceFactory(
                Objects.requireNonNull(getContext()), Util.getUserAgent(getContext(),
                 "Backing App"), bandwidthMeterA);

        eventListener = error ->
                Timber.e("initialisePlayer Error: %s", error.getLocalizedMessage());
        player.prepare(getMediaSourceAndInitialiseViews());
    }

    private void displayVideoAndDescriptionViewsAndHideLoading() {
        recipeVideo_PV.setVisibility(View.VISIBLE);
        descriptionLabel_TV.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.VISIBLE);
        mPrevious_btn.setVisibility(View.VISIBLE);
        mNext_btn.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    private synchronized MediaSource getMediaSourceAndInitialiseViews() {
        String url = urls.get(mStepNumber);
        if (url.isEmpty()) {
            minimizePlayerHeight();
            setDescriptionText();
            mNoVideo_TV.setVisibility(View.VISIBLE);
            displayVideoAndDescriptionViewsAndHideLoading();
        } else {
            mNoVideo_TV.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
        recipeVideo_PV.setVisibility(View.INVISIBLE);

        return new ExtractorMediaSource(Uri.parse(url),
                dataSourceFactory,
                new DefaultExtractorsFactory(),
                1,
                new Handler(),
                eventListener,
                "",
                1);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(BUNDLE_STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(BUNDLE_STATE_RESUME_POSITION, mResumePosition);
        outState.putInt(BUNDLE_STEPS_NUMBER, mStepNumber);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        // according to https://github.com/brianwernick/ExoMedia/issues/425
        // The VideoView automatically handles releasing when appropriate (onDetachedFromWindow())
        // and shouldn't require any additional thought towards onStart/onStop of the parent Fragment or Activity.
        // TODO (2) is this commented code is the appropriate way ?
        //  player.stop();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (recipeVideo_PV == null)
            initialisePlayer();

        initFullscreenDialog();

        if (combination != null && !urls.get(mStepNumber).isEmpty()) {
            int uiOptions;
            if (mPhoneSceneIsInLandscape) {
                uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                ((ViewGroup) recipeVideo_PV.getParent()).removeView(recipeVideo_PV);
                mFullScreenDialog.addContentView(recipeVideo_PV, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mFullScreenDialog.show();
            } else {
                uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
                ((ViewGroup) recipeVideo_PV.getParent()).removeView(recipeVideo_PV);
                ((ConstraintLayout) Objects.requireNonNull(getActivity())
                        .findViewById(R.id.constraint_step)).addView(recipeVideo_PV);
                mFullScreenDialog.dismiss();
            }
            Objects.requireNonNull(getActivity()).getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (recipeVideo_PV != null && recipeVideo_PV.getPlayer() != null) {
            mResumeWindow = recipeVideo_PV.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, recipeVideo_PV.getPlayer().getContentPosition());
            recipeVideo_PV.getPlayer().release();
        }
    }

    private void initFullscreenDialog() {
        mFullScreenDialog = new Dialog(Objects.requireNonNull(getContext()),
                android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                /*if (mPhoneSceneIsInLandscape)
                    closeFullscreenDialog();*/
                super.onBackPressed();
            }
        };
    }

    private void minimizePlayerHeight() {
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                getResources().getDisplayMetrics());
        recipeVideo_PV.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, height));
    }

    private void restorePlayerHeight() {
        recipeVideo_PV.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
    }
}