package quagem.com.uba.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import quagem.com.uba.MainActivity;
import quagem.com.uba.R;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    private final static String TAG = WidgetProvider.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(TAG, "onReceive");

        final String action = intent.getAction();

        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            Log.i(TAG, "ACTION_APPWIDGET_UPDATE");

            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, WidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widget_list_view);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG, "onUpdate");

        for (int appWidgetId : appWidgetIds) {

            RemoteViews views =
                    new RemoteViews(context.getPackageName(), R.layout.widget_list);

            Intent intent = new Intent(context, WidgetRemoteViewService.class);
            views.setRemoteAdapter(R.id.widget_list_view, intent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.i(TAG, "onEnabled");
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG, "onDisabled");
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void sendRefreshBroadcast(Context context) {
        Log.i(TAG, "sendRefreshBroadcast");
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, WidgetProvider.class));
        context.sendBroadcast(intent);
    }


}

