package com.example.online_ethio_gebeya.ui.fragments.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.databinding.FragmentInstructionsBinding;
import com.example.online_ethio_gebeya.helpers.ApplicationHelper;
import com.example.online_ethio_gebeya.viewmodels.InstructionsViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/**
 * This fragment is for
 * # Passwords
 * `   => request password reset link
 * # Confirmation
 * `    => request confirmation link
 * `    => confirm account
 * # Unlocks
 * `    => request account unlock link
 * `    => unlock account
 */
public class InstructionsFragment extends Fragment {
    public static final Integer PASSWORD_INSTRUCTION = -1;
    public static final Integer UNLOCK_INSTRUCTION = 0;
    public static final Integer CONFIRMATION_INSTRUCTION = 1;

    private FragmentInstructionsBinding binding;
    private NavController navController;
    private InstructionsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInstructionsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(InstructionsViewModel.class);

        String fullUrl = getString(R.string.base_url) + "/customers/";

        InstructionsFragmentArgs args = InstructionsFragmentArgs.fromBundle(getArguments());
        int type = args.getInstructionType();
        Button sendButton = binding.sendInstructions;
        TextView confirmation = binding.confirmAccount;
        TextInputEditText email = binding.email;
        ProgressBar loading = binding.progressCircular;

        // observers
        viewModel.getInstructionResponse().observe(getViewLifecycleOwner(), instructionsResponse -> {
            if (instructionsResponse == null) {
                return;
            }

            loading.setVisibility(View.GONE);

            if (instructionsResponse.isUnlockPasswordConfirm()) {
                Bundle arg = new Bundle();
                arg.putString(InstructionSentFragment.MESSAGE, instructionsResponse.getMessage());
                navController.navigate(R.id.open_instruction_sent_nav, arg);
            } else {
                if (instructionsResponse.getOkay()) {
                    Toast.makeText(requireContext(), instructionsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), instructionsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
                // show success modal here
//                navController.navigateUp();
            }
        });

        // if came through deep link
        String unlockToken = args.getUnlockToken();
        String confirmationToken = args.getConfirmationToken();
        if (unlockToken != null || confirmationToken != null) {
            // disable both
            email.setEnabled(false);
            sendButton.setEnabled(false);

            fullUrl += unlockToken != null ? ("unlock?unlock_token=" + unlockToken) : ("confirmation?confirmation_token=" + confirmationToken);
            viewModel.sendFinishRequest(fullUrl);
            return;
        }

        // else
        if (type == UNLOCK_INSTRUCTION) {
            fullUrl += "unlock";
        } else if (type == PASSWORD_INSTRUCTION) {
            fullUrl += "password";
            binding.headerForInstruction.setText(R.string.forgot_password);
            binding.textView2.setText(R.string.password_instruction);
        } else {
            fullUrl += "confirmation";
            confirmation.setVisibility(View.GONE);
            binding.headerForInstruction.setText(R.string.msg_account_confirmed);
            binding.textView2.setText(R.string.confirm_instruction);
        }

        //event
        ApplicationHelper.initEmailWatcher(requireContext(), email, sendButton);
        confirmation.setOnClickListener(v -> {
            Bundle arg = new Bundle();
            arg.putInt("instruction_type", 1);
            arg.putString("lbl_name", getString(R.string.lbl_confirm));
            navController.navigate(R.id.action_navigation_instructions_self, arg);
        });
        // 3 operations
        final String finalFullUrl = fullUrl;
        binding.sendInstructions.setOnClickListener(v -> {
            sendButton.setEnabled(false);
            loading.setVisibility(View.VISIBLE);
            email.setEnabled(false);
            viewModel.sendInstruction(Objects.requireNonNull(email.getText()).toString(), finalFullUrl);
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        viewModel.nullifyLiveData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        navController = null;
        viewModel = null;
    }
}