package com.pradeep.androidintegration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;

import java.util.ArrayList;
import java.util.Map;

public class HomeScreen extends BaseActivity implements DisplayUnitListener {

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
            clevertapDefaultInstance.setDisplayUnitListener(this);

//            clevertapDefaultInstance.pushEvent("Native Display");
        } else {
            // Optional: Log or handle the case where CleverTap is not initialized
            Log.e("CleverTap", "CleverTap instance is null");
        }

        setupCustomBottomBar("search");

//        clevertapDefaultInstance.pushEvent("native_bot");

        Button ImageActivity = findViewById(R.id.button4);

        ImageActivity.setOnClickListener(view -> {
            Toast.makeText(this, "Image Button clicked!", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCustomBottomBar("search");
    }

    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> displayUnits) {
        Log.d("CleverTap", "Inside homescreen onDisplayUnitsLoaded");
        clevertapDefaultInstance.pushEvent("Native Display is working");

        Activity activity = CTapp.getCurrentActivity(); //CTapp is Application Class

        if (displayUnits == null || displayUnits.isEmpty()) return;

        for (CleverTapDisplayUnit unit : displayUnits) {
            Log.d("CleverTap", "CleverTap Response: " + unit.toString());

            Map<String, String> customKV = unit.getCustomExtras();
            if (customKV == null || customKV.isEmpty()) continue;

            if (customKV.containsKey("Username")) {
                Log.d("CleverTap", "Not Required");
            } else if (customKV.containsKey("SonyImageURL")) {
                showSonyLivFloater1(activity, unit, customKV);
            } else {
                Log.d("CleverTap", "Unrecognized display unit, skipping.");
            }
        }
    }

    private void showSonyLivFloater1(Activity activity, CleverTapDisplayUnit unit, Map<String, String> customKV) {
        Log.d("CleverTap", "showSonyLivFloater in HomeScreen: called");


        FrameLayout rootLayout = activity.findViewById(android.R.id.content);

        String date = customKV.getOrDefault("Date", "");
        String time = customKV.getOrDefault("Time", "");
        String imageURL = customKV.getOrDefault("SonyImageURL", "");
        String bgColor = "#A7C7E7";

        LinearLayout container = new LinearLayout(activity);

        FrameLayout.LayoutParams containerParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        containerParams.gravity = Gravity.BOTTOM | Gravity.END;
        containerParams.setMargins(0, 0, 64, 250);
        container.setLayoutParams(containerParams);
        container.setOrientation(LinearLayout.HORIZONTAL);
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
        textColumn.setGravity(Gravity.CENTER_VERTICAL);

        TextView dateView = new TextView(activity);
        dateView.setText(date);
        dateView.setTextColor(Color.BLACK);
        dateView.setTextSize(13);
        dateView.setTypeface(null, Typeface.BOLD);

        TextView timeView = new TextView(activity);
        timeView.setText(time);
        timeView.setTextColor(Color.BLACK);
        timeView.setTextSize(13);
        timeView.setTypeface(null, Typeface.BOLD);

        textColumn.addView(dateView);
        textColumn.addView(timeView);

        container.addView(imageView);
        container.addView(textColumn);

        // Click handler if ClickURL is provided
        String clickUrl = customKV.get("ClickURL");
        if (clickUrl != null && !clickUrl.isEmpty()) {
            container.setOnClickListener(v -> {
                Log.d("CleverTap", "SonyLiv dot clicked. Opening URL: " + clickUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickUrl));
                activity.startActivity(intent);
                clevertapDefaultInstance.pushDisplayUnitClickedEventForID(unit.getUnitID());
            });
        }

        rootLayout.addView(container);

        clevertapDefaultInstance.pushDisplayUnitViewedEventForID(unit.getUnitID());

        // Optional: Add scroll listener to hide text
//        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            private boolean isTextVisible = true;
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
//                if (dy > 10 && isTextVisible) {
//                    textColumn.animate()
//                            .translationX(textColumn.getWidth())
//                            .alpha(0f)
//                            .setDuration(200)
//                            .withEndAction(() -> textColumn.setVisibility(View.GONE))
//                            .start();
//                    isTextVisible = false;
//
//                    container.setPadding(12, 12, 12, 12);
//                    LinearLayout.LayoutParams newImageParams = new LinearLayout.LayoutParams(110, 110);
//                    newImageParams.setMargins(0, 0, 0, 0);
//                    imageView.setLayoutParams(newImageParams);
//                    imageView.requestLayout();
//                } else if (dy < -10 && !isTextVisible) {
//                    textColumn.setVisibility(View.VISIBLE);
//                    textColumn.setAlpha(0f);
//                    textColumn.setTranslationX(textColumn.getWidth());
//                    textColumn.animate()
//                            .translationX(0)
//                            .alpha(1f)
//                            .setDuration(200)
//                            .start();
//                    isTextVisible = true;
//
//                    container.setPadding(16, 24, 30, 24);
//                    LinearLayout.LayoutParams originalImageParams = new LinearLayout.LayoutParams(100, 100);
//                    originalImageParams.setMargins(0, 0, 16, 0);
//                    imageView.setLayoutParams(originalImageParams);
//                    imageView.requestLayout();
//                }
//            }
//        });
    }

}