package quagem.com.uba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeDetailsActivity extends AppCompatActivity {

    private final static String TAG = RecipeDetailsActivity.class.getSimpleName();

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_details);

        // Two pane mode.
        //mTwoPane = findViewById(R.id.recipe_details_fragment_container) != null;
    }
}
