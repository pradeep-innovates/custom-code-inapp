package com.example.androidintegration;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.clevertap.android.pushtemplates.TemplateRenderer;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.InAppNotificationButtonListener;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements CTInboxListener, DisplayUnitListener, InAppNotificationButtonListener {

    private CleverTapAPI cleverTapDefaultInstance;

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    private ImageView notificationIcon;

    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Log.d("CleverTap", "App is started");
//        Toast.makeText(this, "onCreate Activity Called", Toast.LENGTH_SHORT).show();


        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);
        cleverTapDefaultInstance.enablePersonalization();

        TemplateRenderer.setDebugLevel(3);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("DEEPLINK_URL")) {
            String deepLink = intent.getStringExtra("DEEPLINK_URL");
            Log.d("CleverTap", "Received Deep Link in Main Activity      : " + deepLink);

            Intent deepLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLink));
            deepLinkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(deepLinkIntent);
        }


        if (cleverTapDefaultInstance != null) {
            //Set the Notification Inbox Listener
            cleverTapDefaultInstance.setCTNotificationInboxListener(this);
            cleverTapDefaultInstance.setDisplayUnitListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
            cleverTapDefaultInstance.initializeInbox();
            cleverTapDefaultInstance.setInAppNotificationButtonListener(this);
            cleverTapDefaultInstance.setOptOut(false);
            cleverTapDefaultInstance.enableDeviceNetworkInfoReporting(true);
        }


//        String customerType = (String) cleverTapDefaultInstance.getProperty("PetName");
//        Log.d("CleverTap", "Customer Type is : " + customerType);
//
//        String cust1 = (String) cleverTapDefaultInstance.getProperty("Birthday");
//        Log.d("CleverTap", "Customer Type is : " + cust1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if permission is not granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            }
        }

        // Push Primer
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            JSONObject jsonObject = CTLocalInApp.builder()
//                    .setInAppType(CTLocalInApp.InAppType.ALERT)
//                    .setTitleText("Get Notified")
//                    .setMessageText("Please enable notifications on your device to use Push Notifications.")
//                    .followDeviceOrientation(true)
//                    .setPositiveBtnText("Allow")
//                    .setNegativeBtnText("Cancel")
//                    .setFallbackToSettings(true)
//                    .build();
//
//            cleverTapDefaultInstance.promptPushPrimer(jsonObject);
//            Log.d("CleverTap", "Push Primer");
//        }, 500);




//        RequestOptions requestOptions = new RequestOptions()
//                .transform(new ColorFilterTransformation(Color.WHITE)); // White tint

