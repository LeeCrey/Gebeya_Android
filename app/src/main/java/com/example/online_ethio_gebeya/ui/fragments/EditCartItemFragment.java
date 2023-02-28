package com.example.online_ethio_gebeya.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.databinding.FragmentEditItemBinding;
import com.example.online_ethio_gebeya.models.Item;
import com.example.online_ethio_gebeya.viewmodels.EditCartItemFragmentViewModel;
import com.example.online_ethio_gebeya.viewmodels.EditCartItemFragmentViewModelFactory;
import com.example.online_ethio_gebeya.viewmodels.FragmentCartItemViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

// @Author Solomon Boloshe  Jan 2023
public class EditCartItemFragment extends BottomSheetDialogFragment {
    private Item item;
    private FragmentEditItemBinding binding;
    private EditCartItemFragmentViewModel viewModel;
    private FragmentCartItemViewModel fragmentCartItemViewModel;
    private final int clickedItemPosition;

    public EditCartItemFragment(@NonNull Item item, @NonNull FragmentCartItemViewModel _fragmentCartItemViewModel, int position) {
        this.item = item;
        fragmentCartItemViewModel = _fragmentCartItemViewModel;
        clickedItemPosition = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditItemBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainActivityCallBackInterface callBackInterface = (MainActivityCallBackInterface) requireActivity();
        EditCartItemFragmentViewModelFactory factory = new EditCartItemFragmentViewModelFactory(requireActivity().getApplication(),
                callBackInterface.getAuthorizationToken(), item);
        viewModel = new ViewModelProvider(this, factory).get(EditCartItemFragmentViewModel.class);

        // load
        binding.productName.setText(item.getProduct().getName());
        Glide.with(this).load(item.getItemImage()).centerCrop().into(binding.productImage);

        Button increment = binding.incrementBtn;
        Button decrement = binding.decrementBtn;
        Button update = binding.updateBtn;
        TextView quantity = binding.quantity;
        ProgressBar loading = binding.progressCircular;

        // observer
        LiveData<Integer> currentValueObserver = viewModel.getCurrentQuantity();
        currentValueObserver.observe(getViewLifecycleOwner(), integer -> {
            quantity.setText(String.valueOf(integer));
            if (Objects.equals(integer, item.getQuantity())) {
                update.setVisibility(View.INVISIBLE);
            } else {
                update.setVisibility(View.VISIBLE);
            }

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
                item.setQuantity(currentValueObserver.getValue());
                fragmentCartItemViewModel.setUpdateCartItem(clickedItemPosition);
                dismiss();
            } else {
                loading.setVisibility(View.GONE);
                update.setEnabled(true);
                setCancelable(true);
            }
        });

        // event
        increment.setOnClickListener(v -> viewModel.increment());
        decrement.setOnClickListener(v -> viewModel.decrement());
        update.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);
            update.setEnabled(false);
            viewModel.updateCartItem();

            setCancelable(false);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        item = null;
        binding = null;
        viewModel = null;
        fragmentCartItemViewModel = null;
    }
}
