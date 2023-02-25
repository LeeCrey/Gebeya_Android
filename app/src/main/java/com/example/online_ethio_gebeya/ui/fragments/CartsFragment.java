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
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.adapters.CartsAdapter;
import com.example.online_ethio_gebeya.callbacks.CartCallBackInterface;
import com.example.online_ethio_gebeya.models.Cart;
import com.example.online_ethio_gebeya.viewmodels.FragmentCartsViewModel;

public class CartsFragment extends Fragment implements MenuProvider, CartCallBackInterface {
    private CartsAdapter cartsAdapter;
    private FragmentCartsViewModel viewModel;
    private NavController navController;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(FragmentCartsViewModel.class);
        refreshLayout = (SwipeRefreshLayout) view;
        refreshLayout.setRefreshing(true);

        initRecyclerView(view);

        refreshLayout.setOnRefreshListener(() -> viewModel.getCarts());

        // observers
        viewModel.getCartResponse().observe(getViewLifecycleOwner(), carts -> {
            if (carts == null) {
                return;
            }
            refreshLayout.setRefreshing(false);
            cartsAdapter.submitList(carts);
        });

        // add menu host
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        cartsAdapter = null;
        viewModel = null;
        navController = null;
        recyclerView = null;
        refreshLayout = null;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.carts_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        final int id = menuItem.getItemId();
        if (id == R.id.clear_carts) {
            viewModel.deleteAllCarts();
        }
        return false;
    }

    @Override
    public void onItemClicked(@NonNull Cart cart) {
        Bundle arg = new Bundle();
        arg.putString("cartName", cart.getName());
        arg.putLong("cartId", cart.getId());
        navController.navigate(R.id.open_cart_items, arg);
    }

    private void initRecyclerView(View view) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(requireActivity());
        recyclerView = view.findViewById(R.id.carts_recycler_view);
        recyclerView.setLayoutManager(manager);

        cartsAdapter = new CartsAdapter(requireActivity(), this);
        recyclerView.setAdapter(cartsAdapter);

        // event
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Cart cart = cartsAdapter.getCurrentList().get(viewHolder.getAdapterPosition());
                viewModel.deleteCart(cart);
            }
        });
        touchHelper.attachToRecyclerView(recyclerView);
    }
}
