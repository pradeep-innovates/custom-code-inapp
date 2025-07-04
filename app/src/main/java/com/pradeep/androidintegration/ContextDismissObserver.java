package com.pradeep.androidintegration;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplateContext;

public class ContextDismissObserver implements DefaultLifecycleObserver {
    private final CustomTemplateContext.TemplateContext context;
    private boolean isDismissed = false;

    public ContextDismissObserver(CustomTemplateContext.TemplateContext context) {
        this.context = context;
        Log.d("CleverTap", "ContextDismissObserver: " + context);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        dismissIfNeeded("onDestroy");
    }

    private void dismissIfNeeded(String from) {
        if (!isDismissed) {
            context.setDismissed();
            isDismissed = true;
            Log.d("CleverTap", "Context dismissed via LifecycleObserver from: " + from);
        }
    }
}
