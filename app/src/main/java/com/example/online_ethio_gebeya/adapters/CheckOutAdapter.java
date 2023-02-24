package com.example.online_ethio_gebeya.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.databinding.LayoutModalItemBinding;
import com.example.online_ethio_gebeya.models.Item;
import com.example.online_ethio_gebeya.viewholders.CheckoutItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckoutItemViewHolder> {
    private List<Item> itemList;
    private final LayoutInflater inflater;
    private final RequestManager glide;

    public CheckOutAdapter(@NonNull Context context, List<Item> itemList) {
        inflater = LayoutInflater.from(context);
        this.itemList = itemList;
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
        holder.bindView(itemList.get(position), glide);
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Item> _itemList) {
        if (_itemList == null) {
            return;
        }

        itemList = new ArrayList<>(_itemList);
        notifyDataSetChanged();
    }
}
