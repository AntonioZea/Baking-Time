package quagem.com.uba.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import quagem.com.uba.MainActivity;
import quagem.com.uba.R;
import quagem.com.uba.model.ListItem;

import static quagem.com.uba.MainActivity.JSON_EXTRA;
import static quagem.com.uba.RecipeDetailsActivity.SELECTED_RECIPE_ID;

public class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private final static String TAG = WidgetRemoteViewFactory.class.getSimpleName();

    private Context mContext;
    private String mJsonData;
    private List<ListItem> mWidgetItemList;

    WidgetRemoteViewFactory(Context context) {
        mContext = context;

        SharedPreferences sharedPref = mContext.getSharedPreferences(
                mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        mJsonData = sharedPref.getString(MainActivity.JSON_EXTRA, null);
        if (mJsonData != null) mWidgetItemList = sparseJson(mJsonData);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        Log.i(TAG, "onDataSetChanged");


    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mWidgetItemList != null) return mWidgetItemList.size();
        else return 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
            views.setTextViewText(R.id.widget_text_view, "No data");

        if (mJsonData != null && mWidgetItemList != null) {

            Bundle bundle = new Bundle();
            bundle.putString(JSON_EXTRA, mJsonData);
            bundle.putString(SELECTED_RECIPE_ID, mWidgetItemList.get(position).getId());

            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(bundle);

            views.setTextViewText(R.id.widget_text_view, mWidgetItemList.get(position).getName());
            views.setOnClickFillInIntent(R.id.widget_text_view, fillInIntent);
        }

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

    private List<ListItem> sparseJson(String json) {

        List<ListItem> listItemList;

        listItemList = new ArrayList<>();

        final String ID = "id";
        final String NAME = "name";

        try {

            final JSONArray recipes = new JSONArray(json);

            ListItem listItemData;

            for (int i = 0; i < recipes.length(); i++) {

                listItemData = new ListItem();

                JSONObject recipe = recipes.getJSONObject(i);

                listItemData.setName(recipe.getString(NAME));
                listItemData.setId(recipe.getString(ID));

                listItemList.add(listItemData);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listItemList;
    }

}
