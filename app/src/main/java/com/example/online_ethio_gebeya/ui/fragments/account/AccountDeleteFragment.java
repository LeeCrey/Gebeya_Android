package com.example.online_ethio_gebeya.ui.fragments.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.databinding.FragmentAccountDeleteBinding;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.viewmodels.InstructionsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AccountDeleteFragment extends BottomSheetDialogFragment {
    private FragmentAccountDeleteBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountDeleteBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        InstructionsViewModel viewModel = new ViewModelProvider(this).get(InstructionsViewModel.class);
        viewModel.setAuthorizationToken(PreferenceHelper.getAuthToken(requireContext()));

        setCancelable(false);

        Button delete = binding.confirm;
        TextInputEditText password = binding.password;
        ProgressBar loading = binding.progressCircular;

        // obs
        viewModel.getInstructionResponse().observe(getViewLifecycleOwner(), instructionsResponse -> {
            if (instructionsResponse == null) {
                return;
            }

            if (instructionsResponse.getOkay()) {
                PreferenceHelper.clearPref(requireContext());
                dismiss();
            } else {
                delete.setEnabled(true);
            }

            loading.setVisibility(View.GONE);
            Toast.makeText(requireContext(), instructionsResponse.getMessage(), Toast.LENGTH_SHORT).show();
        });

        // event
        delete.setOnClickListener(v -> {
            String pwd = Objects.requireNonNull(password.getText()).toString().trim();
            if (pwd.isEmpty()) {
                password.setError(getString(R.string.required));
            } else {
                delete.setEnabled(false);
                loading.setVisibility(View.VISIBLE);
                viewModel.deleteAccount(pwd);
            }
        });
        binding.cancelButton.setOnClickListener(v -> {
            dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
