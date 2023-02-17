package com.example.online_ethio_gebeya.ui;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.callbacks.MainActivityCallBackInterface;
import com.example.online_ethio_gebeya.databinding.ActivityMainBinding;
import com.example.online_ethio_gebeya.helpers.ApplicationHelper;
import com.example.online_ethio_gebeya.helpers.LocaleHelper;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements MainActivityCallBackInterface {
    private final String authTokenKey = "auth_token";
    private NavController navController;
    private String authorizationToken;
    private View headerView;

    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private SharedPreferences.OnSharedPreferenceChangeListener customListener;
    private SharedPreferences preferences;
    private MenuItem editProfile, feedback, signOut, order;

    private AppBarConfiguration appBarConfiguration;
    private String locale;
    private int fontSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authorizationToken = PreferenceHelper.getAuthToken(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences pref = PreferenceHelper.getSharePref(this);

        locale = preferences.getString("language", "en");
        fontSize = preferences.getInt("font_size", 16);
        LocaleHelper.setLocale(this, locale);

        // listeners
        listener = (sharedPreferences, key) -> {
            switch (key) {
                case "theme_mode":
                    onThemeChange(sharedPreferences);
                    break;
                case "language":
                    // set lang first then recreate activity
                    LocaleHelper.setLocale(MainActivity.this, preferences.getString(key, "en"));
                    recreate();
                    break;
                case "font_size":
                    fontSize = sharedPreferences.getInt(key, 16);
                    break;
            }
        };
        customListener = (sharedPreferences, key) -> {
            if (key == null) {
                authorizationToken = null;
            } else {
                if (key.equals(authTokenKey)) {
                    authorizationToken = sharedPreferences.getString(key, null);
                }
            }
            setCurrentUser();
        };
        preferences.registerOnSharedPreferenceChangeListener(listener);
        pref.registerOnSharedPreferenceChangeListener(customListener);

        final int home = R.id.navigation_home;
        final int carts = R.id.navigation_carts;
        final int search = R.id.navigation_search;
        final int product = R.id.navigation_product;
        final int rate = R.id.navigation_rate;

        final DrawerLayout drawerLayout = binding.drawerLayout;
        final Toolbar toolbar = binding.toolBar;
        final BottomNavigationView bottomNavigationView = binding.bottomNavView;
        final NavigationView navigationView = binding.navView;

        //
        setSupportActionBar(toolbar);

        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        appBarConfiguration = new AppBarConfiguration.Builder(home, carts, search).setOpenableLayout(drawerLayout).build();

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
            NavigationUI.setupWithNavController(navigationView, navController);
        }

        headerView = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        editProfile = menu.findItem(R.id.navigation_profile_edit);
        feedback = menu.findItem(R.id.navigation_feedback);
        signOut = menu.findItem(R.id.logout);
        order = menu.findItem(R.id.navigation_orders);

        // event
        navigationView.setNavigationItemSelectedListener(item -> {
            final int id = item.getItemId();
            if (id == R.id.menu_item_rate_app) {
                rateApp();
            } else if (id == R.id.menu_item_share) {
                shareApp();
            } else if (id == R.id.logout) {
                PreferenceHelper.clearPref(MainActivity.this);
                recreate(); // for the sake of opt menu
                navController.navigateUp();
            } else {
                NavigationUI.onNavDestinationSelected(item, navController);
            }
            drawerLayout.close();
            return false;
        });
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()) {
                case home:
                case carts:
                case search:
                    toolbar.setVisibility(View.VISIBLE);
                    bottomNavigationView.setVisibility(authorizationToken == null ? View.GONE : View.VISIBLE);
                    break;
                case rate:
                    toolbar.setVisibility(View.VISIBLE);
                    bottomNavigationView.setVisibility(View.GONE);
                    break;
                case product:
                    toolbar.setVisibility(View.GONE);
                default:
                    bottomNavigationView.setVisibility(View.GONE);
            }
        });

        setCurrentUser();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public String getAuthorizationToken() {
        return authorizationToken;
    }

    @Override
    public void checkPermission() {
        // permissions
        if (!ApplicationHelper.isInternetAccessGranted(this)) {
            ApplicationHelper.requestInternetAccessPermission(this);
        }
        if (!ApplicationHelper.isLocationAccessGranted(this)) {
            ApplicationHelper.requestLocationAccessPermission(this);
        }
    }

    @Override
    public void onProductClick(@NonNull Product product) {
        final Bundle args = new Bundle();
        args.putString("productName", product.getName());
        args.putLong("productId", product.getId());
        navController.navigate(R.id.to_product_detail, args);
    }

    @Override
    public int getFontSizeForDescription() {
        return fontSize;
    }

    @Override
    public void openEmailApp() {
        Intent gmail = new Intent();
        gmail.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivityGmail");
        if (gmail.resolveActivity(getPackageManager()) != null) {
            startActivity(gmail);
            finish();
        }
    }

    @Override
    public void openLocation(float latitude, float longitude) {
        @SuppressLint("DefaultLocale") String ggl = String.format("google.navigation:q=%f,%f", latitude, longitude);
        Uri gmmIntentUri = Uri.parse(ggl);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public String getLocale() {
        return locale;
    }

    private void rateApp() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void shareApp() {
        final String appUri = getString(R.string.app_play_store_url);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, appUri);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    private void onThemeChange(SharedPreferences preferences) {
        String themeValue = preferences.getString("theme_mode", "auto");

        if (themeValue.equals("auto")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (themeValue.equals("on")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    // custom methods
    private void setCurrentUser() {
        String fullName = PreferenceHelper.getFullName(this);
        String msg = "Welcome back!";

        TextView name = headerView.findViewById(R.id.userFullName);
        TextView msgView = headerView.findViewById(R.id.message);

        if (fullName == null) {
            fullName = "Guest";
            msg = "Sign in to continue";
        }

        name.setText(fullName);
        msgView.setText(msg);

        boolean visibility = authorizationToken != null;

        editProfile.setVisible(visibility);
        signOut.setVisible(visibility);
        feedback.setVisible(visibility);
        order.setVisible(visibility);
    }
}
