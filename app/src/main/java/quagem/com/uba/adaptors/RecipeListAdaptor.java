package quagem.com.uba.adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import quagem.com.uba.R;
import quagem.com.uba.fragments.RecipeListFragment;
import quagem.com.uba.model.Recipe;

/**
 * Created by quagem on 2/8/18.
 *
 */

public class RecipeListAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = RecipeListAdaptor.class.getSimpleName();

    private List<Recipe> mListData;
    private RecipeListFragment.OnRecipeClickListener mListener;

    public RecipeListAdaptor(RecipeListFragment.OnRecipeClickListener listener,
                             List<Recipe> listData) {
        mListData = listData;
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder)holder;

        String name = mListData.get(position).getName();

        viewHolder.recipeId = mListData.get(position).getId();
        viewHolder.mName.setText(name);
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private String recipeId;

        @BindView(R.id.recipe_name_text_view) TextView mName;
        @BindView(R.id.recipe_list_click_view) FrameLayout mClickView;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mClickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRecipeSelected(recipeId);
                }
            });

        }

    }

}
