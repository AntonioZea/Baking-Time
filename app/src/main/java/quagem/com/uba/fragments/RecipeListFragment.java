package quagem.com.uba.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import quagem.com.uba.MainActivity;
import quagem.com.uba.R;
import quagem.com.uba.adaptors.SimpleListAdaptor;
import quagem.com.uba.interfaces.ListItemSelectListener;
import quagem.com.uba.model.ListItem;

public class RecipeListFragment extends Fragment {

    public static final String TAG = RecipeListFragment.class.getSimpleName();

    ListItemSelectListener mCallBack;

    @BindView(R.id.recipe_list_view) RecyclerView recyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Host activity implements callback.
        try {
            mCallBack = (ListItemSelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " need to implement callback");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        View rootView;

        rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments() == null || !getArguments().containsKey(MainActivity.JSON_EXTRA))
            Toast.makeText(getContext(), R.string.error_loading_data, Toast.LENGTH_LONG).show();
        else {

            String json = getArguments().getString(MainActivity.JSON_EXTRA);

            SimpleListAdaptor recipeListAdaptor =
                    new SimpleListAdaptor(mCallBack, sparseJson(json));

            RecyclerView.LayoutManager layoutManager;

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                layoutManager = new GridLayoutManager(getContext(),2);
            else
                layoutManager = new LinearLayoutManager(getContext());

            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(recipeListAdaptor);
            recyclerView.setLayoutManager(layoutManager);
        }

        return rootView;
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
