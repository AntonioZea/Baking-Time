package quagem.com.uba.adaptors;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import quagem.com.uba.R;
import quagem.com.uba.model.Recipe;

/**
 * Created by quagem on 2/8/18.
 *
 */

public class RecipeListAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = RecipeListAdaptor.class.getSimpleName();

    private List<Recipe> listData;

    public RecipeListAdaptor(List<Recipe> listData) {
        this.listData = listData;
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

        String name = listData.get(position).getName();

        viewHolder.recipeId = listData.get(position).getId();
        viewHolder.mName.setText(name);
    }

    @Override
    public long getItemId(int position) {
        return listData.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private long recipeId;

        @BindView(R.id.recipe_name_text_view) TextView mName;
        @BindView(R.id.recipe_list_click_view) FrameLayout mClickView;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mClickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 5/10/2018 Tell main activity.

                }
            });

        }

    }

}
