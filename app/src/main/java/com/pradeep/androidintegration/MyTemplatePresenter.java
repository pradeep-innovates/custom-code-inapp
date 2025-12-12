package com.pradeep.androidintegration;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplateContext;
import com.clevertap.android.sdk.inapp.customtemplates.TemplatePresenter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executors;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;

public class MyTemplatePresenter implements TemplatePresenter  {

    private static final String TAG = "CleverTap";

    @Override
    public void onPresent(@NonNull CustomTemplateContext.TemplateContext context) {
        Log.d("CleverTap", "TemplateContext : " + context);

        // Always fetch the current activity here to ensure it's not null
        Activity activity = CTapp.getCurrentActivity(); //CTapp is Application Class
        Log.d("CleverTap", "onPresent: " + activity);

        if (activity instanceof LifecycleOwner) {
            LifecycleOwner lifecycleOwner = (LifecycleOwner) activity;
            lifecycleOwner.getLifecycle().addObserver(new ContextDismissObserver(context));
        }


        if (activity != null) {
            Log.d("CleverTap", "Showing on Activity : " + activity);
            String templateName = context.getTemplateName();
            Log.d("CleverTap", "Template Name: " + templateName);

            if ("Bottom_InApp".equalsIgnoreCase(templateName)) {
                showBottomInApp(activity, context); // Existing bottom sheet
            } else if ("Progress_Alert".equalsIgnoreCase(templateName)){
                showProgressAlert(activity, context);
            } else if ("Floating_Banner".equalsIgnoreCase(templateName)) {
                showFloatingBanner(activity, context); // New banner
            } else if ("OTT_PIP".equalsIgnoreCase(templateName)) {
                showOTTPIP(activity, context); // OTT PIP
            } else if ("ECommerce_PIP".equalsIgnoreCase(templateName)) {
                showECommercePIP(activity, context); // ECom PIP
            } else if ("Bottom_Banner".equalsIgnoreCase(templateName)) {
                showBottomBanner(activity, context); // ECom PIP
            } else if ("ToolTip".equalsIgnoreCase(templateName)) {
                showTopRightTooltip(activity, context); // ECom PIP
            } else if ("ToolTip_2".equalsIgnoreCase(templateName)) {
                showFirstTooltip(activity, context); // ECom PIP
            } else if ("Scratch_Card".equalsIgnoreCase(templateName)) {
                showCustomBottomRightPopup(activity, context);
            } else if ("Event_Launch".equalsIgnoreCase(templateName)) {
                showEventBanner(activity, context);
            } else if ("SonyLIV_dot".equalsIgnoreCase(templateName)) {
                Log.d("CleverTap", "SonyLIV_dot template is restricted by its master");
                Toast.makeText(activity, "SonyLIV_dot template is restricted", Toast.LENGTH_SHORT).show();
                context.setDismissed();
//                showSonyLIVdot(activity, context);
            } else if ("Mood_Template".equalsIgnoreCase(templateName)) {
                showMoodTemplate(activity, context);
            } else {
                Log.d("CleverTap", templateName + " is not present");
            }

        } else {
            Log.e("CleverTap", "No active activity found. Cannot show UI.");
        }
    }

