package com.example.employeeattendanceapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeeattendanceapp.AdapterViewPager.LeaveManagementAdapter;
import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.R;
import com.google.android.material.tabs.TabLayout;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */


public class LeavesActivity extends AppCompatActivity {

    String TAG = "LeavesActivity";

    public static ViewPager viewPager;
    ImageView img_back;
    TabLayout tabsLayout;
    TextView tvTitle;
    LeaveManagementAdapter leaveManagementAdapter;
    RelativeLayout mainLayout, menuLayout, notificationsLayout, profileLayout, homeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaves);

        notificationsLayout = findViewById(R.id.rl_notifications);
        homeLayout = findViewById(R.id.rl_home);
        tvTitle = findViewById(R.id.tv_title);
        profileLayout = findViewById(R.id.rl_profile);
        mainLayout = findViewById(R.id.layout_main_);
        menuLayout = findViewById(R.id.rl_menu);
        viewPager = findViewById(R.id.view_pager);
        img_back = findViewById(R.id.iv_back);
        tabsLayout = findViewById(R.id.tabs);

        tvTitle.setText("Leave Information");

        leaveManagementAdapter = new LeaveManagementAdapter(LeavesActivity.this, getSupportFragmentManager());
        viewPager.setAdapter(leaveManagementAdapter);
        tabsLayout.setupWithViewPager(viewPager);

        LinearLayout layout3 = ((LinearLayout) ((LinearLayout) tabsLayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) layout3.getLayoutParams();
        layoutParams3.weight = 1f; // e.g. 0.5f
        layout3.setLayoutParams(layoutParams3);


        LinearLayout layout2 = ((LinearLayout) ((LinearLayout) tabsLayout.getChildAt(0)).getChildAt(1));
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layout2.getLayoutParams();
        layoutParams2.weight = 1f; // e.g. 0.5f
        layout2.setLayoutParams(layoutParams2);


//        LinearLayout layout0 = ((LinearLayout) ((LinearLayout) tabsLayout.getChildAt(0)).getChildAt(2));
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout0.getLayoutParams();
//        layoutParams.weight = 1f; // e.g. 0.5f
//        layout0.setLayoutParams(layoutParams);

        /**
         * Method that contains implementation of click listeners
         */
        clickListeners();
    }

    private void clickListeners() {

        /**
         * back icon click listener
         */
        img_back.setOnClickListener(view ->
                onBackPressed()
        );


        /**
         * menu layout click listener
         */
        menuLayout.setOnClickListener(view ->
                DialogHandler.getSharedInstance().popmenu(view, LeavesActivity.this)
        );


        /**
         * home layout click listener
         */
        homeLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        /**
         * notifications layout click listener
         */
        notificationsLayout.setOnClickListener(view -> {
            Toast.makeText(this, "Coming Soon !!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(), NotificationsActivity.class);
//            startActivity(intent);
        });

        /**
         * profile layout click listener
         */
        profileLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        });
    }
}