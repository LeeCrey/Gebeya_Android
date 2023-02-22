package com.example.online_ethio_gebeya.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.adapters.CartItemAdapter;
import com.example.online_ethio_gebeya.adapters.CheckOutAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CheckoutFragment extends BottomSheetDialogFragment {
    private double totalMoney;
    private CartItemAdapter adapter;

    public CheckoutFragment(double total, String baseUrl, String auth, CartItemAdapter adapter) {
        totalMoney = total;
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button btn = view.findViewById(R.id.order_btn);


        RecyclerView recyclerView = view.findViewById(R.id.items_recycler_view);
        CheckOutAdapter checkOutAdapter = new CheckOutAdapter(requireContext(), adapter.getItemList());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(checkOutAdapter);

        /// event list
        btn.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "created view:  " + totalMoney, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        adapter = null;
    }
}