    private void showMoodTemplate(Activity activity, CustomTemplateContext.TemplateContext context) {
        Log.d(TAG, "showMoodTemplate: called");

        int delayInSeconds = 3;
        int delayMillis = delayInSeconds * 1000;

        Toast.makeText(activity, "Template Showing in 3 seconds...", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Extract header and mood details
            String header = context.getString("Header");

            String[] moodTexts = {
                    context.getString("mood1_Text"),
                    context.getString("mood2_Text"),
                    context.getString("mood3_Text")
            };

            String[] moodImages = {
                    context.getString("mood1_Image"),
                    context.getString("mood2_Image"),
                    context.getString("mood3_Image")
            };

            String[] moodImages1 = {
                    "https://feeds.abplive.com/onecms/images/uploaded-images/2022/10/12/20ac1a2fd83423a27087e115c5e40d591665587344294404_original.jpg",
                    "https://assets.thehansindia.com/h-upload/2022/02/27/1279047-aadavallu-meeku-joharlu.webp",
                    "https://i.cdn.newsbytesapp.com/images/l82620220307163454.jpeg"
            };

            int[][] gradients = new int[][]{
                    {Color.parseColor("#FF5F6D"), Color.BLACK}, // Red to Black
                    {Color.parseColor("#36D1DC"), Color.BLACK}, // Teal to Black
                    {Color.parseColor("#C33764"), Color.BLACK}  // Purple to Black
            };



            List<String[]> moodDetails = new ArrayList<>();
            for (String text : moodTexts) {
                String[] parts = text.split(",", 3);
                moodDetails.add(new String[]{
                        parts.length > 0 ? parts[0].trim() : "",
                        parts.length > 1 ? parts[1].trim() : "",
                        parts.length > 2 ? parts[2].trim() : ""
                });
            }

            BottomSheetDialog dialog = new BottomSheetDialog(activity);
            LinearLayout root = new LinearLayout(activity);
            root.setOrientation(LinearLayout.VERTICAL);
            root.setPadding(48, 48, 48, 48);
            root.setBackgroundColor(Color.parseColor("#141425"));

            GradientDrawable background = new GradientDrawable();
            background.setColor(Color.parseColor("#141425"));
            background.setCornerRadii(new float[]{40, 40, 40, 40, 0, 0, 0, 0});
            root.setBackground(background);

            // Title
            TextView title = new TextView(activity);
            title.setText(header);
            title.setTextColor(Color.WHITE);
            title.setTextSize(26);
            title.setTypeface(null, Typeface.BOLD);
            title.setGravity(Gravity.CENTER);
            title.setPadding(0, 100, 0, 100);
            root.addView(title);

            // Mood cards
            for (int i = 0; i < 3; i++) {
                String[] mood = moodDetails.get(i);
                View.OnClickListener clickListener = null;
                switch (i) {
                    case 0:
                        clickListener = v -> {
                            context.triggerActionArgument("mood1_Link", activity);
                            context.setDismissed();
                            dialog.dismiss();
                        };
                        break;
                    case 1:
                        clickListener = v -> {
                            context.triggerActionArgument("mood2_Link", activity);
                            context.setDismissed();
                            dialog.dismiss();
                        };
                        break;
                    case 2:
                        clickListener = v -> {
                            context.triggerActionArgument("mood3_Link", activity);
                            context.setDismissed();
                            dialog.dismiss();
                        };
                        break;
                }
                View moodCard = createMoodCard(activity, moodImages[i], mood[0], mood[1], mood[2],
                        gradients[i][0], gradients[i][1], clickListener);
                root.addView(moodCard);
            }


            // Create the "Explore" button
            Button exploreBtn = new Button(activity);
            exploreBtn.setText("Explore");
            exploreBtn.setTextSize(18);
            exploreBtn.setAllCaps(false);
            exploreBtn.setTextColor(Color.WHITE);

            // Create a GradientDrawable for rounded corners
            GradientDrawable bgDrawable = new GradientDrawable();
            bgDrawable.setColor(Color.parseColor("#FF4081")); // Background pink
            bgDrawable.setCornerRadius(24); // Radius in pixels (use TypedValue for dp if needed)
            bgDrawable.setStroke(1, Color.WHITE); // White border


            // Set the background
            exploreBtn.setBackground(bgDrawable);

            // Set layout params
            LinearLayout.LayoutParams exploreParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            exploreParams.setMargins(0, 32, 0, 0);

            // Add the button to root layout
            root.addView(exploreBtn, exploreParams);


            // Close button (top right overlay)
            FrameLayout wrapper = new FrameLayout(activity);
            wrapper.addView(root);

            ImageButton closeBtn = new ImageButton(activity);
            GradientDrawable closeBg = new GradientDrawable();
            closeBg.setShape(GradientDrawable.OVAL);
            closeBg.setColor(Color.BLACK);
            closeBtn.setBackground(closeBg);

            int closeSize = (int) (30 * activity.getResources().getDisplayMetrics().density);
            FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(closeSize, closeSize);
            closeParams.gravity = Gravity.END | Gravity.TOP;
            closeParams.setMargins(0, 40, 40, 0);
            closeBtn.setLayoutParams(closeParams);
            closeBtn.setPadding(21, 21, 21, 21);
            closeBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            RequestOptions requestOptions1 = new RequestOptions()
                    .transform(new ColorFilterTransformation(Color.WHITE));

            Glide.with(activity)
                    .load("https://cdn-icons-png.flaticon.com/512/1828/1828778.png")
                    .apply(new RequestOptions().override(40, 40))
                    .apply(requestOptions1)
                    .into(closeBtn);

            closeBtn.setOnClickListener(v -> {
                context.setDismissed();
                dialog.dismiss();
            });

            wrapper.addView(closeBtn);

            dialog.setContentView(wrapper);
            dialog.setOnDismissListener(d -> context.setDismissed());
            dialog.show();
            context.setPresented();
        }, delayMillis);
    }

    private View createMoodCard(Context context, String imageUrl, String title, String subtitle, String caption, int startColor, int endColor, View.OnClickListener onClickListener) {
        // Outer Card Container
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setPadding(0, 0, 0, 0);
        card.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                300);  // Increased height
        cardParams.setMargins(0, 0, 0, 40); // Spacing between cards
        card.setLayoutParams(cardParams);

        card.setBackground(getGradientDrawable(startColor, endColor));
        card.setElevation(10);
        card.setClipToOutline(true);

        // Left Image Section
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        imageView.setLayoutParams(imgParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(imageUrl).into(imageView);

        // Right Text Section
        LinearLayout textLayout = new LinearLayout(context);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        textLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        textLayout.setPadding(24, 24, 24, 24);

        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextColor(Color.WHITE);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextSize(22);

        TextView subtitleView = new TextView(context);
        subtitleView.setText(subtitle);
        subtitleView.setTextColor(Color.WHITE);
        subtitleView.setTextSize(14);

        TextView captionView = new TextView(context);
        captionView.setText(caption);
        captionView.setTextColor(Color.parseColor("#CCFFFFFF"));
        captionView.setTextSize(12);
        captionView.setTypeface(null, Typeface.ITALIC);


        textLayout.addView(titleView);
        textLayout.addView(subtitleView);
        textLayout.addView(captionView);

        card.addView(imageView);
        card.addView(textLayout);

        card.setOnClickListener(onClickListener);

        return card;
    }

    private GradientDrawable getGradientDrawable(int startColor, int endColor) {
        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{startColor, endColor});
        gradient.setCornerRadius(32f);
        return gradient;
    }


    private int dpToPx(int dp, Context context) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }


    private static final Set<String> ALLOWED_ACTIVITIES = new HashSet<>(Arrays.asList(
            "com.example.androidintegration.MainActivity"
            // add your allowed activity full names here
    ));


    private static final String CT_TAG = "SonyLIVDotTag";
    private CustomTemplateContext.TemplateContext latestDotContext;

    private void showSonyLIVdot(Activity activity, CustomTemplateContext.TemplateContext context) {
        Log.d("CleverTap", "showSonyLIVdot: called");

        String currentActivityName = activity.getClass().getName();
        Log.d("CleverTap", "currentActivity : " + currentActivityName);

        if (!ALLOWED_ACTIVITIES.contains(currentActivityName)) {
            Log.d("CleverTap", "Activity not allowed for SonyLIV dot: " + currentActivityName);
                context.setDismissed();
                Log.d("CleverTap", "Dismissed in disallowed activity");
                CleverTapAPI instance = CleverTapAPI.getDefaultInstance(activity.getApplicationContext());
                if (instance != null) {
                    instance.pushEvent("bot");
                    Log.d("CleverTap", "bot event fired in disallowed activity");
                }

            return; // Skip UI rendering
        }

        FrameLayout rootLayout = activity.findViewById(android.R.id.content);
        View existingView = rootLayout.findViewWithTag(CT_TAG);

        if (existingView != null) {
            Log.d("CleverTap", "UI already shown, skipping rendering.");

            // Update the latest context
            latestDotContext = context;

            // Attach updated click listener
            existingView.setOnClickListener(v -> {
                Log.d("CleverTap", "dot clicked (duplicate context)");
                if (latestDotContext != null) {
                    latestDotContext.triggerActionArgument("dotClick", activity);
                    latestDotContext.setDismissed();
                }
            });

            // Dismiss current context after 500ms
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                context.setDismissed();
                Log.d("CleverTap", "Dismissed after duplicate context");

                // Trigger re-qualification after another 500ms
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    CleverTapAPI instance = CleverTapAPI.getDefaultInstance(activity.getApplicationContext());
                    if (instance != null) {
                        instance.pushEvent("bot");
                        Log.d("CleverTap", "bot event fired after dismiss");
                    }
                }, 200);
            }, 2000);

            return;
        }

        // === First-time UI rendering ===

        String date = context.getString("Date");
        String time = context.getString("Time");
        String imageURL = context.getString("ImageURL");
        String bgColor = context.getString("BgColor");

        LinearLayout container = new LinearLayout(activity);
        container.setTag(CT_TAG);

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
        dateView.setTextColor(Color.WHITE);
        dateView.setTextSize(13);
        dateView.setTypeface(null, Typeface.BOLD);

        TextView timeView = new TextView(activity);
        timeView.setText(time);
        timeView.setTextColor(Color.WHITE);
        timeView.setTextSize(13);
        timeView.setTypeface(null, Typeface.BOLD);

        textColumn.addView(dateView);
        textColumn.addView(timeView);

        container.addView(imageView);
        container.addView(textColumn);

        // Save latest context and set click listener
        latestDotContext = context;
        container.setOnClickListener(v -> {
            Log.d("CleverTap", "dot clicked");
            if (latestDotContext != null) {
                latestDotContext.triggerActionArgument("dotClick", activity);
                latestDotContext.setDismissed();
            }
        });

        rootLayout.addView(container);
        context.setPresented();

        // Dismiss after 500ms and re-fire bot event after another 500ms
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            context.setDismissed();
            Log.d("CleverTap", "First render: context dismissed");

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                CleverTapAPI instance = CleverTapAPI.getDefaultInstance(activity.getApplicationContext());
                if (instance != null) {
                    instance.pushEvent("bot");
                    Log.d("CleverTap", "First render: bot event fired");
                }
            }, 200);
        }, 2000);

        // Scroll listener (optional)
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean isTextVisible = true;

            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                if (dy > 10 && isTextVisible) {
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
                } else if (dy < -10 && !isTextVisible) {
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
                }
            }
        });
    }


    private void showEventBanner(Activity activity, CustomTemplateContext.TemplateContext context) {
        Log.d("CleverTap", "showEventBanner: called");
//        String ImageURL = context.getString("ImageURL");
        String ImageURL = "https://pradeep-innovates.github.io/image-host/evntimage.png";
        Log.d("CleverTap", "ImageURL : " + ImageURL);
        FrameLayout rootLayout = activity.findViewById(android.R.id.content);
        float density = activity.getResources().getDisplayMetrics().density;

        // OUTER container (floating box in bottom-right)
        FrameLayout outerLayout = new FrameLayout(activity);
        FrameLayout.LayoutParams outerParams = new FrameLayout.LayoutParams(
                (int)(200 * density),
                (int)(200 * density)
        );
        outerParams.gravity = Gravity.BOTTOM | Gravity.END;
        outerParams.setMargins(0, 0, (int)(16 * density), (int)(100 * density)); // Adjust right & bottom margin
        outerLayout.setLayoutParams(outerParams);
//        outerLayout.setBackgroundColor(Color.parseColor("#80000000")); // Fully transparent background
        outerLayout.setBackgroundColor(Color.TRANSPARENT);

        // 2. Background container with rounded corners and purple color
//        FrameLayout backgroundContainer = new FrameLayout(activity);
//        FrameLayout.LayoutParams bgParams = new FrameLayout.LayoutParams(
//                (int)(110 * density),
//                (int)(90 * density)
//        );
//        bgParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
//
//        backgroundContainer.setLayoutParams(bgParams);
//
//        GradientDrawable roundedBackground = new GradientDrawable();
//        roundedBackground.setColor(Color.parseColor("#2e0c69")); // Purple
//        roundedBackground.setCornerRadius(16 * density); // 16dp rounded corners
//        backgroundContainer.setBackground(roundedBackground);


        // IMAGE layer at the bottom
        ImageView imageView = new ImageView(activity);
        int imageWidth = (int)(150 * density);  // adjust as needed
        int imageHeight = (int)(185 * density); // adjust as needed
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                imageWidth,
                imageHeight
        );

        imageParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        imageParams.setMargins(0, 0, 0, 0); // small bottom margin
        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(activity)
                .load(ImageURL)
                .into(imageView);

        // GIF layer (money blast animation)
        ImageView gifView = new ImageView(activity);
        FrameLayout.LayoutParams gifParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        gifParams.setMargins(0, 0, 0, (int)(40 * density));
        gifView.setLayoutParams(gifParams);
        gifView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Load GIF using Glide
        Glide.with(activity)
                .asGif()
                .load("https://pradeep-innovates.github.io/image-host/istock.gif")
                .into(new CustomTarget<GifDrawable>() {
                    @Override
                    public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                        resource.setLoopCount(1); // Play once
                        gifView.setImageDrawable(resource);
                        resource.start();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        // Add to layout
        outerLayout.addView(gifView); // Add before imageView or wherever needed

//
//        // VIDEO layer (green screen money blast)
//        VideoView videoView = new VideoView(activity);
//        FrameLayout.LayoutParams videoParams = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT
//        );
//        videoView.setLayoutParams(videoParams);
//
//        Uri videoUri = Uri.parse("https://pradeep-innovates.github.io/image-host/istockphoto-1284673885-640-adp-unscreen.gif"); // Place `money_blast.mp4` in res/raw
//        videoView.setVideoURI(videoUri);
//        videoView.setOnPreparedListener(mp -> {
//            mp.setLooping(true);
//            mp.start();
//        });

        // CLOSE BUTTON
        ImageButton closeButton = new ImageButton(activity);
        GradientDrawable closeBg = new GradientDrawable();
        closeBg.setShape(GradientDrawable.OVAL);
        closeBg.setColor(Color.parseColor("#66000000")); // Semi-transparent black
        closeButton.setBackground(closeBg);

        int closeButtonSizePx = (int)(24 * density);
        FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(closeButtonSizePx, closeButtonSizePx);
        closeParams.gravity = Gravity.TOP | Gravity.END;
        closeParams.setMargins(0, (int)(18 * density), (int)(18 * density), 0);
        closeButton.setLayoutParams(closeParams);
        closeButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        closeButton.setPadding(15, 15, 15, 15);


        Glide.with(activity)
                .load("https://cdn-icons-png.flaticon.com/512/1828/1828778.png")
                .apply(new RequestOptions().transform(new ColorFilterTransformation(Color.WHITE)))
                .into(closeButton);

        closeButton.setOnClickListener(v -> {
            context.setDismissed();
            rootLayout.removeView(outerLayout);
        });

        // ADD views in correct order
//        outerLayout.addView(videoView);      // Animation
//        outerLayout.addView(backgroundContainer);  // Purple rounded background
        outerLayout.addView(imageView);            // Image centered above everything
        outerLayout.addView(closeButton);          // Close button on top-right

        rootLayout.addView(outerLayout);     // Add to root

        context.setPresented();
    }


    private void showCustomBottomRightPopup(Activity activity, CustomTemplateContext.TemplateContext context) {
        Log.d("CleverTap", "showCustomBottomRightPopup: called");
        FrameLayout rootLayout = activity.findViewById(android.R.id.content);
        float density = activity.getResources().getDisplayMetrics().density;

        // Outer container (350x350dp) with 20% transparent black background
        FrameLayout outerLayout = new FrameLayout(activity);
        FrameLayout.LayoutParams outerParams = new FrameLayout.LayoutParams((int) (180 * density), (int) (225 * density));
        outerParams.gravity = Gravity.BOTTOM | Gravity.END;
        outerParams.setMargins(0, 0, 70, 180); // Margin from screen edge
        outerLayout.setLayoutParams(outerParams);
//        outerLayout.setBackgroundColor(Color.parseColor("#0D000000")); // 20% transparent black
        outerLayout.setBackgroundColor(Color.TRANSPARENT);

        // Create the container FrameLayout
        FrameLayout bottomContainer = new FrameLayout(activity);

        // Set layout parameters
        FrameLayout.LayoutParams bottomParams = new FrameLayout.LayoutParams(
                (int) (150 * density), (int) (150 * density));
        bottomParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        bottomContainer.setLayoutParams(bottomParams);

        // Create a drawable with black background, white border, and corner radius
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor("#212121")); // Background color (black)
        drawable.setCornerRadius(16 * density);         // Corner radius
        drawable.setStroke((int) (1 * density), Color.WHITE); // White border
        bottomContainer.setBackground(drawable);

        // Create a vertical LinearLayout to hold TextViews
        LinearLayout textLayout = new LinearLayout(activity);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        FrameLayout.LayoutParams textLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL
        );
        textLayoutParams.setMargins(0, 0, 0, (int)(20 * density)); // Margin from bottom
        textLayout.setLayoutParams(textLayoutParams);

        // Create the "SCRATCH CARD" TextView
        TextView scratchCardText = new TextView(activity);
        scratchCardText.setText("SCRATCH CARD");
        scratchCardText.setTextColor(Color.parseColor("#E6AA99")); // Light orange
        scratchCardText.setTypeface(null, Typeface.BOLD);
        scratchCardText.setTextSize(16); // Optional: set text size
        scratchCardText.setGravity(Gravity.CENTER);

        // Create the "Reveal a reward" TextView
        TextView rewardText = new TextView(activity);
        rewardText.setText("Reveal a reward");
        rewardText.setTextColor(Color.WHITE);
        rewardText.setTypeface(null, Typeface.BOLD);
        rewardText.setTextSize(14); // Optional
        rewardText.setGravity(Gravity.CENTER);

        // Add space between the texts using margins
        LinearLayout.LayoutParams scratchParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        scratchParams.setMargins(0, 0, 0, (int)(2 * density)); // space between texts
        scratchCardText.setLayoutParams(scratchParams);

        // Add views to layout
        textLayout.addView(scratchCardText);
        textLayout.addView(rewardText);
        bottomContainer.addView(textLayout);

        bottomContainer.setAlpha(0f);  // ← Add this line


        // First inner container: 250x200dp, maroon background (#800000)
        FrameLayout imageContainer = new FrameLayout(activity);
        FrameLayout.LayoutParams maroonParams = new FrameLayout.LayoutParams(
                (int) (100 * density), (int) (125 * density));
        maroonParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        maroonParams.topMargin = (int)(25 * density);  // 20dp from top edge
        imageContainer.setLayoutParams(maroonParams);
//        imageContainer.setBackgroundColor(Color.RED);
        imageContainer.setRotation(12f);

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,  // top-left to bottom-right
                new int[]{
                        Color.parseColor("#F4A174"),
                        Color.parseColor("#E2643F"),
                        Color.parseColor("#D04636"),
                        Color.parseColor("#B22222"),  // firebrick red (replaces purple)
                        Color.parseColor("#9F2E25")
                }
        );
        gradient.setCornerRadius(16 * density);
        gradient.setStroke((int)(1 * density), Color.parseColor("#FFA500")); // orange border
        imageContainer.setBackground(gradient);



        // Container for the circle + icon
        FrameLayout iconContainer = new FrameLayout(activity);
        int circleSize = (int)(36 * density);
        FrameLayout.LayoutParams iconContainerParams = new FrameLayout.LayoutParams(circleSize, circleSize);
        iconContainerParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        iconContainerParams.topMargin = (int)(30 * density);
        iconContainer.setLayoutParams(iconContainerParams);

        // Circular dark orange background
        GradientDrawable circleBg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        Color.parseColor("#B22222"),  // darker orange start (top-left)
                        Color.parseColor("#EA7543")
                }
        );
        circleBg.setShape(GradientDrawable.OVAL);
        iconContainer.setBackground(circleBg);


        // ImageView for icon inside the circle
        ImageView iconImage = new ImageView(activity);
        int iconSize = (int)(20 * density);  // smaller than circle
        FrameLayout.LayoutParams iconImageParams = new FrameLayout.LayoutParams(iconSize, iconSize);
        iconImageParams.gravity = Gravity.CENTER;
        iconImage.setLayoutParams(iconImageParams);

        // Set your icon drawable here — replace with your actual drawable resource
        iconImage.setImageResource(R.drawable.gift_icon);
        iconImage.setColorFilter(Color.parseColor("#E6AA99"));

        // Add icon ImageView to circle container
        iconContainer.addView(iconImage);

        // Add iconContainer to imageContainer
        imageContainer.addView(iconContainer);


        TextView scratchText = new TextView(activity);
        scratchText.setText("--- SCRATCH HERE ---");
        scratchText.setTextColor(Color.parseColor("#E6AA99"));
        scratchText.setTypeface(Typeface.DEFAULT_BOLD);
        scratchText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
        scratchText.setGravity(Gravity.CENTER);

        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        textParams.topMargin = (int)(75 * density);  // below iconCircle
        scratchText.setLayoutParams(textParams);

        imageContainer.addView(scratchText);



        ViewCompat.setElevation(imageContainer, 8 * density);  // LOWER
        ViewCompat.setElevation(bottomContainer, 2 * density);  // HIGHER



        // Close button setup
        ImageButton closeButton = new ImageButton(activity);
        GradientDrawable closeBg = new GradientDrawable();
        closeBg.setShape(GradientDrawable.OVAL);
        closeBg.setColor(Color.parseColor("#807882")); // 40% transparent black
