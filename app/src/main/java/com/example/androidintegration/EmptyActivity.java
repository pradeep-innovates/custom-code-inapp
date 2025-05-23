package com.example.androidintegration;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.clevertap.android.sdk.CleverTapAPI;

public class EmptyActivity extends BaseActivity {

    public CleverTapAPI clevertapDefaultInstance;

    private WebView webView;
    private String midUrl = "https://www.defaulturl.com"; // Default URL if "mid" is missing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_empty);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        Intent nativeIntent = getIntent();
//        String imageUrl = nativeIntent.getStringExtra("imageUrl");
//        int position = nativeIntent.getIntExtra("position", -1); // Default to -1 if not found

//        Log.d("CleverTap", "Received Image URL: " + imageUrl);
//        Log.d("CleverTap", "Received Position: " + position);

//        Button emptyButton = findViewById(R.id.emptyButton);


        // Fetch "mid" from Intent
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra("DEEPLINK_URL")) {
//            midUrl = intent.getStringExtra("DEEPLINK_URL");
//            Log.d("CleverTap", "Received mid URL: " + midUrl);
//        }

        // Initialize WebView
//        webView = findViewById(R.id.webView);

        if (webView != null) { // Ensure WebView is not null
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true); // Enable JavaScript
            webSettings.setDomStorageEnabled(true); // Enable local storage

            webView.setWebViewClient(new WebViewClient());

//            // Set WebViewClient to handle deep links
//            webView.setWebViewClient(new WebViewClient() {
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                    String url = request.getUrl().toString();
//                    Log.d("CleverTap", "URL Clicked: " + url);
//
//                    if (url.startsWith("http") || url.startsWith("https")) {
//                        Log.d("CleverTap", "inside if of http or https : " + url);
//                        return false; // Load normal web URLs in WebView
//                    } else {
//                        Log.d("CleverTap", "Opening in external app : " + url);
//                        // Handle deep links like whatsapp://
//                        openExternalApp(url);
//                        return true; // Prevent WebView from handling it
//                    }
//                }
//            });

            // Inject JavaScript Interface
            webView.addJavascriptInterface(new WebAppInterface(), "Android");

            // Load the HTML page from assets
//            webView.loadUrl(midUrl);
            webView.loadUrl("file:///android_asset/index.html");
        } else {
            Log.e("CleverTap", "WebView is null! Check the XML layout.");
        }

        setupCustomBottomBar("empty"); // or "search", "profile" accordingly
    }

    // JavaScript Interface to handle button click
    public class WebAppInterface {
        @JavascriptInterface
        public void sendCustomEvent(String eventName) {
            clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(EmptyActivity.this);
            if (clevertapDefaultInstance != null) {
                clevertapDefaultInstance.pushEvent(eventName);
                Log.d("CleverTap", "Custom event sent: " + eventName);
            }
        }
//
//        @JavascriptInterface
//        public String getMidUrl() {
//            return midUrl; // Return the "mid" URL to JavaScript
//        }
//
//        @JavascriptInterface
//        public void openMidUrl() {
//            openExternalApp(midUrl);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCustomBottomBar("empty"); // or "search", "profile" accordingly
    }


    // Open deep links in external apps
    private void openExternalApp(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e("CleverTap", "No app found to handle this URL: " + url, e);
        }
    }
}