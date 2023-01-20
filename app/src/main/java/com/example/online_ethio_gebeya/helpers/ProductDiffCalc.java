package com.example.online_ethio_gebeya.helpers;

import androidx.recyclerview.widget.DiffUtil;

import com.example.online_ethio_gebeya.models.Product;

import java.util.List;

public class ProductDiffCalc extends DiffUtil.Callback {
    private final List<Product> oldList;
    private final List<Product> newList;

    public ProductDiffCalc(List<Product> oldList, List<Product> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList == null ? 0 : newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Product old = oldList.get(oldItemPosition);
        Product newP = newList.get(newItemPosition);

        return old.isSimilar(newP);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Product old = oldList.get(oldItemPosition);
        Product newP = newList.get(newItemPosition);

        return old.isContentTheSame(newP);
    }
}
