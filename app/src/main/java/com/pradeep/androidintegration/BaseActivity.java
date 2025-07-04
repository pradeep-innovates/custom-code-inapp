package com.pradeep.androidintegration;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "APP_PREFS";
    private static final String KEY_CURRENT_TAB = "current_tab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base); // Load the base layout
    }

    @Override
    public void setContentView(int layoutResID) {
        FrameLayout container = findViewById(R.id.container);
        if (container != null) {
            LayoutInflater.from(this).inflate(layoutResID, container, true);
        } else {
            // This handles cases where setContentView is called before super.onCreate
            super.setContentView(layoutResID);
        }
    }



    protected void setupCustomBottomBar(String selectedTab) {
        // Save current tab in SharedPreferences
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(KEY_CURRENT_TAB, selectedTab)
                .apply();

        LinearLayout tabHome = findViewById(R.id.tab_home);
        LinearLayout tabSearch = findViewById(R.id.tab_search);
        LinearLayout tabProfile = findViewById(R.id.tab_profile);

        if (tabHome == null || tabSearch == null || tabProfile == null) {
            Log.e("CleverTap", "One or more tab views not found.");
            return;
        }

        tabHome.setOnClickListener(v -> {
            if (!"home".equals(selectedTab)) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        tabSearch.setOnClickListener(v -> {
            if (!"search".equals(selectedTab)) {
                Intent intent = new Intent(this, HomeScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        tabProfile.setOnClickListener(v -> {
            if (!"empty".equals(selectedTab)) {
                Intent intent = new Intent(this, EmptyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        highlightSelectedTab(selectedTab);
    }

    private void highlightSelectedTab(String selectedTab) {

        // Reset all to default color
        resetTabColors();
        resetIconSizes();

        // Define the color values
        int inactiveColor = Color.parseColor("#FFFFFF");
        int activeColor = Color.parseColor("#F8D1B0"); // Light grey color for active tab
        int activeSize = 32;   // dp
        int inactiveSize = 24; // dp

        // Change the background color of the selected tab to light grey (active)
        switch (selectedTab) {
            case "home":
                setTabColor(R.id.tab_home, activeColor);
                setTabIconSize(R.id.icon_home, activeSize);
                break;
            case "search":
                setTabColor(R.id.tab_search, activeColor);
                setTabIconSize(R.id.icon_search, activeSize);
                break;
            case "empty":
                setTabColor(R.id.tab_profile, activeColor);
                setTabIconSize(R.id.icon_profile, activeSize);
                break;
        }

        // Set the background color of the other tabs to white (inactive)
        if (!selectedTab.equals("home")) {
            setTabIconSize(R.id.icon_home, inactiveSize);
            setTabColor(R.id.tab_home, inactiveColor);
        }
        if (!selectedTab.equals("search")) {
            setTabIconSize(R.id.icon_search, inactiveSize);
            setTabColor(R.id.tab_search, inactiveColor);
        }
        if (!selectedTab.equals("empty")) {
            setTabIconSize(R.id.icon_profile, inactiveSize);
            setTabColor(R.id.tab_profile, inactiveColor);
        }
    }

    private void setTabIconSize(int iconId, int sizeInDp) {
        ImageView icon = findViewById(iconId);
        if (icon != null) {
            int sizeInPx = (int) (sizeInDp * getResources().getDisplayMetrics().density);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(sizeInPx, sizeInPx);
            icon.setLayoutParams(params);
        }
    }

    private void resetIconSizes() {
        setTabIconSize(R.id.icon_home, 24);
        setTabIconSize(R.id.icon_search, 24);
        setTabIconSize(R.id.icon_profile, 24);
    }


    // Method to reset all tabs to white background
    private void resetTabColors() {
        setTabColor(R.id.tab_home, Color.WHITE);
        setTabColor(R.id.tab_search, Color.WHITE);
        setTabColor(R.id.tab_profile, Color.WHITE);
    }

    // Method to set the color of the tab
    private void setTabColor(int tabId, int color) {
        LinearLayout tab = findViewById(tabId);
        if (tab != null) {
            // Set the background color of the tab
            tab.setBackgroundColor(color);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore the background color of the selected tab when the activity is in the foreground
        highlightSelectedTab(getCurrentTab());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Reset tab colors to the default state (white for inactive tabs)
        resetTabColors();
    }

    // Optionally: call this method to get the currently selected tab across activities
    protected String getCurrentTab() {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString(KEY_CURRENT_TAB, "home");  // Default to "home" if no tab is saved
    }

}
