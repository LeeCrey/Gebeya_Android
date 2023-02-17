package com.example.online_ethio_gebeya.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.adapters.ProductAdapter;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.callbacks.SearchCallBackInterface;
import com.example.online_ethio_gebeya.databinding.FragmentSearchBinding;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.helpers.ProductHelper;
import com.example.online_ethio_gebeya.models.Product;
import com.example.online_ethio_gebeya.viewmodels.FragmentSearchViewModel;

public class SearchFragment extends Fragment implements MenuProvider, SearchCallBackInterface {
    private MainActivityCallBackInterface callBackInterface;
    private FragmentSearchViewModel viewModel;
    private FragmentSearchBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final FragmentActivity activity = requireActivity();
        callBackInterface = (MainActivityCallBackInterface) activity;
        viewModel = new ViewModelProvider(this).get(FragmentSearchViewModel.class);
        viewModel.setAuthToken(PreferenceHelper.getAuthToken(requireContext()));

        ProductAdapter productAdapter = ProductHelper.initProducts(this, binding.productsRecyclerView, true, false);
        productAdapter.setCalculateProductWidth(false);
        productAdapter.setCallBack(this);

        // observers
        viewModel.getProductResponse().observe(getViewLifecycleOwner(), productResponse -> {
            if (productResponse == null) {
                return;
            }

            productAdapter.setProducts(productResponse.getProducts());
        });

        activity.addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        viewModel = null;
        callBackInterface = null;
        binding = null;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu);

        ProductHelper.registerSearchFunctionality(requireContext(), menu, this);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void productSearch(String query) {
        viewModel.searchProduct(query);
        callBackInterface.closeKeyBoard();
    }

    @Override
    public void onProductClick(Product product) {
        callBackInterface.onProductClick(product);
    }
}