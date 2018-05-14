package quagem.com.uba.fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import quagem.com.uba.R;

import static quagem.com.uba.MainActivity.JSON_EXTRA;
import static quagem.com.uba.RecipeDetailsActivity.SELECTED_RECIPE_ID;

public class IngredientsFragment extends Fragment {

    public static final String TAG = RecipeListFragment.class.getSimpleName();

    private String mRecipeId;
    @BindView(R.id.ingredients_text_view) TextView mIngredientsTextView;

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

        rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();

        if (bundle != null &&
                bundle.containsKey(JSON_EXTRA) &&
                bundle.containsKey(SELECTED_RECIPE_ID)) {

            String mJson = bundle.getString(JSON_EXTRA);
            mRecipeId = bundle.getString(SELECTED_RECIPE_ID);

            mIngredientsTextView.setText(sparseJson(mJson));


        } else Toast.makeText(getContext(), R.string.error_loading_data, Toast.LENGTH_LONG).show();

        return rootView;
    }

    private String sparseJson(String json) {

        StringBuilder stringBuilder = new StringBuilder();

        final String ID = "id";
        final String NAME = "name";
        final String MEASURE = "measure";
        final String QUANTITY = "quantity";
        final String INGREDIENT = "ingredient";
        final String INGREDIENTS = "ingredients";

        try {

            final JSONArray recipes = new JSONArray(json);

            for (int i = 0; i < recipes.length(); i++) {

                JSONObject recipe = recipes.getJSONObject(i);

                if (recipe.getString(ID).equals(mRecipeId)) {

                    if (getActivity() != null) getActivity().setTitle(recipe.getString(NAME));

                    JSONArray ingredients = recipe.getJSONArray(INGREDIENTS);

                    int ingredientCount = 0;

                    for (int ii = 0; ii < ingredients.length(); ii++) {

                        JSONObject ingredient = ingredients.getJSONObject(ii);

                        stringBuilder.append(++ingredientCount);
                        stringBuilder.append(". ");
                        stringBuilder.append(ingredient.getString(INGREDIENT));
                        stringBuilder.append(" ");
                        stringBuilder.append(ingredient.getString(QUANTITY));
                        stringBuilder.append(" ");
                        stringBuilder.append(ingredient.getString(MEASURE));
                        stringBuilder.append(" ");
                        stringBuilder.append("\n");
                    }

                    break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

}
