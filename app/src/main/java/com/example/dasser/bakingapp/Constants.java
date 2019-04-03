package com.example.dasser.bakingapp;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public abstract class Constants {

    public final static String DATABASE_NAME = "recipe_db";

    public final static int LOADER_ID_MAIN_ACTIVITY = 0;
    public final static int LOADER_ID_DETAIL_ACTIVITY = 1;

    public final static String BUNDLE_RECIPE_NAMES = "recipe_names";
    public final static String BUNDLE_RECIPE_CLICKED_POSITION = "recipe_clicked_position";
    public final static String BUNDLE_STEPS_COMBINATION = "steps_combination";
    public final static String BUNDLE_STEPS_NUMBER = "steps_number";
    public final static String BUNDLE_STATE_RESUME_WINDOW = "resumeWindow";
    public final static String BUNDLE_STATE_RESUME_POSITION = "resumePosition";
    public final static String BUNDLE_STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    public final static String BUNDLE_WIDGET_COMBINATION = "widget_combination";

    final static String PREF_RECIPE_OF_THE_DAY_ID = "recipe_of_the_day_id";
    final static String WIDGET_PREF = "widget_pref";

    public final static String FILTER_DATA_IS_READY = "DATA_IS_READY";

    @IntDef({INGREDIENTS, STEPS})
    @Retention(RetentionPolicy.SOURCE)
    @interface  DetailViewType {}
    static final int INGREDIENTS = 0;
    static final int STEPS = 1;

    public abstract void setDetailViewType(@DetailViewType int type);

    @DetailViewType
    public abstract int getDetailViewType();
}
