package com.example.online_ethio_gebeya.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.adapters.OrderAdapter;
import com.example.online_ethio_gebeya.callbacks.ItemClickCallBackInterface;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.callbacks.OrderCallBackInterface;
import com.example.online_ethio_gebeya.databinding.FragmentOrdersBinding;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.models.Item;
import com.example.online_ethio_gebeya.models.Order;
import com.example.online_ethio_gebeya.viewmodels.FragmentOrderViewModelFactory;
import com.example.online_ethio_gebeya.viewmodels.FragmentOrdersViewModel;

public class OrdersFragment extends Fragment implements OrderCallBackInterface, ItemClickCallBackInterface {
    private FragmentOrdersBinding binding;
    private OrderAdapter adapter;
    private FragmentOrdersViewModel viewModel;
    private NavController navController;
    private MainActivityCallBackInterface callBackInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initOrderView();

        navController = Navigation.findNavController(view);
        callBackInterface = (MainActivityCallBackInterface) requireActivity();

        SwipeRefreshLayout refreshLayout = binding.refreshLayout;
        refreshLayout.setRefreshing(true);

        String auth = PreferenceHelper.getAuthToken(requireContext());
        FragmentOrderViewModelFactory factory = new FragmentOrderViewModelFactory(requireActivity().getApplication(), auth);
        viewModel = new ViewModelProvider(this, factory).get(FragmentOrdersViewModel.class);

        //observer
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
        navController = null;
        callBackInterface = null;
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
        Bundle arg = new Bundle();
        arg.putLong("orderId", order.getId());
        arg.putString("orderStatus", order.getStatus());
        navController.navigate(R.id.open_order_items, arg);
    }

    @Override
    public void itemClick(@NonNull Item item) {
        callBackInterface.onProductClick(item.getProduct(), false);
    }
}
