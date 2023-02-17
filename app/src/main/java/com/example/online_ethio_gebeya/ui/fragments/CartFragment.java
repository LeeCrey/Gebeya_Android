package com.example.online_ethio_gebeya.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
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
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.adapters.CartItemAdapter;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.viewmodels.FragmentCartItemViewModel;

// show cart items
public class CartFragment extends Fragment implements MenuProvider {
    long cartId = -1L;
    private CartItemAdapter cartItemAdapter;
    private FragmentCartItemViewModel fragmentCartItemViewModel;
    private String merchantId;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        fragmentCartItemViewModel = new ViewModelProvider(this).get(FragmentCartItemViewModel.class);
        MainActivityCallBackInterface callBackInterface = (MainActivityCallBackInterface) requireActivity();

        // init
        initView(view);
        fragmentCartItemViewModel.init(callBackInterface.getAuthorizationToken());
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        // observer
        fragmentCartItemViewModel.getCartItemResponse().observe(getViewLifecycleOwner(), cartItemAdapter::updateList);

//        // api
        CartFragmentArgs args = CartFragmentArgs.fromBundle(getArguments());
        cartId = args.getCartId();
        merchantId = args.getMerchantId();
        fragmentCartItemViewModel.getCartItems(cartId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        cartItemAdapter = null;
        fragmentCartItemViewModel = null;
        merchantId = null;
        navController = null;
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.cart_items_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        cartItemAdapter = new CartItemAdapter(this);
        recyclerView.setAdapter(cartItemAdapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                fragmentCartItemViewModel.removeItemFromCart(viewHolder.getAdapterPosition());
            }
        });
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.cart_item_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        final int id = menuItem.getItemId();
        if (id == R.id.check_out) {
            String baseUrl = getString(R.string.base_url) + "/carts/" + cartId;
            String auth = PreferenceHelper.getAuthToken(requireContext());
            // open dialog to order
        }

        return false;
    }
}