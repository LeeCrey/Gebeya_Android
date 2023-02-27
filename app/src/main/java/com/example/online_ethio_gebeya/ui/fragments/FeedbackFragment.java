package com.example.online_ethio_gebeya.ui.fragments;

import android.os.Bundle;
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

import com.example.online_ethio_gebeya.databinding.FragmentFeedbackBinding;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.viewmodels.InstructionsViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class FeedbackFragment extends Fragment {
    private FragmentFeedbackBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFeedbackBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextInputEditText feedback = binding.feedBack;
        Button send = binding.sendFeedback;
        ProgressBar loading = binding.loading;

        InstructionsViewModel viewModel = new ViewModelProvider(this).get(InstructionsViewModel.class);

        // observer
        viewModel.getInstructionResponse().observe(getViewLifecycleOwner(), instructionsResponse -> {
            if (instructionsResponse == null) {
                return;
            }

            if (instructionsResponse.getOkay()) {
                feedback.setText("");
            }
            send.setEnabled(true);
            loading.setVisibility(View.GONE);

            Toast.makeText(requireContext(), instructionsResponse.getMessage(), Toast.LENGTH_SHORT).show();
        });

        // event list
        final String authToken = PreferenceHelper.getAuthToken(requireContext());
        send.setOnClickListener(v -> {
            String msg = Objects.requireNonNull(feedback.getText()).toString().trim();
            if (msg.isEmpty()) {
                Toast.makeText(requireContext(), "message required", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.sendFeedback(authToken, Objects.requireNonNull(feedback.getText()).toString());
                send.setEnabled(false);
                loading.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
