package com.example.online_ethio_gebeya.helpers;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LocaleHelper {
    public static Context setLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    public static Context setLocale(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    public static Locale getCurrentLocale(Context context) {
        return context.getResources().getConfiguration().getLocales().get(0);
    }

    public static String getCurrentLocaleName(Context context) {
        Locale locale = context.getResources().getConfiguration().getLocales().get(0);
        return locale.getLanguage();
    }

    public static boolean isAmharic(Context context) {
        Locale amharicLocal = new Locale("am");

        return amharicLocal.equals(getCurrentLocale(context));
    }
}