//        cleverTapDefaultInstance.pushEvent("Native_Home");

        // Inside onCreate method
        ImageView profileIcon = findViewById(R.id.profileIcon);
        notificationIcon = findViewById(R.id.notificationIcon);

        ImageView companyLogo = findViewById(R.id.companyLogo);

        Glide.with(this)
                .load("https://mma.prnewswire.com/media/1064423/CleverTap_Logo.jpg?p=facebook")
                .into(companyLogo);

        // Load profile icon from URL
        Glide.with(this)
                .load("https://cdn-icons-png.flaticon.com/512/9308/9308008.png")
                .into(profileIcon);

        // Load bell icon from URL
        Glide.with(this)
                .load("https://cdn-icons-png.flaticon.com/512/3239/3239952.png")
                .into(notificationIcon);

        profileIcon.setOnClickListener(view -> {
            Toast.makeText(this, "Profile Icon clicked!", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });

        ImageView logoIcon = findViewById(R.id.companyLogo);
        logoIcon.setOnClickListener(view -> {
            cleverTapDefaultInstance.pushEvent("Inbox_ToolTip");
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<CardItem> cardItemList = new ArrayList<>();

        cardItemList.add(new CardItem(
                "HTML In-App",
                "Custom HTML In-App Templates",
                "See Documentation",
                "https://www.youtube.com",
                "https://www.youtube.com",
                DetailListActivity.class  // Pass the activity class to be opened on card click
        ));

        cardItemList.add(new CardItem(
                "Native In-App",
                "Custom Code In-App Templates",
                "See Documentation",
                "https://www.youtube.com",
                "https://www.youtube.com",
                DetailListActivity.class  // Pass the activity class to be opened on card click
        ));

        cardItemList.add(new CardItem(
                "Push Templates",
                "Different Templates to show Push Notification",
                "See Documentation",
                "https://www.wikipedia.org",
                "https://developer.clevertap.com/docs/push-templates-android",
                DetailListActivity.class  // Pass the activity class to be opened on card click
        ));

        cardItemList.add(new CardItem(
                "Custom App Inbox",
                "Styled Inbox rendered by the app, with message content fetched from CleverTap.",
                "See Documentation",
                "https://www.google.com",
                "https://developer.clevertap.com/docs/android-app-inbox",
                InboxActivity.class
        ));

        cardItemList.add(new CardItem(
                "Custom Events",
                "Events you define and track with our SDK or API.",
                "See Documentation",
                "https://www.openai.com",
                "https://developer.clevertap.com/docs/events#custom-events",
                CustomEvents.class  // Pass the activity class to be opened on card click
        ));

        cardItemList.add(new CardItem(
                "Native Display",
                "Native Display helps to display content natively within your app without interrupting the user.",
                "See Documentation",
                "https://github.com",
                "https://docs.clevertap.com/docs/native-display",
                HomeScreen.class  // Pass the activity class to be opened on card click
        ));

        cardItemList.add(new CardItem(
                "In-App",
                "Community of developers asking and answering questions",
                "Visit Stack Overflow",
                "https://stackoverflow.com",
                "https://stackoverflow.com",
                HomeScreen.class  // Pass the activity class to be opened on card click
        ));

        cardItemList.add(new CardItem(
                "Android Developers",
                "Official site for Android development",
                "Visit Android Dev",
                "https://developer.android.com",
                "https://developer.android.com",
                HomeScreen.class  // Pass the activity class to be opened on card click
        ));


        cardAdapter = new CardAdapter(this, cardItemList);
        recyclerView.setAdapter(cardAdapter);

        // Load layout animation from resource
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(
                this, R.anim.layout_animation_fall_down);

        // Set animation controller to RecyclerView
        recyclerView.setLayoutAnimation(controller);

        // Run the animation
        recyclerView.scheduleLayoutAnimation();




//        Button fetchDisplayUnitsButton = findViewById(R.id.fetchDisplayUnitsButton);
//        fetchDisplayUnitsButton.setOnClickListener(view -> {
//            // Trigger fetching display units manually
//            fetchDisplayUnitsAndOpenNewActivity(cleverTapDefaultInstance);
//        });
//
//        // Get CleverTap ID
//        Button getCleverTapID = findViewById(R.id.getCleverTapID);
//        getCleverTapID.setOnClickListener(view -> {
//            String cleverTapID = cleverTapDefaultInstance.getCleverTapID();
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("CleverTap ID");
//            builder.setMessage(cleverTapID);
//            builder.setPositiveButton("Copy", (dialog, which) -> {
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText("CleverTap ID", cleverTapID);
//                clipboard.setPrimaryClip(clip);
//                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
//            });
//            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
//            builder.show();
//        });


        setupCustomBottomBar("home"); // or "search", "profile" accordingly
    }

    private void fetchDisplayUnitsAndOpenNewActivity(CleverTapAPI cleverTapDefaultInstance) {
        Log.d("CleverTap", "fetchDisplayUnitsAndOpenNewActivity: inside");
        ArrayList<CleverTapDisplayUnit> displayUnits = cleverTapDefaultInstance.getAllDisplayUnits();
        ArrayList<String> imageUrls = new ArrayList<>();

        if (displayUnits != null && !displayUnits.isEmpty()) {
            for (CleverTapDisplayUnit unit : displayUnits) {
                for (CleverTapDisplayUnitContent content : unit.getContents()) {
                    String imageUrl = content.getMedia();
                    if (imageUrl != null) {
                        Log.d("CleverTap", "Image URL: " + imageUrl);
                        imageUrls.add(imageUrl);
                    }
                }
            }

            // Open new activity and pass the imageUrls
            Intent intent = new Intent(MainActivity.this, DisplayUnitsActivity.class);
            intent.putStringArrayListExtra("DISPLAY_UNITS", imageUrls);
            startActivity(intent);
        } else {
            Log.d("CleverTap", "No Display Units available.");
        }
    }

//    private void fetchDisplayUnits() {
//        ArrayList<CleverTapDisplayUnit> displayUnits = cleverTapDefaultInstance.getAllDisplayUnits();
//        if (displayUnits != null && !displayUnits.isEmpty()) {
//            // Handle display units (e.g., update carousel)
//            Log.d("CleverTap", "Fetched Display Units: " + displayUnits.toString());
//            updateCarousel(displayUnits);  // Example: update your UI with the new display units
//        } else {
//            Log.d("CleverTap", "No Display Units available.");
//        }
//    }


//    private void updateCarousel(ArrayList<CleverTapDisplayUnit> displayUnits) {
//        List<String> imageUrls = new ArrayList<>();
//        for (CleverTapDisplayUnit unit : displayUnits) {
//            for (CleverTapDisplayUnitContent content : unit.getContents()) {
//                String imageUrl = content.getMedia();
//                if (imageUrl != null && !imageUrl.isEmpty()) {
//                    imageUrls.add(imageUrl);
//                }
//            }
//        }
//        // Set the new adapter to your ViewPager
//        ImageCarouselAdapter adapter = new ImageCarouselAdapter(this, imageUrls);
//        viewPager.setAdapter(adapter);
//    }


    // Foreground/Background
    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        /**
         * On Android 12, Raise notification clicked event when Activity is already running in activity backstack
         */
        Log.d("CleverTap", "onNewIntent: called");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            cleverTapDefaultInstance.pushNotificationClickedEvent(intent.getExtras());
            NotificationUtils.dismissNotification(intent, getApplicationContext());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
//        Toast.makeText(this, "onStart Activity Called", Toast.LENGTH_SHORT).show();

//        cleverTapDefaultInstance.setCTPushNotificationListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "onResume Activity Called", Toast.LENGTH_SHORT).show();
//        LocalBroadcastManager.getInstance(this).registerReceiver(unreadCountReceiver, new IntentFilter("UPDATE_UNREAD_COUNT"));

//        updateUnreadCount();
        setupCustomBottomBar("home"); // or "search", "profile" accordingly

    }



    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(this, "onPause Activity Called", Toast.LENGTH_SHORT).show();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(unreadCountReceiver);

    }

