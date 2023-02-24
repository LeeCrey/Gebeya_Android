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
import com.example.online_ethio_gebeya.adapters.ItemAdapter;
import com.example.online_ethio_gebeya.callbacks.CartItemCallBackInterface;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.models.Item;
import com.example.online_ethio_gebeya.viewmodels.FragmentCartItemViewModel;

// show cart items
public class CartFragment extends Fragment implements MenuProvider, CartItemCallBackInterface {
    long cartId = -1L;
    private ItemAdapter itemAdapter;
    private FragmentCartItemViewModel viewModel;
    private NavController navController;
    private MainActivityCallBackInterface callBackInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(FragmentCartItemViewModel.class);
        callBackInterface = (MainActivityCallBackInterface) requireActivity();

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view;
        refreshLayout.setRefreshing(true);

        // init
        initView(view);
        viewModel.init(callBackInterface.getAuthorizationToken());
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        // observer
        viewModel.getCartItemResponse().observe(getViewLifecycleOwner(), response -> {
            itemAdapter.updateList(response);
            refreshLayout.setRefreshing(false);
            refreshLayout.setEnabled(false);
        });
        viewModel.getOrderCreated().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean == null) {
                return;
            }

            if (aBoolean) {
                navController.navigateUp();
            }
        });
        viewModel.getUpdatedCartItemPosition().observe(getViewLifecycleOwner(), itemAdapter::cartItemUpdate);

        // api
        CartFragmentArgs args = CartFragmentArgs.fromBundle(getArguments());
        cartId = args.getCartId();
        viewModel.getCartItems(cartId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        itemAdapter = null;
        viewModel = null;
        navController = null;
        callBackInterface = null;
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.cart_items_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(this);
        itemAdapter.setCallBack(this);
        recyclerView.setAdapter(itemAdapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.removeItemFromCart(viewHolder.getAdapterPosition());
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
            createDialog();
        }

        return false;
    }

    private void createDialog() {
        CheckoutFragment bottomSheet = new CheckoutFragment(itemAdapter.getCalculatedValue(), itemAdapter,
                cartId, callBackInterface.getAuthorizationToken(), viewModel);
        bottomSheet.show(getParentFragmentManager(), "ModalBottomSheet");
    }

    @Override
    public void onCartItemClick(@NonNull Item item, int position) {
        EditCartItemFragment bottomSheet = new EditCartItemFragment(item, viewModel, position);
        bottomSheet.show(getParentFragmentManager(), "CartItemEditBottomSheet");
    }
}