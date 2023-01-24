package com.example.online_ethio_gebeya.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.navigation.NavDestination;
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
    private MenuItem editProfile, feedback, signOut;

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authorizationToken = PreferenceHelper.getAuthToken(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences pref = PreferenceHelper.getSharePref(this);

        String lkl = preferences.getString("language", "en");
        LocaleHelper.setLocale(this, lkl);

        // listeners
        listener = (sharedPreferences, key) -> {
            if (key.equals("theme_mode")) {
                onThemeChange(sharedPreferences);
            } else if (key.equals("language")) {
                // set lang first then recreate activity
                LocaleHelper.setLocale(MainActivity.this, preferences.getString(key, "en"));
                recreate();
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
            } else {
                NavigationUI.onNavDestinationSelected(item, navController);
            }
            drawerLayout.close();
            return false;
        });
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                switch (navDestination.getId()) {
                    case home:
                        toolbar.setVisibility(View.VISIBLE);
                    case carts:
                    case search:
                        bottomNavigationView.setVisibility(authorizationToken == null ? View.GONE : View.VISIBLE);
                        break;
                    case product:
                        toolbar.setVisibility(View.GONE);
                        bottomNavigationView.setVisibility(View.GONE);
                        break;
                    default:
                        bottomNavigationView.setVisibility(View.GONE);
                }
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
        args.putInt("productId", product.getId());
        navController.navigate(R.id.to_product_detail, args);
    }

    @Override
    public String getMapType() {
        return null;
    }

    @Override
    public int getFontSizeForDescription() {
        return 0;
    }

    @Override
    public void openEmailApp() {
        Intent gmail = new Intent();
        gmail.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivityGmail");
        startActivity(gmail);
        finish();
    }

    private void rateApp() {
        final String pkgName = "com.example.online_ethio_gebeya";
        final String appStoreUrl = "https://play.google.com/store/apps/details?id=" + pkgName;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, appStoreUrl);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, appStoreUrl));
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
    }
}
