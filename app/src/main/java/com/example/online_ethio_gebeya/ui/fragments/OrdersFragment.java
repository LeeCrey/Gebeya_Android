package com.example.online_ethio_gebeya.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.adapters.OrderAdapter;
import com.example.online_ethio_gebeya.databinding.FragmentOrdersBinding;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.viewmodels.FragmentOrderViewModelFactory;
import com.example.online_ethio_gebeya.viewmodels.FragmentOrdersViewModel;

public class OrdersFragment extends Fragment {
    private FragmentOrdersBinding binding;
    private OrderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initOrderView();

        String auth = PreferenceHelper.getAuthToken(requireContext());
        FragmentOrderViewModelFactory factory = new FragmentOrderViewModelFactory(requireActivity().getApplication(), auth);
        FragmentOrdersViewModel viewModel = new ViewModelProvider(this, factory).get(FragmentOrdersViewModel.class);

        //observer
        viewModel.getOrders().observe(getViewLifecycleOwner(), adapter::setOrderList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        adapter = null;
    }

    private void initOrderView() {
        RecyclerView orders = binding.orderRecyclerView;
        adapter = new OrderAdapter(requireContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        orders.setAdapter(adapter);
        orders.setLayoutManager(layoutManager);
    }
}
