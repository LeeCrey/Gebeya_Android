package com.example.online_ethio_gebeya.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.databinding.LayoutModalItemBinding;
import com.example.online_ethio_gebeya.models.Item;

public class CheckoutItemViewHolder extends RecyclerView.ViewHolder {
    private final LayoutModalItemBinding binding;

    public CheckoutItemViewHolder(@NonNull LayoutModalItemBinding _binding) {
        super(_binding.getRoot());

        binding = _binding;
    }

    public void bindView(@NonNull Item item, @NonNull RequestManager glide) {
        binding.setItem(item);

        glide.load(item.getItemImage()).centerCrop().placeholder(R.drawable.load_error).into(binding.productImage);
    }
}
