package quagem.com.uba.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class WidgetService extends IntentService {

    private final static String TAG = WidgetService.class.getSimpleName();
    public static final String ACTION_UPDATE_WIDGET =
            "com.quagem.uba.action.update.widget";

    public WidgetService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "onHandleIntent");

        if (intent != null && ACTION_UPDATE_WIDGET.equals(intent.getAction()))
            handleActionUpdateWidget();
    }

    public static void startActionUpdateWidget(Context context) {
        Log.i(TAG, "startActionUpdateWidget");

        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    private void handleActionUpdateWidget() {
        Log.i(TAG, "handleActionOpenRecipeDetails");
        // TODO: 5/22/2018
    }
}
