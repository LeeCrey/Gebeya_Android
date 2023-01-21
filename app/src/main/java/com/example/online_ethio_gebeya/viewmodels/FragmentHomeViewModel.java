package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.online_ethio_gebeya.models.Category;
import com.example.online_ethio_gebeya.models.Product;

import java.util.List;

public class FragmentHomeViewModel extends ProductsViewModel {
    private final LiveData<List<Category>> categoryList;
    private Product currentProduct; // for show

    public FragmentHomeViewModel(@NonNull Application application) {
        super(application);

        categoryList = repository.getCategories();
        repository.makeApiRequestForCategory();
    }

    public LiveData<List<Category>> getCategoryList() {
        return categoryList;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(Product product) {
        currentProduct = product;
    }
}
