package com.example.online_ethio_gebeya.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.callbacks.CartCallBackInterface;
import com.example.online_ethio_gebeya.models.Cart;
import com.example.online_ethio_gebeya.viewholders.CartViewHolder;

import java.util.List;

public class CartsAdapter extends ListAdapter<Cart, CartViewHolder> {
    private static final DiffUtil.ItemCallback<Cart> CALL_BACK = new DiffUtil.ItemCallback<Cart>() {
        @Override
        public boolean areItemsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
            long old = oldItem.getId();
            long newCart = newItem.getId();

            return old == newCart;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };

    private static final String TAG = "CartsAdapter";
    private final LayoutInflater inflater;
    private final CartCallBackInterface callBackInterface;

    public CartsAdapter(Activity activity, CartCallBackInterface cartCallBackInterface) {
        super(CALL_BACK);

        this.callBackInterface = cartCallBackInterface;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.layout_cart, parent, false);

        final CartViewHolder viewHolder = new CartViewHolder(view);

        // on click listener...
        if (callBackInterface != null) {
            view.setOnClickListener(v -> callBackInterface.onItemClicked(getItem(viewHolder.getAdapterPosition())));
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bindView(getItem(position));
    }
}
