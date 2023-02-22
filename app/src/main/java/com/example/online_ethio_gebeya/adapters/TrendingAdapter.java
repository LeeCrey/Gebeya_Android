package com.example.online_ethio_gebeya.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.callbacks.ProductCallBackInterface;
import com.example.online_ethio_gebeya.databinding.LayoutTrendingBinding;
import com.example.online_ethio_gebeya.helpers.diff_calc.ProductItemCallBack;
import com.example.online_ethio_gebeya.models.Product;
import com.example.online_ethio_gebeya.viewholders.TrendingViewHolder;

import java.util.List;

public class TrendingAdapter extends ListAdapter<Product, TrendingViewHolder> {
    private final LayoutInflater inflater;
    private ProductCallBackInterface callBack;
    private final RequestManager glide;

    public TrendingAdapter(@NonNull Activity activity) {
        super(new ProductItemCallBack());

        inflater = LayoutInflater.from(activity);
        glide = Glide.with(activity.getBaseContext());
    }

    @NonNull
    @Override
    public TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutTrendingBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_trending, parent, false);

        TrendingViewHolder vh = new TrendingViewHolder(binding);

        View view = binding.getRoot();
        view.setOnClickListener(v -> {
            if (callBack != null) {
                callBack.onProductClick(getItem(vh.getAdapterPosition()));
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingViewHolder holder, int position) {
        holder.bindView(getItem(position), glide);
    }

    public void setProducts(List<Product> list) {
        if (list == null) {
            return;
        }

        submitList(list);
    }

    public void setCallBack(ProductCallBackInterface callBack) {
        this.callBack = callBack;
    }
}
