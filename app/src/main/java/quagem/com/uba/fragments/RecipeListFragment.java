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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import quagem.com.uba.adaptors.RecipeListAdaptor;
import quagem.com.uba.model.Recipe;

public class RecipeListFragment extends Fragment {

    public static final String TAG = RecipeListFragment.class.getSimpleName();

    OnRecipeClickListener mCallBack;

    public interface OnRecipeClickListener {
        void onRecipeSelected(String recipeId);
    }

    @BindView(R.id.recipe_list_view) RecyclerView recyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Host activity implements callback.
        try {
            mCallBack = (OnRecipeClickListener) context;
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

            RecipeListAdaptor recipeListAdaptor =
                    new RecipeListAdaptor(mCallBack, sparseJson(json));

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

    private List<Recipe> sparseJson(String json) {

        List<Recipe> recipeList;

        recipeList = new ArrayList<>();

        final String ID = "id";
        final String NAME = "name";
        final String INGREDIENTS = "ingredients";
        final String QUANTITY = "quantity";
        final String MEASURE = "measure";
        final String INGREDIENT = "ingredient";
        final String STEPS = "steps";
        final String SHORT_DESCRIPTION = "shortDescription";
        final String DESCRIPTION = "description";
        final String VIDEO_URL = "videoURL";
        final String THUMBNAIL_URL = "thumbnailURL";

        try {

            final JSONArray recipes = new JSONArray(json);

            Recipe recipeData;

            for (int i = 0; i < recipes.length(); i++) {

                recipeData = new Recipe();

                JSONObject recipe = recipes.getJSONObject(i);

                recipeData.setName(recipe.getString(NAME));
                recipeData.setId(recipe.getString(ID));

                recipeList.add(recipeData);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeList;
    }

}
