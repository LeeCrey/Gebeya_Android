package com.example.online_ethio_gebeya.ui.fragments.account;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.databinding.FragmentRegistrationsBinding;
import com.example.online_ethio_gebeya.helpers.ApplicationHelper;
import com.example.online_ethio_gebeya.models.FormErrors;
import com.example.online_ethio_gebeya.viewmodels.account.RegistrationsViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistrationsFragment extends Fragment {
    private TextInputEditText firstName, lastName, email, password, passwordConfirmation;
    private TextInputLayout passwordLayout;
    private Button signUp;
    private FragmentRegistrationsBinding binding;
    private RegistrationsViewModel viewModel;
    private ProgressBar loading;

    private MainActivityCallBackInterface callBackInterface;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegistrationsBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(RegistrationsViewModel.class);
        navController = Navigation.findNavController(view);

        // observer
        viewModel.getRegistrationResponse().observe(getViewLifecycleOwner(), registrationResponse -> {
            if (null == registrationResponse) {
                return;
            }

            if (registrationResponse.getOkay()) {
                navController.navigateUp();
            } else {
                if (null != registrationResponse.getMsg()) {
                    Toast.makeText(requireContext(), registrationResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    signUp.setEnabled(true);
                } else {
                    setStatusForPlaceholders(true);
                    showFormError(registrationResponse.getErrors());
                }
            }
        });

        // else
        callBackInterface = (MainActivityCallBackInterface) requireActivity();
        signUp = binding.signUp;
        firstName = binding.firstName;
        lastName = binding.lastName;
        email = binding.email;
        password = binding.password;
        passwordConfirmation = binding.passwordConfirmation;
        passwordLayout = binding.passwordLayout;
        loading = binding.progressCircular;
        binding.termsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());

        // event list ...
        signUp.setOnClickListener(v -> {
            callBackInterface.checkPermission();
            if (ApplicationHelper.checkConnection(requireActivity())) {
                v.setEnabled(false);
                viewModel.signUp(requireContext());
                setStatusForPlaceholders(false);
            }
        });
        makeTextWatcher();

        // observers
        viewModel.getFormState().observe(getViewLifecycleOwner(), this::showFormError);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        signUp = null;
        viewModel = null;
        firstName = null;
        lastName = null;
        email = null;
        password = null;
        passwordConfirmation = null;
        passwordLayout = null;
        navController = null;
    }

    private void makeTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                Map<String, String> data = new HashMap<>();
                data.put(getString(R.string.firstName), Objects.requireNonNull(firstName.getText()).toString());
                data.put(getString(R.string.lastName), Objects.requireNonNull(lastName.getText()).toString());
                data.put(getString(R.string.email), Objects.requireNonNull(email.getText()).toString());
                data.put(getString(R.string.password), Objects.requireNonNull(password.getText()).toString());
                data.put(getString(R.string.passwordConfirmation), Objects.requireNonNull(passwordConfirmation.getText()).toString());
                viewModel.dataChanged(data, requireContext());
            }
        };
        firstName.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        passwordConfirmation.addTextChangedListener(textWatcher);
    }

    private void showFormError(FormErrors errors) {
        if (errors == null) {
            return;
        }

        if (null != errors.getFirstNameError()) {
            firstName.setError(errors.getFirstNameError());
        }
        if (null != errors.getLastNameError()) {
            lastName.setError(errors.getLastNameError());
        }
        if (null != errors.getEmailError()) {
            email.setError(errors.getEmailError());
        }
        if (null != errors.getPasswordError()) {
            password.setError(errors.getPasswordError());
        }
        if (null != errors.getPasswordConfirmationError()) {
            passwordConfirmation.setError(errors.getPasswordConfirmationError());
        }
        signUp.setEnabled(errors.isRegistrationValid());
        setStatusForPlaceholders(true);
    }

    private void setStatusForPlaceholders(boolean b) {
        firstName.setEnabled(b);
        lastName.setEnabled(b);
        email.setEnabled(b);
        password.setEnabled(b);
        passwordConfirmation.setEnabled(b);
        loading.setVisibility(b ? View.GONE : View.VISIBLE);

        if (b) {
            passwordLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        } else {
            passwordLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        }
    }
}
