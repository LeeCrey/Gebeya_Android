package com.example.online_ethio_gebeya.helpers;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.adapters.CategoryAdapter;
import com.example.online_ethio_gebeya.adapters.ProductAdapter;
import com.example.online_ethio_gebeya.adapters.TrendingAdapter;
import com.example.online_ethio_gebeya.callbacks.SearchCallBackInterface;

public class ProductHelper {
    public static CategoryAdapter initCategory(View view, FragmentActivity context) {
        final RecyclerView categoriesRecyclerView = view.findViewById(R.id.category_list);
        LinearLayoutManager linearLayout = new LinearLayoutManager(context);
        linearLayout.setOrientation(RecyclerView.HORIZONTAL);
        categoriesRecyclerView.setLayoutManager(linearLayout);
        CategoryAdapter adapter = new CategoryAdapter(context);
        categoriesRecyclerView.setAdapter(adapter);

        return adapter;
    }

    // common in HomeFragment and Product fragment
    public static ProductAdapter initProducts(Fragment activity, RecyclerView recyclerView, boolean useGridView, boolean recommended) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        if (useGridView) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(activity.getActivity(), 2);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getContext());
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
        }

        ProductAdapter productAdapter = new ProductAdapter(activity);
        productAdapter.setCalculateProductWidth(true);

        recyclerView.setAdapter(productAdapter);

        return productAdapter;
    }

    public static TrendingAdapter initTrending(Fragment activity, RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        TrendingAdapter trendingAdapter = new TrendingAdapter(activity.requireActivity());
        recyclerView.setAdapter(trendingAdapter);

        return trendingAdapter;
    }


    // common in home fragment and search fragment
    public static void registerSearchFunctionality(Context context, Menu menu, SearchCallBackInterface callBackInterface) {
        final MenuItem search = menu.findItem(R.id.menu_item_search);
        if (search != null) {
            SearchView searchView = (SearchView) search.getActionView();
            searchView.setQueryHint(context.getString(R.string.query_text));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    callBackInterface.productSearch(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    public static ProductAdapter initRecommendedProducts(Fragment activity, RecyclerView recyclerView) {
        return initProducts(activity, recyclerView, false, true);
    }
}
