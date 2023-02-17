package com.example.online_ethio_gebeya.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.repositories.ProductRepository;
import com.example.online_ethio_gebeya.models.responses.ProductShowResponse;

public class FragmentProductDetailViewModel extends AndroidViewModel {
    private final LiveData<ProductShowResponse> showResponse;
    private final long productId;
    private ProductRepository repository;
    private MutableLiveData<Integer> quantity;
    private int productQuantity;

    private boolean increment, decrement;

    public FragmentProductDetailViewModel(@NonNull ProductRepository _repository, long _productId) {
        super(_repository.getApplication());
        repository = _repository;
        showResponse = repository.getShowResponse();
        quantity = new MutableLiveData<>(1);
        productId = _productId;
    }

    public MutableLiveData<Integer> getQuantity() {
        return quantity;
    }

    public LiveData<ProductShowResponse> getShowResponse() {
        return showResponse;
    }

    public void getProductDetail() {
        repository.getProductDetail(productId);
    }

    public void increment() {
        Integer value = quantity.getValue();
        if (value != null) {
            decrement = value > 0;
            if (value < productQuantity) {
                value += 1;
                increment = value < productQuantity;
                quantity.postValue(value);
            }
        }
    }

    public void decrement() {
        Integer value = quantity.getValue();
        if (value != null) {
            increment = true;
            if (value > 1) {
                value -= 1;
                // after update the value
                decrement = value > 1;
                quantity.postValue(value);
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        repository.cancelConnection();
    }

    //
    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public boolean isIncrement() {
        return increment;
    }

    public boolean isDecrement() {
        return decrement;
    }
}