//        int borderWidthPx = (int) (2 * density);
//        closeBg.setStroke(borderWidthPx, Color.BLACK);
        closeButton.setBackground(closeBg);

        int closeButtonSizePx = (int) (28 * density);
        FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(closeButtonSizePx, closeButtonSizePx);
        closeParams.gravity = Gravity.END | Gravity.TOP;
        closeParams.setMargins(0, (int) (5 * density), (int) (5 * density), 0);
        //
        closeButton.setLayoutParams(closeParams);

        closeButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        closeButton.setAdjustViewBounds(true);
        closeButton.setPadding(21, 21, 21, 21);

        RequestOptions requestOptions = new RequestOptions()
                .transform(new ColorFilterTransformation(Color.WHITE));
        Glide.with(activity)
                .load("https://cdn-icons-png.flaticon.com/512/1828/1828778.png")
                .apply(requestOptions)
                .into(closeButton);

        closeButton.setOnClickListener(v -> {
            context.setDismissed();
            rootLayout.removeView(outerLayout);
        });

        // Add views to outer layout in correct order
        outerLayout.addView(bottomContainer);     // On top of scratch card
//        outerLayout.addView(closeButton);         // Always on top


        // Add to root layout
        rootLayout.addView(outerLayout);

        outerLayout.setOnClickListener(v -> {
            rootLayout.removeView(outerLayout);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                showScratchCard(activity, context);
            }, 400);
        });

        imageContainer.setTranslationY(150 * density);

        new Handler().postDelayed(() -> {
            // Fade in bottomContainer first
            bottomContainer.setAlpha(0f);
            ObjectAnimator fadeInBottom = ObjectAnimator.ofFloat(bottomContainer, "alpha", 0f, 1f);
            fadeInBottom.setDuration(500);
            fadeInBottom.setInterpolator(new DecelerateInterpolator());

            fadeInBottom.start();

            fadeInBottom.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    outerLayout.addView(imageContainer);     // Add first (goes behind)

                    // Step 1: Move up
                    ObjectAnimator moveUp = ObjectAnimator.ofFloat(
                            imageContainer,
                            "translationY",
                            150 * density,
                            0
                    );
                    moveUp.setDuration(500);
                    moveUp.setInterpolator(new DecelerateInterpolator());

                    // Step 2: Shake by rotating left and return to 12°
                    ObjectAnimator shakeTilt = ObjectAnimator.ofFloat(
                            imageContainer,
                            "rotation",
                            12f, -12f, 12f  // Tilt left (4°), then back to 12°
                    );
                    shakeTilt.setDuration(300);
                    shakeTilt.setInterpolator(new AccelerateDecelerateInterpolator());

                    // Chain move and shake
                    AnimatorSet dropSet = new AnimatorSet();
                    dropSet.playSequentially(moveUp, shakeTilt);

                    dropSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // Add close button
                            ViewCompat.setElevation(closeButton, 16 * density);
                            outerLayout.addView(closeButton);
                        }
                    });

                    // Start the animation
                    dropSet.start();


                }
            });


        }, 100);

        context.setPresented();
    }


    private void showScratchCard(Activity activity, CustomTemplateContext.TemplateContext context) {
        String companyInitial = context.getString("Company Initial");
        String companyName = context.getString("Company Name");
        String discountTxt = context.getString("Discount Text");
        String coupncode = context.getString("Coupon Code");
        String buttonName = context.getString("Button Name");
        FrameLayout rootLayout = activity.findViewById(android.R.id.content);
        float density = activity.getResources().getDisplayMetrics().density;


        // Outer layout: fixed size and centered, with transparent background
        FrameLayout outerLayout = new FrameLayout(activity);
        FrameLayout.LayoutParams outerParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        outerLayout.setLayoutParams(outerParams);
        outerLayout.setBackgroundColor(Color.parseColor("#99000000")); // Transparent black
        outerLayout.setClickable(true);
        outerLayout.setFocusable(true);
//        outerLayout.setBackgroundColor(Color.TRANSPARENT);

        // Container for bounce effect
        FrameLayout bounceContainer = new FrameLayout(activity);
        FrameLayout.LayoutParams bounceParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        bounceContainer.setLayoutParams(bounceParams);

        // Inner layout: white box with black border and rounded corners
        FrameLayout innerLayout = new FrameLayout(activity);
        FrameLayout.LayoutParams innerParams = new FrameLayout.LayoutParams(
                (int) (275 * density), (int) (345 * density));
        innerParams.gravity = Gravity.CENTER;
        innerLayout.setLayoutParams(innerParams);

        GradientDrawable innerBg = new GradientDrawable();
        innerBg.setColor(Color.parseColor("#FEFEFF"));
        innerBg.setStroke(2, Color.parseColor("#FFA500"));
        innerBg.setCornerRadius(48);
        innerLayout.setBackground(innerBg);

        // Vertical layout for texts + button + response
        LinearLayout textContainer = new LinearLayout(activity);
        textContainer.setOrientation(LinearLayout.VERTICAL);
        textContainer.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        textContainer.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        // Define common spacing
        int spacing = (int) (8 * density);
        int buttonHorizontalMargin = (int) (16 * density);
        int horizontalMargin = (int) (12 * density);
        int buttonBottomMargin = (int) (18 * density);

        // First TextView
        TextView initalText = new TextView(activity);
        initalText.setText(companyInitial);
        initalText.setTextSize(60f);
        initalText.setTextColor(Color.BLACK);
        initalText.setGravity(Gravity.CENTER);
        initalText.setTypeface(Typeface.DEFAULT_BOLD); // <-- Make text bold
        LinearLayout.LayoutParams initialTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        initialTextParams.topMargin = spacing;
        initalText.setLayoutParams(initialTextParams);

        // Second TextView
        TextView companyText = new TextView(activity);
        companyText.setText(companyName);
        companyText.setTextSize(28f);
        companyText.setTextColor(Color.BLACK);
        companyText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams companyTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        companyTextParams.topMargin = spacing;
        companyText.setLayoutParams(companyTextParams);

        // Third TextView
        TextView discountText = new TextView(activity);
        discountText.setText(discountTxt);
        discountText.setTextSize(32f);
        discountText.setTextColor(Color.BLACK);
        discountText.setGravity(Gravity.CENTER);
        discountText.setTypeface(Typeface.DEFAULT_BOLD); // <-- Make text bold
        LinearLayout.LayoutParams discountTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        discountTextParams.topMargin = spacing;
        discountTextParams.bottomMargin = spacing;
        discountText.setLayoutParams(discountTextParams);

        // Create dashed line
        // Create dashed line
        View dashedLine = new View(activity);
        LinearLayout.LayoutParams dashedParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (2 * density)  // 2dp height for line
        );
        dashedParams.setMargins(horizontalMargin, spacing, horizontalMargin, spacing);
        dashedLine.setLayoutParams(dashedParams);

        ShapeDrawable dashedDrawable = new ShapeDrawable(new RectShape());
        Paint paint = dashedDrawable.getPaint();
        paint.setColor(Color.parseColor("#D3D3D3"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2 * density);
        paint.setPathEffect(new DashPathEffect(new float[]{10 * density, 10 * density}, 0));

        dashedLine.setBackground(dashedDrawable);



        // Coupon Code Container
        LinearLayout couponContainer = new LinearLayout(activity);
        couponContainer.setOrientation(LinearLayout.HORIZONTAL);
        couponContainer.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams couponContainerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        couponContainerParams.setMargins(0, 10, 0, (int) (18 * activity.getResources().getDisplayMetrics().density));
        couponContainer.setLayoutParams(couponContainerParams);

        // Coupon Code Text
        TextView couponCode = new TextView(activity);
        couponCode.setText(coupncode);
        couponCode.setTextSize(22f);
        couponCode.setTextColor(Color.parseColor("#D3D3D3"));
        couponCode.setPadding(0, 0, (int) (8 * density), 0);

        // Copy Icon
        ImageView copyIcon = new ImageView(activity);
        copyIcon.setImageResource(R.drawable.ic_copy); // Replace with your actual copy icon resource
        copyIcon.setColorFilter(Color.BLACK);
        int copyIconSize = (int) (20 * density);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(copyIconSize, copyIconSize);
        copyIcon.setLayoutParams(iconParams);
        copyIcon.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Coupon Code", coupncode);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(activity, "Coupon copied!", Toast.LENGTH_SHORT).show();
        });

        couponContainer.addView(couponCode);
        couponContainer.addView(copyIcon);


        // Button: "Guess what" with rounded corners and soft pink background
        Button claimButton = new Button(activity);
        claimButton.setText(buttonName);
        claimButton.setTextSize(18f);
        claimButton.setTextColor(Color.parseColor("#F67C44")); // Orange text
        claimButton.setTypeface(Typeface.DEFAULT_BOLD);
        claimButton.setAllCaps(false);

        LinearLayout.LayoutParams fullWidthParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        fullWidthParams.setMargins(buttonHorizontalMargin, 0, buttonHorizontalMargin, buttonBottomMargin);
        claimButton.setLayoutParams(fullWidthParams);

        // Faded Orange Background
        GradientDrawable claimBg = new GradientDrawable();
        claimBg.setColor(Color.parseColor("#FFECE5")); // Light faded orange
        claimBg.setCornerRadius(16 * density);
