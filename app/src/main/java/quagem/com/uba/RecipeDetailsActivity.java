package quagem.com.uba;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import quagem.com.uba.fragments.RecipeListFragment;
import quagem.com.uba.fragments.RecipeStepsListFragment;
import quagem.com.uba.interfaces.ListItemSelectListener;

import static quagem.com.uba.MainActivity.JSON_EXTRA;

public class RecipeDetailsActivity extends AppCompatActivity implements ListItemSelectListener {

    private final static String TAG = RecipeDetailsActivity.class.getSimpleName();

    public final static String SELECTED_RECIPE_ID = "selectedRecipeId";

    private boolean mTwoPane;
    private String mRecipeId;
    private String mJsonData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_details);

        Intent intent = getIntent();

        if (intent.hasExtra(JSON_EXTRA) && intent.hasExtra(SELECTED_RECIPE_ID)) {

            mJsonData = intent.getStringExtra(JSON_EXTRA);
            mRecipeId = intent.getStringExtra(SELECTED_RECIPE_ID);

            // Two pane mode.
            //mTwoPane = findViewById(R.id.recipe_details_fragment_container) != null;

            Bundle bundle = new Bundle();
            bundle.putString(JSON_EXTRA, mJsonData);
            bundle.putString(SELECTED_RECIPE_ID, mRecipeId);

            Fragment fragment = new RecipeStepsListFragment();
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.recipe_steps_container, fragment).commit();

        } else {
            Toast.makeText(this, R.string.error_loading_data, Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public void OnListItemSelect(String itemId) {
        // TODO: 5/13/2018
    }
}
