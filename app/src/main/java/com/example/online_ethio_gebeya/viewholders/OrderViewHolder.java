package com.example.online_ethio_gebeya.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.models.Order;

public class OrderViewHolder extends RecyclerView.ViewHolder {
    private TextView shopName, status;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        shopName = itemView.findViewById(R.id.shop_name);
        status = itemView.findViewById(R.id.status);
    }

    public void bindView(@NonNull Order order) {
        shopName.setText(order.getShopName());
        status.setText(order.getStatus());
    }
}
