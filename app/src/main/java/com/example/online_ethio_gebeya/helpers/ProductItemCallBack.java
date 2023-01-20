package com.example.online_ethio_gebeya.helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.online_ethio_gebeya.models.Product;

public class ProductItemCallBack extends DiffUtil.ItemCallback<Product> {

    @Override
    public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
        final int oldItemId = oldItem.getId();
        final int newItemId = newItem.getId();
        return oldItemId == newItemId;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
        return oldItem.isContentTheSame(newItem);
    }
}
