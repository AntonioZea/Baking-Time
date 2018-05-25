package quagem.com.uba.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import static quagem.com.uba.RecipeDetailsActivity.TWO_PANE;

public class RecipeStepDetailsFragment extends Fragment {

    public static final String TAG = RecipeStepDetailsFragment.class.getSimpleName();
    public static final String VIDEO_RESUME_POSITION = "videoResumePosition";
    public static final String VIDEO_STATE = "videoState";

    private String mRecipeId;
    private boolean mVideoPlayWhenReady;
    private String mCurrentStepId;
    private SimpleExoPlayer mExoPlayer;

    private List<RecipeStep> mRecipeSteps;

    // Mime type
    private static final String VIDEO = "video";
    private static final String IMAGE = "image";

    private  long mResumePosition;

    @BindView(R.id.recipe_step_image_view) ImageView mImageView;
    @BindView(R.id.recipe_step_video) SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.recipe_instructions_text_view) TextView mInstructionsTextView;

    @BindView(R.id.recipe_nav_controls) LinearLayout mControlPanel;
    @BindView(R.id.next_recipe_image_button) ImageButton mNextImageButton;
    @BindView(R.id.previous_recipe_image_button) ImageButton mPrevImageButton;

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

        View rootView = null;

        Bundle bundle = getArguments();

        if (bundle != null &&
                bundle.containsKey(JSON_EXTRA) &&
                bundle.containsKey(SELECTED_RECIPE_ID) &&
                bundle.containsKey(SELECTED_RECIPE_STEP_ID)) {

            if (savedInstanceState != null &&
                    savedInstanceState.containsKey(SELECTED_RECIPE_STEP_ID)) {
                mCurrentStepId = savedInstanceState.getString(SELECTED_RECIPE_STEP_ID);
                mResumePosition = savedInstanceState.getLong(VIDEO_RESUME_POSITION);
                mVideoPlayWhenReady = savedInstanceState.getBoolean(VIDEO_STATE);
            } else {
                mVideoPlayWhenReady = true;
                mCurrentStepId = bundle.getString(SELECTED_RECIPE_STEP_ID);
            }

            mRecipeId = bundle.getString(SELECTED_RECIPE_ID);
            sparseJson(bundle.getString(JSON_EXTRA));

            // Get recipe step to see if there is a video.
            boolean isVideo = false;
            for (RecipeStep rs : mRecipeSteps) {
                if (rs.getId().equals(mCurrentStepId) && rs.getVideoURL() != null) {
                    isVideo = true;
                    break;
                }
            }

            int orientation = getResources().getConfiguration().orientation;

            if (orientation == Configuration.ORIENTATION_LANDSCAPE && isVideo)
                rootView = inflater.inflate(R.layout.fragment_recipe_step_details_landscape,
                        container, false);
            else
                rootView = inflater.inflate(R.layout.fragment_recipe_step_details,
                        container, false);

            ButterKnife.bind(this, rootView);

            // Set nav panel if is not two pane mode or landscape
            if (!bundle.getBoolean(TWO_PANE) && orientation != Configuration.ORIENTATION_LANDSCAPE){

                mControlPanel.setVisibility(View.VISIBLE);

                mNextImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nextStep = getNextStep();
                        if (nextStep != null) {
                            mCurrentStepId = nextStep;
                            displayStep();
                        }
                    }
                });

                mPrevImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String prevStep = getPrevStep();
                        if (prevStep != null) {
                            mCurrentStepId = prevStep;
                            displayStep();
                        }
                    }
                });
            }

            displayStep();

        } else Toast.makeText(getContext(), R.string.error_loading_data, Toast.LENGTH_LONG).show();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        outState.putString(SELECTED_RECIPE_STEP_ID, mCurrentStepId);
        outState.putBoolean(VIDEO_STATE, mVideoPlayWhenReady);
        outState.putLong(VIDEO_RESUME_POSITION, mResumePosition);
        super.onSaveInstanceState(outState);
    }

    private void displayStep() {

        if (mExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    getActivity(), trackSelector, loadControl);

            mExoPlayerView.setPlayer(mExoPlayer);
        }

        RecipeStep recipeStep = null;

        /* Recipe id is written in the json and is not the position in the list so need to match
        id by searching. */

        for (RecipeStep rs : mRecipeSteps) {
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
                mExoPlayer.seekTo(mResumePosition);
                mExoPlayer.setPlayWhenReady(mVideoPlayWhenReady);

                mExoPlayerView.setVisibility(View.VISIBLE);

            } else mExoPlayerView.setVisibility(View.GONE);

        }
    }

    private String getNextStep() {

        // Recipe steps ids are written by humans in the json so need to find instead of just
        // increment to avoid errors.
        for (int i = 0; i < mRecipeSteps.size(); i++)
            if (mRecipeSteps.get(i).getId().equals(mCurrentStepId))
                if (i < mRecipeSteps.size() - 1) return mRecipeSteps.get(i + 1).getId();

        return null;
    }

    private String getPrevStep() {

        for (int i = 0; i < mRecipeSteps.size(); i++)
            if (mRecipeSteps.get(i).getId().equals(mCurrentStepId))
                if (i > 0) return mRecipeSteps.get(i - 1).getId();

        return null;
    }

    private void sparseJson(String json) {

        mRecipeSteps = new ArrayList<>();

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

                        mRecipeSteps.add(new RecipeStep(
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
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
        releasePlayer();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayStep();
    }

    private void releasePlayer(){

        if (mExoPlayer != null) {

            mResumePosition = mExoPlayer.getCurrentPosition();
            mVideoPlayWhenReady = mExoPlayer.getPlayWhenReady();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

}
