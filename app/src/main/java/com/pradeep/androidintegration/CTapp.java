package com.pradeep.androidintegration;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class CTapp extends Application implements CTPushNotificationListener, android.app.Application.ActivityLifecycleCallbacks {

    private static Context context;
    private static final String TAG = "CleverTap";
    public CleverTapAPI cleverTapDefaultInstance;

    private static final boolean REGISTER_CLEVERTAP_LIFECYCLE = true; // Change to false to skip

    private static Activity currentActivity;



    @Override
    public void onCreate() {
//        boolean status = fetchLifecycleStatusBlocking();  // Blocking API call
//        Log.d(TAG, "onCreate: status is " + status);
//
//        if (REGISTER_CLEVERTAP_LIFECYCLE) {
//            CleverTapAPI.changeCredentials(
//                    "TEST-4W5-9RR-646Z",
//                    "TEST-22c-504"
//            );
//
//            CleverTapAPI cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
//            if (cleverTapDefaultInstance != null) {
//                cleverTapDefaultInstance.pushEvent("SDK_Initialized_From_Application_Class");
//            }
//
//            ActivityLifecycleCallback.register(this);
//            Log.d(TAG, "Activity Lifecycle Initiated via Flag");
//        }
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
                                .name("ToolTip_2")
                                .presenter(new MyTemplatePresenter())
                                .stringArgument("title1", "")
                                .stringArgument("title2", "")
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
                                .build(),
                        new CustomTemplate.TemplateBuilder()
                                .name("SonyLIV_dot")
                                .presenter(new MyTemplatePresenter())
                                .stringArgument("Date","")
                                .stringArgument("Time","")
                                .stringArgument("ImageURL","")
                                .stringArgument("BgColor","")
                                .actionArgument("dotClick")
                                .build(),
//                        new CustomTemplate.TemplateBuilder()
//                                .name("Mood_Template")
//                                .presenter(new MyTemplatePresenter())
//                                .stringArgument("Header", "")
//                                .stringArgument("mood1_Text", "")
//                                .stringArgument("mood1_Image", "")
//                                .stringArgument("mood2_Text", "")
//                                .stringArgument("mood2_Image", "")
//                                .stringArgument("mood3_Text", "")
//                                .stringArgument("mood3_Image", "")
//                                .intArgument("delayInSeconds", 3)
//                                .actionArgument("mood1_Link")
//                                .actionArgument("mood2_Link")
//                                .actionArgument("mood3_Link")
//                                .build(),
                        new CustomTemplate.TemplateBuilder()
                                .name("test_template")
                                .presenter(new MyTemplatePresenter())
                                .stringArgument("icon1","")
                                .stringArgument("icon2", "")
                                .actionArgument("action")
                                .build()
                )

        );
//
//
////        CleverTapInstanceConfig clevertapAdditionalInstanceConfig = CleverTapInstanceConfig.createInstance(
////                getApplicationContext(),
////                "TEST-W56-ZKK-6K7Z",
////                "TEST-331-650"
////        );
//
////        clevertapAdditionalInstanceConfig.setEncryptionLevel(CryptHandler.EncryptionLevel.MEDIUM)
//
        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());

        if (cleverTapDefaultInstance != null) {
            // To sync the templates, mark the user profile as test profile
            cleverTapDefaultInstance.syncRegisteredInAppTemplates();
        }


//        cleverTapDefaultInstance.setOptOut(false);



//        initializeSignedCallSDK(cleverTapDefaultInstance);




//        cleverTapDefaultInstance = CleverTapAPI.instanceWithConfig(getApplicationContext() ,clevertapAdditionalInstanceConfig);

        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);     //Set Log level to VERBOSE
        Log.d("firbase1", "inside app: ");
        // Creating a Notification Channel With Sound Support        cleverTapDefaultInstance.setCTPushNotificationListener(this);
        CleverTapAPI.createNotificationChannel(getApplicationContext(), "channel3", "channel3", "channel3", NotificationManager.IMPORTANCE_MAX, true);
        CleverTapAPI.createNotificationChannel(getApplicationContext(), "channel4", "channel4", "channel4", NotificationManager.IMPORTANCE_LOW, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CleverTapAPI.createNotificationChannelGroup(getApplicationContext(),"123","MyGroup1");
            CleverTapAPI.createNotificationChannel(getApplicationContext(),"channel1","channel1","channel1",NotificationManager.IMPORTANCE_MAX,"123",true);
            CleverTapAPI.createNotificationChannelGroup(getApplicationContext(),"456","MyGroup2");
            CleverTapAPI.createNotificationChannel(getApplicationContext(),"channel2","channel2","channel2",NotificationManager.IMPORTANCE_MAX,"456",true);
        }
        CleverTapAPI.setNotificationHandler((NotificationHandler) new PushTemplateNotificationHandler());
    }

    private boolean fetchLifecycleStatusBlocking() {
        boolean status = false;
        try {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);

            Log.d(TAG, "fetchLifecycleStatusBlocking: inside try block");
            URL url = new URL("https://android.free.beeceptor.com/set_flag");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json"); // Expect JSON response
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "fetchLifecycleStatusBlocking: responseCode is " + responseCode);
            if (responseCode == 200) {
                Log.d(TAG, "fetchLifecycleStatusBlocking: response code is 200");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                status = jsonResponse.optBoolean("status", false);
                Log.d(TAG, "fetchLifecycleStatusBlocking: status is: " + status);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred", e);
        }
        return status;
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
        Log.d("CleverTap", "onActivityCreated: Worked");

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            Intent intent = activity.getIntent();
//
//            if (intent != null && intent.getExtras() != null) {
//                Log.d("CleverTap", "Checking if activity was launched from a notification...");
//                NotificationUtils.dismissNotification(intent, getApplicationContext());
//            } else {
//                Log.d("CleverTap", "No notification data found in intent.");
//            }
//        }
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
