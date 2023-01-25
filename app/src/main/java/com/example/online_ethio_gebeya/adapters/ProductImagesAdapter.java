package com.example.online_ethio_gebeya.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.viewholders.ProductImageHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductImagesAdapter extends RecyclerView.Adapter<ProductImageHolder> {
    private final LayoutInflater inflater;
    private final Picasso picasso;
    private List<String> imagesList;

    public ProductImagesAdapter(List<String> imagesList, @NonNull Context context) {
        this.imagesList = imagesList;
        inflater = LayoutInflater.from(context);
        picasso = Picasso.get();
    }

    @NonNull
    @Override
    public ProductImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_product_image, parent, false);

        return new ProductImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductImageHolder holder, int position) {
        String url = imagesList.get(position);
        holder.bindView(url, picasso);
    }

    @Override
    public int getItemCount() {
        return imagesList == null ? 0 : imagesList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setImagesList(List<String> images) {
        if (images != null) {
            imagesList = images;
            notifyDataSetChanged();
        }
    }
}
