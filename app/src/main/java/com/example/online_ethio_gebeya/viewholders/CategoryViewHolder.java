package com.example.online_ethio_gebeya.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.models.Category;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "CategoryViewHolder";
    private final TextView categoryName;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryName = itemView.findViewById(R.id.category_name);
    }

    public TextView getCategoryName() {
        return categoryName;
    }

    public void bindView(Category category, Context context, boolean isAmharic) {
        if (isAmharic) {
            categoryName.setText(category.getAmharic());
        } else {
            categoryName.setText(category.getName());
        }

        if (category.isSelected()) {
            categoryName.setBackgroundColor(context.getColor(R.color.primary));
            categoryName.setTextColor(context.getColor(R.color.onPrimary));
        } else {
            categoryName.setBackgroundColor(context.getColor(R.color.category));
            categoryName.setTextColor(context.getColor(R.color.textPrimaryColor));
        }
    }
}
