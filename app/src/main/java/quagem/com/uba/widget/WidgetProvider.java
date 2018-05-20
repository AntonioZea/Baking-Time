package quagem.com.uba.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import quagem.com.uba.MainActivity;
import quagem.com.uba.R;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
            WidgetUpdateService.startActionUpdateList(context);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            RemoteViews views =
                    new RemoteViews(context.getPackageName(), R.layout.widget_list);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 0, intent, 0);

            Intent fillInIntent = new Intent(context, MainActivity.class);

            views.setPendingIntentTemplate(R.id.recipe_list_view_widget, pendingIntent);
            views.setOnClickFillInIntent(R.id.recipe_list_view_widget, fillInIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
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
}

