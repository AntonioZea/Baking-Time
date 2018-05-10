package quagem.com.uba.fragments;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import quagem.com.uba.R;
import quagem.com.uba.adaptors.RecipeListAdaptor;
import quagem.com.uba.model.Recipe;

public class RecipeFragment extends Fragment {

    public static final String TAG = RecipeFragment.class.getSimpleName();

    private List<Recipe> mListData;
    @BindView(R.id.recipe_list_view) RecyclerView recyclerView;


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

        rootView = inflater.inflate(R.layout.recipe_list, container, false);
        ButterKnife.bind(this, rootView);

        mListData = new ArrayList<>();

        RecipeListAdaptor recipeListAdaptor = new RecipeListAdaptor(mListData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipeListAdaptor);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: 5/10/2018 Try to code click here.
                Log.i(TAG, "View Clicked: " + v.toString());
            }
        });

        return rootView;
    }

}
