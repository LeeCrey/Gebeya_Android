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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.adapters.CommentAdapter;
import com.example.online_ethio_gebeya.adapters.ProductAdapter;
import com.example.online_ethio_gebeya.adapters.ProductImagesAdapter;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.callbacks.SingleProductCallBack;
import com.example.online_ethio_gebeya.data.repositories.CartItemRepository;
import com.example.online_ethio_gebeya.databinding.FragmentProductBinding;
import com.example.online_ethio_gebeya.helpers.ProductHelper;
import com.example.online_ethio_gebeya.models.Comment;
import com.example.online_ethio_gebeya.models.Product;
import com.example.online_ethio_gebeya.models.responses.ProductShowResponse;
import com.example.online_ethio_gebeya.viewmodels.FragmentProductDetailViewModel;
import com.example.online_ethio_gebeya.viewmodels.ProductDetailFragmentViewModelFactory;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.List;

// detail page for product
public class ProductFragment extends Fragment implements SingleProductCallBack {
    private FragmentProductBinding binding;
    private ProductAdapter adapter;
    private CartItemRepository cartItemRepository;
    private NavController navController;
    private MainActivityCallBackInterface callBackInterface;

    private ProductImagesAdapter productImagesAdapter;
    private RatingBar rate;
    private Product product;
    private SpeedDialView btn;

    private CommentAdapter commentAdapter;

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
        ProductFragmentArgs arg = ProductFragmentArgs.fromBundle(getArguments());
        final long productId = arg.getProductId();

        cartItemRepository = new CartItemRepository(app);
        cartItemRepository.setAuthorizationToken(callBackInterface.getAuthorizationToken());

        // product images
        productImagesAdapter = new ProductImagesAdapter(null, requireContext());
        ViewPager2 pager2 = binding.productImages;
        pager2.setAdapter(productImagesAdapter);

        // we don't need view model here
        adapter = ProductHelper.initRecommendedProducts(this, binding.recommendedProducts);
        adapter.setCallBack(this);
        initComment();
        NavController navController = Navigation.findNavController(view);

        // p-holders
        final Button addToCart = binding.addItemToCart;
        rate = binding.productRates;
        btn = binding.speedDial;

        // view models
        FragmentProductDetailViewModel viewModel = new ViewModelProvider(this, (
                new ProductDetailFragmentViewModelFactory(app, callBackInterface.getAuthorizationToken(), productId)))
                .get(FragmentProductDetailViewModel.class);

        // config
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

        // event ...
        addToCart.setOnClickListener(v -> cartItemRepository.addItemToCart(productId, addToCart));
        btn.inflate(R.menu.floating_menu);
        // remove if unauthorized
        if (callBackInterface.getAuthorizationToken() == null) {
            btn.removeActionItem(0);
        } else {
            addToCart.setVisibility(View.VISIBLE);
        }

        btn.setOnActionSelectedListener(actionItem -> {
            if (actionItem.getId() == R.id.location) {
                callBackInterface.openLocation(6.048946f, 37.554239f);
            } else {
                openRateProduct(arg);
            }
            return false;
        });

        // observers
        viewModel.getShowResponse().observe(getViewLifecycleOwner(), this::setUiData);

        // request
        viewModel.getProductDetail();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        adapter = null;
        cartItemRepository = null;
        navController = null;
        callBackInterface = null;
        rate = null;
        product = null;
        btn = null;
        commentAdapter = null;
    }

    @Override
    public void onProductClick(Product product) {
        Bundle arg = new Bundle();
        arg.putString("productName", product.getName());
        arg.putLong("productId", product.getId());
        navController.navigate(R.id.action_navigation_product_self, arg);
    }

    // custom methods
    private void setUiData(ProductShowResponse productShowResponse) {
        if (productShowResponse == null) {
            return;
        }

        // current product
        setData(productShowResponse.getProduct());
        // related products
        adapter.setProducts(productShowResponse.getRelatedProducts());
        // comments
        List<Comment> commentList = productShowResponse.getComments();
        if (commentList.isEmpty()) {
            int v = View.GONE;
            binding.ratingAndReview.setVisibility(v);
            binding.seeAll.setVisibility(v);
        } else {
            commentAdapter.setCommentList(commentList);
        }
    }

    private void setData(Product product) {
        if (product == null) {
            return;
        }

        this.product = product;
        btn.setVisibility(View.VISIBLE);

        // shop
        TextView shopName = binding.shopNameValue;
        stopShimmer(binding.shopShimmer);
        shopName.setBackground(null);
        shopName.setText(product.getShop().getName());

        // origin
        ShimmerFrameLayout originShimmer = binding.productOriginShimmer;
        stopShimmer(originShimmer);
        TextView productOrigin = binding.productOrigin;
        TextView origin = binding.origin;
        if (product.getOrigin() == null) {
            originShimmer.setVisibility(View.GONE);
            origin.setVisibility(View.GONE);
        } else {
            if (product.getOrigin().trim().isEmpty()) {
                originShimmer.setVisibility(View.GONE);
                origin.setVisibility(View.GONE);
            } else {
                productOrigin.setBackground(null);
                productOrigin.setText(product.getOrigin());
            }
        }

        // price
        stopShimmer(binding.priceShimmer);
        String price = getString(R.string.price_in_ethio, product.getPrice());
        TextView priceView = binding.priceValue;
        priceView.setBackground(null);
        priceView.setText(price);

        // description
        stopShimmer(binding.detailShimmer);
        TextView productDescription = binding.productDescription;
        productDescription.setBackground(null);
        productDescription.setText(product.getDescription());
        productDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, callBackInterface.getFontSizeForDescription());

        // rating
        stopShimmer(binding.ratingShimmer);
        rate.setBackground(null);
        rate.setRating(product.getRate());
        rate.setLongClickable(true);

        productImagesAdapter.setImagesList(product.getImages());
    }

    private void stopShimmer(@NonNull ShimmerFrameLayout shimmerFrameLayout) {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setShimmer(null);
    }

    private void openRateProduct(ProductFragmentArgs arg) {
        Bundle paraArg = new Bundle();
        paraArg.putString("productName", arg.getProductName());
        paraArg.putLong("productId", product.getId());
        navController.navigate(R.id.action_navigation_product_to_rateFragment, paraArg);
    }

    private void initComment() {
        RecyclerView comments = binding.comments;
        comments.setLayoutManager(new LinearLayoutManager(requireContext()));
        commentAdapter = new CommentAdapter(requireActivity());
        comments.setAdapter(commentAdapter);
    }
}