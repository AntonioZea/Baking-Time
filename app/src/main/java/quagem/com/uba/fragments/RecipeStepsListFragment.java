package quagem.com.uba.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import quagem.com.uba.R;
import quagem.com.uba.adaptors.SimpleListAdaptor;
import quagem.com.uba.interfaces.ListItemSelectListener;
import quagem.com.uba.model.ListItem;

import static quagem.com.uba.MainActivity.JSON_EXTRA;
import static quagem.com.uba.RecipeDetailsActivity.SELECTED_RECIPE_ID;

public class RecipeStepsListFragment extends Fragment {

    public static final String TAG = RecipeListFragment.class.getSimpleName();

    private String mRecipeId;
    private ListItemSelectListener mCallBack;

    @BindView(R.id.recipe_steps_list_view) RecyclerView recyclerView;

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

        rootView = inflater.inflate(R.layout.fragment_recipe_steps_list,
                container, false);

        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();

        if (bundle != null &&
                bundle.containsKey(JSON_EXTRA) &&
                bundle.containsKey(SELECTED_RECIPE_ID)) {

            String mJsonData = bundle.getString(JSON_EXTRA);
            mRecipeId = bundle.getString(SELECTED_RECIPE_ID);

            SimpleListAdaptor recipeListAdaptor =
                    new SimpleListAdaptor(mCallBack, sparseJson(mJsonData));

            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(recipeListAdaptor);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        } else errorLoadingData();

        return rootView;
    }

    private void errorLoadingData(){
        Toast.makeText(getContext(), R.string.error_loading_data, Toast.LENGTH_LONG).show();
    }

    private List<ListItem> sparseJson(String json) {

        List<ListItem> listItemList;

        listItemList = new ArrayList<>();

        final String ID = "id";
        final String NAME = "name";
        final String STEPS = "steps";
        final String SHORT_DESCRIPTION = "shortDescription";

        // First add the ingredients as step #1 as displayed on udacity web page.
        ListItem listItem = new
                ListItem("-1", getResources().getString(R.string.ingredients));
        listItemList.add(listItem);

        try {

            final JSONArray recipes = new JSONArray(json);

            for (int i = 0; i < recipes.length(); i++) {

                JSONObject recipe = recipes.getJSONObject(i);

                if (recipe.getString(ID).equals(mRecipeId)) {

                    if (getActivity() != null) getActivity().setTitle(recipe.getString(NAME));

                    JSONArray steps = recipe.getJSONArray(STEPS);

                    for (int ii = 0; ii < steps.length(); ii++) {

                        JSONObject step = steps.getJSONObject(ii);

                        listItem = new ListItem(
                                step.getString(ID),
                                step.getString(SHORT_DESCRIPTION));

                        listItemList.add(listItem);
                    }

                    break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listItemList;
    }
}
