package com.example.online_ethio_gebeya.ui.fragments.account;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.databinding.FragmentSessionsBinding;
import com.example.online_ethio_gebeya.helpers.ApplicationHelper;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.viewmodels.account.SessionsViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SessionsFragment extends Fragment {
    private MainActivityCallBackInterface callBackInterface;
    private NavController navController;
    private TextInputEditText email, password;
    private TextInputLayout passwordLayout;
    private SessionsViewModel sessionsViewModel;
    private Button signIn;
    private ProgressBar loading;
    private FragmentSessionsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSessionsBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        callBackInterface = (MainActivityCallBackInterface) requireActivity();
        navController = Navigation.findNavController(view);
        sessionsViewModel = new ViewModelProvider(this).get(SessionsViewModel.class);

        email = binding.email;
        password = binding.password;
        passwordLayout = binding.passwordLayout;
        Button signUp = binding.btnSignUp;
        signIn = binding.btnSignIn;
        loading = binding.progressCircular;
        TextView textView = binding.unlockAccount;
        TextView forgotPassword = binding.forgotPassword;
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        // event list...
        signUp.setOnClickListener(v -> navController.navigate(R.id.open_navigation_registrations));
        textView.setOnClickListener(v -> openInstructionFragment(InstructionsFragment.UNLOCK_INSTRUCTION));
        forgotPassword.setOnClickListener(v -> openInstructionFragment(InstructionsFragment.PASSWORD_INSTRUCTION));

        // event
        signIn.setOnClickListener(v -> {
            callBackInterface.closeKeyBoard();
            callBackInterface.checkPermission();
            if (ApplicationHelper.checkConnection(requireActivity())) {
                setUiPlace(false);
                sessionsViewModel.login(callBackInterface.getLocale());
            }
        });
        afterInputChanged();

        // observers
        sessionsViewModel.getSessionResult().observe(getViewLifecycleOwner(), sessionResult -> {
            if (sessionResult == null) {
                return;
            }

            if (sessionResult.getOkay()) {
                PreferenceHelper.setAuthToken(requireActivity(), sessionResult.getAuthToken());
                PreferenceHelper.setFullName(requireContext(), sessionResult.getFullName());
                Toast.makeText(requireContext(), sessionResult.getFullName() + ", " + sessionResult.getMessage(), Toast.LENGTH_SHORT).show();
                navController.navigateUp();
            } else {
                Toast.makeText(requireContext(), sessionResult.getError(), Toast.LENGTH_SHORT).show();
                setUiPlace(true);
            }
        });
        sessionsViewModel.getFormErrors().observe(getViewLifecycleOwner(), errors -> {
            if (errors == null) {
                return;
            }

            email.setError(errors.getEmailError());
            if (null == errors.getPasswordError()) {
                passwordLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
            } else {
                passwordLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
            }
            password.setError(errors.getPasswordError());

            signIn.setEnabled(errors.isRegistrationValid());
        });
    }

    private void openInstructionFragment(int instructionType) {
        Bundle arg = new Bundle();
        String lblName = "lbl_name";
        // There is only two possible condition here(-1, 0 )
        String lbl = instructionType == InstructionsFragment.PASSWORD_INSTRUCTION ? getString(R.string.lbl_password) : getString(R.string.lbl_unlock);
        arg.putInt("instructionType", instructionType);
        arg.putString(lblName, lbl);
        navController.navigate(R.id.action_navigation_sessions_to_navigation_instructions, arg);
    }

    @Override
    public void onPause() {
        super.onPause();

        // when from login to signUp and back pressed, it shows previous errors
        sessionsViewModel.nullifyLiveData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        passwordLayout = null;
        email = null;
        password = null;
        navController = null;
        callBackInterface = null;
        sessionsViewModel = null;
        signIn = null;
        loading = null;
        binding = null;
    }

    private void setUiPlace(boolean status) {
        signIn.setEnabled(status);
        loading.setVisibility(status ? View.GONE : View.VISIBLE);
        passwordLayout.setEndIconMode(status ? TextInputLayout.END_ICON_PASSWORD_TOGGLE : TextInputLayout.END_ICON_NONE);

        email.setEnabled(status);
        password.setEnabled(status);
    }

    private void afterInputChanged() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String emailValue = Objects.requireNonNull(email.getText()).toString();
                String passwordValue = Objects.requireNonNull(password.getText()).toString();
                sessionsViewModel.loginInputChanged(requireContext(), emailValue, passwordValue);
            }
        };

        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
    }
}