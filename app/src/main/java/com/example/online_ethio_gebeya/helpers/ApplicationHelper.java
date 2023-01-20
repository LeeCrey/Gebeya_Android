package com.example.online_ethio_gebeya.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.online_ethio_gebeya.R;
import com.google.android.material.textfield.TextInputEditText;

public class ApplicationHelper {
    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;
    private static final int REQUEST_CODE = 835;

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected = false;
        if (networkInfo != null) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                case ConnectivityManager.TYPE_MOBILE:
                    connected = true;
                    break;
            }
        }
        return connected;
    }

    public static boolean checkConnection(Activity context) {
        boolean available = ApplicationHelper.isInternetAvailable(context);
        if (!available) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.msg_connection_error))
                    .setMessage(context.getString(R.string.msg_not_connected))
                    .setPositiveButton(context.getString(R.string.btn_connect), (dialog, which) -> context.startActivity(new Intent(Settings.ACTION_SETTINGS)))
                    .setNegativeButton(context.getString(R.string.btn_cancel), (dialog, which) -> context.onBackPressed());
            builder.create().show();
        }

        return available;
    }

    /* only unlock, password and confirmation activities */
    public static void initEmailWatcher(@NonNull Context context, @NonNull TextInputEditText email, @NonNull Button send) {
        email.addTextChangedListener(new TextWatcher() {
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
                String emailValue = s.toString();
                // am gonna use directly
                String emailError = InputHelper.checkEmail(emailValue, context);
                email.setError(emailError);
                send.setEnabled(emailError == null);
            }
        });
    }

    // internet
    public static boolean isInternetAccessGranted(Context context) {
        return ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.INTERNET) == GRANTED;
    }

    public static void requestInternetAccessPermission(Activity context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.INTERNET)) {
            Toast.makeText(context, "go to settings and give permission.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.INTERNET}, REQUEST_CODE);
        }
    }

    // location
    public static boolean isLocationAccessGranted(Context context) {
        return ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestLocationAccessPermission(Activity context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Toast.makeText(context.getApplicationContext(), "go to settings and give permission.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }
    }
}
