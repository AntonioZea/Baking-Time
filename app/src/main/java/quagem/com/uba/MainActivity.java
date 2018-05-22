package quagem.com.uba;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import quagem.com.uba.fragments.RecipeListFragment;
import quagem.com.uba.interfaces.ListItemSelectListener;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String>, ListItemSelectListener {

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static int LOADER_ID = 101;

    public final static String JSON_EXTRA = "jsonExtra";

    private String mJsonData = null;

    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // First check if there is an instant saved and contains json data.
        // Else, if there is a connection try to get network data.
        // Finally display error message if every other possibility fails.

        // This will work even if devise is rotated while loading data and
        // only access network once by passing the json data to persistent fragments
        // so they can parse data as needed by their views.

        if (savedInstanceState != null && savedInstanceState.containsKey(JSON_EXTRA)) {
            // Save json data from saved instant state.
            mJsonData = savedInstanceState.getString(JSON_EXTRA);
            progressBar.setVisibility(View.GONE);

        } else if (isConnected()) {
            // When loader finishes, the RecipeListFragment is inflated.
            // This will only happen once.
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        }
        else Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        if (mJsonData != null) outState.putString(JSON_EXTRA, mJsonData);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i(TAG, "onCreateLoader");

        progressBar.setVisibility(View.VISIBLE);
        return new RecipeListAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        Log.i(TAG, "onLoadFinished");

        mJsonData = data; // save json network data.

        Bundle bundle = new Bundle();
        bundle.putString(JSON_EXTRA, mJsonData);

        Fragment fragment = new RecipeListFragment();
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.recipe_list_container, fragment).commit();

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    private boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager)this.
                getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        }

        return false;
    }

    @Override
    public void OnListItemSelect(String itemId) {
        Log.i(TAG, "ListItem clicked!: " + itemId);

        Intent intent = new Intent(this, RecipeDetailsActivity.class);

        intent.putExtra(MainActivity.JSON_EXTRA, mJsonData);
        intent.putExtra(RecipeDetailsActivity.SELECTED_RECIPE_ID, itemId);

        startActivity(intent);
    }

    public static class RecipeListAsyncTaskLoader extends
            AsyncTaskLoader<String> {

        private static final String TAG = RecipeListAsyncTaskLoader.class.getSimpleName();

        private String results = null;

        RecipeListAsyncTaskLoader(@NonNull Context context) {
            super(context);
            Log.i(TAG, "RecipeListAsyncTaskLoader");
        }

        @Override
        protected void onStartLoading() {
            Log.i(TAG, "onStartLoadingData");

            if (results == null) forceLoad(); // New data.
            else deliverResult(results);      // Cached data.
        }

        @Nullable
        @Override
        public String loadInBackground() {
            Log.i(TAG, "loadInBackground");

            HttpURLConnection connection;

            try {

                URL url = new URL("https://d17h27t6h515a5.cloudfront.net/topher/" +
                        "2017/May/59121517_baking/baking.json");

                connection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = connection.getInputStream();

                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");

                if (scanner.hasNext()) results = scanner.next();

                return results;

            } catch (IOException e) {
                // TODO: 5/5/2018 Notify user.
                e.printStackTrace();
            }

            return results;
        }

    }
}
