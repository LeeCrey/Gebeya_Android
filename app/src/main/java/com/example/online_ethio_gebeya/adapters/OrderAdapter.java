package com.example.online_ethio_gebeya.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.callbacks.OrderCallBackInterface;
import com.example.online_ethio_gebeya.models.Order;
import com.example.online_ethio_gebeya.viewholders.OrderViewHolder;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    private final List<Order> orderList;
    private final LayoutInflater inflater;
    private final OrderCallBackInterface callBackInterface;

    private static final String TAG = "OrderAdapter";

    public OrderAdapter(@NonNull Context context, @NonNull OrderCallBackInterface _callBackInterface) {
        inflater = LayoutInflater.from(context);
        orderList = new ArrayList<>();
        callBackInterface = _callBackInterface;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_order, parent, false);

        OrderViewHolder vh = new OrderViewHolder(view);

        if (callBackInterface != null) {
            view.setOnClickListener(v -> {
                int pos = vh.getAdapterPosition();
                callBackInterface.onOrderClick(orderList.get(pos), pos);
            });
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bindView(orderList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setOrderList(List<Order> list) {
        if (list == null) {
            return;
        }

        Log.d(TAG, "setOrderList: received with count: " + list.size());
        orderList.addAll(list);
        notifyDataSetChanged();
    }
}
