package com.example.online_ethio_gebeya.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.databinding.FragmentProfileBinding;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.viewmodels.FragmentProfileViewModelFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private TextInputEditText firstName, lastName, currentPassword, password, passwordConfirmation;
    private FragmentProfileBinding binding;
    private FragmentProfileViewModelFragment viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainActivityCallBackInterface callBackInterface = (MainActivityCallBackInterface) requireActivity();

        viewModel = new ViewModelProvider(this).get(FragmentProfileViewModelFragment.class);
        viewModel.setAuthorizationToken(PreferenceHelper.getAuthToken(requireContext()));

        firstName = binding.firstName;
        lastName = binding.lastName;
        currentPassword = binding.currentPassword;
        passwordConfirmation = binding.passwordConfirmation;
        password = binding.password; // new password

        Button save = binding.saveChanges;

        // observers
        viewModel.getCustomer().observe(getViewLifecycleOwner(), this::setCurrentCustomer);
        viewModel.getRegistrationResponse().observe(getViewLifecycleOwner(), instructionsResponse -> {
            if (instructionsResponse == null) {
                return;
            }

            if (instructionsResponse.getOkay()) {
                Toast.makeText(requireContext(), instructionsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                save.setEnabled(false);
            }
        });
        viewModel.getFormState().observe(getViewLifecycleOwner(), formErrors -> {
            if (formErrors == null) {
                return;
            }

            firstName.setError(formErrors.getFirstNameError());
            lastName.setError(formErrors.getLastNameError());
            password.setError(formErrors.getPasswordError());
            passwordConfirmation.setError(formErrors.getPasswordConfirmationError());
            currentPassword.setError(formErrors.getPasswordError());

            boolean status = formErrors.getFirstNameError() == null && formErrors.getLastNameError() == null && formErrors.getPasswordError() == null;
            save.setEnabled(status);
        });

        save.setOnClickListener(v -> viewModel.updateAccount(requireContext()));
        makeTextWatcher();

        // make api
        viewModel.getCurrentCustomer(callBackInterface.getAuthorizationToken());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        firstName = null;
        lastName = null;
        currentPassword = null;
        passwordConfirmation = null;
        password = null;
        viewModel = null;
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
                data.put(getString(R.string.password), Objects.requireNonNull(password.getText()).toString());
                data.put(getString(R.string.passwordConfirmation), Objects.requireNonNull(passwordConfirmation.getText()).toString());
                data.put(getString(R.string.currentPassword), Objects.requireNonNull(currentPassword.getText()).toString());
                viewModel.accountUpdateDataChanged(data, requireContext());
            }
        };
        firstName.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        currentPassword.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        passwordConfirmation.addTextChangedListener(textWatcher);
    }

    private void setCurrentCustomer(Customer customer) {
        if (customer == null) {
            return;
        }

        firstName.setText(customer.getFirstName());
        lastName.setText(customer.getLastName());
        currentPassword.setError(null);
    }
}
