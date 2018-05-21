package quagem.com.uba.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import quagem.com.uba.widget.WidgetRemoteViewFactory;

public class WidgetRemoteViewService extends RemoteViewsService {

    private final static String TAG = WidgetRemoteViewService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i(TAG, "onGetViewFactory");
        return new WidgetRemoteViewFactory(this.getApplicationContext(), intent);
    }
}
