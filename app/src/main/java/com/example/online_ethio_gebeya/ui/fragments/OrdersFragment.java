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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.online_ethio_gebeya.adapters.OrderAdapter;
import com.example.online_ethio_gebeya.callbacks.OrderCallBackInterface;
import com.example.online_ethio_gebeya.databinding.FragmentOrdersBinding;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.models.Order;
import com.example.online_ethio_gebeya.viewmodels.FragmentOrderViewModelFactory;
import com.example.online_ethio_gebeya.viewmodels.FragmentOrdersViewModel;

public class OrdersFragment extends Fragment implements OrderCallBackInterface {
    private FragmentOrdersBinding binding;
    private OrderAdapter adapter;
    private FragmentOrdersViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initOrderView();

        SwipeRefreshLayout refreshLayout = binding.refreshLayout;
        refreshLayout.setRefreshing(true);

        String auth = PreferenceHelper.getAuthToken(requireContext());
        FragmentOrderViewModelFactory factory = new FragmentOrderViewModelFactory(requireActivity().getApplication(), auth);
        viewModel = new ViewModelProvider(this, factory).get(FragmentOrdersViewModel.class);

        //observer
        viewModel.getPaidOrderPosition().observe(getViewLifecycleOwner(), pos -> {
            if (pos == null) {
                return;
            }
            adapter.notifyItemChanged(pos);
        });
        viewModel.getOrders().observe(getViewLifecycleOwner(), value -> {
            if (value == null) {
                return;
            }
            adapter.setOrderList(value);
            refreshLayout.setEnabled(false);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        adapter = null;
        viewModel = null;
    }

    private void initOrderView() {
        RecyclerView orders = binding.orderRecyclerView;
        adapter = new OrderAdapter(requireContext(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        orders.setAdapter(adapter);
        orders.setLayoutManager(layoutManager);
    }

    @Override
    public void onOrderClick(@NonNull Order order, int pos) {
        PaymentFragment bottomSheet = new PaymentFragment(viewModel, order, pos);
        bottomSheet.show(getParentFragmentManager(), "PaymentBottomSheet");
    }
}
