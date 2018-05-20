package quagem.com.uba.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;

    public WidgetRemoteViewFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        // TODO: 5/20/2018 get jSon.
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 5; // TODO: 5/20/2018
    }

    @Override
    public RemoteViews getViewAt(int position) {
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
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
