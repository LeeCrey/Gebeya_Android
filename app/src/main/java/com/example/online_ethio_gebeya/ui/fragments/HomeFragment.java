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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
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
import com.example.online_ethio_gebeya.viewmodels.ProductsViewModelFactory;

import java.util.List;

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

    private RecyclerView trendingRecycler;
    private TextView trendingTxt, recommendedTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        callBackInterface = (MainActivityCallBackInterface) requireActivity();

        // location
        ProductsViewModelFactory factory = new ProductsViewModelFactory(requireActivity().getApplication(),
                callBackInterface.getAuthorizationToken(), callBackInterface.getLocation());
        viewModel = new ViewModelProvider(this, factory).get(FragmentHomeViewModel.class);
        navController = Navigation.findNavController(view);

        //
        refreshLayout = binding.refreshLayout;
        trendingTxt = binding.trending;
        recommendedTxt = binding.titleRecommended;

        trendingRecycler = binding.trendingProductList;
        // init
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        refreshLayout.setRefreshing(true);
        trendingAdapter = ProductHelper.initTrending(this, trendingRecycler);
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
        refreshLayout.setOnRefreshListener(() -> viewModel.makeApiRequest(categoryAdapter.getSelectedCategoryName()));

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
        trendingAdapter = null;
        trendingRecycler = null;
        trendingTxt = null;
        recommendedTxt = null;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        if (callBackInterface.getAuthorizationToken() != null) {
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
        viewModel.searchProduct(query, 0);
    }

    @Override
    public void onProductClick(Product product) {
        viewModel.setCurrentProduct(product);
        callBackInterface.onProductClick(product); // do the navigation part
    }

    @Override
    public void onCategorySelected(int position) {
        categoryAdapter.setSelectedCategoryPosition(position);

        refreshLayout.setRefreshing(true);
        viewModel.searchProductWithCategory(categoryAdapter.getSelectedCategoryName());
    }

    // custom
    private void setProductInAsync(ProductResponse productResponse) {
        if (productResponse == null) {
            return;
        }

        if (trendingAdapter != null) {
            List<Product> tList = productResponse.getTrending();
            int v = View.VISIBLE;
            if (tList == null) {
                v = View.GONE;
            } else {
                if (tList.isEmpty()) {
                    v = View.GONE;
                }
            }

            trendingTxt.setVisibility(v);
            trendingRecycler.setVisibility(v);
            trendingAdapter.setProducts(tList);
        }

        // product list
        productsRunnable = () -> requireActivity().runOnUiThread(() -> {
            refreshLayout.setRefreshing(false);
            productAdapter.setProducts(productResponse.getProducts());
        });
        customHandler.postDelayed(productsRunnable, 2_000);

        // recommended
        recommendRunnable = () -> requireActivity().runOnUiThread(() -> {
            if (recommendedAdapter != null) {
                if (recommendedTxt != null) {
                    List<Product> recProducts = productResponse.getRecommended();
                    int v;
                    if (recProducts != null) {
                        v = recProducts.isEmpty() ? View.GONE : View.VISIBLE;
                        recommendedAdapter.setProducts(recProducts);
                    } else {
                        v = View.GONE;
                    }
                    recommendedTxt.setVisibility(v);
                }
            }
        });
        customHandler.postDelayed(recommendRunnable, 3_000);
    }
}
