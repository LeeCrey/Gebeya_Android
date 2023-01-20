package com.example.online_ethio_gebeya.helpers;

import android.content.Context;
import android.util.Patterns;

import com.example.online_ethio_gebeya.R;

public class InputHelper {
    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }
        return !(password.trim().isEmpty());
    }

    public static boolean isInputNotEmpty(String data) {
        if (data == null) {
            return false;
        }

        return !data.trim().isEmpty();
    }

    public static boolean isPasswordConfirmationValid(String password, String passwordConfirmation) {
        if (password != null && passwordConfirmation != null) {
            return password.equals(passwordConfirmation);
        }
        return false;
    }

    public static String checkInput(String data, Context context) {
        boolean invalid = (data == null) || (data.trim().isEmpty());
        if (invalid) {
            return context.getString(R.string.required);
        }

        return null;
    }

    public static String checkEmail(String email, Context context) {
        String msg = checkInput(email, context);
        if (msg != null) {
            return msg;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return context.getString(R.string.invalid);
        }

        return null;
    }

    public static String checkPassword(String password, Context context) {
        String msg = checkInput(password, context);
        if (msg != null) {
            return msg;
        }

        if (password.length() < 6) {
            return context.getString(R.string.password_error);
        }

        return null;
    }

    public static String checkPasswordConfirmation(String password, String passwordConfirmation, Context context) {
        String msg = checkInput(passwordConfirmation, context);
        if (msg != null) {
            return msg;
        }

        if (password.equals(passwordConfirmation)) {
            return null;
        }

        return context.getString(R.string.don_t_match);
    }
}
