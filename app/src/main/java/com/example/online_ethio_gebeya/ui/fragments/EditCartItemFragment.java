package com.example.online_ethio_gebeya.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.databinding.FragmentEditCartItemBinding;
import com.example.online_ethio_gebeya.models.CartItem;
import com.example.online_ethio_gebeya.viewmodels.EditCartItemFragmentViewModel;
import com.example.online_ethio_gebeya.viewmodels.EditCartItemFragmentViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class EditCartItemFragment extends BottomSheetDialogFragment {
    private CartItem cartItem;
    private FragmentEditCartItemBinding binding;
    private EditCartItemFragmentViewModel viewModel;

    private Button update;

    public EditCartItemFragment(@NonNull CartItem cartItem) {
        this.cartItem = cartItem;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditCartItemBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainActivityCallBackInterface callBackInterface = (MainActivityCallBackInterface) requireActivity();
        EditCartItemFragmentViewModelFactory factory = new EditCartItemFragmentViewModelFactory(
                requireActivity().getApplication(), callBackInterface.getAuthorizationToken(), cartItem);
        viewModel = new ViewModelProvider(this, factory).get(EditCartItemFragmentViewModel.class);

        // load
        binding.productName.setText(cartItem.getProduct().getName());
        Glide.with(this).load(cartItem.getItemImage()).centerCrop().into(binding.productImage);

        Button increment = binding.incrementBtn;
        Button decrement = binding.decrementBtn;
        update = binding.updateBtn;
        TextView quantity = binding.quantity;

        // observer
        viewModel.getEnableUpdateButton().observe(getViewLifecycleOwner(), value -> setButtonStatus(!value));
        viewModel.getCurrentQuantity().observe(getViewLifecycleOwner(), integer -> {
            quantity.setText(String.valueOf(integer));
            setButtonStatus(Objects.equals(integer, cartItem.getQuantity()));

            increment.setEnabled(viewModel.isIncrement());
            decrement.setEnabled(viewModel.isDecrement());
        });

        // event
        increment.setOnClickListener(v -> viewModel.increment());
        decrement.setOnClickListener(v -> viewModel.decrement());
        update.setOnClickListener(v -> viewModel.updateCartItem());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        update = null;
        cartItem = null;
        binding = null;
        viewModel = null;
    }

    private void setButtonStatus(boolean status) {
        int visibility = status ? View.INVISIBLE : View.VISIBLE;
        update.setVisibility(visibility);
    }
}
