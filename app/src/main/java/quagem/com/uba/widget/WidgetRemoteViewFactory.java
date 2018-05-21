package quagem.com.uba.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import quagem.com.uba.R;

public class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private final static String TAG = WidgetRemoteViewFactory.class.getSimpleName();

    private Context mContext;
    private List<String> test;

    public WidgetRemoteViewFactory(Context mContext, Intent intent) {
        Log.i(TAG, "WidgetRemoteViewFactory");
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDataSetChanged() {
        Log.i(TAG, "onDataSetChanged");
        // TODO: 5/20/2018 get jSon.
        test = new ArrayList<>(5);
        test.add("test 1");
        test.add("test 2");
        test.add("test 3");
        test.add("test 4");
        test.add("test 5");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
    }

    @Override
    public int getCount() {
        Log.i(TAG, "getCount");
        return 5; // TODO: 5/20/2018
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.i(TAG, "getViewAt");
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        views.setTextViewText(R.id.widget_text_view, "FUCK!!!!!");

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        Log.i(TAG, "getLoadingView");
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
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
