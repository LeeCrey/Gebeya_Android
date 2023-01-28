package com.example.online_ethio_gebeya.viewmodels.account;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.repositories.account.SessionsRepository;
import com.example.online_ethio_gebeya.helpers.InputHelper;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.FormErrors;
import com.example.online_ethio_gebeya.models.responses.SessionResponse;

// view model for both login and logout
public class FragmentSessionsViewModel extends AndroidViewModel {
    private final MutableLiveData<FormErrors> mFormErrors;
    private final LiveData<SessionResponse> mSessionResult;
    private final LiveData<SessionResponse> mLogoutResult;
    private final SessionsRepository repository;
    private String email, password;

    public FragmentSessionsViewModel(@NonNull Application application) {
        super(application);

        repository = new SessionsRepository(application);
        mFormErrors = new MutableLiveData<>();
        mSessionResult = repository.getSessionResult();
        mLogoutResult = repository.getLogoutResult();
    }

    public LiveData<SessionResponse> getSessionResult() {
        return mSessionResult;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    /*BEGIN LOGIN PART*/

    public LiveData<FormErrors> getFormErrors() {
        return mFormErrors;
    }

    public void setFormErrors(FormErrors mFormError) {
        mFormErrors.postValue(mFormError);
    }

    public void loginInputChanged(Context context, String emailValue, String passwordValue) {
        email = emailValue;
        password = passwordValue;
        FormErrors errors = new FormErrors();
        errors.setEmailError(InputHelper.checkEmail(emailValue, context));
        errors.setPasswordError(InputHelper.checkInput(password, context));
        setFormErrors(errors);
    }

    // Login

    public LiveData<SessionResponse> getLogoutResult() {
        return mLogoutResult;
    }

    public void login(String locale) {
        Customer customer = new Customer().setCredentials(email, password);
        repository.login(customer, locale);
    }
    /*END LOGIN PART*/

    /*BEGIN LOGOUT PART*/
    public void logout(Context context) {
        String authToken = PreferenceHelper.getAuthToken(context);
        repository.logout(authToken);
    }
    /*END LOGOUT PART*/

    public void cancelConnection() {
        if (repository != null) {
            repository.cancelConnection();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        cancelConnection();
    }

    // set null
    // this helps errors not to be shown when fragments change state from pause to resume
    public void nullifyLiveData() {
        mFormErrors.setValue(null);
        repository.nullifyData();
    }
}
