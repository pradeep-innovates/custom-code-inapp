package com.pradeep.androidintegration;

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
            detailList.add(new DetailCardItem("Disappearing Inapp", "https://example.com/carousel", "disinapp"));
            detailList.add(new DetailCardItem("Footer Survey", "https://example.com/carousel", "footersurvey"));
            detailList.add(new DetailCardItem("Feedback Rating with PlayStore", "https://example.com/carousel", "feedbackrating"));
            detailList.add(new DetailCardItem("Stories", "https://example.com/carousel", "Stories"));
            detailList.add(new DetailCardItem("Scratch Card", "https://example.com/carousel", "scratch1"));
            detailList.add(new DetailCardItem("Memory Flip Game", "https://example.com/carousel", "memoryflip"));
            detailList.add(new DetailCardItem("Timer in-app", "https://example.com/carousel", "Timer In-App"));
            detailList.add(new DetailCardItem("Feedback Campaign", "https://example.com/carousel", "Initiate Feedback"));
            detailList.add(new DetailCardItem("Spin the wheel", "https://example.com/carousel", "Spin It"));
            detailList.add(new DetailCardItem("Copy Coupon Code", "https://example.com/carousel", "Coupon Code"));
            detailList.add(new DetailCardItem("Footer - auto dismiss", "https://example.com/banner", "footer3sec"));
            detailList.add(new DetailCardItem("Gif + Copy code", "https://example.com/carousel", "gifcopy"));
            detailList.add(new DetailCardItem("Carousel", "https://example.com/carousel", "carousel"));
            detailList.add(new DetailCardItem("Gif - Footer", "https://example.com/carousel", "footergif"));
            detailList.add(new DetailCardItem("Gif - Full screen", "https://example.com/carousel", "fullgif"));
            detailList.add(new DetailCardItem("Carousel - footer", "https://example.com/carousel", "carouselfooter"));
            detailList.add(new DetailCardItem("OTT PIP", "https://example.com/carousel", "PIP 1"));
            detailList.add(new DetailCardItem("Ecommerce PIP", "https://example.com/carousel", "PIP 2"));
            detailList.add(new DetailCardItem("Have you watched PIP", "https://example.com/carousel", "PIP 3"));
            detailList.add(new DetailCardItem("Watch Now Template", "https://example.com/carousel", "SONY 1"));
            detailList.add(new DetailCardItem("Quick Quiz", "https://example.com/carousel", "SONY 2"));
            detailList.add(new DetailCardItem("Gender Survey", "https://example.com/carousel", "Video"));
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
            detailList.add(new DetailCardItem("SonyLIV", "https://example.com/carousel", "bot"));
            detailList.add(new DetailCardItem("Event Launch", "https://example.com/carousel", "Event_Launch"));
            detailList.add(new DetailCardItem("Mood Template", "https://example.com/carousel", "Mood_Template"));
            detailList.add(new DetailCardItem("Sample", "https://example.com/carousel", "Sample"));
            detailList.add(new DetailCardItem("Sample 1", "https://example.com/carousel", "Sample1"));
            detailList.add(new DetailCardItem("Sample 2", "https://example.com/carousel", "Sample2"));
            detailList.add(new DetailCardItem("Sample 3", "https://example.com/carousel", "Sample3"));
        } else if ("Push Templates".equals(category)) {
            detailList.add(new DetailCardItem("Basic Template", "https://example.com/rich", "push_basic"));
            detailList.add(new DetailCardItem("Auto Carousel Template", "https://example.com/custom", "push_carousel"));
            detailList.add(new DetailCardItem("Manual Carousel Template", "https://example.com/custom", "push_manual_carousel"));
            detailList.add(new DetailCardItem("Rating Template", "https://example.com/custom", "push_rating"));
            detailList.add(new DetailCardItem("Product Catalog Template", "https://example.com/custom", "push_product_display"));
            detailList.add(new DetailCardItem("Five Icons Template", "https://example.com/custom", "push_five_icons"));
            detailList.add(new DetailCardItem("Timer Template w.r.t Actual Time", "https://example.com/custom", "push_timer"));
            detailList.add(new DetailCardItem("Timer Template in Seconds", "https://example.com/custom", "push_timer_seconds"));
            detailList.add(new DetailCardItem("Zero Bezel Template", "https://example.com/custom", "push_zero_bezel"));
            detailList.add(new DetailCardItem("Input Box Template", "https://example.com/custom", "push_input"));
            detailList.add(new DetailCardItem("GIF Template", "https://example.com/carousel", "push_gif"));
            detailList.add(new DetailCardItem("Sample", "https://example.com/carousel", "Sample"));
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
