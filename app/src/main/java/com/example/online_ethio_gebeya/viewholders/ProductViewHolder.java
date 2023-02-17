package com.example.online_ethio_gebeya.viewholders;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.databinding.LayoutProductBinding;
import com.example.online_ethio_gebeya.models.Product;
import com.squareup.picasso.Picasso;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    private final LayoutProductBinding productBinding;

    public ProductViewHolder(@NonNull LayoutProductBinding binding) {
        super(binding.getRoot());

        productBinding = (LayoutProductBinding) binding;
    }

    // if it is search layout attach only image.
    public void bindView(final Context context, final Product product) {
        productBinding.setProduct(product);
        Double currentPrice = product.getPrice();
        if (product.getDiscount() != null) {
            currentPrice = product.getPrice() - product.getDiscount();
        }
        productBinding.setPrice(context.getString(R.string.price_in_ethio, currentPrice));
    }
}