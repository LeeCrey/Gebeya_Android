package com.example.online_ethio_gebeya.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.adapters.CategoryAdapter;
import com.example.online_ethio_gebeya.adapters.ProductAdapter;
import com.example.online_ethio_gebeya.adapters.TrendingAdapter;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.callbacks.ProductCallBackInterface;
import com.example.online_ethio_gebeya.databinding.FragmentHomeBinding;
import com.example.online_ethio_gebeya.helpers.ProductHelper;
import com.example.online_ethio_gebeya.models.Product;
import com.example.online_ethio_gebeya.models.responses.ProductResponse;
import com.example.online_ethio_gebeya.viewmodels.FragmentHomeViewModel;

public class HomeFragment extends Fragment implements MenuProvider, ProductCallBackInterface {
    private SwipeRefreshLayout refreshLayout;
    private FragmentHomeBinding binding;
    private ProductAdapter productAdapter, recommendedAdapter;
    private TrendingAdapter trendingAdapter;

    private Runnable productsRunnable, recommendRunnable;
    private Handler customHandler;
    private HandlerThread handlerThread;
    private MainActivityCallBackInterface callBackInterface;
    private NavController navController;
    private FragmentHomeViewModel viewModel;
    private CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(FragmentHomeViewModel.class);
        callBackInterface = (MainActivityCallBackInterface) requireActivity();
        navController = Navigation.findNavController(view);

        //
        refreshLayout = binding.refreshLayout;

        // init
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        refreshLayout.setRefreshing(true);
        trendingAdapter = ProductHelper.initTrending(this, binding.trendingProductList);
        trendingAdapter.setCallBack(this);
        categoryAdapter = ProductHelper.initCategory(view, requireActivity());
        categoryAdapter.setCallBack(this);
        productAdapter = ProductHelper.initProducts(this, binding.productsRecyclerView, true, false);
        productAdapter.setCalculateProductWidth(false);
        productAdapter.setCallBack(this);
        recommendedAdapter = ProductHelper.initRecommendedProducts(this, binding.recommendedProducts);
        recommendedAdapter.setCallBack(this);

        // observers
        viewModel.getProductIndex().observe(getViewLifecycleOwner(), this::setProductInAsync);
        viewModel.getCategoryList().observe(getViewLifecycleOwner(), categoryAdapter::setCategories);

        // event
        refreshLayout.setOnRefreshListener(() -> {
            viewModel.makeApiRequest("all");
        });

        // handlers
        handlerThread = new HandlerThread("customUiHandler");
        handlerThread.start();
        customHandler = new Handler(handlerThread.getLooper());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        /* stop handler  */
        handlerThread.quit();
        customHandler.removeCallbacks(productsRunnable);
        customHandler.removeCallbacks(recommendRunnable);

        handlerThread = null;
        customHandler = null;
        productsRunnable = null;

        binding = null;
        refreshLayout = null;
        productAdapter = null;
        recommendedAdapter = null;
        navController = null;
        callBackInterface = null;
        viewModel = null;
        categoryAdapter = null;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        String token = callBackInterface.getAuthorizationToken();
        if (token != null) {
            return;
        }

        menuInflater.inflate(R.menu.home_menu, menu);
        ProductHelper.registerSearchFunctionality(requireContext(), menu, this);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return NavigationUI.onNavDestinationSelected(menuItem, navController);
    }

    @Override
    public void productSearch(String query) {

    }

    @Override
    public void onProductClick(Product product) {
        viewModel.setCurrentProduct(product);
        callBackInterface.onProductClick(product); // do the navigation part
    }

    @Override
    public void onCategorySelected(int position) {
        categoryAdapter.setSelectedCategoryPosition(position);

        String lst = categoryAdapter.getSelectedCategoryName();
        viewModel.searchProductWithCategory(position);
    }

    // custom
    private void setProductInAsync(ProductResponse productResponse) {
        if (productResponse == null) {
            return;
        }

        if (trendingAdapter != null) {
            trendingAdapter.setProducts(productResponse.getProducts());
        }

        // product list
        productsRunnable = () -> requireActivity().runOnUiThread(() -> {
            refreshLayout.setRefreshing(false);
            if (productAdapter != null) {
                productAdapter.setProducts(productResponse.getProducts());
            }
        });
        customHandler.postDelayed(productsRunnable, 1_500);

        // recommended
        recommendRunnable = () -> requireActivity().runOnUiThread(() -> {
            if (recommendedAdapter != null) {
                recommendedAdapter.setProducts(productResponse.getProducts());
            }
        });
        customHandler.postDelayed(recommendRunnable, 2_500);
    }
}
