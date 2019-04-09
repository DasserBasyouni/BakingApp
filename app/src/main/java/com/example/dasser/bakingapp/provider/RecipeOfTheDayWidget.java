package com.example.dasser.bakingapp.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.dasser.bakingapp.R;
import com.example.dasser.bakingapp.Utils;
import com.example.dasser.bakingapp.database.AppDatabaseUtils;
import com.example.dasser.bakingapp.ui.RecipeDetailActivity;

import static com.example.dasser.bakingapp.Constants.BUNDLE_RECIPE_CLICKED_POSITION;
import static com.example.dasser.bakingapp.Utils.getWidgetIngredientsFormat;


public class RecipeOfTheDayWidget extends AppWidgetProvider {
    Context context;
    //BroadcastReceiver broadcastReceiver;
    //IntentFilter dataIsReadyFilter;

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        this.context = context;

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_of_the_day);

        // TODO (1) I allowing allowMainThreadQueries() to do those widget queries, is there is a better way?
        int id = Utils.getRecipeOfTheDayIdAndPlusOneId(context);
        String name = AppDatabaseUtils.getRecipesNames(context).get(id);
        String widgetIngredientsFormat = getWidgetIngredientsFormat(context, id);

        views.setTextViewText(R.id.textView_widget_recipe_name, name);
        views.setTextViewText(R.id.textView_widget_recipe_ingredients, widgetIngredientsFormat
                .replaceAll("\\((.*?)\\)", ""));

        // service with broadcast that setTextViewText() is not setting the text
        //Intent serviceIntent = new Intent(context, GetWidgetData.class);
        //context.startService(serviceIntent);

        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_RECIPE_CLICKED_POSITION, id);

        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0
                , intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.frameLayout_widget, pendingIntent);

        /*BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Combinations.RecipeNameAndIngredients combination =
                            bundle.getParcelable(BUNDLE_WIDGET_COMBINATION);
                    views.setTextViewText(R.id.textView_widget_recipe_name, combination.getName());
                    views.setTextViewText(R.id.textView_widget_recipe_ingredients, combination.getIngredients());
                    context.unregisterReceiver(this);
                }
            }
        };

        dataIsReadyFilter = new IntentFilter(FILTER_DATA_IS_READY);
        context.getApplicationContext().registerReceiver(broadcastReceiver, dataIsReadyFilter);*/

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    /*public static class GetWidgetData extends IntentService {

        public GetWidgetData() {
            super("GetWidgetData");
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            int id = Utils.getRecipeOfTheDayIdAndPlusOneId(this);

            String name = AppDatabaseUtils.getRecipesNames(this).get(id);
            String widgetIngredientsFormat = getWidgetIngredientsFormat(this, id);

            Bundle bundle = new Bundle();
            bundle.putParcelable(BUNDLE_WIDGET_COMBINATION,
                    new Combinations.RecipeNameAndIngredients(name, widgetIngredientsFormat));

            Intent i = new Intent();
            i.putExtras(bundle);
            i.setAction(FILTER_DATA_IS_READY);
            sendBroadcast(i);
        }
    }*/
}