//        claimBg.setStroke(2, Color.parseColor("#FF5722"));
        claimButton.setBackground(claimBg);

        // Show mentorText when button is clicked
        claimButton.setOnClickListener(v -> {
            context.triggerActionArgument("ButtonURL", activity);
            context.setDismissed();
            rootLayout.removeView(outerLayout);
            Log.d("CleverTap", "showScratchCard: Clicked on Button");
        });


        // Add views to container in correct order
        textContainer.addView(initalText);
        textContainer.addView(companyText);
        textContainer.addView(discountText);   // Initially hidden, appears below second text
        textContainer.addView(dashedLine); // Red dashed line added here
        textContainer.addView(couponContainer);
        textContainer.addView(claimButton); // Appears below everything with spacing

        // Build your imageContainer layout
        FrameLayout imageContainer = new FrameLayout(activity);
        FrameLayout.LayoutParams maroonParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        maroonParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        maroonParams.topMargin = (int)(25 * density);  // 20dp from top edge
        imageContainer.setLayoutParams(maroonParams);
        imageContainer.setRotation(12f);

        // Background gradient
        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        Color.parseColor("#F4A174"),
                        Color.parseColor("#E2643F"),
                        Color.parseColor("#D04636"),
                        Color.parseColor("#B22222"),
                        Color.parseColor("#9F2E25")
                });
        gradient.setCornerRadius(16 * density);
        gradient.setStroke((int)(1 * density), Color.parseColor("#FFA500"));
        imageContainer.setBackground(gradient);

        // Circle + icon
        FrameLayout iconContainer = new FrameLayout(activity);
        int circleSize = (int)(100 * density);
        FrameLayout.LayoutParams iconContainerParams = new FrameLayout.LayoutParams(circleSize, circleSize);
        iconContainerParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        iconContainerParams.topMargin = (int)(80 * density);
        iconContainer.setLayoutParams(iconContainerParams);

        // Circular background
        GradientDrawable circleBg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        Color.parseColor("#B22222"),
                        Color.parseColor("#EA7543")
                });
        circleBg.setShape(GradientDrawable.OVAL);
        iconContainer.setBackground(circleBg);

        // Gift icon
        ImageView iconImage = new ImageView(activity);
        int iconSize = (int)(55 * density);
        FrameLayout.LayoutParams iconImageParams = new FrameLayout.LayoutParams(iconSize, iconSize);
        iconImageParams.gravity = Gravity.CENTER;
        iconImage.setLayoutParams(iconImageParams);
        iconImage.setImageResource(R.drawable.gift_icon);
        iconImage.setColorFilter(Color.parseColor("#E6AA99"));
        iconContainer.addView(iconImage);

        // Add iconContainer to imageContainer
        imageContainer.addView(iconContainer);

        // Scratch text
        TextView scratchText = new TextView(activity);
        scratchText.setText("--- SCRATCH HERE ---");
        scratchText.setTextColor(Color.parseColor("#E6AA99"));
        scratchText.setTypeface(Typeface.DEFAULT_BOLD);
        scratchText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        scratchText.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        textParams.topMargin = (int)(230 * density);
        scratchText.setLayoutParams(textParams);
        imageContainer.addView(scratchText);


        // ScratchView on top of inner layout
        ScratchView scratchView = new ScratchView(activity);
        FrameLayout.LayoutParams scratchParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        scratchParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        scratchView.setLayoutParams(scratchParams);

        // Delay to wait for layout measurements to be ready
        scratchView.post(() -> {
            scratchView.setOverlayView(imageContainer);
        });

        // Bounce entire bounceContainer (which includes innerLayout + closeButton)
        scratchView.setOnScratchCompleteListener(() -> {
            scratchView.clearRemainingArea();
            bounceContainer.setScaleX(0.8f);
            bounceContainer.setScaleY(0.8f);
            bounceContainer.animate()
                    .scaleX(1.1f).scaleY(1.1f)
                    .setDuration(200)
                    .withEndAction(() -> bounceContainer.animate()
                            .scaleX(1f).scaleY(1f)
                            .setDuration(100)
                            .start())
                    .start();
            innerLayout.removeView(scratchView);
        });

        // Add text and scratch view to inner layout
        innerLayout.addView(textContainer);
        innerLayout.addView(scratchView);

        // Close button setup
        ImageButton closeButton = new ImageButton(activity);
        GradientDrawable closeBg = new GradientDrawable();
        closeBg.setShape(GradientDrawable.OVAL);
        closeBg.setColor(Color.parseColor("#99000000")); // 60% transparent black
        closeBg.setStroke(2, Color.WHITE); // White stroke with 2px width
        closeButton.setBackground(closeBg);

        int closeButtonSizePx = (int) (30 * activity.getResources().getDisplayMetrics().density);
        FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(closeButtonSizePx, closeButtonSizePx);
        closeParams.gravity = Gravity.START | Gravity.TOP;
        closeParams.setMargins(100, 300, 0, 0);
        closeButton.setLayoutParams(closeParams);

        closeButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        closeButton.setAdjustViewBounds(true);
        closeButton.setPadding(21, 21, 21, 21);

        RequestOptions requestOptions1 = new RequestOptions()
                .transform(new ColorFilterTransformation(Color.WHITE));
        Glide.with(activity)
                .load("https://cdn-icons-png.flaticon.com/512/1828/1828778.png")
                .apply(requestOptions1)
                .into(closeButton);

        closeButton.setOnClickListener(v -> {
            context.setDismissed();
            rootLayout.removeView(outerLayout);
        });

        // Assemble view hierarchy
        bounceContainer.addView(innerLayout);
        outerLayout.addView(closeButton);
        outerLayout.addView(bounceContainer);
        rootLayout.addView(outerLayout);

        context.setPresented();
    }

    private void showFirstTooltip(Activity activity, CustomTemplateContext.TemplateContext context) {
        String content1 = context.getString("title1");
        Log.d("CleverTap", "showFirstTooltip: called");

        FrameLayout rootLayout = activity.findViewById(android.R.id.content);

        // Wrapper layout
        FrameLayout wrapper = new FrameLayout(activity);
        FrameLayout.LayoutParams wrapperParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.END
        );
        wrapperParams.setMargins(0, dpToPx(250), dpToPx(30), 0);
        wrapper.setLayoutParams(wrapperParams);

        // Vertical container for text + image
        LinearLayout tooltipContent = new LinearLayout(activity);
        tooltipContent.setOrientation(LinearLayout.VERTICAL);
        tooltipContent.setGravity(Gravity.CENTER_HORIZONTAL);
        FrameLayout.LayoutParams tooltipParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        tooltipContent.setLayoutParams(tooltipParams);

        // Content box (TextView)
        TextView contentBox = new TextView(activity);
        contentBox.setText(content1);
        contentBox.setTextColor(Color.WHITE);
        contentBox.setTextSize(14f);
        contentBox.setPadding(dpToPx(12), dpToPx(6), dpToPx(12), dpToPx(2));
        // Background for top (content box)
        GradientDrawable topBg = new GradientDrawable();
        topBg.setColor(Color.parseColor("#F6842B"));
        topBg.setCornerRadii(new float[]{
                dpToPx(8), dpToPx(8),   // top-left, top-right
                dpToPx(8), dpToPx(8),   // top-right, top-right (extra safety)
                0f, 0f,                 // bottom-left
                0f, 0f                  // bottom-right
        });
        contentBox.setBackground(topBg);

        LinearLayout.LayoutParams contentBoxParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        contentBoxParams.setMargins(0, dpToPx(12), 0, 0);
        contentBox.setLayoutParams(contentBoxParams);

        // Image container
        FrameLayout imageContainer = new FrameLayout(activity);
        LinearLayout.LayoutParams imageContainerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(100)
        );
        imageContainerParams.setMargins(0, 0, 0, 0);
        imageContainer.setLayoutParams(imageContainerParams);

        // ImageView inside container
        ImageView imageView = new ImageView(activity);
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        imageView.setLayoutParams(imageParams);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        int radius = dpToPx(8);
        int borderSize = dpToPx(2);
        int borderColor = Color.parseColor("#F6842B");

        RequestOptions requestOptions1 = new RequestOptions()
                .transform(new BottomRoundedBorderTransformation(radius, borderSize, borderColor));

        // Load image using Glide
        Glide.with(activity)
                .load("https://static.vecteezy.com/system/resources/thumbnails/011/432/528/small/enter-login-and-password-registration-page-on-screen-sign-in-to-your-account-creative-metaphor-login-page-mobile-app-with-user-page-flat-illustration-vector.jpg")
                .apply(requestOptions1)
                .into(imageView);

        // Add imageView inside imageContainer
        imageContainer.addView(imageView);

        // Triangle view
        View triangle = new View(activity);
        int triangleWidth = dpToPx(23);
        int triangleHeight = dpToPx(23);
        FrameLayout.LayoutParams triangleParams = new FrameLayout.LayoutParams(triangleWidth, triangleHeight);
        triangleParams.gravity = Gravity.TOP | Gravity.END;
        triangleParams.setMargins(0, 10, 50, 0);
        triangle.setLayoutParams(triangleParams);
        triangle.setRotation(45f);
        triangle.setBackgroundColor(Color.parseColor("#F6842B"));

        // Add views in correct order
        tooltipContent.addView(contentBox);
        tooltipContent.addView(imageContainer);
        wrapper.addView(triangle);
        wrapper.addView(tooltipContent);
        rootLayout.addView(wrapper);

        // Auto-dismiss
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            rootLayout.removeView(wrapper);
            context.setDismissed();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                showSecondTooltip(activity, context);
            }, 500);
        }, 3000);

        context.setPresented();
    }

    private void showSecondTooltip(Activity activity, CustomTemplateContext.TemplateContext context) {
        String content2 = context.getString("title2");
//        String content2 = "Complete the SignUp Process!";
        Log.d("CleverTap", "showSecondTooltip: called");

        FrameLayout rootLayout = activity.findViewById(android.R.id.content);

        // Wrapper layout
        FrameLayout wrapper = new FrameLayout(activity);
        FrameLayout.LayoutParams wrapperParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.START
        );
        wrapperParams.setMargins(dpToPx(128), dpToPx(438), 0, 0); // shift from left and top
        wrapper.setLayoutParams(wrapperParams);

        // Frame to hold text + triangle overlay
        FrameLayout tooltipBox = new FrameLayout(activity);
        FrameLayout.LayoutParams tooltipParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        tooltipBox.setLayoutParams(tooltipParams);

        // Content box (TextView)
        TextView contentBox = new TextView(activity);
        contentBox.setText(content2);
        contentBox.setTextColor(Color.WHITE);
        contentBox.setTextSize(14f);
        contentBox.setPadding(dpToPx(12), dpToPx(8), dpToPx(12), dpToPx(8));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.parseColor("#F6842B"));
        bg.setCornerRadius(dpToPx(8));
        contentBox.setBackground(bg);
        FrameLayout.LayoutParams contentBoxParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        contentBoxParams.setMargins(0, dpToPx(12), 0, 0); // adds top spacing between tooltipBox top and text
        contentBox.setLayoutParams(contentBoxParams);

        // Triangle view
        View triangle = new View(activity);
        int triangleWidth = dpToPx(23);
        int triangleHeight = dpToPx(23);
        FrameLayout.LayoutParams triangleParams = new FrameLayout.LayoutParams(triangleWidth, triangleHeight);
        triangleParams.gravity = Gravity.TOP | Gravity.START;
        triangleParams.setMargins(50, 10, 0, 0); // mirror the margins for left side
        triangle.setLayoutParams(triangleParams);
        triangle.setRotation(45f);
        triangle.setBackgroundColor(Color.parseColor("#F6842B"));

        // Add views
        tooltipBox.addView(triangle);
        tooltipBox.addView(contentBox);
        wrapper.addView(tooltipBox);
        rootLayout.addView(wrapper);

        context.setPresented();


        // Auto-dismiss
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            rootLayout.removeView(wrapper);
            context.setDismissed();
        }, 3000);

    }


    private void showTopRightTooltip(Activity activity, CustomTemplateContext.TemplateContext context) {
        String content1 = context.getString("content1");
        Log.d("CleverTap", "showTopRightBox: called");

        FrameLayout rootLayout = activity.findViewById(android.R.id.content);

        // Wrapper layout
        FrameLayout wrapper = new FrameLayout(activity);
        FrameLayout.LayoutParams wrapperParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.END
        );
        wrapperParams.setMargins(0, dpToPx(100), dpToPx(12), 0);
        wrapper.setLayoutParams(wrapperParams);

        // Frame to hold text + triangle overlay
        FrameLayout tooltipBox = new FrameLayout(activity);
        FrameLayout.LayoutParams tooltipParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        tooltipBox.setLayoutParams(tooltipParams);

        // Content box (TextView)
        TextView contentBox = new TextView(activity);
        contentBox.setText(content1);
        contentBox.setTextColor(Color.WHITE);
        contentBox.setTextSize(14f);
        contentBox.setPadding(dpToPx(12), dpToPx(8), dpToPx(12), dpToPx(8));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.parseColor("#FF4081"));
        bg.setCornerRadius(dpToPx(8));
        contentBox.setBackground(bg);
        FrameLayout.LayoutParams contentBoxParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        contentBoxParams.setMargins(0, dpToPx(12), 0, 0); // adds top spacing between tooltipBox top and text
        contentBox.setLayoutParams(contentBoxParams);


        // Triangle view
        View triangle = new View(activity);
        int triangleSize = dpToPx(50);
        // Define fixed size for the triangle
        int triangleWidth = dpToPx(23);
        int triangleHeight = dpToPx(23);
        FrameLayout.LayoutParams triangleParams = new FrameLayout.LayoutParams(triangleWidth, triangleHeight);
        triangleParams.gravity = Gravity.TOP | Gravity.END;
        triangleParams.setMargins(0, 10, 50, 0); // position top-right, overlap
        triangle.setLayoutParams(triangleParams);
        triangle.setRotation(45f);
        triangle.setBackgroundColor(Color.parseColor("#FF4081"));

        // Add views
        tooltipBox.addView(triangle);
        tooltipBox.addView(contentBox);
        wrapper.addView(tooltipBox);
        rootLayout.addView(wrapper);

        // Auto-dismiss
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            rootLayout.removeView(wrapper);
            context.setDismissed();
            // Delay the next tooltip (e.g. 500ms after removal)
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                showTopLeftTooltip(activity, context);
            }, 500);
            }, 1500);

        context.setPresented();
    }

    private void showTopLeftTooltip(Activity activity, CustomTemplateContext.TemplateContext context) {
        String content2 = context.getString("content2");
//        String content2 = "Complete the SignUp Process!";
        Log.d("CleverTap", "showTopLeftTooltip: called");

        FrameLayout rootLayout = activity.findViewById(android.R.id.content);

        // Wrapper layout
        FrameLayout wrapper = new FrameLayout(activity);
        FrameLayout.LayoutParams wrapperParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.START
        );
        wrapperParams.setMargins(dpToPx(12), dpToPx(100), 0, 0); // shift from left and top
        wrapper.setLayoutParams(wrapperParams);

        // Frame to hold text + triangle overlay
        FrameLayout tooltipBox = new FrameLayout(activity);
        FrameLayout.LayoutParams tooltipParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        tooltipBox.setLayoutParams(tooltipParams);

        // Content box (TextView)
        TextView contentBox = new TextView(activity);
        contentBox.setText(content2);
        contentBox.setTextColor(Color.WHITE);
        contentBox.setTextSize(14f);
        contentBox.setPadding(dpToPx(12), dpToPx(8), dpToPx(12), dpToPx(8));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.parseColor("#FF4081"));
        bg.setCornerRadius(dpToPx(8));
        contentBox.setBackground(bg);
        FrameLayout.LayoutParams contentBoxParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        contentBoxParams.setMargins(0, dpToPx(12), 0, 0); // adds top spacing between tooltipBox top and text
        contentBox.setLayoutParams(contentBoxParams);

        // Triangle view
        View triangle = new View(activity);
        int triangleWidth = dpToPx(23);
        int triangleHeight = dpToPx(23);
        FrameLayout.LayoutParams triangleParams = new FrameLayout.LayoutParams(triangleWidth, triangleHeight);
        triangleParams.gravity = Gravity.TOP | Gravity.START;
        triangleParams.setMargins(50, 10, 0, 0); // mirror the margins for left side
        triangle.setLayoutParams(triangleParams);
        triangle.setRotation(45f);
        triangle.setBackgroundColor(Color.parseColor("#FF4081"));

        // Add views
        tooltipBox.addView(triangle);
        tooltipBox.addView(contentBox);
        wrapper.addView(tooltipBox);
        rootLayout.addView(wrapper);

        context.setPresented();


        // Auto-dismiss
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            rootLayout.removeView(wrapper);
            context.setDismissed();
        }, 1500);

    }



    private void showBottomBanner(Activity activity, CustomTemplateContext.TemplateContext context) {
        String content = context.getString("content");
        String buttonName = context.getString("buttonName");

        Log.d("CleverTap", "showBottomBanner: called");

        RequestOptions requestOptions = new RequestOptions()
                .transform(new ColorFilterTransformation(Color.WHITE));

        FrameLayout rootLayout = activity.findViewById(android.R.id.content);

        // Container with semi-transparent black background
        FrameLayout bannerContainer = new FrameLayout(activity);
        FrameLayout.LayoutParams containerParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
        );
        containerParams.setMargins(0, 0, 0, dpToPx(36)); // move banner up from bottom
        bannerContainer.setLayoutParams(containerParams);
        bannerContainer.setBackgroundColor(Color.parseColor("#D9000000")); // 80% transparent black
