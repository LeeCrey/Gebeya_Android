package com.example.online_ethio_gebeya.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import com.example.online_ethio_gebeya.ui.fragments.account.AccountDeleteFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements MainActivityCallBackInterface {
    private final String authTokenKey = "auth_token";
    private final String languageKey = "language";
    private NavController navController;
    private String authorizationToken;
    private MenuItem editProfile, feedback, signOut, order, deleteAccount, wallet;
    private AppBarConfiguration appBarConfiguration;
    private String locale;
    private int fontSize;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private final int REQUEST_CODE = 835;
    private Location lokation;

    //    if pref change listener because locale var, the won't listen to any change
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private SharedPreferences.OnSharedPreferenceChangeListener customListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authorizationToken = PreferenceHelper.getAuthToken(this);
        lokation = PreferenceHelper.getLocation(this);
        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences pref = PreferenceHelper.getSharePref(this);

        locale = sharedPreferences1.getString(languageKey, "en");
        fontSize = sharedPreferences1.getInt("font_size", 16);

        if (!ApplicationHelper.isLocationGranted(this)) {
            requestPermissions();
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        final DrawerLayout drawerLayout = binding.drawerLayout;
        final Toolbar toolbar = binding.toolBar;
        final BottomNavigationView bottomNavigationView = binding.bottomNavView;
        final NavigationView navigationView = binding.navView;

        // listeners
        listener = (preferences, key) -> {
            switch (key) {
                case "theme_mode":
                    onThemeChange(preferences);
                    break;
                case "language":
                    recreate();
                    break;
                case "font_size":
                    fontSize = preferences.getInt(key, 16);
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
            if (authorizationToken == null) {
                bottomNavigationView.setVisibility(View.GONE);
            }
            setCurrentUser();
        };
        sharedPreferences1.registerOnSharedPreferenceChangeListener(listener);
        pref.registerOnSharedPreferenceChangeListener(customListener);

        final int home = R.id.navigation_home;
        final int carts = R.id.navigation_carts;
        final int search = R.id.navigation_search;
        final int product = R.id.navigation_product;
        final int rate = R.id.navigation_rate;

        setSupportActionBar(toolbar);

        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        appBarConfiguration = new AppBarConfiguration.Builder(home, carts, search).setOpenableLayout(drawerLayout).build();

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
            NavigationUI.setupWithNavController(navigationView, navController);
        }

        Menu menu = navigationView.getMenu();
        editProfile = menu.findItem(R.id.navigation_profile_edit);
        feedback = menu.findItem(R.id.navigation_feedback);
        signOut = menu.findItem(R.id.logout);
        order = menu.findItem(R.id.navigation_orders);
        deleteAccount = menu.findItem(R.id.delete_account);
        wallet = menu.findItem(R.id.navigation_wallet);

        // event
        navigationView.setNavigationItemSelectedListener(item -> {
            final int id = item.getItemId();
            if (id == R.id.menu_item_rate_app) {
                rateApp();
            } else if (id == R.id.menu_item_share) {
                shareApp();
            } else if (id == signOut.getItemId()) {
                PreferenceHelper.clearPref(MainActivity.this);
                recreate(); // for the sake of opt menu
                navController.navigateUp();
            } else if (id == deleteAccount.getItemId()) {
                openConfirmModal();
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
    protected void attachBaseContext(Context newBase) {
        SharedPreferences shf = PreferenceManager.getDefaultSharedPreferences(newBase);
        String lkl = "en";
        if (shf != null) {
            lkl = shf.getString(languageKey, "en");
        }
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lkl));
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

    @SuppressLint("QueryPermissionsNeeded")
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
    public void openGoogleMap(double latitude, double longitude) {
        @SuppressLint("DefaultLocale") String ggl = String.format("google.navigation:q=%f,%f", latitude, longitude);
        Uri gmmIntentUri = Uri.parse(ggl);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(mapIntent);
        } catch (Exception exception) {
            Toast.makeText(this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public Location getLocation() {
        return lokation;
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
    private void rateApp() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void shareApp() {
        final String appUri = getString(R.string.app_play_store_url) + getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, appUri);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    private void setCurrentUser() {
        boolean visibility = authorizationToken != null;

        editProfile.setVisible(visibility);
        signOut.setVisible(visibility);
        feedback.setVisible(visibility);
        order.setVisible(visibility);
        deleteAccount.setVisible(visibility);
        wallet.setVisible(visibility);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation();
            } else {
                Toast.makeText(MainActivity.this, "Allow access to location. ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchLastLocation() {
        if (!ApplicationHelper.isLocationGranted(this)) {
            requestPermissions();
        } else {
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(this, location -> {
                if (location != null) {
                    if (lokation.getLongitude() == PreferenceHelper.location_default_value) {
                        Toast.makeText(this, "Please close and re-open the app.", Toast.LENGTH_SHORT).show();
                    }
                    PreferenceHelper.putLocation(MainActivity.this, location);
                } else {
                    if ((lokation.getLatitude() == PreferenceHelper.location_default_value) || (lokation.getLongitude() == PreferenceHelper.location_default_value)) {
                        locationEnabled();
                    }
                }
            });
        }
    }

    private void openConfirmModal() {
        new AccountDeleteFragment().show(getSupportFragmentManager(), "ModalBottomSheet");
    }

    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("GPS Enable")
                    .setPositiveButton("Settings", (paramDialogInterface, paramInt) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        Toast.makeText(this, "No product will be shown", Toast.LENGTH_SHORT).show();
                    }).show();
        }
    }
}
