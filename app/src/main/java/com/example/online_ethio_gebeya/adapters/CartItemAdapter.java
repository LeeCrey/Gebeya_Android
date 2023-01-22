package com.example.online_ethio_gebeya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.databinding.LayoutCartItemBinding;
import com.example.online_ethio_gebeya.models.CartItem;
import com.example.online_ethio_gebeya.models.Product;
import com.example.online_ethio_gebeya.ui.fragments.CartFragment;
import com.example.online_ethio_gebeya.viewholders.CartItemViewHolder;

import java.util.List;

public class CartItemAdapter extends ListAdapter<CartItem, CartItemViewHolder> {
    private static final DiffUtil.ItemCallback<CartItem> CALL_BACK = new DiffUtil.ItemCallback<CartItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            final int oldItemId = oldItem.getId();
            final int newItemId = newItem.getId();
            return oldItemId == newItemId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.areContentsTheSame(newItem);
        }
    };
    private final LayoutInflater inflater;
    private final Context context;

    public CartItemAdapter(@NonNull CartFragment fragment) {
        super(CALL_BACK);

        context = fragment.getContext();
        inflater = LayoutInflater.from(fragment.getContext());
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCartItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_cart_item, parent, false);

        return new CartItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        holder.bind(context, getItem(position));
    }

    public CartItem getCartItem(int position) {
        return getItem(position);
    }

    // custom
    public void updateList(List<CartItem> cartItems) {
        if (cartItems == null) {
            return;
        }

        submitList(cartItems);
    }

    public double getCalculatedValue() {
        List<CartItem> list = getCurrentList();
        if (list.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (CartItem item : list) {
            int quantity = item.getQuantity();
            Product p = item.getProduct();
            double finalValue = p.getPrice();
            if (p.getDiscount() != null) {
                finalValue = p.getPrice() - p.getDiscount();
            }
            sum += (finalValue * quantity);
        }

        return sum;
    }
}
