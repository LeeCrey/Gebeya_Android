package com.example.online_ethio_gebeya.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import java.util.Locale;

public class LocaleHelper {
    public static Locale getCurrentLocale(Context context) {
        return context.getResources().getConfiguration().getLocales().get(0);
    }

    public static boolean isAmharic(Context context) {
        Locale amharicLocal = new Locale("am");

        return amharicLocal.equals(getCurrentLocale(context));
    }

    public static Context setLocale(@NonNull Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());

        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }
}
