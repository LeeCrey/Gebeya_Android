package com.example.online_ethio_gebeya.viewholders;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.databinding.LayoutProductBinding;
import com.example.online_ethio_gebeya.models.Product;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    private final LayoutProductBinding productBinding;

    public ProductViewHolder(@NonNull LayoutProductBinding binding) {
        super(binding.getRoot());

        productBinding = (LayoutProductBinding) binding;
    }

    // if it is search layout attach only image.
    public void bindView(final @NonNull Context context, final Product product, @NonNull RequestManager glide) {
        productBinding.setProduct(product);
        Double currentPrice = product.getPrice();
        if (product.getDiscount() != null) {
            currentPrice = product.getPrice() - product.getDiscount();
        }

        productBinding.setPrice(context.getString(R.string.price_in_ethio, currentPrice));

        glide.load(product.getThumbnail()).centerCrop().placeholder(R.drawable.load_error).into(productBinding.productImage);
    }
}