//    private void updateUnreadCount() {
//        if (cleverTapDefaultInstance != null) {
//            Log.d("CleverTap", "updateUnreadCount: called");
//
//            int unreadCount = cleverTapDefaultInstance.getInboxMessageUnreadCount();
//            Button customAppInbox = findViewById(R.id.customAppInbox);
//
//            if (unreadCount > 0) {
//                customAppInbox.setText("Custom App Inbox (" + unreadCount + ")");
//            } else {
//                customAppInbox.setText("Custom App Inbox"); // Hide count if 0
//            }
//        }
//    }

//    private BroadcastReceiver unreadCountReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.d("CleverTap", "onReceive: broadcast received");
//            if ("UPDATE_UNREAD_COUNT".equals(intent.getAction())) {
//                updateUnreadCount();
//            }
//        }
//    };

    @Override
    protected void onStop() {
        super.onStop();
//        Toast.makeText(this, "onStop Activity Called", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Toast.makeText(this, "onRestart Activity Called", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this.getApplicationContext(), "onDestroy Activity Called", Toast.LENGTH_SHORT).show();
        Log.d("CleverTap", "onDestroy: called");

    }
    @Override
    public void inboxDidInitialize() {
        Log.d("CleverTap", "inboxDidInitialize: called");
//        Log.d("CleverTap", "messageCount: " + cleverTapDefaultInstance.getInboxMessageCount());


        int unreadCount = cleverTapDefaultInstance.getInboxMessageUnreadCount();

//        Button yourAppInbox = findViewById(R.id.cleverTapInboxButton);
//        if (unreadCount > 0) {
//            yourAppInbox.setText("CleverTap App Inbox (" + unreadCount + ")");
//            Log.d("CleverTap", "CleverTap App Inbox Count: " + unreadCount);
//        } else {
//            yourAppInbox.setText("CleverTap App Inbox");
//        }

//        Button customAppInbox = findViewById(R.id.customAppInbox);
//        if (unreadCount > 0) {
//            customAppInbox.setText("Custom Inbox (" + unreadCount + ")");
//            Log.d("CleverTap", "Custom App Inbox Count: " + unreadCount);
//        } else {
//            customAppInbox.setText("Custom Inbox");
//        }

//        Button cleverTapInboxButton = findViewById(R.id.cleverTapInboxButton);
        notificationIcon.setOnClickListener(view -> {
            Toast.makeText(this, "Notification Icon clicked!", Toast.LENGTH_SHORT).show();
            cleverTapDefaultInstance.showAppInbox();//Opens Activity with default style configs

        });
    }

    @Override
    public void inboxMessagesDidUpdate() {
        Log.d("CleverTap", "inboxMessagesDidUpdate: called");
        int unreadCount = cleverTapDefaultInstance.getInboxMessageUnreadCount();

//        Button yourAppInbox = findViewById(R.id.cleverTapInboxButton);
//        if (unreadCount > 0) {
//            yourAppInbox.setText("CleverTap App Inbox (" + unreadCount + ")");
//            Log.d("CleverTap", "CleverTap App Inbox Count: " + unreadCount);
//        } else {
//            yourAppInbox.setText("CleverTap App Inbox");
//        }
    }


