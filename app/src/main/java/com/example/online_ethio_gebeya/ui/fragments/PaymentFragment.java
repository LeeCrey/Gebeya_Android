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
import com.example.online_ethio_gebeya.databinding.FragmentCheckoutAndPaymentBinding;
import com.example.online_ethio_gebeya.helpers.ApplicationHelper;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.helpers.ProductHelper;
import com.example.online_ethio_gebeya.models.Order;
import com.example.online_ethio_gebeya.viewmodels.FragmentOrdersViewModel;
import com.example.online_ethio_gebeya.viewmodels.FragmentPaymentViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PaymentFragment extends BottomSheetDialogFragment {
    private Order order;
    private final int position;
    private FragmentCheckoutAndPaymentBinding binding;
    private FragmentOrdersViewModel ordersViewModel;
    private Button pay;
    private ProgressBar loading;
    private CheckOutAdapter adapter;

    public PaymentFragment(@NonNull FragmentOrdersViewModel _viewModel, @NonNull Order _order, int _pos) {
        order = _order;
        position = _pos;
        ordersViewModel = _viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCheckoutAndPaymentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adapter = ApplicationHelper.initItems(requireActivity(), binding);
        pay = binding.finishOperation;
        loading = binding.progressCircular;
        FragmentPaymentViewModel viewModel = new ViewModelProvider(this).get(FragmentPaymentViewModel.class);
        viewModel.setAuthorizationToken(PreferenceHelper.getAuthToken(requireContext()));

        boolean paid = order.getStatus().equalsIgnoreCase("paid");
        // observer
        viewModel.getResponse().observe(getViewLifecycleOwner(), instructionsResponse -> {
            if (instructionsResponse == null) {
                return;
            }

            if (instructionsResponse.getOkay()) {
                ordersViewModel.makePaid(position);
                order.setStatus("Paid");
                dismiss();
            } else {
                setStatus(true);
            }

            Toast.makeText(requireActivity(), instructionsResponse.getMessage(), Toast.LENGTH_SHORT).show();
        });
        viewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            if (items == null) {
                return;
            }

            if (paid) {
                pay.setText(getString(R.string.paid));
            } else {
                pay.setEnabled(true);
            }
            adapter.setList(items);
            binding.itemsLoading.setVisibility(View.GONE);
            binding.totalPrice.setText(getString(R.string.price_in_ethio, ProductHelper.getTotalMoney(items)));
        });

        // event
        pay.setOnClickListener(v -> {
            viewModel.makePayment(order);
            setStatus(false);
        });

        //
        viewModel.makeGetItemList(order.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        pay = null;
        loading = null;
        order = null;
        binding = null;
        ordersViewModel = null;
        adapter = null;
    }

    private void setStatus(boolean status) {
        pay.setEnabled(status);
        setCancelable(status);
        loading.setVisibility(status ? View.GONE : View.VISIBLE);
    }
}
