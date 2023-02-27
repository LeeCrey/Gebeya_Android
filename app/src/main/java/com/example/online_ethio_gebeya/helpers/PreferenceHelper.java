package com.example.online_ethio_gebeya.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class PreferenceHelper {
    private static final String fullName = "full_name";
    private static final String EMAIL = "email";
    private static final String PWD = "password";
    private static final String TOKEN = "auth_token";
    private static final String rememberMe = "remember_me";
    private static final String PREFS_FILE = "customer_credentials";
    private static final int PREFS_MODE = Context.MODE_PRIVATE;

    public static final float location_default_value = -9_999.0f;

    public static final String latitude = "latitude";
    public static final String longitude = "longitude";

    public static void setRememberMe(Context context, Boolean remember) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(rememberMe, remember);
        editor.apply();
    }

    public static Boolean getRememberMe(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        return preferences.getBoolean(rememberMe, false);
    }

    public static void storeCredentials(Context context, @NonNull String email, @NonNull String password) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(EMAIL, email);
        editor.putString(PWD, password);
        editor.apply();
    }

    // put authorization token
    public static void setAuthToken(Context context, String token) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(TOKEN, token);
        editor.apply();
    }

    public static HashMap<String, String> getCredentials(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        HashMap<String, String> data = new HashMap<>();

        data.put("email", pref.getString(EMAIL, null));
        data.put("password", pref.getString(PWD, null));

        return data;
    }

    // get authorization token
    public static String getAuthToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        return pref.getString(TOKEN, null);
    }

    // clear saved data
    public static void clearPref(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    public static void setFullName(Context context, String fName) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(fullName, fName);
        editor.apply();
    }

    public static String getFullName(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        return pref.getString(fullName, null);
    }

    public static SharedPreferences getSharePref(Context context) {
        return context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
    }

    public static void putLocation(@NonNull Context context, @NonNull Location location) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(latitude, (float) location.getLatitude());
        editor.putFloat(longitude, (float) location.getLongitude());
        editor.apply();
    }

    public static Location getLocation(@NonNull Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);

        Location location = new Location("provider-name");
        location.setLatitude((double) pref.getFloat(latitude, location_default_value));
        location.setLongitude((double) pref.getFloat(longitude, location_default_value));

        return location;
    }
}