//        bannerContainer.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        // TextView for content (top-left, with right padding/margin to avoid overlap)
        TextView textView = new TextView(activity);
        textView.setText(content);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(12f); // smaller text
        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.gravity = Gravity.START | Gravity.TOP;
        textParams.setMargins(dpToPx(16), dpToPx(30), dpToPx(120), dpToPx(30)); // right margin to prevent overlap with button
        textView.setLayoutParams(textParams);

        TextView actionButton = new TextView(activity);
        actionButton.setText(buttonName);
        actionButton.setTextColor(Color.parseColor("#FF4081")); // pink
        actionButton.setTextSize(14f);
        actionButton.setAllCaps(false);
        actionButton.setBackgroundColor(Color.TRANSPARENT); // transparent background
        actionButton.setPadding(dpToPx(16), 0, 0, 0); // no padding

        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.gravity = Gravity.END | Gravity.BOTTOM;
        buttonParams.setMargins(0, 0, dpToPx(16), dpToPx(12)); // no margin
//        buttonParams.setMargins(0,0,0,0);
        actionButton.setLayoutParams(buttonParams);
        actionButton.setClickable(true);
        actionButton.setFocusable(true);



        actionButton.setOnClickListener(v -> {
            Toast.makeText(activity, "Action clicked", Toast.LENGTH_SHORT).show();
            context.triggerActionArgument("buttonClick", activity);
            rootLayout.removeView(bannerContainer);
            context.setDismissed();
        });

        // Close button
        ImageButton closeButton = new ImageButton(activity);

        // Create circular black transparent background
        GradientDrawable closeBg = new GradientDrawable();
        closeBg.setShape(GradientDrawable.OVAL);
        closeBg.setColor(Color.parseColor("#404040")); // Dark gray
        closeButton.setBackground(closeBg);

        int closeButtonSizePx = (int) (22 * activity.getResources().getDisplayMetrics().density); // 30dp

        FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(closeButtonSizePx, closeButtonSizePx);
        closeParams.gravity = Gravity.END | Gravity.TOP;
        closeParams.setMargins(0, 24, 24, 0); // Top and right margins
        closeButton.setLayoutParams(closeParams);

        closeButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        closeButton.setAdjustViewBounds(true);
        closeButton.setPadding(17, 17, 17, 17);

        // Load the custom close icon using Glide
        Glide.with(activity)
                .load("https://cdn-icons-png.flaticon.com/512/2976/2976286.png") // Black 'X' icon
                .apply(requestOptions)
                .into(closeButton);

        closeButton.setOnClickListener(v -> {
            rootLayout.removeView(bannerContainer);
            context.setDismissed();
        });

        bannerContainer.addView(textView);
        bannerContainer.addView(actionButton);
        bannerContainer.addView(closeButton);

        rootLayout.addView(bannerContainer);
        context.setPresented();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }



    private void showProgressAlert(Activity activity, CustomTemplateContext.TemplateContext context) {
        String videoString = context.getString("videoId");
//        String videoString = "video1, video2, video3";
        List<String> videoIds = Arrays.asList(videoString.split(",\\s*"));

        String progressBarColorRaw = context.getString("progressBarColor");
        String buttonBgColorRaw = context.getString("buttonBgColor");
//        String progressBarColor = "#A259FF";
//        String buttonColor = "#6A0DAD";

        // Provide fallback defaults
        final String progressBarColor = (progressBarColorRaw == null || progressBarColorRaw.isEmpty())
                ? "#A259FF" : progressBarColorRaw;
        final String buttonBgColor = (buttonBgColorRaw == null || buttonBgColorRaw.isEmpty())
                ? "#6A0DAD" : buttonBgColorRaw;

        Executors.newSingleThreadExecutor().execute(() -> {
            VideoProgressDao dao = DatabaseClient.getInstance(activity).getAppDatabase().videoProgressDao();
            for (String videoId : videoIds) {
                VideoProgressEntity videoProgress = dao.getProgressSync(videoId); // You must define this DAO method
                if (videoProgress != null) {
                    activity.runOnUiThread(() -> {
                        Toast.makeText(activity, "Video progress found. Showing in 3 seconds...", Toast.LENGTH_SHORT).show();
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            showProgressDialogWithVideo(activity, context, videoProgress, progressBarColor, buttonBgColor);
                        }, 3000);
                    });
                    Log.d("CleverTap", "This video is playing : " + videoId);
                    return;
                }
            }
            // If no video found
            activity.runOnUiThread(() -> {
                Log.e("CleverTap", "No matching video found in database for any of: " + videoIds);
                context.setDismissed();

                AlertDialog alertDialog = new AlertDialog.Builder(activity)
//                        .setTitle("No Video Data Found")
                        .setMessage("No video data found in the app. Watch any video first from Video Activity.")
                        .setCancelable(true)
                        .setPositiveButton("Go to Video Activity", (dialog, which) -> {
                            Intent intent = new Intent(activity, VideoPlayerActivity.class); // Replace with your actual activity
                            activity.startActivity(intent);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();

                // Change button text colors after showing
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#C2185B")); // Strong pink
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

            });
        });
    }

    @OptIn(markerClass = UnstableApi.class)
    private void showProgressDialogWithVideo(Activity activity, CustomTemplateContext.TemplateContext context, VideoProgressEntity videoProgress, String progressBarColor, String buttonBgColor) {
        String videoUrl = videoProgress.videoUrl;
//                    String videoUrl = "app://p2";

        BottomSheetDialog dialog = new BottomSheetDialog(activity);

        // Root Layout
        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(48, 48, 48, 48);
        root.setBackgroundColor(Color.BLACK);
        root.setGravity(Gravity.CENTER);

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.parseColor("#1E1E1E")); // Dark Netflix background
        background.setCornerRadii(new float[]{40, 40, 40, 40, 0, 0, 0, 0});
        root.setBackground(background);

        // ExoPlayer Setup
        ExoPlayer player = new ExoPlayer.Builder(activity).build();
        PlayerView playerView = new PlayerView(activity);
        playerView.setPlayer(player);
        playerView.setUseController(false);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM); // Crop fill
        player.setMediaItem(MediaItem.fromUri(videoUrl));
        player.setRepeatMode(Player.REPEAT_MODE_ALL); // Loop the single video
        player.prepare();
        player.play();
        player.setVolume(0f); // Start muted

        // Frame container for video and buttons
        FrameLayout videoContainer = new FrameLayout(activity);
        FrameLayout.LayoutParams videoParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, 500);
        videoContainer.setLayoutParams(videoParams);
        videoContainer.addView(playerView);

        // Mute Button
        ImageButton muteButton = new ImageButton(activity);
        muteButton.setImageResource(android.R.drawable.ic_lock_silent_mode);
        muteButton.setBackgroundColor(Color.TRANSPARENT);
        FrameLayout.LayoutParams muteParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        muteParams.gravity = Gravity.END | Gravity.BOTTOM;
        muteParams.setMargins(25, 25, 25, 0);
        muteButton.setLayoutParams(muteParams);
        videoContainer.addView(muteButton);

        // Close Button
        ImageButton closeButton = new ImageButton(activity);
        closeButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        closeButton.setBackgroundColor(Color.TRANSPARENT);
        FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        closeParams.gravity = Gravity.END | Gravity.TOP;
        closeParams.setMargins(25, 0, 25, 25);
        closeButton.setLayoutParams(closeParams);
        videoContainer.addView(closeButton);

        // Toggle mute/play/pause
        final boolean[] isMuted = {true};
        final boolean[] isPlaying = {true};

        muteButton.setOnClickListener(v -> {
            isMuted[0] = !isMuted[0];
            player.setVolume(isMuted[0] ? 0f : 1f);
            muteButton.setImageResource(isMuted[0]
                    ? android.R.drawable.ic_lock_silent_mode
                    : android.R.drawable.ic_lock_silent_mode_off);
        });

        closeButton.setOnClickListener(v -> {
            player.release();
            dialog.dismiss();
            context.setDismissed();
            Log.d("CleverTap", "Dialog dismissed by close button");
        });

        videoContainer.setOnClickListener(v -> {
            isPlaying[0] = !isPlaying[0];
            if (isPlaying[0]) {
                player.play();
            } else {
                player.pause();
            }
        });

        root.addView(videoContainer);

        // Add some spacing between video and progress bar
        Space space = new Space(activity);
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 32);
        space.setLayoutParams(spaceParams);
        root.addView(space);

        // Progress Bar
        ProgressBar progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax((int) videoProgress.totalDuration);
        progressBar.setProgress((int) videoProgress.playedDuration);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 20));

        // Set red progress bar (only works on Lollipop+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(progressBarColor))); // Netflix red
            progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF"))); // dark grey track
        }

        // Time labels overlay (FrameLayout over progress bar)
        FrameLayout timeOverlay = new FrameLayout(activity);
        timeOverlay.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        timeOverlay.setPadding(0, 8, 0, 8);

        // 0:00 label - aligned left
        TextView startTime = new TextView(activity);
        startTime.setText("0:00");
        startTime.setTextColor(Color.WHITE);
        FrameLayout.LayoutParams startParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        startParams.gravity = Gravity.START;
        timeOverlay.addView(startTime, startParams);

        // Total duration label - aligned right
        TextView endTime = new TextView(activity);
        endTime.setText(formatTime(videoProgress.totalDuration));
        endTime.setTextColor(Color.WHITE);
        FrameLayout.LayoutParams endParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        endParams.gravity = Gravity.END;
        timeOverlay.addView(endTime, endParams);

        // Played duration label - positioned proportionally
        TextView playedTime = new TextView(activity);
        playedTime.setText(formatTime(videoProgress.playedDuration));
        playedTime.setTextColor(Color.WHITE);

        // Calculate played position as % of total
        float playedFraction = (float) videoProgress.playedDuration / (float) videoProgress.totalDuration;
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int horizontalPadding = 48 * 2; // matches root.setPadding(48, 48, 48, 48)
        int progressBarWidth = screenWidth - horizontalPadding;
        int playedX = (int) (progressBarWidth * playedFraction);
        playedX = Math.max(0, playedX - 30); // shift label slightly left

        FrameLayout.LayoutParams playedParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        playedParams.leftMargin = playedX;
        timeOverlay.addView(playedTime, playedParams);


        root.addView(progressBar);

        root.addView(timeOverlay);

        // Update progress bar as video plays