//    @Override
//    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> displayUnits) {
//        Log.d("CleverTap", "Inside onDisplayUnitsLoaded");
//        cleverTapDefaultInstance.pushEvent("Native Display is working");
//
//        if (displayUnits != null && !displayUnits.isEmpty()) {
//            List<String> imageUrls = new ArrayList<>();
//            List<String> topImageUrls = new ArrayList<>();
//            List<String> unitIds = new ArrayList<>(); // Store unit IDs
//            List<String> actionUrls = new ArrayList<>(); // Store action URLs
//            List<String> titles = new ArrayList<>();
//            List<String> messages = new ArrayList<>();
//
//            for (CleverTapDisplayUnit unit : displayUnits) {
//                Log.d("CleverTap", "Full CleverTap Response: " + unit.toString()); // Log full response
//
//                cleverTapDefaultInstance.pushDisplayUnitViewedEventForID(unit.getUnitID());
//
//                // Safely get the position value
//                String position = null;
//                if (unit.getCustomExtras() != null) { // Null check
//                    position = unit.getCustomExtras().get("position");
//                }
//
//                for (CleverTapDisplayUnitContent content : unit.getContents()) {
//                    String title = content.getTitle();
//                    String message = content.getMessage();
//                    String imageUrl = content.getMedia();
//                    String actionUrl = content.getActionUrl(); // Get the action URL
//
//
//                    if (imageUrl != null && !imageUrl.isEmpty()) {
//                        Log.d("CleverTap", "Image URL: " + imageUrl);
//                        Log.d("CleverTap", "Title : " + title);
//                        Log.d("CleverTap", "Message : " + message);
//
//
//                        if ("top".equalsIgnoreCase(position)) {
//                            topImageUrls.add(imageUrl); // Separate logic for "top"
//                        } else {
//                            imageUrls.add(imageUrl); // Default logic
//                        }
//                        unitIds.add(unit.getUnitID()); // Add unit ID to list
//                        actionUrls.add(actionUrl); // Add the action URL to the list
//
//                        // Add title and message with null checks
//                        titles.add(title);
//                        messages.add(message);
//
//
//                    } else {
//                        Log.e("CleverTap", "Image URL is empty or null");
//                    }
//                }
//            }
//
//            // Handling UI logic based on position
//            if (!topImageUrls.isEmpty()) {
////                setupTopBanner(topImageUrls, unitIds, actionUrls, titles, messages); // Implement this method for 'top' images
//            } else if (!imageUrls.isEmpty()) {
//                setupImageCarousel(imageUrls, unitIds, actionUrls, titles, messages); // Implement this method for default images
//            }
//        }
//    }

    // Method to set up the top banner
