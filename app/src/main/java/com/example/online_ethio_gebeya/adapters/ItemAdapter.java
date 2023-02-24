package com.example.online_ethio_gebeya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.callbacks.CartItemCallBackInterface;
import com.example.online_ethio_gebeya.databinding.LayoutCartItemBinding;
import com.example.online_ethio_gebeya.models.Item;
import com.example.online_ethio_gebeya.models.Product;
import com.example.online_ethio_gebeya.ui.fragments.CartFragment;
import com.example.online_ethio_gebeya.viewholders.CartItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends ListAdapter<Item, CartItemViewHolder> {
    private static final DiffUtil.ItemCallback<Item> CALL_BACK = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            final long oldItemId = oldItem.getId();
            final long newItemId = newItem.getId();
            return oldItemId == newItemId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.areContentsTheSame(newItem);
        }
    };
    private final LayoutInflater inflater;
    private final Context context;
    private final RequestManager glide;
    private CartItemCallBackInterface callBackInterface;
    private static final String TAG = "CartItemAdapter";

    public ItemAdapter(@NonNull CartFragment fragment) {
        super(CALL_BACK);

        context = fragment.getContext();
        inflater = LayoutInflater.from(fragment.getContext());
        glide = Glide.with(fragment);
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCartItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_cart_item, parent, false);
        View root = binding.getRoot();
        CartItemViewHolder vh = new CartItemViewHolder(binding);

        if (callBackInterface != null) {
            root.setOnClickListener(v -> {
                int pos = vh.getAdapterPosition();
                callBackInterface.onCartItemClick(getCartItem(pos), pos);
            });
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        holder.bind(context, getItem(position), glide);
    }

    public Item getCartItem(int position) {
        return getItem(position);
    }

    // custom
    public void updateList(List<Item> items) {
        if (items == null) {
            return;
        }

        submitList(items);
    }

    public double getCalculatedValue() {
        List<Item> list = getCurrentList();
        if (list.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (Item item : list) {
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

    public List<Item> getItemList() {
        return new ArrayList<>(getCurrentList());
    }

    public void setCallBack(CartFragment cartFragment) {
        callBackInterface = cartFragment;
    }

    public void cartItemUpdate(Integer updateItemPosition) {
        // btw the data will silently update but the changed won't be reflected in the UI.
        // If you don't like it, change the logic. Clone the cartItem and pass it.
        // then come here and update the list.
        // @lee_crey Telegram

        if (updateItemPosition == null) {
            return;
        }

        notifyItemChanged(updateItemPosition);
    }
}
