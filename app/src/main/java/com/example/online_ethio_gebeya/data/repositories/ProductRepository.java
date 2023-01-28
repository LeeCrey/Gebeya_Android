package com.example.online_ethio_gebeya.data.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.ProductApi;
import com.example.online_ethio_gebeya.models.Category;
import com.example.online_ethio_gebeya.models.responses.ProductResponse;
import com.example.online_ethio_gebeya.models.responses.ProductShowResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private static final String TAG = "ProductRepository";
    private final MutableLiveData<ProductResponse> mProductIndex;
    private final MutableLiveData<List<Category>> mCategories;
    private final MutableLiveData<ProductShowResponse> mShowResponse;
    private final ProductApi api;
    private Application application;
    private String authorizationToken = null;
    private Call<ProductResponse> productResponseCall;
    private Call<List<Category>> categoryCall;
    private Call<ProductShowResponse> showResponseCall;

    public ProductRepository(@NonNull Application application) {
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(ProductApi.class);

        mProductIndex = new MutableLiveData<>();
        mCategories = new MutableLiveData<>();
        mShowResponse = new MutableLiveData<>();
    }

    public LiveData<ProductResponse> getProductIndex() {
        return mProductIndex;
    }

    public LiveData<List<Category>> getCategories() {
        return mCategories;
    }

    public LiveData<ProductShowResponse> getShowResponse() {
        return mShowResponse;
    }

    // product index page
    public void getListFromApi(String category) {
        if (productResponseCall != null) {
            productResponseCall.cancel();
        }

        productResponseCall = api.index(authorizationToken, category);
        productResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    mProductIndex.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                // ignore
                t.printStackTrace();
                ProductResponse rep = new ProductResponse();
                rep.setProducts(new ArrayList<>());
                mProductIndex.postValue(rep);
            }
        });
    }

    public void makeApiRequestForCategory() {
        if (categoryCall != null) {
            categoryCall.cancel();
        }

        // code for e-tag
        categoryCall = api.categories();
        categoryCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                Category all = new Category();
                all.setCategoryId(-1);
                all.setName("All");
                all.setAmharic("ሁሉም");
                all.setSelected(true);
                List<Category> categories = new ArrayList<>();
                categories.add(all);
                List<Category> res = response.body();
                if (res != null) {
                    categories.addAll(res);
                }
                mCategories.postValue(categories);
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                // ignore
            }
        });
    }

    // show
    public void getProductDetail(long productId) {
        if (showResponseCall != null) {
            showResponseCall.cancel();
        }

        showResponseCall = api.show(authorizationToken, productId);
        showResponseCall.enqueue(new Callback<ProductShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductShowResponse> call, @NonNull Response<ProductShowResponse> response) {
                if (response.isSuccessful()) {
                    mShowResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductShowResponse> call, @NonNull Throwable t) {
                // ignore
            }
        });
    }

    public void cancelConnection() {
        if (productResponseCall != null) {
            productResponseCall.cancel();
        }

        if (categoryCall != null) {
            categoryCall.cancel();
        }

        if (showResponseCall != null) {
            showResponseCall.cancel();
        }
    }

    //
    public void setAuthToken(String authToken) {
        authorizationToken = authToken;
    }

    public Application getApplication() {
        return application;
    }
}