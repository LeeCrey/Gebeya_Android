package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.helpers.InputHelper;
import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.FormErrors;
import com.example.online_ethio_gebeya.viewmodels.account.FragmentRegistrationsViewModel;

import java.util.Map;

public class FragmentProfileViewModelFragment extends FragmentRegistrationsViewModel {
    private final LiveData<Customer> oCustomer;

    public FragmentProfileViewModelFragment(@NonNull Application application) {
        super(application);

        repository.initForAccountUpdate();
        oCustomer = repository.getCustomer();
    }

    public LiveData<Customer> getCustomer() {
        return oCustomer;
    }

    // api
    public void updateAccount(@NonNull Context context) {
        Customer customer = new Customer().setFullName(super.map.get(context.getString(R.string.firstName)), super.map.get(context.getString(R.string.lastName)));
        customer.setPassword(map.get(context.getString(R.string.password)));
        customer.setCurrentPassword(map.get(context.getString(R.string.currentPassword)));
        customer.setPasswordConfirmation(map.get(context.getString(R.string.passwordConfirmation)));

        repository.updateProfile(customer);
    }

    public void accountUpdateDataChanged(@NonNull Map<String, String> data, @NonNull Context context) {
        map = data;

        String fName = data.get(context.getString(R.string.firstName));
        String lName = data.get(context.getString(R.string.lastName));
        String password = data.get(context.getString(R.string.password));
        String pwdConfirmation = data.get(context.getString(R.string.passwordConfirmation));
        String currentPassword = data.get(context.getString(R.string.currentPassword));

        FormErrors errors = new FormErrors();
        errors.setFirstNameError(InputHelper.checkInput(fName, context));
        errors.setLastNameError(InputHelper.checkInput(lName, context));
        errors.setCurrentPasswordError(InputHelper.checkPassword(currentPassword, context));
        if (password != null) {
            if (!password.trim().isEmpty()) {
                errors.setPasswordError(InputHelper.checkPassword(password, context));
                errors.setPasswordConfirmationError(InputHelper.checkPasswordConfirmation(pwdConfirmation, password, context));
            }
        }

        mFormState.postValue(errors);
    }

    public void getCurrentCustomer(String authorizationToken) {
        repository.getCurrentCustomer(authorizationToken);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        super.mFormState.setValue(null);
    }

    public void setAuthorizationToken(String authToken) {
        repository.setAuthorization(authToken);
    }
}
