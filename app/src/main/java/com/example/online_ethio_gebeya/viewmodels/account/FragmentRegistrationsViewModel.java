package com.example.online_ethio_gebeya.viewmodels.account;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.data.repositories.account.RegistrationsRepository;
import com.example.online_ethio_gebeya.helpers.InputHelper;
import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.FormErrors;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import java.util.Map;

public class FragmentRegistrationsViewModel extends AndroidViewModel {
    protected final MutableLiveData<InstructionsResponse> mRegistrationResponse;
    protected final MutableLiveData<FormErrors> mFormState;
    protected final RegistrationsRepository repository;
    protected Map<String, String> map;

    public FragmentRegistrationsViewModel(@NonNull Application application) {
        super(application);

        repository = new RegistrationsRepository(application);
        mFormState = new MutableLiveData<>();
        mRegistrationResponse = repository.getRegistrationResponse();
    }

    public LiveData<InstructionsResponse> getRegistrationResponse() {
        return mRegistrationResponse;
    }

    public LiveData<FormErrors> getFormState() {
        return mFormState;
    }

    public void dataChanged(Map<String, String> data, Context context) {
        map = data;

        String fName = data.get(context.getString(R.string.firstName));
        String lName = data.get(context.getString(R.string.lastName));
        String email = data.get(context.getString(R.string.email));
        String password = data.get(context.getString(R.string.password));
        String passwordConfirmation = data.get(context.getString(R.string.passwordConfirmation));

        FormErrors errors = new FormErrors();
        errors.setFirstNameError(InputHelper.checkInput(fName, context));
        errors.setLastNameError(InputHelper.checkInput(lName, context));
        errors.setEmailError(InputHelper.checkEmail(email, context));
        errors.setPasswordError(InputHelper.checkPassword(password, context));
        errors.setPasswordConfirmationError(InputHelper.checkPasswordConfirmation(password, passwordConfirmation, context));

        mFormState.postValue(errors);
    }

    public void changePasswordDataChanged(@NonNull String pwd, @NonNull String pwdConfirmation, Context context) {
        FormErrors errors = new FormErrors();
        errors.setPasswordError(InputHelper.checkPassword(pwd, context));
        errors.setPasswordConfirmationError(InputHelper.checkPasswordConfirmation(pwd, pwdConfirmation, context));

        mFormState.postValue(errors);
    }

    // APIs
    public void signUp(Context context) {
        if (null != repository) {
            Customer customer = new Customer().setCredentials(map.get(context.getString(R.string.email)), map.get(context.getString(R.string.password))).setFullName(map.get(context.getString(R.string.firstName)), map.get(context.getString(R.string.lastName)));
            customer.setPasswordConfirmation(map.get(context.getString(R.string.passwordConfirmation)));
            repository.signUp(customer);
        }
    }

    public void confirmAccount(@NonNull String confirmUrl) {
        repository.confirmAccount(confirmUrl);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (null != repository) {
            repository.cancelConnection();
        }
    }

    public void makeChangePasswordRequest(String pwd, String pwdConfirmation, String passwordsToken) {
        Customer customer = new Customer();
        customer.setPassword(pwd);
        customer.setPasswordConfirmation(pwdConfirmation);
        customer.setResetPasswordToken(passwordsToken);

        repository.changePassword(customer);
    }
}
