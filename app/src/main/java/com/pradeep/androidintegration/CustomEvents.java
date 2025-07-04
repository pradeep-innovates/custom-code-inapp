package com.pradeep.androidintegration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import androidx.appcompat.widget.Toolbar;


public class CustomEvents extends AppCompatActivity {

    private CleverTapAPI cleverTapDefaultInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_custom_events);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set the toolbar as the ActionBar
        Toolbar toolbar = findViewById(R.id.eventToolbar);
        setSupportActionBar(toolbar);

        // Enable the back button in the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Custom Events");  // Optional, you can customize the title here
        }

        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);

        // Video Activity
        Button videoActivity = findViewById(R.id.videoActivity);
        videoActivity.setOnClickListener(view -> {
            Intent videoIntent = new Intent(CustomEvents.this, VideoPlayerActivity.class);
            startActivity(videoIntent);
        });

        // Product Viewed
        Button productViewed = findViewById(R.id.productViewed);
        productViewed.setOnClickListener(view -> {
            // event with properties
            HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
            prodViewedAction.put("Product Name", "Casio Chronograph Watch");
            prodViewedAction.put("L2 category", "White Snow");
            prodViewedAction.put("Interestrate", " ");
            prodViewedAction.put("Date", new java.util.Date());

            cleverTapDefaultInstance.pushEvent("Product viewed", prodViewedAction);
            Toast.makeText(this, "Product Viewed", Toast.LENGTH_SHORT).show();
        });



        // Remove FCM Token
        Button removeFcmToken = findViewById(R.id.removeFcmToken);
        removeFcmToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String fcmToken = task.getResult();
                                CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
                                if (clevertapDefaultInstance != null) {
                                    clevertapDefaultInstance.pushFcmRegistrationId(fcmToken, false); // Remove token
                                    Log.d("CleverTap", "FCM token removed: " + fcmToken);
                                    Toast.makeText(getApplicationContext(), "Token removed from CleverTap", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("CleverTap", "Failed to get FCM token", task.getException());
                            }
                        });
            }
        });

        // Add FCM Token
        Button addFcmToken = findViewById(R.id.addFcmToken);
        addFcmToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String fcmToken = task.getResult();
                                CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
                                if (clevertapDefaultInstance != null) {
                                    clevertapDefaultInstance.pushFcmRegistrationId(fcmToken, true); // Remove token
                                    Log.d("CleverTap", "FCM token added: " + fcmToken);
                                    Toast.makeText(getApplicationContext(), "Token added to CleverTap", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("CleverTap", "Failed to get FCM token", task.getException());
                            }
                        });
            }
        });

        // Bottom_InApp
        Button bottom_Inapp = findViewById(R.id.bottom_inapp);
        bottom_Inapp.setOnClickListener(view -> {
            cleverTapDefaultInstance.pushEvent("Bottom_InApp");
        });

        // Progress_Alert
        Button progress_alert = findViewById(R.id.progress_alert);
        progress_alert.setOnClickListener(view -> {
            cleverTapDefaultInstance.pushEvent("Progress_Alert");
        });

        // Floating_Image
        Button floating_Image = findViewById(R.id.floating_image);
        floating_Image.setOnClickListener(view -> {
            cleverTapDefaultInstance.pushEvent("Floating_Image");
        });

        // Floating_GIF
        Button floating_Gif = findViewById(R.id.floating_gif);
        floating_Gif.setOnClickListener(view -> {
            cleverTapDefaultInstance.pushEvent("Floating_GIF");
        });

        // Floating_Video
        Button ott_landscape = findViewById(R.id.ott_landscape);
        ott_landscape.setOnClickListener(view -> {
            cleverTapDefaultInstance.pushEvent("Floating_Video");
        });


        // Floating_Video
        Button ecom_portrait = findViewById(R.id.ecom_portrait);
        ecom_portrait.setOnClickListener(view -> {
            cleverTapDefaultInstance.pushEvent("Floating_Video_Portrait");
        });


        // Charged Event
        Button charged = findViewById(R.id.charged);
        charged.setOnClickListener(view -> {
            HashMap<String, Object> chargeDetails = new HashMap<String, Object>();
            chargeDetails.put("Amount", 200);
            chargeDetails.put("Payment Mode", "Credit card");
            chargeDetails.put("Button Clicked", "Charged9");
            chargeDetails.put("Charged ID", 2405209);

            HashMap<String, Object> item1 = new HashMap<String, Object>();
            item1.put("item_category", "chair");
            item1.put("item_name", "my chair");
            item1.put("Price", 100);

            HashMap<String, Object> item2 = new HashMap<String, Object>();
            item2.put("item_category", "bean bag");
            item2.put("item_name", "short bag");
            item2.put("Price", 500);

            HashMap<String, Object> item3 = new HashMap<String, Object>();
            item3.put("item_category", "lamp");
            item3.put("item_name", "yellow lamp");
            item3.put("Price", 500);


            ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
            items.add(item1);
            items.add(item2);
            items.add(item3);

            cleverTapDefaultInstance.pushChargedEvent(chargeDetails, items);
            Toast.makeText(this, "Charged Event", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Handles back button in toolbar
        return true;
    }
}