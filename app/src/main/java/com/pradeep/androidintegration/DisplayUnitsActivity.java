package com.pradeep.androidintegration;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class DisplayUnitsActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ImageCarouselAdapter imageCarouselAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display_units);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.native_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toast.makeText(this, "onCreate DisplayUnitsActivity Called", Toast.LENGTH_SHORT).show();

        // Get the image URLs or other display unit data passed to the activity
        ArrayList<String> imageUrls = getIntent().getStringArrayListExtra("DISPLAY_UNITS");
        Log.d("CleverTap ", "Image URLs: " + imageUrls);


        // Set up ViewPager2
        viewPager = findViewById(R.id.native_image_carousel);

        // Use the ImageCarouselAdapter to display images
        if (imageUrls != null && !imageUrls.isEmpty()) {
//            imageCarouselAdapter = new ImageCarouselAdapter(this, imageUrls);
            viewPager.setAdapter(imageCarouselAdapter);
        }
    }
}