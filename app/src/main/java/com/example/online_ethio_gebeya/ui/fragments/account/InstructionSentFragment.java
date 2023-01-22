package com.example.online_ethio_gebeya.ui.fragments.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.databinding.FragmentInstructionSentBinding;

public class InstructionSentFragment extends Fragment {
    private FragmentInstructionSentBinding binding;
    private NavController navController;
    public static final String MESSAGE = "message";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInstructionSentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);

        String msg = InstructionSentFragmentArgs.fromBundle(getArguments()).getMessage();
        binding.message.setText(msg);

        // event
        binding.openEmail.setOnClickListener(v -> ((MainActivityCallBackInterface) requireActivity()).openEmailApp());
        binding.skipIWillConfirm.setOnClickListener(v -> navController.navigateUp());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        navController = null;
    }
}