package com.example.online_ethio_gebeya.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.online_ethio_gebeya.data.repositories.CommentRepository;
import com.example.online_ethio_gebeya.databinding.FragmentRateBinding;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;

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
        CommentRepository repository = new CommentRepository(requireActivity().getApplication());
        repository.setAuthorizationToken(PreferenceHelper.getAuthToken(requireContext()));

        TextView comment = binding.comment;
        RatingBar ratingBar = binding.ratingValue;
        long pId = RateFragmentArgs.fromBundle(getArguments()).getProductId();

        // event
        binding.submitComment.setOnClickListener(v -> repository.sendComment(pId, comment.getText().toString().trim(), ratingBar.getRating()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
