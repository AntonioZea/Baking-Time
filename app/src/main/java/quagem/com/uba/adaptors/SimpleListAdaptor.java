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
import quagem.com.uba.interfaces.ListItemSelectListener;
import quagem.com.uba.model.ListItem;

/**
 * Created by quagem on 2/8/18.
 *
 */

/**
 * {@link SimpleListAdaptor} A simple list adaptor to manage a name and an id.
 */
public class SimpleListAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = SimpleListAdaptor.class.getSimpleName();

    private List<ListItem> mListData;
    private ListItemSelectListener mListener;

    public SimpleListAdaptor(ListItemSelectListener listener,
                             List<ListItem> listData) {
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

        viewHolder.itemId = mListData.get(position).getId();
        viewHolder.mName.setText(name);
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private String itemId;

        @BindView(R.id.recipe_name_text_view) TextView mName;
        @BindView(R.id.recipe_list_click_view) FrameLayout mClickView;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mClickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnListItemSelect(itemId);
                }
            });

        }

    }

}
