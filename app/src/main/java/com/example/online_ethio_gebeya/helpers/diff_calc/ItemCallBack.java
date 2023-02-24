package com.example.online_ethio_gebeya.helpers.diff_calc;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.online_ethio_gebeya.models.Item;

public class ItemCallBack extends DiffUtil.ItemCallback<Item> {

    @Override
    public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
        return oldItem.areContentsTheSame(newItem);
    }
}
