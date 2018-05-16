package quagem.com.uba.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import quagem.com.uba.R;
import quagem.com.uba.model.RecipeStep;

import static quagem.com.uba.MainActivity.JSON_EXTRA;
import static quagem.com.uba.RecipeDetailsActivity.SELECTED_RECIPE_ID;
import static quagem.com.uba.RecipeDetailsActivity.SELECTED_RECIPE_STEP_ID;

public class RecipeStepDetails extends Fragment {

    public static final String TAG = RecipeStepDetails.class.getSimpleName();

    private String mRecipeId;
    private String mCurrentStepId;
    private SimpleExoPlayer mExoPlayer;

    private List<RecipeStep> recipeSteps;

    // Mime type
    private static final String VIDEO = "video";
    private static final String IMAGE = "image";

    @BindView(R.id.recipe_step_image_view) ImageView mImageView;
    @BindView(R.id.recipe_step_video) SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.recipe_instructions_text_view) TextView mInstructionsTextView;

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

        rootView = inflater.inflate(R.layout.fragment_recipe_step_details,
                container, false);

        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();

        if (bundle != null &&
                bundle.containsKey(JSON_EXTRA) &&
                bundle.containsKey(SELECTED_RECIPE_ID) &&
                bundle.containsKey(SELECTED_RECIPE_STEP_ID)) {

            mCurrentStepId = bundle.getString(SELECTED_RECIPE_STEP_ID);
            mRecipeId = bundle.getString(SELECTED_RECIPE_ID);
            sparseJson(bundle.getString(JSON_EXTRA));

            // ExoPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    getActivity(), trackSelector, loadControl);

            mExoPlayerView.setPlayer(mExoPlayer);

            displayStep();

        } else Toast.makeText(getContext(), R.string.error_loading_data, Toast.LENGTH_LONG).show();

        return rootView;
    }

    private void displayStep() {

        RecipeStep recipeStep = null;

        /* Recipe id is written in the json and is not the position in the list so need to match
        id by searching. */

        for (RecipeStep rs : recipeSteps) {
            if (rs.getId().equals(mCurrentStepId)) {
                recipeStep = rs;
                break;
            }
        }

        if (recipeStep != null) {

            mInstructionsTextView.setText(recipeStep.getDescription());

            // Thumbnail
            // There where no thumbnails in the json so i could not see sizes.
            // No placeholder or error because imageView will not be visible if no URL.
            if (recipeStep.getThumbnailURL() != null) {
                Picasso.with(getContext())
                        .load(recipeStep.getThumbnailURL())
                        .into(mImageView);

                mImageView.setVisibility(View.VISIBLE);

            } else mImageView.setVisibility(View.GONE);

            // Video
            if (getContext() != null && recipeStep.getVideoURL() != null) {

                MediaSource mediaSource = new ExtractorMediaSource(
                        Uri.parse(recipeStep.getVideoURL()),
                        new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(),
                                null)),
                        new DefaultExtractorsFactory(), null, null);

                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);

                mExoPlayerView.setVisibility(View.VISIBLE);

            } else mExoPlayerView.setVisibility(View.GONE);

        }
    }

    private void sparseJson(String json) {

        recipeSteps = new ArrayList<>();

        final String ID = "id";
        final String STEPS = "steps";
        final String VIDEO_URL = "videoURL";
        final String DESCRIPTION = "description";
        final String THUMBNAIL_URL = "thumbnailURL";

        try {

            final JSONArray recipes = new JSONArray(json);

            for (int i = 0; i < recipes.length(); i++) {

                JSONObject recipe = recipes.getJSONObject(i);

                if (recipe.getString(ID).equals(mRecipeId)) {

                    JSONArray steps = recipe.getJSONArray(STEPS);

                    for (int ii = 0; ii < steps.length(); ii++) {

                        JSONObject step = steps.getJSONObject(ii);

                        String mime;
                        String videoUrl = null;
                        String imageUrl = null;

                        // Search for image.
                        mime = mimeType(step.getString(THUMBNAIL_URL));
                        if (mime != null && mime.equals(IMAGE))
                            imageUrl = step.getString(THUMBNAIL_URL);
                        else {

                            mime = mimeType(step.getString(VIDEO_URL));
                            if (mime != null && mime.equals(IMAGE))
                                imageUrl = step.getString(VIDEO_URL);
                        }

                        // Search for video.
                        mime = mimeType(step.getString(VIDEO_URL));
                        if (mime != null && mime.equals(VIDEO))
                            videoUrl = step.getString(VIDEO_URL);
                        else {

                            mime = mimeType(step.getString(THUMBNAIL_URL));
                            if (mime != null && mime.equals(VIDEO))
                                videoUrl = step.getString(THUMBNAIL_URL);
                        }

                        recipeSteps.add(new RecipeStep(
                                step.getString(ID),
                                step.getString(DESCRIPTION),
                                videoUrl, imageUrl
                        ));

                    }

                    break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String mimeType(String url) {

        String mime = URLConnection.guessContentTypeFromName(url);

        if (mime != null) {

            if (mime.startsWith(IMAGE)) return IMAGE;
            if (mime.startsWith(VIDEO)) return VIDEO;
        }

        return null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");

        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

}
