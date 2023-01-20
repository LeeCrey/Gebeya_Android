package com.example.online_ethio_gebeya.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.models.Cart;

public class CartViewHolder extends RecyclerView.ViewHolder {
    private final TextView cartName;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        cartName = itemView.findViewById(R.id.cart_name);
    }

    public void bindView(@NonNull Cart item) {
        cartName.setText(item.getName());
    }
}
