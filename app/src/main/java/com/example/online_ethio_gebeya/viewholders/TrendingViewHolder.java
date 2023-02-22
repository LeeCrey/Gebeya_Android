package com.example.online_ethio_gebeya.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.databinding.LayoutTrendingBinding;
import com.example.online_ethio_gebeya.models.Product;

public class TrendingViewHolder extends RecyclerView.ViewHolder {
    final LayoutTrendingBinding binding;

    public TrendingViewHolder(@NonNull LayoutTrendingBinding _binding) {
        super(_binding.getRoot());
        binding = _binding;
    }

    public void bindView(Product product, @NonNull RequestManager glide) {
        binding.setProduct(product);
        glide.load(product.getThumbnail()).centerCrop().placeholder(R.drawable.load_error).into(binding.productImage);
    }
}
