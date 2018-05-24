package quagem.com.uba.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import quagem.com.uba.MainActivity;
import quagem.com.uba.R;
import quagem.com.uba.RecipeDetailsActivity;
import quagem.com.uba.model.ListItem;

import static quagem.com.uba.MainActivity.SELECTED_RECIPE_NAME;
import static quagem.com.uba.RecipeDetailsActivity.SELECTED_RECIPE_ID;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    private final static String TAG = WidgetProvider.class.getSimpleName();

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.i(TAG, "updateAppWidget");

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String selectedRecipeName = sharedPref.getString(SELECTED_RECIPE_NAME, null);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list);

        if (selectedRecipeName != null)
            views.setTextViewText(R.id.widget_recipe_name, selectedRecipeName);

        Intent intent = new Intent(context, WidgetListService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent );

        Intent activityIntent = new Intent(context, RecipeDetailsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.widget_list_view, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG, "onUpdate");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds)
            updateAppWidget(context, appWidgetManager, appWidgetId);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

