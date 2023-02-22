package com.example.online_ethio_gebeya.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.databinding.FragmentEditCartItemBinding;
import com.example.online_ethio_gebeya.models.CartItem;
import com.example.online_ethio_gebeya.viewmodels.EditCartItemFragmentViewModel;
import com.example.online_ethio_gebeya.viewmodels.EditCartItemFragmentViewModelFactory;
import com.example.online_ethio_gebeya.viewmodels.FragmentCartItemViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class EditCartItemFragment extends BottomSheetDialogFragment {
    private CartItem cartItem;
    private FragmentEditCartItemBinding binding;
    private EditCartItemFragmentViewModel viewModel;
    private FragmentCartItemViewModel fragmentCartItemViewModel;
    private Button update;
    private final int clickedItemPosition;

    public EditCartItemFragment(@NonNull CartItem cartItem, @NonNull FragmentCartItemViewModel _fragmentCartItemViewModel, int position) {
        this.cartItem = cartItem;
        fragmentCartItemViewModel = _fragmentCartItemViewModel;
        clickedItemPosition = position;
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
        EditCartItemFragmentViewModelFactory factory = new EditCartItemFragmentViewModelFactory(requireActivity().getApplication(),
                callBackInterface.getAuthorizationToken(), cartItem);
        viewModel = new ViewModelProvider(this, factory).get(EditCartItemFragmentViewModel.class);

        // load
        binding.productName.setText(cartItem.getProduct().getName());
        Glide.with(this).load(cartItem.getItemImage()).centerCrop().into(binding.productImage);

        Button increment = binding.incrementBtn;
        Button decrement = binding.decrementBtn;
        update = binding.updateBtn;
        TextView quantity = binding.quantity;

        // observer
        LiveData<Integer> currentValueObserver = viewModel.getCurrentQuantity();
        currentValueObserver.observe(getViewLifecycleOwner(), integer -> {
            quantity.setText(String.valueOf(integer));
            setButtonStatus(Objects.equals(integer, cartItem.getQuantity()));

            increment.setEnabled(viewModel.isIncrement());
            decrement.setEnabled(viewModel.isDecrement());
        });
        viewModel.getEnableUpdateButton().observe(getViewLifecycleOwner(), value -> {
            if (value == null) {
                return;
            }

            if (!value) {
                // bwt this does update internally the original value from list
                // so we have to notify the adapter in adapter
                cartItem.setQuantity(currentValueObserver.getValue());
                fragmentCartItemViewModel.setUpdateCartItem(clickedItemPosition);
                dismiss();
            } else {
                setButtonStatus(false);
            }
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
        fragmentCartItemViewModel = null;
    }

    private void setButtonStatus(boolean status) {
        int visibility = status ? View.INVISIBLE : View.VISIBLE;
        update.setVisibility(visibility);
    }
}
