package com.example.online_ethio_gebeya.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.callbacks.ProductCallBackInterface;
import com.example.online_ethio_gebeya.models.Category;
import com.example.online_ethio_gebeya.viewholders.CategoryViewHolder;

import java.util.List;

public class CategoryAdapter extends ListAdapter<Category, CategoryViewHolder> {
    private static final DiffUtil.ItemCallback<Category> DIFF_CALC_CALLBACK = new DiffUtil.ItemCallback<Category>() {
        @Override
        public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            final long oldItemId = oldItem.getCategoryId();
            final long newItemId = newItem.getCategoryId();

            return oldItemId == newItemId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getName().equals(newItem.getName()) && (oldItem.isSelected() == newItem.isSelected());
        }
    };
    private final LayoutInflater inflater;
    private final Context context;
    private final boolean isAmharic;
    private int selectedCategoryPosition;
    private ProductCallBackInterface callBackInterface;

    public CategoryAdapter(final Activity activity) {
        super(DIFF_CALC_CALLBACK);

        inflater = LayoutInflater.from(activity);
        context = activity.getApplicationContext();
        selectedCategoryPosition = 0;
        isAmharic = false;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.layout_category, parent, false);

        final CategoryViewHolder vh = new CategoryViewHolder(view);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

        // event ...
        final CardView cardView = (CardView) view;
        cardView.setOnClickListener(v -> callBackInterface.onCategorySelected(vh.getAdapterPosition()));

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bindView(getItem(position), context, isAmharic);
    }

    public void setCallBack(ProductCallBackInterface callBack) {
        callBackInterface = callBack;
    }

    public String getSelectedCategoryName() {
        try {
            return getItem(selectedCategoryPosition).getName();
        } catch (Exception ex) {
            return "all";
        }
    }

    public int getSelectedCategoryPosition() {
        return selectedCategoryPosition;
    }

    public void setSelectedCategoryPosition(final @NonNull Integer position) {
        if (position == -1) {
            return;
        }

        if (selectedCategoryPosition == position) {
            return;
        }

        Category category = getItem(position);
        category.setSelected(true);

        notifyItemChanged(position);

        // old
        Category oldCategory = getItem(selectedCategoryPosition);
        oldCategory.setSelected(false);

        notifyItemChanged(position, category);
        notifyItemChanged(selectedCategoryPosition, oldCategory);

        selectedCategoryPosition = position;
    }

    public void setCategories(final List<Category> categories) {
        if (categories == null) {
            return;
        }

        submitList(categories);
    }
}
