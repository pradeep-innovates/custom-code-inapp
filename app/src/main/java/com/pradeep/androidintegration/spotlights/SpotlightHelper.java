package com.pradeep.androidintegration.spotlights;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.pradeep.androidintegration.R;
import com.pradeep.androidintegration.spotlights.effect.RippleEffect;
import com.pradeep.androidintegration.spotlights.shape.Circle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SpotlightHelper {

    public void showSpotlight(AppCompatActivity activity, JSONObject unit, Runnable onComplete) {
        try {
            JSONObject customKv = unit.optJSONObject("custom_kv");

            if (customKv == null) {
                throw new JSONException("Missing 'custom_kv' in unit JSONObject");
            }

            if (customKv.has("nd_json")) {
                String ndJsonString = customKv.optString("nd_json", null);
                if (ndJsonString != null && !ndJsonString.isEmpty()) {
                    JSONObject parsedNdJson = new JSONObject(ndJsonString);
                    Iterator<String> keys = parsedNdJson.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        customKv.put(key, parsedNdJson.get(key));
                    }
                }
            }

            Log.d("SpotlightHelper", "Merged customKv: " + customKv);

            int spotlightCount = customKv.getInt("nd_spotlight_count");
            String textColor = customKv.optString("nd_text_color", "#FFFFFF");

            ArrayList<Target> targets = new ArrayList<>();
            for (int i = 1; i <= spotlightCount; i++) {
                try {
                    String title = customKv.getString("nd_view" + i + "_title");
                    String subtitle = customKv.optString("nd_view" + i + "_subtitle", "");
                    String anchorId = customKv.getString("nd_view" + i + "_id");

                    if (!title.isEmpty() && !anchorId.isEmpty()) {
                        Log.d("CleverTap", "showSpotlight: title and anchorID is not empty");
                        Target target = createTarget(activity, anchorId, title, subtitle, textColor);
                        targets.add(target);
                    }
                } catch (Exception e) {
                    Log.e("SpotlightHelper", "Error parsing spotlight view " + i + ": " + e.getMessage());
                }
            }

            if (targets.isEmpty()) {
                Log.e("SpotlightHelper", "No valid targets found for spotlight.");
                return;
            }

            Spotlight spotlight = new Spotlight.Builder(activity)
                    .setTargets(targets)
                    .setBackgroundColorRes(R.color.spotlightBackground)
                    .setDuration(1000L)
                    .setAnimation(new FastOutSlowInInterpolator())
                    .setOnSpotlightListener(new OnSpotlightListener() {
                        @Override
                        public void onStarted() {
                            Log.d("SpotlightHelper", "Spotlight started");
                        }

                        @Override
                        public void onEnded() {
                            Log.d("SpotlightHelper", "Spotlight ended");
                            onComplete.run();
                        }
                    })
                    .build();

            spotlight.start();

            View.OnClickListener nextTarget = v -> spotlight.next();
            for (Target target : targets) {
                View overlay = target.getOverlay();
                if (overlay != null) {
                    overlay.setOnClickListener(nextTarget);
                }
            }

        } catch (Exception e) {
            Log.e("SpotlightHelper", "Error parsing spotlight JSON: " + e.getMessage());
        }
    }

    private Target createTarget(AppCompatActivity activity, String anchorId, String title, String subtitle, String textColor) throws Exception {
        FrameLayout rootView = new FrameLayout(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View overlay = inflater.inflate(R.layout.layout_target, rootView, false);

        TextView titleText = overlay.findViewById(R.id.title_text);
        TextView subtitleText = overlay.findViewById(R.id.subtitle_text);

        titleText.setText(title);
        titleText.setTextSize(20f);
        titleText.setTypeface(null, Typeface.BOLD);
        titleText.setTextColor(Color.parseColor(textColor));
        titleText.setPadding(16, 8, 16, 4);

        if (subtitle != null && !subtitle.isEmpty()) {
            subtitleText.setText(subtitle);
            subtitleText.setTextSize(16f);
            subtitleText.setTextColor(Color.LTGRAY);
            subtitleText.setPadding(16, 4, 16, 8);
            subtitleText.setVisibility(View.VISIBLE);
        } else {
            subtitleText.setVisibility(View.GONE);
        }

        overlay.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            try {
                View anchorView = activity.findViewById(
                        activity.getResources().getIdentifier(anchorId, "id", activity.getPackageName()));
                int[] anchorLocation = new int[2];
                anchorView.getLocationOnScreen(anchorLocation);
                int anchorCenterX = anchorLocation[0] + anchorView.getWidth() / 2;
                int anchorCenterY = anchorLocation[1] + anchorView.getHeight() / 2;

                int screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
                int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;

                int circleRadius = 200;

                FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                FrameLayout.LayoutParams subtitleParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

                if (anchorCenterY < screenHeight / 2) {
                    titleParams.topMargin = anchorLocation[1] + circleRadius + 20;
                    subtitleParams.topMargin = titleParams.topMargin + titleText.getHeight() + 10;
                } else {
                    subtitleParams.topMargin = anchorLocation[1] - circleRadius - subtitleText.getHeight() - 20;
                    titleParams.topMargin = subtitleParams.topMargin - titleText.getHeight() - 10;
                }

                if (anchorCenterX < screenWidth / 3) {
                    titleParams.leftMargin = anchorLocation[0];
                    subtitleParams.leftMargin = anchorLocation[0];
                } else if (anchorCenterX > screenWidth * 2 / 3) {
                    titleParams.leftMargin = anchorLocation[0] - titleText.getWidth() + anchorView.getWidth();
                    subtitleParams.leftMargin = anchorLocation[0] - subtitleText.getWidth() + anchorView.getWidth();
                } else {
                    titleParams.leftMargin = anchorCenterX - titleText.getWidth() / 2;
                    subtitleParams.leftMargin = anchorCenterX - subtitleText.getWidth() / 2;
                }

                titleText.setLayoutParams(titleParams);
                subtitleText.setLayoutParams(subtitleParams);

            } catch (Exception e) {
                Log.e("SpotlightHelper", "Error adjusting target layout: " + e.getMessage());
            }
        });

        View anchorView = activity.findViewById(
                activity.getResources().getIdentifier(anchorId, "id", activity.getPackageName()));

        return new Target.Builder()
                .setAnchor(anchorView)
                .setShape(new Circle(200f))
                .setOverlay(overlay)
                .setEffect(new RippleEffect(100f, 200f, Color.argb(30, 124, 255, 90)))
                .setOnTargetListener(new OnTargetListener() {
                    @Override
                    public void onStarted() {
                        Log.d("SpotlightHelper", title + " started");
                    }

                    @Override
                    public void onEnded() {
                        Log.d("SpotlightHelper", title + " ended");
                    }
                })
                .build();
    }
}
