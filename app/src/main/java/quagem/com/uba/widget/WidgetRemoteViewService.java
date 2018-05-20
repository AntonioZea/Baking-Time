package quagem.com.uba.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import quagem.com.uba.widget.WidgetRemoteViewFactory;

public class WidgetRemoteViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewFactory(this.getApplicationContext());
    }
}
