package quagem.com.uba.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class WidgetListService extends RemoteViewsService {

    private final static String TAG = WidgetListService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i(TAG, "onGetViewFactory");
        return new WidgetRemoteViewFactory(this.getApplicationContext());
    }
}
