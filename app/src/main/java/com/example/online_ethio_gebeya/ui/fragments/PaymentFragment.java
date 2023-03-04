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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.adapters.CheckOutAdapter;
import com.example.online_ethio_gebeya.callbacks.ItemClickCallBackInterface;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.databinding.FragmentCheckoutAndPaymentBinding;
import com.example.online_ethio_gebeya.helpers.ApplicationHelper;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.helpers.ProductHelper;
import com.example.online_ethio_gebeya.models.Item;
import com.example.online_ethio_gebeya.viewmodels.FragmentPaymentViewModel;

public class PaymentFragment extends Fragment implements ItemClickCallBackInterface {
    private FragmentCheckoutAndPaymentBinding binding;
    private Button pay;
    private ProgressBar loading;
    private CheckOutAdapter adapter;
    private MainActivityCallBackInterface callBackInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCheckoutAndPaymentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adapter = ApplicationHelper.initItems(requireActivity(), binding);
        adapter.setCallBackInterface(this);

        callBackInterface = (MainActivityCallBackInterface) requireActivity();
        pay = binding.finishOperation;
        loading = binding.progressCircular;
        FragmentPaymentViewModel viewModel = new ViewModelProvider(this).get(FragmentPaymentViewModel.class);
        viewModel.setAuthorizationToken(PreferenceHelper.getAuthToken(requireContext()));
        PaymentFragmentArgs args = PaymentFragmentArgs.fromBundle(getArguments());

        String paidStr = "paid";
        boolean paid = args.getOrderStatus().equalsIgnoreCase(paidStr);
        // observer
        viewModel.getResponse().observe(getViewLifecycleOwner(), instructionsResponse -> {
            if (instructionsResponse == null) {
                return;
            }

            if (instructionsResponse.getOkay()) {
                pay.setText(paidStr);
                pay.setEnabled(false);
                loading.setVisibility(View.GONE);
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
            viewModel.makePayment(args.getOrderId());
            setStatus(false);
        });

        //
        viewModel.makeGetItemList(args.getOrderId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        pay = null;
        loading = null;
        binding = null;
        adapter = null;
        callBackInterface = null;
    }

    private void setStatus(boolean status) {
        pay.setEnabled(status);
        loading.setVisibility(status ? View.GONE : View.VISIBLE);
    }

    @Override
    public void itemClick(@NonNull Item item) {
        callBackInterface.onProductClick(item.getProduct(), false);
    }
}
