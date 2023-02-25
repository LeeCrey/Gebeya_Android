package com.example.online_ethio_gebeya.ui.fragments.account;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.databinding.FragmentPasswordsBinding;
import com.example.online_ethio_gebeya.viewmodels.account.FragmentRegistrationsViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PasswordsFragment extends Fragment {
    private FragmentPasswordsBinding binding;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPasswordsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // get authorization Token
        navController = Navigation.findNavController(view);
        FragmentRegistrationsViewModel registrationsViewModel = new ViewModelProvider(this).get(FragmentRegistrationsViewModel.class);
        String passwordsToken = PasswordsFragmentArgs.fromBundle(getArguments()).getPwdToken();

        TextInputLayout passwordLayout = binding.passwordLayout;
        TextInputLayout passwordConfirmationLayout = binding.passwordConfirmationLayout;
        Button save = binding.saveChanges;
        TextInputEditText password = binding.password;
        TextInputEditText passwordConfirmation = binding.passwordConfirmation;

        makeTextWatcher(password, passwordConfirmation, registrationsViewModel);
        save.setOnClickListener(v -> {
            registrationsViewModel.makeChangePasswordRequest(Objects.requireNonNull(password.getText()).toString(),
                    Objects.requireNonNull(passwordConfirmation.getText()).toString(), passwordsToken);
            save.setEnabled(false);
        });

        // observer
        registrationsViewModel.getFormState().observe(getViewLifecycleOwner(), formErrors -> {
            if (formErrors == null) {
                return;
            }

            passwordLayout.setError(formErrors.getPasswordError());
            passwordConfirmationLayout.setError(formErrors.getPasswordConfirmationError());
            save.setEnabled(formErrors.isChangePasswordFormValid());
        });
        registrationsViewModel.getRegistrationResponse().observe(getViewLifecycleOwner(), instructionsResponse -> {
            if (instructionsResponse == null) {
                return;
            }

            if (instructionsResponse.getOkay()) {
                navController.navigateUp();
            }
            Toast.makeText(requireContext(), "" + instructionsResponse.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        navController = null;
    }

    private void makeTextWatcher(@NonNull TextInputEditText password, @NonNull TextInputEditText passwordConfirmation, @NonNull FragmentRegistrationsViewModel viewModel) {
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
                viewModel.changePasswordDataChanged(Objects.requireNonNull(password.getText()).toString(), Objects.requireNonNull(passwordConfirmation.getText()).toString(), requireContext());
            }
        };
        password.addTextChangedListener(textWatcher);
        passwordConfirmation.addTextChangedListener(textWatcher);
    }
}
