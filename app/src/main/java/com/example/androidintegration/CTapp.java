package com.example.androidintegration;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.clevertap.android.pushtemplates.PushTemplateNotificationHandler;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.Application;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplate;
import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplatesExtKt;
import com.clevertap.android.sdk.interfaces.NotificationHandler;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;

import java.util.HashMap;


public class CTapp extends Application implements CTPushNotificationListener, android.app.Application.ActivityLifecycleCallbacks {

    private static Context context;
    private static final String TAG = "CleverTap";
    public CleverTapAPI cleverTapDefaultInstance;

    private static Activity currentActivity;


    @Override
    public void onCreate() {
        ActivityLifecycleCallback.register(this);
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        Toast.makeText(this, "onCreate Application Called", Toast.LENGTH_SHORT).show();

        CleverTapAPI.registerCustomInAppTemplates(ctConfig ->
                CustomTemplatesExtKt.templatesSet(
                        new CustomTemplate.TemplateBuilder()
                                .name("Bottom_InApp")  // Unique name
                                .presenter(new MyTemplatePresenter()) // Your presenter
                                .stringArgument("MediaURL", "media_URL")
                                .stringArgument("BodyURL", "body_URL")
                                .stringArgument("Content", "Content")
                                .stringArgument("btn1_Text", "button1")
                                .stringArgument("btn2_Text", "button2")
                                .actionArgument("btn1_URL")
                                .actionArgument("btn2_URL")
                                .build(),
                        new CustomTemplate.TemplateBuilder()
                                .name("Floating_Banner")
                                .presenter(new MyTemplatePresenter())
                                .stringArgument("MediaURL", "media_URL")
                                .actionArgument("BodyURL")
                                .build(),
                        new CustomTemplate.TemplateBuilder()
                                .name("OTT_PIP")
                                .presenter(new MyTemplatePresenter())
                                .stringArgument("MediaURL", "media_URL")
                                .stringArgument("Orientation", "Portrait or Landscape")
                                .actionArgument("BodyURL")
                                .build(),
                        new CustomTemplate.TemplateBuilder()
                                .name("ECommerce_PIP")
                                .presenter(new MyTemplatePresenter())
                                .stringArgument("MediaURL", "media_URL")
                                .stringArgument("Orientation", "Portrait or Landscape")
                                .actionArgument("ButtonURL")
                                .build(),
                        new CustomTemplate.TemplateBuilder()
                                .name("Progress_Alert")
                                .presenter(new MyTemplatePresenter())
                                .stringArgument("videoId","videoId")
                                .stringArgument("progressBarColor", "")
                                .stringArgument("buttonBgColor", "")
                                .actionArgument("buttonClick")
                                .build(),
                        new CustomTemplate.TemplateBuilder()
                                .name("ToolTip")
                                .presenter(new MyTemplatePresenter())
                                .stringArgument("content1", "")
                                .stringArgument("content2", "")
                                .build(),
                        new CustomTemplate.TemplateBuilder()
                                .name("Bottom_Banner")
                                .presenter(new MyTemplatePresenter())
                                .stringArgument("content", "")
                                .stringArgument("buttonName", "")
                                .actionArgument("buttonClick")
                                .build(),
                        new CustomTemplate.TemplateBuilder()
                                .name("Scratch_Card")
                                .presenter(new MyTemplatePresenter())
                                .stringArgument("Company Initial", "")
                                .stringArgument("Company Name", "")
                                .stringArgument("Discount Text", "")
                                .stringArgument("Coupon Code", "")
                                .stringArgument("Button Name", "")
                                .actionArgument("ButtonURL")
                                .build(),
                        new CustomTemplate.FunctionBuilder(false)
                                .name("Trigger_Event")
                                .presenter(new MyFunctionPresenter())
                                .stringArgument("eventName","")
                                .build(),
                        new CustomTemplate.TemplateBuilder()
                                .name("Event_Launch")
                                .presenter(new MyTemplatePresenter())
                                .stringArgument("ImageURL", "")
                                .actionArgument("ImageClick")
                                .build()
                )

        );


//        CleverTapInstanceConfig clevertapAdditionalInstanceConfig = CleverTapInstanceConfig.createInstance(
//                getApplicationContext(),
//                "TEST-W56-ZKK-6K7Z",
//                "TEST-331-650"
//        );

//        clevertapAdditionalInstanceConfig.setEncryptionLevel(CryptHandler.EncryptionLevel.MEDIUM)

        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        // To sync the templates, mark the user profile as test profile
        cleverTapDefaultInstance.syncRegisteredInAppTemplates();

//        initializeSignedCallSDK(cleverTapDefaultInstance);



//        cleverTapDefaultInstance = CleverTapAPI.instanceWithConfig(getApplicationContext() ,clevertapAdditionalInstanceConfig);

        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);     //Set Log level to VERBOSE
        Log.d("firbase1", "inside app: ");
        // Creating a Notification Channel With Sound Support        cleverTapDefaultInstance.setCTPushNotificationListener(this);
        CleverTapAPI.createNotificationChannel(getApplicationContext(), "got", "Game of Thrones", "Game Of Thrones", NotificationManager.IMPORTANCE_MAX, true);

        CleverTapAPI.setNotificationHandler((NotificationHandler) new PushTemplateNotificationHandler());
    }




    @Override
    public void onTerminate() {
        super.onTerminate();
        Toast.makeText(this, "onTerminate Application Called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNotificationClickedPayloadReceived(HashMap<String, Object> hashMap) {
        Log.d("CleverTap", "Notification is clicked in Application: " + hashMap.toString());

        if (hashMap.containsKey("mid")) {
            Log.d("CleverTap", "inside if mid`");
            String deepLink = (String) hashMap.get("mid");

            if (deepLink != null && !deepLink.isEmpty()) {
                Log.d("CleverTap", "Deeplink Called: " + deepLink);

                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainIntent.putExtra("DEEPLINK_URL", deepLink);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(mainIntent);
            }
        } else {
            Log.d("CleverTap", "onNotificationClickedPayloadReceived: inside else");
//            Intent openIntent = new Intent(getApplicationContext(), MainActivity.class);
//            openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            getApplicationContext().startActivity(openIntent);
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
        Log.d("CleverTap", "Activity started: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
        Log.d("CleverTap", "Activity resumed: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        if (currentActivity == activity) {
            currentActivity = null;
        }
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        Log.d(TAG, "onActivityDestroyed Application Called");
    }

//    @Override
//    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
//        Log.d("CleverTap", "onActivityCreated: inside");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            Intent intent = activity.getIntent();
//            if (intent != null) {
//                Log.d("CleverTap", "onActivityCreated: inside intent");
//                NotificationUtils.dismissNotification(intent, getApplicationContext());
//            }
//        }
//    }


}
