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

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.adapters.CheckOutAdapter;
import com.example.online_ethio_gebeya.adapters.ItemAdapter;
import com.example.online_ethio_gebeya.databinding.FragmentCheckoutAndPaymentBinding;
import com.example.online_ethio_gebeya.helpers.ApplicationHelper;
import com.example.online_ethio_gebeya.viewmodels.CheckoutFragmentViewModel;
import com.example.online_ethio_gebeya.viewmodels.FragmentCartItemViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

// @Author Solomon Boloshe    Feb 2023
public class CheckoutFragment extends BottomSheetDialogFragment {
    private final double totalMoney;
    private FragmentCheckoutAndPaymentBinding binding;
    private final long cartId;
    private String authorizationToken;
    private FragmentCartItemViewModel cartItemViewModel;
    private ItemAdapter itemAdapter;

    public CheckoutFragment(double total, @NonNull ItemAdapter adapter, long _cartId, @NonNull String _authorizationToken, @NonNull FragmentCartItemViewModel fragmentCartItemViewModel) {
        totalMoney = total;
        cartId = _cartId;
        authorizationToken = _authorizationToken;
        cartItemViewModel = fragmentCartItemViewModel;
        itemAdapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCheckoutAndPaymentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CheckoutFragmentViewModel viewModel = new ViewModelProvider(this).get(CheckoutFragmentViewModel.class);

        CheckOutAdapter checkOutAdapter = ApplicationHelper.initItems(requireContext(), binding);
        checkOutAdapter.submitList(itemAdapter.getItemList());
        binding.itemsLoading.setVisibility(View.GONE);

        // plc h
        Button btn = binding.finishOperation;
        String value = getString(R.string.price_in_ethio, totalMoney);
        binding.totalPrice.setText(value);

        ProgressBar loading = binding.progressCircular;
        /// event list
        btn.setText(getString(R.string.lbl_order));
        btn.setEnabled(true);
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
                cartItemViewModel.setOrderCreated();
                dismiss();
            } else {
                loading.setVisibility(View.GONE);
                btn.setEnabled(true);
                setCancelable(true);
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        authorizationToken = null;
        cartItemViewModel = null;
        itemAdapter = null;
    }
}