//                    player.addListener(new Player.Listener() {
//                        @Override
//                        public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {
//                            long currentPosition = player.getCurrentPosition();
//                            progressBar.setProgress((int) currentPosition);
//                        }
//                    });

        // Continue Watching Button
        Button continueWatchingButton = new Button(activity);
        continueWatchingButton.setText("Continue Watching");
        continueWatchingButton.setAllCaps(false);
        continueWatchingButton.setElevation(8); // Gives a nice shadow on modern Android

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(0, 24, 0, 0);  // Adds top spacing
        continueWatchingButton.setLayoutParams(buttonParams);
        continueWatchingButton.setPadding(32, 24, 32, 24); // Inner padding

        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setColor(Color.parseColor(buttonBgColor)); // Red background
        buttonBackground.setCornerRadius(24); // Rounded corners
        continueWatchingButton.setBackground(buttonBackground);
        continueWatchingButton.setTextColor(Color.WHITE); // White text
        continueWatchingButton.setTextSize(16); // A bit larger text
        continueWatchingButton.setTypeface(Typeface.DEFAULT_BOLD);



        root.addView(continueWatchingButton);

        continueWatchingButton.setOnClickListener(v -> {
            context.triggerActionArgument("buttonClick", activity);
            String clickUrl = "app://p1";

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(clickUrl));

            // Check if there's an app to handle the intent
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            }

            player.release();
            dialog.dismiss();
            context.setDismissed();
        });

        dialog.setContentView(root);

        dialog.setOnDismissListener(dialogInterface -> {
            player.release();
            context.setDismissed();
            Log.d("CleverTap", "Dialog dismissed");
        });

        dialog.show();
        Log.d("CleverTap", "Bottom sheet shown");

        context.setPresented();  // Always call after showing UI
    }

    private String formatTime(long millis) {
        int totalSeconds = (int) (millis / 1000);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
    }



    @Override
    public void onClose(@NonNull CustomTemplateContext.TemplateContext context) {
        Log.d("CleverTap", "onClose: called");
        context.setDismissed(); // Just in case any clean-up is needed
    }

    @OptIn(markerClass = UnstableApi.class)
    private void showBottomInApp(Activity activity, CustomTemplateContext.TemplateContext context) {
        String mediaURL = context.getString("MediaURL");
        String bodyURL = context.getString("BodyURL");
        String content = context.getString("Content");
        String btn1_Text = context.getString("btn1_Text");
        String btn2_Text = context.getString("btn2_Text");

        BottomSheetDialog dialog = new BottomSheetDialog(activity);

        // Root Layout
        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(48, 48, 48, 48);
        root.setBackgroundColor(Color.parseColor("#222222"));
        root.setGravity(Gravity.CENTER);

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.parseColor("#222222"));
        background.setCornerRadii(new float[]{40, 40, 40, 40, 0, 0, 0, 0});
        root.setBackground(background);

        ExoPlayer player = new ExoPlayer.Builder(activity).build();

        View mediaView;

        if (mediaURL.endsWith(".mp4")) {
            player.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
            player.setVolume(0f); // Start muted
            player.setMediaItem(MediaItem.fromUri(mediaURL));
            player.prepare();
            player.play();

            // Create the player view
            PlayerView playerView = new PlayerView(activity);
            playerView.setPlayer(player);
            playerView.setUseController(false); // Hide default controls
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM); // Center crop effect
            playerView.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            ));

            // Create container
            FrameLayout videoContainer = new FrameLayout(activity);
            videoContainer.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, 500));
            videoContainer.addView(playerView);

            // Mute Button
            ImageButton muteButton = new ImageButton(activity);
            muteButton.setImageResource(android.R.drawable.ic_lock_silent_mode);
            muteButton.setBackgroundColor(Color.TRANSPARENT);
            FrameLayout.LayoutParams muteParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            muteParams.gravity = Gravity.END | Gravity.BOTTOM;
            muteParams.setMargins(25, 25, 25, 0);
            muteButton.setLayoutParams(muteParams);
            videoContainer.addView(muteButton);

            // Close Button
            ImageButton closeButton = new ImageButton(activity);
            closeButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            closeButton.setBackgroundColor(Color.TRANSPARENT);
            FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            closeParams.gravity = Gravity.END | Gravity.TOP;
            closeParams.setMargins(25, 0, 25, 25);
            closeButton.setLayoutParams(closeParams);
            videoContainer.addView(closeButton);

            // Play/Pause on container click
            final boolean[] isMuted = {true};
            final boolean[] isPlaying = {true};

            muteButton.setOnClickListener(v -> {
                isMuted[0] = !isMuted[0];
                player.setVolume(isMuted[0] ? 0f : 1f);
                muteButton.setImageResource(isMuted[0]
                        ? android.R.drawable.ic_lock_silent_mode
                        : android.R.drawable.ic_lock_silent_mode_off);
            });

            videoContainer.setOnClickListener(v -> {
                isPlaying[0] = !isPlaying[0];
                if (isPlaying[0]) {
                    player.play();
                } else {
                    player.pause();
                }
            });

            closeButton.setOnClickListener(v -> {
                player.release();
                context.setDismissed();
                dialog.dismiss();
            });

            mediaView = videoContainer;

        } else {
            // Image handling
            ImageView imageView = new ImageView(activity);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 500);
            imageView.setLayoutParams(imgParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Glide.with(activity)
                    .load(mediaURL)
                    .centerCrop()
                    .into(imageView);

            imageView.setOnClickListener(v -> {
                if (bodyURL != null && !bodyURL.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bodyURL));
                    if (intent.resolveActivity(activity.getPackageManager()) != null) {
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity, "No app found to handle the URL", Toast.LENGTH_SHORT).show();
                    }
                    context.setDismissed();
                    dialog.dismiss();
                }
            });

            mediaView = imageView;
        }

        root.addView(mediaView);

        // Content Text
        TextView textView = new TextView(activity);
        textView.setText(content);
        textView.setTextSize(18);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 48, 0, 24);
        root.addView(textView);

        // YES Button
        Button btnYes = new Button(activity);
        btnYes.setText(btn1_Text);
        btnYes.setAllCaps(false);
        root.addView(btnYes);

        // NO Button
        Button btnNo = new Button(activity);
        btnNo.setText(btn2_Text);
        btnNo.setAllCaps(false);
        root.addView(btnNo);

        // Button click listeners
        btnYes.setOnClickListener(v -> {
            Toast.makeText(activity, "Thanks for watching!", Toast.LENGTH_SHORT).show();
//            if (btn1_URL != null && !btn1_URL.isEmpty()) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(btn1_URL));
//                if (intent.resolveActivity(activity.getPackageManager()) != null) {
//                    activity.startActivity(intent);
//                } else {
//                    Toast.makeText(activity, "No app found to handle the URL", Toast.LENGTH_SHORT).show();
//                }
//            }
            context.triggerActionArgument("btn1_URL", activity);
            player.release();
            context.setDismissed();
            dialog.dismiss();
        });

        btnNo.setOnClickListener(v -> {
            Toast.makeText(activity, "Check it out soon!", Toast.LENGTH_SHORT).show();
//            if (btn2_URL != null && !btn2_URL.isEmpty()) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(btn2_URL));
//                if (intent.resolveActivity(activity.getPackageManager()) != null) {
//                    activity.startActivity(intent);
//                } else {
//                    Toast.makeText(activity, "No app found to handle the URL", Toast.LENGTH_SHORT).show();
//                }
//            }
            context.triggerActionArgument("btn2_URL", activity);
            player.release();
            context.setDismissed();
            dialog.dismiss();
        });

        dialog.setContentView(root);
