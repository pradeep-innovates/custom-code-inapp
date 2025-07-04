package com.pradeep.androidintegration;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.pradeep.androidintegration.spotlights.SpotlightHelper;

import org.json.JSONObject;

import java.util.ArrayList;

public class SpotlightsActivity extends AppCompatActivity implements DisplayUnitListener {
    private static final String TAG = "CleverTap";
    private CleverTapAPI cleverTapDefaultInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spotlights);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        if (cleverTapDefaultInstance != null) {
            cleverTapDefaultInstance.setDisplayUnitListener(this);
            cleverTapDefaultInstance.pushEvent("spotlights_nd");
        }
    }

    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        Log.d(TAG, "onDisplayUnitsLoaded: called inside SpotlightsActivity");
        if (units == null) return;

        for (CleverTapDisplayUnit unit : units) {
            prepareDisplayView(unit);
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
}