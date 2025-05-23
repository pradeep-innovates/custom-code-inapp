package com.example.androidintegration;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.clevertap.android.sdk.CleverTapAPI;

public class HomeScreen extends BaseActivity {

    public CleverTapAPI clevertapDefaultInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());


        if (clevertapDefaultInstance != null) {
            clevertapDefaultInstance.pushEvent("Native Display");
        } else {
            // Optional: Log or handle the case where CleverTap is not initialized
            Log.e("CleverTap", "CleverTap instance is null");
        }

        setupCustomBottomBar("search");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCustomBottomBar("search");
    }

}