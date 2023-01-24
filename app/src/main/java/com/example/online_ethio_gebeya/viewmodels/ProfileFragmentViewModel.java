package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.helpers.InputHelper;
import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.FormErrors;
import com.example.online_ethio_gebeya.viewmodels.account.RegistrationsViewModel;

import java.io.File;
import java.util.Map;

public class ProfileFragmentViewModel extends RegistrationsViewModel {
    private final LiveData<Customer> oCustomer;

    public ProfileFragmentViewModel(@NonNull Application application) {
        super(application);

        repository.initForAccountUpdate();
        oCustomer = repository.getCustomer();
    }

    public LiveData<Customer> getCustomer() {
        return oCustomer;
    }

    // api
    public void updateAccount(@NonNull Context context, String authorization, File file) {
        Customer customer = new Customer().setFullName(super.map.get(context.getString(R.string.firstName)), super.map.get(context.getString(R.string.lastName)));
        customer.setProfile(file);
        customer.setPassword(map.get(context.getString(R.string.password)));
        // api
        repository.updateProfile(customer, authorization);
    }

    public void accountUpdateDataChanged(@NonNull Map<String, String> data, @NonNull Context context) {
        super.map = data;

        String fName = data.get(context.getString(R.string.firstName));
        String lName = data.get(context.getString(R.string.lastName));
        String password = data.get(context.getString(R.string.password));

        FormErrors errors = new FormErrors();
        errors.setFirstNameError(InputHelper.checkInput(fName, context));
        errors.setLastNameError(InputHelper.checkInput(lName, context));
        errors.setPasswordError(InputHelper.checkPassword(password, context));

        super.mFormState.postValue(errors);
    }

    public void getCurrentCustomer(String authorizationToken) {
        repository.getCurrentCustomer(authorizationToken);
    }

    @Override
    protected void onCleared() {
        super.mFormState.setValue(null);

        super.onCleared();
    }
}
