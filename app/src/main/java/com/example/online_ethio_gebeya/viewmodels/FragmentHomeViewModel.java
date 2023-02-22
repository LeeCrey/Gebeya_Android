package com.example.online_ethio_gebeya.viewmodels;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.online_ethio_gebeya.data.repositories.ProductRepository;
import com.example.online_ethio_gebeya.models.Category;
import com.example.online_ethio_gebeya.models.Product;

import java.util.List;

public class FragmentHomeViewModel extends ProductsViewModel {
    private final LiveData<List<Category>> categoryList;
    private Product currentProduct; // for show
    private Location location;

    public FragmentHomeViewModel(@NonNull ProductRepository repository) {
        super(repository);

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

    public void searchProductWithCategory(@NonNull String cat) {
        repository.getListFromApi(cat);
    }

    public void makeApiRequest(String cat) {
        repository.getListFromApi(cat);
    }

    public void searchProduct(String query, int offSet) {
        repository.searchProduct(query, offSet);
    }

    public void setLocation(Location location) {
        repository.setLocation(location);
    }
}