//    private void setupTopBanner(List<String> topImageUrls, List<String> unitIds, List<String> actionUrls, List<String> titles, List<String> messages) {
//        ViewPager2 topBannerViewPager = findViewById(R.id.native_image_carousel2);
//        ImageCarouselAdapter adapter = new ImageCarouselAdapter(this, topImageUrls, unitIds, actionUrls, titles, messages);
//        topBannerViewPager.setAdapter(adapter);
//        setupAutoScroll(topBannerViewPager, topImageUrls.size());
//    }

    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> displayUnits) {
        Log.d("CleverTap", "Inside new onDisplayUnitsLoaded");
        cleverTapDefaultInstance.pushEvent("Native Display is working");

        TextView welcomeTitle = findViewById(R.id.welcomeTitle);


        if (displayUnits != null && !displayUnits.isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            List<String> unitIds = new ArrayList<>();
            List<String> actionUrls = new ArrayList<>();
//            List<String> titles = new ArrayList<>();
//            List<String> messages = new ArrayList<>();
            String username = ""; // Default username

            for (CleverTapDisplayUnit unit : displayUnits) {
                Log.d("CleverTap", "Full CleverTap Response: " + unit.toString());

                cleverTapDefaultInstance.pushDisplayUnitViewedEventForID(unit.getUnitID());

                Map<String, String> customKV = unit.getCustomExtras();
                if (customKV != null) {
                    // Get username if present
                    if (customKV.containsKey("Username")) {
                        username = customKV.get("Username");
                        welcomeTitle.setText("Welcome, " + username);
                        Log.d("CleverTap", "Username: " + username);
                    }

                    // Extract multiple images (Image1, Image2, Image3, etc.)
                    for (int i = 1; i <= 5; i++) {
                        String imageKey = "Image" + i;
                        String imageUrl = customKV.get(imageKey);
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            imageUrls.add(imageUrl);
                            unitIds.add(unit.getUnitID());

                            // Optional fallback title/message per image
//                            titles.add(customKV.getOrDefault("Title", "Title"));
//                            messages.add(customKV.getOrDefault("Message", "Message"));
                            actionUrls.add(customKV.getOrDefault("ActionUrl", "")); // Optional
                            Log.d("CleverTap", "Fetched from custom_kv: " + imageUrl);
                        }
                    }
                } else {
                    Log.e("CleverTap", "Custom KV is null");
                }
            }

            ViewPager2 carouselViewPager = findViewById(R.id.carouselViewPager);

            // Display carousel
            if (!imageUrls.isEmpty()) {
//                setupImageCarousel(imageUrls, unitIds, actionUrls, titles, messages);
                carouselViewPager.setVisibility(View.VISIBLE);
                setupImageCarousel(imageUrls, unitIds, actionUrls);
            }
        }
    }


    // Method to set up the default image carousel
    private void setupImageCarousel(List<String> imageUrls, List<String> unitIds, List<String> actionUrls) {
        ViewPager2 viewPager2 = findViewById(R.id.carouselViewPager);
        ImageCarouselAdapter adapter = new ImageCarouselAdapter(this, imageUrls, unitIds, actionUrls);
        viewPager2.setAdapter(adapter);
        setupAutoScroll(viewPager2, imageUrls.size());
    }


    private void setupAutoScroll(ViewPager2 viewPager2, int itemCount) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int currentPage = 0;

            @Override
            public void run() {
                if (currentPage >= itemCount) currentPage = 0;
                viewPager2.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000); // Change slide every 3 seconds
            }
        };
        handler.postDelayed(runnable, 2000);
    }




//    @Override
//    public void onInAppButtonClick(HashMap<String, String> payload) {
//        if(payload != null){
//            Log.d("CleverTap", "In-App Button Clicked! Received payload: " + payload.toString());
//
//            // Iterate and log all key-value pairs
//            for (String key : payload.keySet()) {
//                Log.d("CleverTap", "Key: " + key + ", Value: " + payload.get(key));
//            }
//
//            if (payload.containsKey("url")) {
//                String url = payload.get("url");
//
//                if (url != null && !url.isEmpty()) {
//                    Log.d("CleverTap", "Redirecting to URL: " + url);
//                    openurl(url);
//                } else {
//                    Log.d("CleverTap", "URL is empty or null");
//                }
//            } else {
//                Log.d("CleverTap", "No URL found in payload");
//            }
//        }
//    }

    @Override
    public void onInAppButtonClick(HashMap<String, String> hashMap) {
        Log.d("CleverTap", "onInAppButtonClick: called");
    }
}