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

        String jsonData = sharedPref.getString(MainActivity.JSON_EXTRA, null);
        String recipeId = sharedPref.getString(SELECTED_RECIPE_ID, null);

        String recipeName= "";
        String recipeIngredients = "";

        if (jsonData != null) {

            final String ID = "id";
            final String NAME = "name";
            final String MEASURE = "measure";
            final String QUANTITY = "quantity";
            final String INGREDIENT = "ingredient";
            final String INGREDIENTS = "ingredients";

            try {

                final JSONArray recipes = new JSONArray(jsonData);

                // if no recipe is selected, select first.
                if (recipeId == null) recipeId = recipes.getJSONObject(0).getString(ID);

                Log.i(TAG, "recipeId: " + recipeId);

                for (int i = 0; i < recipes.length(); i++) {

                    JSONObject recipe = recipes.getJSONObject(i);

                    if (recipe.getString(ID).equals(recipeId)) {

                        recipeName = recipe.getString(NAME);

                        JSONArray ingredients = recipe.getJSONArray(INGREDIENTS);

                        int ingredientCount = 0;
                        StringBuilder stringBuilder = new StringBuilder();

                        for (int ii = 0; ii < ingredients.length(); ii++) {

                            JSONObject ingredient = ingredients.getJSONObject(ii);

                            stringBuilder.append(++ingredientCount);
                            stringBuilder.append(". ");
                            stringBuilder.append(ingredient.getString(INGREDIENT));
                            stringBuilder.append(" ");
                            stringBuilder.append(ingredient.getString(QUANTITY));
                            stringBuilder.append(" ");
                            stringBuilder.append(ingredient.getString(MEASURE));
                            stringBuilder.append(" ");
                            stringBuilder.append("\n");
                        }

                        recipeIngredients = stringBuilder.toString();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        views.setTextViewText(R.id.widget_ingredients, recipeIngredients);

//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list);
//        Intent intent = new Intent(context, WidgetListService.class);
//        views.setRemoteAdapter(R.id.widget_list_view, intent );
//
//        Intent activityIntent = new Intent(context, RecipeDetailsActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
//                activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        views.setPendingIntentTemplate(R.id.widget_list_view, pendingIntent);

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

