package com.example.online_ethio_gebeya.viewholders;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.squareup.picasso.Picasso;

public class ProductImageHolder extends RecyclerView.ViewHolder {
    private final ImageView imageView;

    public ProductImageHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.product_image);
    }

    public void bindView(String url, @NonNull Picasso picasso) {
        picasso.load(url).error(R.drawable.load_error).into(imageView);
    }
}
