package com.example.online_ethio_gebeya.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.databinding.LayoutModalItemBinding;
import com.example.online_ethio_gebeya.models.CartItem;

public class CheckoutItemViewHolder extends RecyclerView.ViewHolder {
    private final LayoutModalItemBinding binding;

    public CheckoutItemViewHolder(@NonNull LayoutModalItemBinding _binding) {
        super(_binding.getRoot());

        binding = _binding;
    }

    public void bindView(@NonNull CartItem item, @NonNull RequestManager glide) {
        binding.setCartItem(item);

        glide.load(item.getItemImage()).centerCrop().placeholder(R.drawable.load_error).into(binding.productImage);
    }
}
