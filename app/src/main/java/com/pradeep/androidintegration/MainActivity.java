package com.pradeep.androidintegration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.clevertap.android.pushtemplates.TemplateRenderer;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.InAppNotificationButtonListener;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pradeep.androidintegration.spotlights.SpotlightHelper;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends BaseActivity implements CTInboxListener, DisplayUnitListener, InAppNotificationButtonListener {

    private static final String TAG = "CleverTap";
    private static final int REQUEST_LOCATION_PERMISSION = 1001;
    private CleverTapAPI cleverTapDefaultInstance;

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;

    private ImageView notificationIcon;

    private CleverTapDisplayUnit sonyUnit;
    private Map<String, String> sonyKV;

    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;

    private FirebaseAnalytics mFirebaseAnalytics;

    @SuppressLint("WrongThread")
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

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mFirebaseAnalytics.setUserProperty("ct_objectId", Objects.requireNonNull(CleverTapAPI.getDefaultInstance(this)).getCleverTapID());


        Log.d("CleverTap", "App is started");
//        Toast.makeText(this, "onCreate Activity Called", Toast.LENGTH_SHORT).show();


        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);
//        cleverTapDefaultInstance.enablePersonalization();

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
//            cleverTapDefaultInstance.setOptOut(false);
            cleverTapDefaultInstance.enableDeviceNetworkInfoReporting(true);



            HashMap<String, Object> eventProps = new HashMap<>();
            eventProps.put("date_time", new java.util.Date());
            cleverTapDefaultInstance.pushEvent("custom_event", eventProps);

//            cleverTapDefaultInstance.pushEvent("native_spotlight");
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the location permission is not granted
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request the location permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            }
        }
        Location location = cleverTapDefaultInstance.getLocation();
        cleverTapDefaultInstance.setLocation(location);


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
//            Toast.makeText(this, "Profile Icon clicked!", Toast.LENGTH_SHORT).show();
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
                "app://p1",
                DetailListActivity.class  // Pass the activity class to be opened on card click
        ));

        cardItemList.add(new CardItem(
                "Native In-App",
                "Custom Code In-App Templates",
                "See Documentation",
                "https://www.youtube.com",
                "https://wizrocket.atlassian.net/wiki/spaces/TAMKB/pages/5605457971/Android+Custom+Code+In-App+Templates",
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
                "Custom Events",
                "Events you define and track with our SDK or API.",
                "See Documentation",
                "https://www.openai.com",
                "https://developer.clevertap.com/docs/events#custom-events",
                CustomEvents.class  // Pass the activity class to be opened on card click
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
                "SpotLight",
                "Spotlight highlights parts of the screen to guide users.",
                "See Documentation",
                "https://github.com",
                "https://docs.clevertap.com/docs/native-display",
                SpotlightsActivity.class  // Pass the activity class to be opened on card click
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


        setupCustomBottomBar("home"); // or "search", "profile" accordingly

//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//        }, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (sonyUnit != null && sonyKV != null) {
                showSonyLivFloater(this, sonyUnit, sonyKV);
                sonyUnit = null;
                sonyKV = null;
            }
        }
    }


    // Foreground/Background
    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        /**
         * On Android 12, Raise notification clicked event when Activity is already running in activity backstack
         */
        Log.d("CleverTap", "onNewIntent: called");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d("CleverTap", "onNewIntent 1: called");
            cleverTapDefaultInstance.pushNotificationClickedEvent(intent.getExtras());
