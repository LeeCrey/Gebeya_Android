package com.example.online_ethio_gebeya.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.viewholders.ProductImageHolder;

import java.util.List;

public class ProductImagesAdapter extends RecyclerView.Adapter<ProductImageHolder> {
    private final LayoutInflater inflater;
    private List<String> imagesList;
    private final RequestManager glide;

    public ProductImagesAdapter(List<String> imagesList, @NonNull Context context) {
        this.imagesList = imagesList;
        inflater = LayoutInflater.from(context);
        glide = Glide.with(context);
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
        holder.bindView(url, glide);
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
