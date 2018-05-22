package quagem.com.uba.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import quagem.com.uba.MainActivity;
import quagem.com.uba.R;

import static quagem.com.uba.RecipeDetailsActivity.SELECTED_RECIPE_ID;

public class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private final static String TAG = WidgetRemoteViewFactory.class.getSimpleName();

    private Context mContext;

    WidgetRemoteViewFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        Log.i(TAG, "onDataSetChanged");

        // TODO: 5/22/2018 get json data.
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 15;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // TODO: 5/22/2018

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        views.setTextViewText(R.id.widget_text_view, "FUCKING TEST");

        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.JSON_EXTRA, "");
        bundle.putString(SELECTED_RECIPE_ID, "");

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(bundle);

        views.setOnClickFillInIntent(R.id.widget_text_view, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
