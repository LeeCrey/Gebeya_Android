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
import java.util.Objects;

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
        customer.setCurrentPassword(null);

        repository.updateProfile(customer);
    }

    public void accountUpdateDataChanged(@NonNull Map<String, String> data, @NonNull Context context) {
        super.map = data;

        String fName = data.get(context.getString(R.string.firstName));
        String lName = data.get(context.getString(R.string.lastName));
        String password = data.get(context.getString(R.string.currentPassword));
        String pwd = Objects.requireNonNull(data.get(context.getString(R.string.password))).trim();
        String pwdConfirmation = Objects.requireNonNull(data.get(context.getString(R.string.passwordConfirmation))).trim();

        FormErrors errors = new FormErrors();
        errors.setFirstNameError(InputHelper.checkInput(fName, context));
        errors.setLastNameError(InputHelper.checkInput(lName, context));
        errors.setPasswordError(InputHelper.checkPassword(password, context));
        if (!pwd.isEmpty()) {
            errors.setPasswordError(InputHelper.checkPassword(pwd, context));
            errors.setPasswordConfirmationError(InputHelper.checkPasswordConfirmation(pwd, pwdConfirmation, context));
        }

        super.mFormState.postValue(errors);
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
