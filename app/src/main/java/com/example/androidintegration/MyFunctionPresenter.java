package com.example.androidintegration;

import android.util.Log;

import androidx.annotation.NonNull;

import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplateContext;
import com.clevertap.android.sdk.inapp.customtemplates.FunctionPresenter;

public class MyFunctionPresenter implements FunctionPresenter {
    private static final String TAG = "CleverTap";

    @Override
    public void onPresent(@NonNull CustomTemplateContext.FunctionContext functionContext) {
        Log.d(TAG, "onPresent: FunctionPresenter" + functionContext);

        Log.d(TAG, "Function Name : " + functionContext.getTemplateName());
    }
}
