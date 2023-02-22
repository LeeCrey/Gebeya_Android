package com.example.online_ethio_gebeya.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.adapters.CartItemAdapter;
import com.example.online_ethio_gebeya.adapters.CheckOutAdapter;
import com.example.online_ethio_gebeya.databinding.FragmentCheckoutBinding;
import com.example.online_ethio_gebeya.viewmodels.CheckoutFragmentViewModel;
import com.example.online_ethio_gebeya.viewmodels.FragmentCartItemViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CheckoutFragment extends BottomSheetDialogFragment {
    private final double totalMoney;
    private CartItemAdapter adapter;
    private FragmentCheckoutBinding binding;
    private final long cartId;
    private String authorizationToken;
    private FragmentCartItemViewModel cartItemViewModel;

    public CheckoutFragment(double total, @NonNull CartItemAdapter adapter, long _cartId, @NonNull String _authorizationToken, @NonNull FragmentCartItemViewModel fragmentCartItemViewModel) {
        totalMoney = total;
        this.adapter = adapter;
        cartId = _cartId;
        authorizationToken = _authorizationToken;
        cartItemViewModel = fragmentCartItemViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CheckoutFragmentViewModel viewModel = new ViewModelProvider(this).get(CheckoutFragmentViewModel.class);

        // plc h
        Button btn = binding.orderBtn;

        initRecycler();
        String value = getString(R.string.price_in_ethio, totalMoney);
        binding.totalPrice.setText(value);

        ProgressBar loading = binding.progressCircular;
        /// event list
        btn.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);
            btn.setEnabled(false);

            viewModel.createOrder(authorizationToken, cartId);
        });

        // observer
        viewModel.getInstructionResponse().observe(getViewLifecycleOwner(), instructionsResponse -> {
            if (instructionsResponse == null) {
                return;
            }

            if (instructionsResponse.getOkay()) {
                dismiss();
                cartItemViewModel.setOrderCreated();
            } else {
                loading.setVisibility(View.GONE);
                btn.setEnabled(true);
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        adapter = null;
        binding = null;
        authorizationToken = null;
        cartItemViewModel = null;
    }

    private void initRecycler() {
        RecyclerView recyclerView = binding.itemsRecyclerView;
        CheckOutAdapter checkOutAdapter = new CheckOutAdapter(requireContext(), adapter.getItemList());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(checkOutAdapter);
    }
}
