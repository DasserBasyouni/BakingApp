package com.example.dasser.bakingapp;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public abstract class Constants {

    public final static String DATABASE_NAME = "recipe_db";

    public final static int LOADER_ID_MAIN_ACTIVITY = 0;
    public final static int LOADER_ID_DETAIL_ACTIVITY = 1;

    public final static String BUNDLE_RECIPE_NAMES = "recipe_names";
    public final static String BUNDLE_RECIPE_CLICKED_POSITION = "recipe_clicked_position";
    public final static String BUNDLE_STEP_VIDEO_URL = "step_video_url";
    public final static String BUNDLE_STEP_DESCRIPTION = "step_description";

    @IntDef({INGREDIENTS, STEPS})
    @Retention(RetentionPolicy.SOURCE)
    @interface  DetailViewType {}
    static final int INGREDIENTS = 0;
    static final int STEPS = 1;

    public abstract void setDetailViewType(@DetailViewType int type);

    @DetailViewType
    public abstract int getDetailViewType();
}
