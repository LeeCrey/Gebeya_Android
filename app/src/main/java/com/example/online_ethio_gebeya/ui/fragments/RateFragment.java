package com.example.online_ethio_gebeya.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.online_ethio_gebeya.databinding.FragmentRateBinding;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.viewmodels.FragmentRateViewModel;

public class RateFragment extends Fragment {
    private FragmentRateBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRateBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FragmentRateViewModel viewModel = new ViewModelProvider(this).get(FragmentRateViewModel.class);

        viewModel.setAuthorization(PreferenceHelper.getAuthToken(requireContext()));
        viewModel.setProductId(RateFragmentArgs.fromBundle(getArguments()).getProductId());

        //
        ProgressBar loading = binding.progressCircular;
        Button submit = binding.submitComment;
        RatingBar ratingBar = binding.ratingValue;
        EditText comment = binding.comment;

        // observer
        viewModel.getInstructionResponse().observe(getViewLifecycleOwner(), instructionsResponse -> {
            if (instructionsResponse == null) {
                return;
            }

            if (instructionsResponse.getOkay()) {
                comment.setText("");
            }
            Toast.makeText(requireContext(), "" + instructionsResponse.getMessage(), Toast.LENGTH_SHORT).show();

            loading.setVisibility(View.GONE);
            submit.setEnabled(true);
        });

        // event
        submit.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);
            submit.setEnabled(false);
            viewModel.submitRatingWithComment(comment.getText().toString().trim(), ratingBar.getRating());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