//        dialog.setCanceledOnTouchOutside(true);

        dialog.setOnDismissListener(dialogInterface -> {
            player.release();
            context.setDismissed();
            Log.d("CleverTap", "Dialog dismissed by outside touch or programmatically");
        });

        dialog.show();
        Log.d("CleverTap", "showUI: Bottom sheet shown");

        context.setPresented();  // Always call after showing UI

    }


    private void showFloatingBanner(Activity activity, CustomTemplateContext.TemplateContext context) {
        String mediaUrl = context.getString("MediaURL");

        FrameLayout rootLayout = activity.findViewById(android.R.id.content);

        // Container
        FrameLayout bannerContainer = new FrameLayout(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(400, 400);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.setMargins(0, 0, 128, 216); // Margin from right & bottom
        bannerContainer.setLayoutParams(params);
        bannerContainer.setBackgroundColor(Color.TRANSPARENT);
        bannerContainer.setElevation(10f);
        bannerContainer.setPadding(0, 0, 0, 0);

        // --- IMAGE VIEW ---
        ImageView bannerImage = new ImageView(activity);
        bannerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        bannerImage.setLayoutParams(imageParams);
        bannerImage.setPadding(0, 0, 0, 0);

        int cornerRadiusInPx = (int) (12 * activity.getResources().getDisplayMetrics().density);
        Glide.with(activity)
                .load(mediaUrl)
                .transform(new RoundedCorners(cornerRadiusInPx))
                .into(bannerImage);

        bannerImage.setOnClickListener(v -> {
            context.triggerActionArgument("BodyURL", activity);
            context.setDismissed();
            rootLayout.removeView(bannerContainer);
        });

        // Close Button (custom image, circular background, top-right relative to container)
        ImageButton closeButton = new ImageButton(activity);
        Glide.with(activity)
                .load("https://cdn-icons-png.flaticon.com/512/665/665304.png")
                .into(closeButton);

        int sizeInPx = (int) (30 * activity.getResources().getDisplayMetrics().density);
        FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(sizeInPx, sizeInPx);
        closeParams.gravity = Gravity.END | Gravity.TOP;
        closeParams.setMargins(0, 10, 10, 0);
        closeButton.setLayoutParams(closeParams);

        GradientDrawable closeBg = new GradientDrawable();
        closeBg.setColor(0x80FFFFFF); // 50% opacity white
        closeBg.setCornerRadius(12);
        closeButton.setBackground(closeBg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            closeButton.setClipToOutline(true);
            closeButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }

        closeButton.setOnClickListener(v -> {
            context.setDismissed();
            rootLayout.removeView(bannerContainer);
        });

        // Add views in order
        bannerContainer.addView(bannerImage);
        bannerContainer.addView(closeButton);
        rootLayout.addView(bannerContainer);

        context.setPresented();  // Always call after showing UI

    }

    @OptIn(markerClass = UnstableApi.class)
    private void showOTTPIP(Activity activity, CustomTemplateContext.TemplateContext context) {
        String mediaUrl = context.getString("MediaURL");
        String orientation = context.getString("Orientation");
        int width = 0;
        int height = 0;


        if ("Portrait".equalsIgnoreCase(orientation)) {
            width = 350;
            height = 550;
        } else if ("Landscape".equalsIgnoreCase(orientation)) {
            width = 550;
            height = 350;
        } else {
            Log.d("CleverTap", "Empty values in Width and Height");
        }

        FrameLayout rootLayout = activity.findViewById(android.R.id.content);

        // Create container
        FrameLayout pipContainer = new FrameLayout(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.leftMargin = rootLayout.getWidth() - width - 128; // Push it to the right
        params.topMargin = rootLayout.getHeight() - height - 256; // Push it to the bottom
        pipContainer.setLayoutParams(params);
        pipContainer.setBackgroundColor(Color.TRANSPARENT);
        pipContainer.setElevation(10f);

        pipContainer.setOnTouchListener(new View.OnTouchListener() {
            private int lastX, lastY;
            private int startX, startY;
            private long startClickTime;
            private static final int CLICK_DRAG_TOLERANCE = 10; // Tolerance to differentiate between click and drag

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                // Clamp values within screen bounds
                int parentWidth = rootLayout.getWidth();
                int parentHeight = rootLayout.getHeight();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startClickTime = System.currentTimeMillis();
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        startX = layoutParams.leftMargin;
                        startY = layoutParams.topMargin;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;

                        int newLeft = startX + dx;
                        int newTop = startY + dy;

                        newLeft = Math.max(0, Math.min(newLeft, parentWidth - view.getWidth()));
                        newTop = Math.max(0, Math.min(newTop, parentHeight - view.getHeight()));

                        layoutParams.leftMargin = newLeft;
                        layoutParams.topMargin = newTop;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;

                        view.setLayoutParams(layoutParams);
                        return true;

                    case MotionEvent.ACTION_UP:
                        long clickDuration = System.currentTimeMillis() - startClickTime;
                        if (clickDuration < 200 &&
                                Math.abs((int) event.getRawX() - lastX) < CLICK_DRAG_TOLERANCE &&
                                Math.abs((int) event.getRawY() - lastY) < CLICK_DRAG_TOLERANCE) {
                            view.performClick(); // It was a click
                        } else {
                            // It was a drag, so now snap to nearest corner
                            int viewCenterX = layoutParams.leftMargin + view.getWidth() / 2;
                            int viewCenterY = layoutParams.topMargin + view.getHeight() / 2;

                            int targetLeft = 0;
                            int targetTop = 0;

                            // Horizontal position
                            if (viewCenterX < parentWidth / 2) {
                                targetLeft = 64; // some padding from edges
                            } else {
                                targetLeft = parentWidth - view.getWidth() - 64;
                            }

                            // Vertical position
                            if (viewCenterY < parentHeight / 2) {
                                targetTop = 128; // some padding from top
                            } else {
                                targetTop = parentHeight - view.getHeight() - 128;
                            }

                            // Animate to new position with bounce
                            view.animate()
                                    .x(targetLeft)
                                    .y(targetTop)
                                    .setInterpolator(new OvershootInterpolator()) // bounce effect
                                    .setDuration(500) // half second
                                    .start();
                        }
                        return true;

                }
                return false;
            }
        });

        // ExoPlayer setup
        ExoPlayer player = new ExoPlayer.Builder(activity).build();
        player.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
        player.setVolume(0f); // Start muted
        player.setMediaItem(MediaItem.fromUri(mediaUrl));
        player.prepare();
        player.play();

        if (activity instanceof LifecycleOwner) {
            ((LifecycleOwner) activity).getLifecycle().addObserver(new PlayerReleaserObserver(player));
        }

        // Player View
        PlayerView playerView = new PlayerView(activity);
        playerView.setPlayer(player);
        playerView.setUseController(false);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        playerView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        int buttonSizePx = (int) (30 * activity.getResources().getDisplayMetrics().density); // 30dp

        // Mute button
        final boolean[] isMuted = {true};
        ImageButton muteButton = new ImageButton(activity);
        muteButton.setBackgroundColor(Color.TRANSPARENT);
        // Set background color and corner radius
//        GradientDrawable muteBg = new GradientDrawable();
//        muteBg.setColor(0x20FFFFFF); // 12.5% transparent white
//        muteBg.setCornerRadius(12); // Rounded corners
//        muteButton.setBackground(muteBg);

        // Set fixed size for mute button (for example 40dp x 40dp)
//        int muteButtonSize = (int) (30 * activity.getResources().getDisplayMetrics().density); // 40dp to pixels
        FrameLayout.LayoutParams muteParams = new FrameLayout.LayoutParams(
                buttonSizePx,
                buttonSizePx
        );

        muteParams.gravity = Gravity.END | Gravity.BOTTOM;
        muteParams.setMargins(0, 0, 16, 16); // Margin from bottom-right corner
        muteButton.setLayoutParams(muteParams);
        muteButton.setPadding(10, 10, 10, 10);

        // Important: Scale the image properly
        muteButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        muteButton.setAdjustViewBounds(true);

        RequestOptions requestOptions = new RequestOptions()
                .transform(new ColorFilterTransformation(Color.WHITE));

        // Load initial mute icon (muted state)
        Glide.with(activity)
                .load("https://cdn-icons-png.flaticon.com/512/561/561106.png")
                .apply(requestOptions)
                .into(muteButton);

        muteButton.setOnClickListener(v -> {
            isMuted[0] = !isMuted[0];
            player.setVolume(isMuted[0] ? 0f : 1f);

            String iconUrl = isMuted[0] ?
                    "https://cdn-icons-png.flaticon.com/512/561/561106.png" : // muted
                    "https://cdn-icons-png.flaticon.com/512/608/608417.png";  // unmuted

            // Load the appropriate icon
            Glide.with(activity)
                    .load(iconUrl)
                    .apply(requestOptions)
                    .into(muteButton);
        });

        // Tap to pause/play and open link
        FrameLayout videoContainer = new FrameLayout(activity);
        videoContainer.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        // Apply rounded background
        GradientDrawable videoBg = new GradientDrawable();
        videoBg.setColor(Color.BLACK); // or Color.TRANSPARENT
        videoBg.setCornerRadius(24f); // Example radius
        videoContainer.setBackground(videoBg);
        videoContainer.setClipToOutline(true);

        videoContainer.addView(playerView);
        videoContainer.addView(muteButton);

        final boolean[] isPlaying = {true};
        // Handle click for play/pause
        pipContainer.setOnClickListener(v -> {
//            isPlaying[0] = !isPlaying[0];
//            if (isPlaying[0]) {
//                player.play();
//            } else {
//                player.pause();
//            }
            context.triggerActionArgument("BodyURL", activity);
            player.release();
            context.setDismissed();
            rootLayout.removeView(pipContainer);
        });

        // Close button
        ImageButton closeButton = new ImageButton(activity);
        closeButton.setBackgroundColor(Color.TRANSPARENT);

//        int sizeInPx = (int) (30 * activity.getResources().getDisplayMetrics().density);
        FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(buttonSizePx, buttonSizePx);
        closeParams.gravity = Gravity.END | Gravity.TOP;
        closeParams.setMargins(0, 16, 16, 0);
        closeButton.setLayoutParams(closeParams);
        closeButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        closeButton.setAdjustViewBounds(true);
        closeButton.setPadding(18, 18, 18, 18);


//        GradientDrawable closeBg = new GradientDrawable();
//        closeBg.setColor(0x20FFFFFF); // more transparent white (12.5% opacity)
//        closeBg.setCornerRadius(12);
//        closeButton.setBackground(closeBg);

        Glide.with(activity)
                .load("https://cdn-icons-png.flaticon.com/512/1828/1828778.png") // This is a black 'x'
                .apply(requestOptions)
                .into(closeButton);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            closeButton.setClipToOutline(true);
//            closeButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        }

        closeButton.setOnClickListener(v -> {
            player.release();
            context.setDismissed();
            rootLayout.removeView(pipContainer);
        });

        pipContainer.addView(videoContainer);
        pipContainer.addView(closeButton);
        rootLayout.addView(pipContainer);

        context.setPresented();  // Always call after showing UI

    }

    @OptIn(markerClass = UnstableApi.class)
    private void showECommercePIP(Activity activity, CustomTemplateContext.TemplateContext context) {
        String mediaUrl = context.getString("MediaURL");
        String orientation = context.getString("Orientation");
        int width = 0;
        int height = 0;


        if ("Portrait".equalsIgnoreCase(orientation)) {
            width = 350;
            height = 550;
        } else if ("Landscape".equalsIgnoreCase(orientation)) {
            width = 550;
            height = 350;
        } else {
            Log.d("CleverTap", "Empty values in Width and Height");
        }


        FrameLayout rootLayout = activity.findViewById(android.R.id.content);

        // Create container
        FrameLayout pipContainer = new FrameLayout(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.leftMargin = rootLayout.getWidth() - width - 128; // Push it to the right
        params.topMargin = rootLayout.getHeight() - height - 256; // Push it to the bottom
        pipContainer.setLayoutParams(params);
        pipContainer.setBackgroundColor(Color.TRANSPARENT);
        pipContainer.setElevation(10f);
        pipContainer.setOnTouchListener(createDragTouchListener(rootLayout));


        // ExoPlayer setup
        ExoPlayer player = new ExoPlayer.Builder(activity).build();
        player.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
        player.setVolume(0f); // Start muted
        player.setMediaItem(MediaItem.fromUri(mediaUrl));
        player.prepare();
        player.play();

        if (activity instanceof LifecycleOwner) {
            ((LifecycleOwner) activity).getLifecycle().addObserver(new PlayerReleaserObserver(player));
        }

        // Player View
        PlayerView playerView = new PlayerView(activity);
        playerView.setPlayer(player);
        playerView.setUseController(false);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        playerView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        int mutebuttonSizePx = (int) (32 * activity.getResources().getDisplayMetrics().density); // 30dp

        // Mute button
        final boolean[] isMuted = {true};
        ImageButton muteButton = new ImageButton(activity);

        // Create circular black transparent background
        GradientDrawable muteBg = new GradientDrawable();
        muteBg.setShape(GradientDrawable.OVAL);
        muteBg.setColor(Color.parseColor("#26000000")); // 40% transparent black (#66 = 40% alpha)
        muteButton.setBackground(muteBg);

        FrameLayout.LayoutParams muteParams = new FrameLayout.LayoutParams(
                mutebuttonSizePx,
                mutebuttonSizePx
        );

        muteParams.gravity = Gravity.END | Gravity.BOTTOM;
        muteParams.setMargins(0, 0, 16, 16); // Margin from bottom-right corner
        muteButton.setLayoutParams(muteParams);
        muteButton.setPadding(15, 15, 15, 15);

        // Important: Scale the image properly
        muteButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        muteButton.setAdjustViewBounds(true);

        RequestOptions requestOptions = new RequestOptions()
                .transform(new ColorFilterTransformation(Color.WHITE));

        // Load initial mute icon (muted state)
        Glide.with(activity)
                .load("https://cdn-icons-png.flaticon.com/512/561/561106.png")
                .apply(requestOptions)
                .into(muteButton);

        muteButton.setOnClickListener(v -> {
            isMuted[0] = !isMuted[0];
            player.setVolume(isMuted[0] ? 0f : 1f);

            String iconUrl = isMuted[0] ?
                    "https://cdn-icons-png.flaticon.com/512/561/561106.png" : // muted
                    "https://cdn-icons-png.flaticon.com/512/608/608417.png";  // unmuted

            // Load the appropriate icon
            Glide.with(activity)
                    .load(iconUrl)
                    .apply(requestOptions)
                    .into(muteButton);
        });

        // Tap to pause/play and open link
        FrameLayout videoContainer = new FrameLayout(activity);
        videoContainer.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        // Apply rounded background
        GradientDrawable videoBg = new GradientDrawable();
        videoBg.setColor(Color.BLACK); // or Color.TRANSPARENT
        videoBg.setCornerRadius(24f); // Example radius
        videoContainer.setBackground(videoBg);
        videoContainer.setClipToOutline(true);

        videoContainer.addView(playerView);
        videoContainer.addView(muteButton);

        final boolean[] isPlaying = {true};
        // Handle click for play/pause
        pipContainer.setOnClickListener(v -> {
            isPlaying[0] = !isPlaying[0];
            if (isPlaying[0]) {
                player.play();
            } else {
                player.pause();
            }
        });

        // Close button
        ImageButton closeButton = new ImageButton(activity);

        // Create circular black transparent background
        GradientDrawable closeBg = new GradientDrawable();
        closeBg.setShape(GradientDrawable.OVAL);
        closeBg.setColor(Color.parseColor("#23000000")); // 40% transparent black
        closeButton.setBackground(closeBg);

        int closebuttonSizePx = (int) (30 * activity.getResources().getDisplayMetrics().density); // 30dp

        FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(closebuttonSizePx, closebuttonSizePx);
        closeParams.gravity = Gravity.END | Gravity.TOP;
        closeParams.setMargins(0, 16, 16, 0);
        closeButton.setLayoutParams(closeParams);

        closeButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        closeButton.setAdjustViewBounds(true);
        closeButton.setPadding(21, 21, 21, 21);

        Glide.with(activity)
                .load("https://cdn-icons-png.flaticon.com/512/1828/1828778.png") // This is a black 'x'
                .apply(requestOptions)
                .into(closeButton);

        closeButton.setOnClickListener(v -> {
            player.release();
            context.setDismissed();
            rootLayout.removeView(pipContainer);
        });

        int fullscreenbuttonSizePx = (int) (32 * activity.getResources().getDisplayMetrics().density); // 30dp

        // Full Screen button
        ImageButton fullscreenButton = new ImageButton(activity);

        // Create circular black transparent background
        GradientDrawable fullscreenBg = new GradientDrawable();
        fullscreenBg.setShape(GradientDrawable.OVAL);
        fullscreenBg.setColor(Color.parseColor("#26000000")); // 40% transparent black (#66 = 40% alpha)
        fullscreenButton.setBackground(fullscreenBg);

        FrameLayout.LayoutParams fullscreenParams = new FrameLayout.LayoutParams(
                fullscreenbuttonSizePx,
                fullscreenbuttonSizePx
        );

        fullscreenParams.gravity = Gravity.START | Gravity.BOTTOM;
        fullscreenParams.setMargins(16, 0, 0, 16); // Margin from bottom-right corner
        fullscreenButton.setLayoutParams(fullscreenParams);
        fullscreenButton.setPadding(15, 15, 15, 15);

        // Important: Scale the image properly
        fullscreenButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        fullscreenButton.setAdjustViewBounds(true);

        Glide.with(activity)
                .load("https://cdn-icons-png.flaticon.com/512/8839/8839253.png")
                .apply(requestOptions)
                .into(fullscreenButton);


        // Declare once at the top (or in onCreate/init)
        final Button shopNowButton = new Button(activity);
        shopNowButton.setText("Shop Now");
        shopNowButton.setTextColor(Color.BLACK);
        shopNowButton.setAllCaps(false);
        shopNowButton.setTextSize(18f);

        // Round background
        GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(56f);
        bg.setColor(Color.WHITE);
        shopNowButton.setBackground(bg);

        // Layout params
        int buttonWidthDp = 150; // Example width in dp
        float density = activity.getResources().getDisplayMetrics().density;
        int buttonWidthPx = (int) (buttonWidthDp * density);

        FrameLayout.LayoutParams shopparams = new FrameLayout.LayoutParams(
                buttonWidthPx,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        shopparams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        shopparams.setMargins(0, 0, 0, 48);
        shopNowButton.setLayoutParams(shopparams);

        // Add OnClickListener for the Shop Now button
        shopNowButton.setOnClickListener(v -> {
            // Your action when the button is clicked
            Toast.makeText(activity, "Shop Now clicked!", Toast.LENGTH_SHORT).show();
            context.triggerActionArgument("ButtonURL", activity);
            player.release();
            rootLayout.removeView(pipContainer);
            context.setDismissed();
            // You can replace this with your actual action, like opening a shopping page.
        });

        // Initially hidden
        shopNowButton.setVisibility(View.GONE);

        // Add it to the container once
        pipContainer.addView(shopNowButton);

        final boolean[] isFullScreen = {false}; // Track fullscreen state

        fullscreenButton.setOnClickListener(v -> {
            if (!isFullScreen[0]) {
                // Enter Fullscreen
                FrameLayout.LayoutParams fullScreenParams = (FrameLayout.LayoutParams) pipContainer.getLayoutParams();
                fullScreenParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
                fullScreenParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
                fullScreenParams.leftMargin = 0;
                fullScreenParams.topMargin = 100;
                pipContainer.setLayoutParams(params);

                pipContainer.setTranslationX(0);
                pipContainer.setTranslationY(0);

                pipContainer.setOnTouchListener(null);

                // Change fullscreen button icon to "exit fullscreen" icon
                Glide.with(activity)
                        .load("https://cdn-icons-png.flaticon.com/512/3502/3502504.png")
                        .apply(requestOptions)
                        .into(fullscreenButton);

                // 👉 Increase button sizes when entering fullscreen
                int biggerButtonSizePx = (int) (56 * activity.getResources().getDisplayMetrics().density); // e.g., 56dp

                // Update fullscreenButton size
                FrameLayout.LayoutParams fullscreenButtonParams = new FrameLayout.LayoutParams(
                        biggerButtonSizePx,
                        biggerButtonSizePx
                );
                fullscreenButtonParams.gravity = Gravity.START | Gravity.BOTTOM;
                fullscreenButtonParams.setMargins(16, 0, 0, 16);
                fullscreenButton.setLayoutParams(fullscreenButtonParams);
                fullscreenButton.setPadding(30, 30, 30, 30);

                // Similarly update muteButton and closeButton if needed
                FrameLayout.LayoutParams muteButtonParams = new FrameLayout.LayoutParams(
                        biggerButtonSizePx,
                        biggerButtonSizePx
                );
                muteButtonParams.gravity = Gravity.END | Gravity.BOTTOM;
                muteButtonParams.setMargins(0, 0, 16, 16);
                muteButton.setLayoutParams(muteButtonParams);
                muteButton.setPadding(30, 30, 30, 30);

                FrameLayout.LayoutParams closeButtonParams = new FrameLayout.LayoutParams(
                        biggerButtonSizePx,
                        biggerButtonSizePx
                );
                closeButtonParams.gravity = Gravity.END | Gravity.TOP;
                closeButtonParams.setMargins(0, 16, 16, 0);
                closeButton.setLayoutParams(closeButtonParams);
                closeButton.setPadding(40, 40, 40, 40);

                shopNowButton.setVisibility(View.VISIBLE);

                isFullScreen[0] = true;
            } else {
                int originalWidthPx = 350;
                int originalHeightPx = 550;

                // Exiting Fullscreen
                FrameLayout.LayoutParams normalParams = new FrameLayout.LayoutParams(
                        originalWidthPx, // whatever your original width was
                        originalHeightPx  // whatever your original height was
                );
                normalParams.leftMargin = rootLayout.getWidth() - originalWidthPx - 128; // Push it to the right
                normalParams.topMargin = rootLayout.getHeight() - originalHeightPx - 256; // Push it to the bottom
                pipContainer.setLayoutParams(normalParams);

                pipContainer.setOnTouchListener(createDragTouchListener(rootLayout));


                // Change fullscreen button icon back to "enter fullscreen" icon
                Glide.with(activity)
                        .load("https://cdn-icons-png.flaticon.com/512/8839/8839253.png") // your original icon
                        .apply(requestOptions)
                        .into(fullscreenButton);

                // 👉 Reset button sizes to normal
                int normalButtonSizePx = (int) (32 * activity.getResources().getDisplayMetrics().density); // e.g., 40dp

                FrameLayout.LayoutParams fullscreenButtonParams = new FrameLayout.LayoutParams(
                        normalButtonSizePx,
                        normalButtonSizePx
                );
                fullscreenButtonParams.gravity = Gravity.START | Gravity.BOTTOM;
                fullscreenButtonParams.setMargins(16, 0, 0, 16);
                fullscreenButton.setLayoutParams(fullscreenButtonParams);
                fullscreenButton.setPadding(10, 10, 10, 10);

                FrameLayout.LayoutParams muteButtonParams = new FrameLayout.LayoutParams(
                        normalButtonSizePx,
                        normalButtonSizePx
                );
                muteButtonParams.gravity = Gravity.END | Gravity.BOTTOM;
                muteButtonParams.setMargins(0, 0, 16, 16);
                muteButton.setLayoutParams(muteButtonParams);
                muteButton.setPadding(10, 10, 10, 10);

                FrameLayout.LayoutParams closeButtonParams = new FrameLayout.LayoutParams(
                        normalButtonSizePx,
                        normalButtonSizePx
                );
                closeButtonParams.gravity = Gravity.END | Gravity.TOP;
                closeButtonParams.setMargins(0, 16, 16, 0);
                closeButton.setLayoutParams(closeButtonParams);
                closeButton.setPadding(21, 21, 21, 21);

                shopNowButton.setVisibility(View.GONE);

                isFullScreen[0] = false;

            }
        });


//        pipContainer.setOnTouchListener(new View.OnTouchListener() {
//            private int lastX, lastY;
//            private int startX, startY;
//            private long startClickTime;
//            private static final int CLICK_DRAG_TOLERANCE = 10; // Tolerance to differentiate between click and drag
//
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
//                // Clamp values within screen bounds
//                int parentWidth = rootLayout.getWidth();
//                int parentHeight = rootLayout.getHeight();
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        startClickTime = System.currentTimeMillis();
//                        lastX = (int) event.getRawX();
//                        lastY = (int) event.getRawY();
//                        startX = layoutParams.leftMargin;
//                        startY = layoutParams.topMargin;
//                        return true;
//
//                    case MotionEvent.ACTION_MOVE:
//                        int dx = (int) event.getRawX() - lastX;
//                        int dy = (int) event.getRawY() - lastY;
//
//                        int newLeft = startX + dx;
//                        int newTop = startY + dy;
//
//                        newLeft = Math.max(0, Math.min(newLeft, parentWidth - view.getWidth()));
//                        newTop = Math.max(0, Math.min(newTop, parentHeight - view.getHeight()));
//
//                        layoutParams.leftMargin = newLeft;
//                        layoutParams.topMargin = newTop;
//                        layoutParams.rightMargin = 0;
//                        layoutParams.bottomMargin = 0;
//
//                        view.setLayoutParams(layoutParams);
//                        return true;
//
//                    case MotionEvent.ACTION_UP:
//                        long clickDuration = System.currentTimeMillis() - startClickTime;
//                        if (clickDuration < 200 &&
//                                Math.abs((int) event.getRawX() - lastX) < CLICK_DRAG_TOLERANCE &&
//                                Math.abs((int) event.getRawY() - lastY) < CLICK_DRAG_TOLERANCE) {
//                            view.performClick(); // It was a click
//                        } else {
//                            // It was a drag, so now snap to nearest corner
//                            int viewCenterX = layoutParams.leftMargin + view.getWidth() / 2;
//                            int viewCenterY = layoutParams.topMargin + view.getHeight() / 2;
//
//                            int targetLeft = 0;
//                            int targetTop = 0;
//
//                            // Horizontal position
//                            if (viewCenterX < parentWidth / 2) {
//                                targetLeft = 64; // some padding from edges
//                            } else {
//                                targetLeft = parentWidth - view.getWidth() - 64;
//                            }
//
//                            // Vertical position
//                            if (viewCenterY < parentHeight / 2) {
//                                targetTop = 128; // some padding from top
//                            } else {
//                                targetTop = parentHeight - view.getHeight() - 128;
//                            }
//
//                            // Animate to new position with bounce
//                            view.animate()
//                                    .x(targetLeft)
//                                    .y(targetTop)
//                                    .setInterpolator(new OvershootInterpolator()) // bounce effect
//                                    .setDuration(500) // half second
//                                    .start();
//                        }
//                        return true;
//                }
//                return false;
//            }
//        });


        pipContainer.addView(videoContainer);
        pipContainer.addView(closeButton);
        pipContainer.addView(fullscreenButton);
        rootLayout.addView(pipContainer);

        context.setPresented();  // Always call after showing UI

    }

    private View.OnTouchListener createDragTouchListener(FrameLayout rootLayout) {
        return new View.OnTouchListener() {
            private int lastX, lastY;
            private int startX, startY;
            private long startClickTime;
            private static final int CLICK_DRAG_TOLERANCE = 10; // Tolerance to differentiate between click and drag

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                int parentWidth = rootLayout.getWidth();
                int parentHeight = rootLayout.getHeight();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startClickTime = System.currentTimeMillis();
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        startX = layoutParams.leftMargin;
                        startY = layoutParams.topMargin;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;

                        int newLeft = startX + dx;
                        int newTop = startY + dy;

                        newLeft = Math.max(0, Math.min(newLeft, parentWidth - view.getWidth()));
                        newTop = Math.max(0, Math.min(newTop, parentHeight - view.getHeight()));

                        layoutParams.leftMargin = newLeft;
                        layoutParams.topMargin = newTop;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;

                        view.setLayoutParams(layoutParams);
                        return true;

                    case MotionEvent.ACTION_UP:
                        long clickDuration = System.currentTimeMillis() - startClickTime;
                        if (clickDuration < 200 &&
                                Math.abs((int) event.getRawX() - lastX) < CLICK_DRAG_TOLERANCE &&
                                Math.abs((int) event.getRawY() - lastY) < CLICK_DRAG_TOLERANCE) {
                            view.performClick(); // It was a click
                        } else {
                            // Snap to nearest corner
                            int viewCenterX = layoutParams.leftMargin + view.getWidth() / 2;
                            int viewCenterY = layoutParams.topMargin + view.getHeight() / 2;

                            int targetLeft, targetTop;

                            if (viewCenterX < parentWidth / 2) {
                                targetLeft = 64; // padding from edges
                            } else {
                                targetLeft = parentWidth - view.getWidth() - 64;
                            }

                            if (viewCenterY < parentHeight / 2) {
                                targetTop = 128; // padding from top
                            } else {
                                targetTop = parentHeight - view.getHeight() - 128;
                            }

                            view.animate()
                                    .x(targetLeft)
                                    .y(targetTop)
                                    .setInterpolator(new OvershootInterpolator()) // bounce effect
                                    .setDuration(500)
                                    .start();
                        }
                        return true;
                }
                return false;
            }
        };
    }

}
