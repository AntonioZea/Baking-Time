package quagem.com.uba;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import quagem.com.uba.fragments.IngredientsFragment;
import quagem.com.uba.fragments.RecipeStepDetailsFragment;
import quagem.com.uba.fragments.RecipeStepsListFragment;
import quagem.com.uba.interfaces.ListItemSelectListener;

import static quagem.com.uba.MainActivity.JSON_EXTRA;

public class RecipeDetailsActivity extends AppCompatActivity implements ListItemSelectListener {

    private final static String TAG = RecipeDetailsActivity.class.getSimpleName();

    public final static String SELECTED_RECIPE_ID = "selectedRecipeId";
    public final static String SELECTED_RECIPE_STEP_ID = "selectedRecipeStepId";
    public final static String TWO_PANE = "twoPane";

    private boolean mTwoPane;
    private String mRecipeId;

    private String mJsonData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_details);

        mTwoPane = findViewById(R.id.recipe_step_details_container) != null;

        Intent intent = getIntent();

        if (savedInstanceState != null){

            if (savedInstanceState.containsKey(JSON_EXTRA))
                mJsonData = savedInstanceState.getString(JSON_EXTRA);

            if (savedInstanceState.containsKey(SELECTED_RECIPE_ID))
                mRecipeId = savedInstanceState.getString(SELECTED_RECIPE_ID);

        } else {

            if (intent.hasExtra(JSON_EXTRA) && intent.hasExtra(SELECTED_RECIPE_ID)) {

                mJsonData = intent.getStringExtra(JSON_EXTRA);
                mRecipeId = intent.getStringExtra(SELECTED_RECIPE_ID);

                Bundle bundle = new Bundle();
                bundle.putString(JSON_EXTRA, mJsonData);
                bundle.putString(SELECTED_RECIPE_ID, mRecipeId);

                Fragment fragment = new RecipeStepsListFragment();
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.recipe_steps_container, fragment).commit();

                // Ingredients fragment id = -1.
                if (mTwoPane) inflateDetailsFragment("-1");

            } else {
                Toast.makeText(this, R.string.error_loading_data, Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }

    private void inflateDetailsFragment(String recipeStepId) {
        Log.i(TAG, "inflateDetailsFragment: " + recipeStepId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putBoolean(TWO_PANE, mTwoPane);
        bundle.putString(JSON_EXTRA, mJsonData);
        bundle.putString(SELECTED_RECIPE_ID, mRecipeId);
        bundle.putString(SELECTED_RECIPE_STEP_ID, recipeStepId);

        Fragment fragment;

        if (recipeStepId.equals("-1")) fragment = new IngredientsFragment(); //Ingredients fragment.
        else fragment = new RecipeStepDetailsFragment();

        fragment.setArguments(bundle);

        if (mTwoPane)
            fragmentTransaction.replace(R.id.recipe_step_details_container, fragment).commit();
        else
            fragmentTransaction.add(R.id.recipe_steps_container, fragment).addToBackStack(null)
                    .commit();
    }

    @Override
    public void OnListItemSelect(String itemId) {
        Log.i(TAG, "OnListItemSelect: " + itemId);
        inflateDetailsFragment(itemId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        if (mJsonData != null) outState.putString(JSON_EXTRA, mJsonData);
        if (mRecipeId != null) outState.putString(SELECTED_RECIPE_ID, mRecipeId);
    }
}
