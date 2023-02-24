package com.example.online_ethio_gebeya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.databinding.LayoutModalItemBinding;
import com.example.online_ethio_gebeya.helpers.diff_calc.ItemCallBack;
import com.example.online_ethio_gebeya.models.Item;
import com.example.online_ethio_gebeya.viewholders.CheckoutItemViewHolder;

import java.util.List;

public class CheckOutAdapter extends ListAdapter<Item, CheckoutItemViewHolder> {
    private final LayoutInflater inflater;
    private final RequestManager glide;

    public CheckOutAdapter(@NonNull Context context) {
        super(new ItemCallBack());

        inflater = LayoutInflater.from(context);
        glide = Glide.with(context);
    }

    @NonNull
    @Override
    public CheckoutItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutModalItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_modal_item, parent, false);

        return new CheckoutItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutItemViewHolder holder, int position) {
        holder.bindView(getItem(position), glide);
    }

    public void setList(List<Item> _itemList) {
        if (_itemList == null) {
            return;
        }

        submitList(_itemList);
    }
}
