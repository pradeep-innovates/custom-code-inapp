package com.example.androidintegration;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DetailListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DetailCardAdapter adapter;
    List<DetailCardItem> detailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);

        // Get category name from intent
        String category = getIntent().getStringExtra("category");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(category); // Set title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back button
        }

        recyclerView = findViewById(R.id.detailRecyclerView);

        int spanCount = 2; // number of columns
        int spacing = getResources().getDimensionPixelSize(R.dimen.grid_spacing); // 16dp or as you want
        boolean includeEdge = true;

        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        detailList = new ArrayList<>();

        // Load sample data based on category
        if ("HTML In-App".equals(category)) {
            detailList.add(new DetailCardItem("footer 3 sec auto dismiss", "https://example.com/banner", "footer3sec"));
            detailList.add(new DetailCardItem("gif + copy", "https://example.com/carousel", "gifcopy"));
            detailList.add(new DetailCardItem("Floating_Image", "https://example.com/carousel", "Floating_Image"));
            detailList.add(new DetailCardItem("Floating_GIF", "https://example.com/carousel", "Floating_GIF"));
            detailList.add(new DetailCardItem("OTT_Landscape", "https://example.com/carousel", "Floating_Video"));
            detailList.add(new DetailCardItem("ECom_Portrait", "https://example.com/carousel", "Floating_Video_Portrait"));
            detailList.add(new DetailCardItem("Bottom_Banner", "https://example.com/carousel", "Bottom_Banner"));
            detailList.add(new DetailCardItem("Scratch Card", "https://example.com/carousel", "Scratch_Card"));
            detailList.add(new DetailCardItem("ToolTip", "https://example.com/carousel", "Inbox_ToolTip"));
        } else if ("Native In-App".equals(category)) {
            detailList.add(new DetailCardItem("Bottom_InApp", "https://example.com/banner", "Bottom_InApp"));
            detailList.add(new DetailCardItem("Progress_Alert", "https://example.com/carousel", "Progress_Alert"));
            detailList.add(new DetailCardItem("Floating_Image", "https://example.com/carousel", "Floating_Image"));
            detailList.add(new DetailCardItem("Floating_GIF", "https://example.com/carousel", "Floating_GIF"));
            detailList.add(new DetailCardItem("OTT_Landscape", "https://example.com/carousel", "Floating_Video"));
            detailList.add(new DetailCardItem("ECom_Portrait", "https://example.com/carousel", "Floating_Video_Portrait"));
            detailList.add(new DetailCardItem("Bottom_Banner", "https://example.com/carousel", "Bottom_Banner"));
            detailList.add(new DetailCardItem("Scratch Card", "https://example.com/carousel", "Scratch_Card"));
            detailList.add(new DetailCardItem("ToolTip", "https://example.com/carousel", "Inbox_ToolTip"));
            detailList.add(new DetailCardItem("Event Launch", "https://example.com/carousel", "Event_Launch"));
            detailList.add(new DetailCardItem("Sample", "https://example.com/carousel", "Sample"));
            detailList.add(new DetailCardItem("Sample", "https://example.com/carousel", "Sample"));
            detailList.add(new DetailCardItem("Sample", "https://example.com/carousel", "Sample"));
        } else if ("Push Templates".equals(category)) {
            detailList.add(new DetailCardItem("Rich Notification", "https://example.com/rich", "Rich_Notification_Event"));
            detailList.add(new DetailCardItem("Custom Template", "https://example.com/custom", "Custom_Template_Event"));
        }

        adapter = new DetailCardAdapter(this, detailList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Handles back button in toolbar
        return true;
    }
}
