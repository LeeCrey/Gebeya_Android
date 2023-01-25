package com.example.online_ethio_gebeya.ui.fragments;

import android.app.Application;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.adapters.ProductAdapter;
import com.example.online_ethio_gebeya.adapters.ProductImagesAdapter;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.callbacks.SingleProductCallBack;
import com.example.online_ethio_gebeya.data.repositories.CartItemRepository;
import com.example.online_ethio_gebeya.databinding.FragmentProductBinding;
import com.example.online_ethio_gebeya.helpers.ProductHelper;
import com.example.online_ethio_gebeya.models.Product;
import com.example.online_ethio_gebeya.models.responses.ProductShowResponse;
import com.example.online_ethio_gebeya.viewmodels.ProductDetailFragmentViewModel;
import com.example.online_ethio_gebeya.viewmodels.ProductDetailFragmentViewModelFactory;
import com.facebook.shimmer.ShimmerFrameLayout;

// detail page for product
public class ProductFragment extends Fragment implements SingleProductCallBack {
    private FragmentProductBinding binding;
    private ProductAdapter adapter;
    private CartItemRepository cartItemRepository;
    private NavController navController;
    private MainActivityCallBackInterface callBackInterface;

    private ProductImagesAdapter productImagesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Application app = requireActivity().getApplication();
        navController = Navigation.findNavController(view);
        callBackInterface = (MainActivityCallBackInterface) requireActivity();
        final int productId = ProductFragmentArgs.fromBundle(getArguments()).getProductId();
        cartItemRepository = new CartItemRepository(app);
        cartItemRepository.setAuthorizationToken(callBackInterface.getAuthorizationToken());

        // product images
        productImagesAdapter = new ProductImagesAdapter(null, requireContext());
        ViewPager2 pager2 = binding.productImages;
        pager2.setAdapter(productImagesAdapter);

        // we don't need view model here
        adapter = ProductHelper.initRecommendedProducts(this, binding.recommendedProducts);
        adapter.setCallBack(this);
        NavController navController = Navigation.findNavController(view);

        // p-holders
        final Button addToCart = binding.addItemToCart;

        // view models
        ProductDetailFragmentViewModel thisViewModel = new ViewModelProvider(this, (
                new ProductDetailFragmentViewModelFactory(app, callBackInterface.getAuthorizationToken(), productId)))
                .get(ProductDetailFragmentViewModel.class);

        // config
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

        if (callBackInterface.getAuthorizationToken() != null) {
            addToCart.setVisibility(View.VISIBLE);
        }

        // event ...
        addToCart.setOnClickListener(v -> cartItemRepository.addItemToCart(productId, addToCart));

        // observers
        thisViewModel.getShowResponse().observe(getViewLifecycleOwner(), this::setUiData);

        // request
        thisViewModel.getProductDetail();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        adapter = null;
        cartItemRepository = null;
        navController = null;
        callBackInterface = null;
    }

    @Override
    public void onProductClick(Product product) {
        Bundle arg = new Bundle();
        arg.putString("productName", product.getName());
        arg.putInt("productId", product.getId());
        navController.navigate(R.id.action_navigation_product_self, arg);
    }

    // custom methods
    private void setUiData(ProductShowResponse productShowResponse) {
        if (productShowResponse == null) {
            return;
        }

        setData(productShowResponse.getProduct());
//        adapter.setFLAG(2); // does not matter
        adapter.setProducts(productShowResponse.getRelatedProducts());
    }

    private void setData(Product product) {
        if (product == null) {
            return;
        }

        // origin
        ShimmerFrameLayout originShimmer = binding.productOriginShimmer;
        stopShimmer(originShimmer);
        TextView productOrigin = binding.productOrigin;
        productOrigin.setBackground(null);
        productOrigin.setText(product.getOrigin());

        // price
        ShimmerFrameLayout priceShimmer = binding.priceShimmer;
        stopShimmer(priceShimmer);
        String price = getString(R.string.price_in_ethio, product.getPrice());
        TextView priceView = binding.priceValue;
        priceView.setBackground(null);
        priceView.setText(price);

        // description
        ShimmerFrameLayout detailShimmer = binding.detailShimmer;
        stopShimmer(detailShimmer);
        TextView productDescription = binding.productDescription;
        productDescription.setBackground(null);
        productDescription.setText(product.getDescription());
        productDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, callBackInterface.getFontSizeForDescription());

        // rating
        ShimmerFrameLayout ratingShimmer = binding.ratingShimmer;
        stopShimmer(ratingShimmer);
        RatingBar productRate = binding.productRates;
        productRate.setBackground(null);
        productRate.setRating(product.getRate());

        productImagesAdapter.setImagesList(product.getImages());
    }

    private void stopShimmer(@NonNull ShimmerFrameLayout shimmerFrameLayout) {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setShimmer(null);
    }
}