//            NotificationUtils.dismissNotification(intent, getApplicationContext());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
//        Toast.makeText(this, "onStart Activity Called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "onResume Activity Called", Toast.LENGTH_SHORT).show();
        setupCustomBottomBar("home"); // or "search", "profile" accordingly
    }



    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(this, "onPause Activity Called", Toast.LENGTH_SHORT).show();
    }

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

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void inboxDidInitialize() {
        Log.d("CleverTap", "inboxDidInitialize: called");
        notificationIcon.setOnClickListener(view -> {
//            Toast.makeText(this, "Notification Icon clicked!", Toast.LENGTH_SHORT).show();
            cleverTapDefaultInstance.showAppInbox();//Opens Activity with default style configs
        });
    }

    @Override
    public void inboxMessagesDidUpdate() {
        Log.d("CleverTap", "inboxMessagesDidUpdate: called");
        int unreadCount = cleverTapDefaultInstance.getInboxMessageUnreadCount();

    }

    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> displayUnits) {
        Log.d("CleverTap", "Inside new onDisplayUnitsLoaded");
//        cleverTapDefaultInstance.pushEvent("Native Display is working");

        Activity activity = CTapp.getCurrentActivity(); //CTapp is Application Class

        if (displayUnits == null || displayUnits.isEmpty()) return;

        for (CleverTapDisplayUnit unit : displayUnits) {
            prepareDisplayView(unit);
        }

        for (CleverTapDisplayUnit unit : displayUnits) {
            Log.d("CleverTap", "Full CleverTap Response: " + unit.toString());

            Map<String, String> customKV = unit.getCustomExtras();
            if (customKV == null || customKV.isEmpty()) continue;

            if (customKV.containsKey("Username")) {
                showHomeScreenBanner(unit, customKV);
            } else if (customKV.containsKey("SonyImageURL")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                                != PackageManager.PERMISSION_GRANTED) {

                    // Save unit for later
                    sonyUnit = unit;
                    sonyKV = customKV;

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            REQUEST_NOTIFICATION_PERMISSION);

                    showSonyLivFloater(this, unit, customKV);
                } else if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                    showSonyLivFloater(this, unit, customKV);
                }
            } else {
                Log.d("CleverTap", "Unrecognized display unit, skipping.");
            }
        }
    }

    private void prepareDisplayView(CleverTapDisplayUnit unit) {
        Log.d(TAG, "prepareDisplayView: called inside SpotlightsActivity");
        if ("nd_spotlight".equals(unit.getCustomExtras().get("nd_id"))) {
            if (cleverTapDefaultInstance != null) {
                cleverTapDefaultInstance.pushDisplayUnitViewedEventForID(unit.getUnitID());
            }

            try {
                JSONObject jsonObject = unit.getJsonObject();
                new SpotlightHelper().showSpotlight(this, jsonObject, () -> {
                    if (cleverTapDefaultInstance != null) {
                        cleverTapDefaultInstance.pushDisplayUnitClickedEventForID(unit.getUnitID());
                    }
                });
            } catch (Exception e) {
                Log.e("SpotlightError", "Error showing spotlight", e);
            }
        } else {
            Log.d("SpotlightsActivity", "Not a spotlight unit");
        }
    }

    private void showSonyLivFloater(Activity activity, CleverTapDisplayUnit unit, Map<String, String> customKV) {
        Log.d("CleverTap", "showSonyLivFloater: called");

        FrameLayout rootLayout = activity.findViewById(android.R.id.content);

        String text = customKV.getOrDefault("Text", "");
        String dateTime = customKV.getOrDefault("DateTime", "");
        String imageURL = customKV.getOrDefault("SonyImageURL", "");
        String bgColor = customKV.getOrDefault("BgColor", "#A2C644"); // default fallback color
        String type = customKV.getOrDefault("Type", "");
//        String type = "notTimer";

        Log.d(TAG, "showSonyLivFloater: type is " + type);

        // Extract date and time parts
        String date = "", time = "";
        if (dateTime.contains(" ")) {
            int lastSpace = dateTime.lastIndexOf(" ");
            date = dateTime.substring(0, lastSpace).trim();
            time = dateTime.substring(lastSpace + 1).trim().toUpperCase();
        }

        LinearLayout container = new LinearLayout(activity);
        container.setOrientation(LinearLayout.VERTICAL);

        FrameLayout.LayoutParams containerParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        containerParams.gravity = Gravity.BOTTOM | Gravity.END;
        containerParams.setMargins(0, 0, 64, 250);
        container.setLayoutParams(containerParams);
        container.setPadding(16, 24, 30, 24);
        container.setElevation(12f);

        GradientDrawable bgDrawable = new GradientDrawable();
        try {
            bgDrawable.setColor(Color.parseColor(bgColor));
        } catch (IllegalArgumentException e) {
            bgDrawable.setColor(Color.LTGRAY);
        }
        bgDrawable.setCornerRadius(75f);
        container.setBackground(bgDrawable);

        LinearLayout topRow = new LinearLayout(activity);
        topRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams topRowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        topRow.setLayoutParams(topRowParams);

        ImageView imageView = new ImageView(activity);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(100, 100);
        imageParams.setMargins(0, 0, 16, 0);
        imageView.setLayoutParams(imageParams);

        Glide.with(activity)
                .load(imageURL)
                .transform(new CircleCrop())
                .into(imageView);

        LinearLayout textColumn = new LinearLayout(activity);
        textColumn.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textColumnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        textColumnParams.setMargins(0, 0, 12, 0);
        textColumn.setLayoutParams(textColumnParams);
        textColumn.setGravity(Gravity.CENTER);

        topRow.addView(imageView);


        TextView countdownView = null;

        if ("Timer".equalsIgnoreCase(type)) {
            TextView matchTextView = new TextView(activity);
            matchTextView.setText(text);
            matchTextView.setTextColor(Color.WHITE);
            matchTextView.setTextSize(10);
            matchTextView.setTypeface(null, Typeface.BOLD);

            textColumn.addView(matchTextView);

            countdownView = new TextView(activity);
            countdownView.setTextColor(Color.WHITE);
            countdownView.setTextSize(16);
            countdownView.setTypeface(null, Typeface.BOLD);
            countdownView.setPadding(0, 0, 0, 0);
            countdownView.setGravity(Gravity.CENTER_HORIZONTAL);

            textColumn.addView(countdownView);
        } else {
            Log.d(TAG, "showSonyLivFloater: inside else part");
            TextView textView = new TextView(activity);
            textView.setText(text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(16);
            textView.setTypeface(null, Typeface.BOLD);

            textColumn.addView(textView);
        }

        // Add topRow and countdown to container
        container.addView(topRow);

        topRow.addView(textColumn);

        // Click handler if ClickURL is provided
        String clickUrl = customKV.get("ClickURL");
        if (clickUrl != null && !clickUrl.isEmpty()) {
            container.setOnClickListener(v -> {
                Log.d("CleverTap", "SonyLiv dot clicked. Opening URL: " + clickUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickUrl));
                activity.startActivity(intent);
                cleverTapDefaultInstance.pushDisplayUnitClickedEventForID(unit.getUnitID());
            });
        }

        rootLayout.addView(container);
        cleverTapDefaultInstance.pushDisplayUnitViewedEventForID(unit.getUnitID());

        TextView finalCountdownView = countdownView;


        if ("Timer".equalsIgnoreCase(type) && countdownView != null) {
            try {
                String normalizedDate = date.replaceAll("(st|nd|rd|th)", "");
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                String fullDateTime = normalizedDate + " " + currentYear + " " + time;

                SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy hh:mma", Locale.ENGLISH);
                sdf.setLenient(false);
                Date targetDate = sdf.parse(fullDateTime);

                if (targetDate != null) {
                    long millisUntilEnd = targetDate.getTime() - System.currentTimeMillis();
                    if (millisUntilEnd > 0) {
                        new CountDownTimer(millisUntilEnd, 1000) {
                            public void onTick(long millisUntilFinished) {
                                long hours = millisUntilFinished / (1000 * 60 * 60);
                                long minutes = (millisUntilFinished / (1000 * 60)) % 60;
                                long seconds = (millisUntilFinished / 1000) % 60;
                                finalCountdownView.setText(String.format(Locale.getDefault(), "%02d\u2009:\u2009%02d\u2009:\u2009%02d", hours, minutes, seconds));
                            }

                            public void onFinish() {
                                finalCountdownView.setText("00:00:00");
                            }
                        }.start();
                    } else {
                        countdownView.setText("00:00:00");
                    }
                }
            } catch (ParseException e) {
                Log.e("Countdown", "Date parsing failed: " + e.getMessage());
            }
        }


        // Scroll listener to hide textColumn and countdown
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean isTextVisible = true;

            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                boolean isAtTop = !rv.canScrollVertically(-1); // true if RecyclerView is at top

                if (isAtTop && !isTextVisible) {
                    // Show views only at top
                    textColumn.setVisibility(View.VISIBLE);
                    textColumn.setAlpha(0f);
                    textColumn.setTranslationX(textColumn.getWidth());
                    textColumn.animate()
                            .translationX(0)
                            .alpha(1f)
                            .setDuration(200)
                            .start();

                    isTextVisible = true;

                    container.setPadding(16, 24, 30, 24);
                    LinearLayout.LayoutParams originalImageParams = new LinearLayout.LayoutParams(100, 100);
                    originalImageParams.setMargins(0, 0, 16, 0);
                    imageView.setLayoutParams(originalImageParams);
                    imageView.requestLayout();
                } else if (!isAtTop && isTextVisible) {
                    // Hide views if scrolled down
                    textColumn.animate()
                            .translationX(textColumn.getWidth())
                            .alpha(0f)
                            .setDuration(200)
                            .withEndAction(() -> textColumn.setVisibility(View.GONE))
                            .start();

                    isTextVisible = false;

                    container.setPadding(12, 12, 12, 12);
                    LinearLayout.LayoutParams newImageParams = new LinearLayout.LayoutParams(110, 110);
                    newImageParams.setMargins(0, 0, 0, 0);
                    imageView.setLayoutParams(newImageParams);
                    imageView.requestLayout();
                }
            }
        });
    }


    private void showHomeScreenBanner(CleverTapDisplayUnit unit, Map<String, String> kv) {
        Log.d(TAG, "showHomeScreenBanner: called");

        String username = kv.get("Username");
        TextView welcomeTitle = findViewById(R.id.welcomeTitle);
        welcomeTitle.setText(username);

        List<String> imageUrls = new ArrayList<>();
        List<String> unitIds = new ArrayList<>();
        List<String> actionUrls = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String key = "Image" + i;
            String imageUrl = kv.get(key);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                imageUrls.add(imageUrl);
                unitIds.add(unit.getUnitID());
                actionUrls.add(""); // Optional: could also support per-image click
            }
        }

        ViewPager2 carouselViewPager = findViewById(R.id.carouselViewPager);


        if (!imageUrls.isEmpty()) {
            carouselViewPager.setVisibility(View.VISIBLE);
            setupImageCarousel(imageUrls, unitIds, actionUrls);
        }

        cleverTapDefaultInstance.pushDisplayUnitViewedEventForID(unit.getUnitID());
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