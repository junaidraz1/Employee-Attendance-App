package com.example.employeeattendanceapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.employeeattendanceapp.AdapterViewPager.ProfileViewPagerAdapter;
import com.example.employeeattendanceapp.DB.DBManager;
import com.example.employeeattendanceapp.DB.DatabaseHelper;
import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Listeners.DialogClickListener;
import com.example.employeeattendanceapp.Manager.ConnectionManager;
import com.example.employeeattendanceapp.Model.Response.EmployeeProfileResponse;
import com.example.employeeattendanceapp.R;
import com.example.employeeattendanceapp.Retrofit.apiUtils;
import com.google.android.material.tabs.TabLayout;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class ProfileActivity extends AppCompatActivity {

    String TAG = "ProfileActivity", userId = "";

    public static ViewPager viewPager;
    ImageView img_back;
    TabLayout tabsLayout;
    ProfileViewPagerAdapter pagerTabAdapter;
    TextView titleTv;
    ImageView imageLoader;
    RelativeLayout mainLayout, menuLayout, notificationsLayout, profileLayout, homeLayout, loaderLayout;

    public static EmployeeProfileResponse employeeProfileModel;
    DBManager dbManager;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        notificationsLayout = findViewById(R.id.rl_notifications);
        homeLayout = findViewById(R.id.rl_home);
        profileLayout = findViewById(R.id.rl_profile);
        mainLayout = findViewById(R.id.layout_main_);
        menuLayout = findViewById(R.id.rl_menu);
        viewPager = findViewById(R.id.view_pager);
        img_back = findViewById(R.id.iv_back);
        tabsLayout = findViewById(R.id.tabs);
        titleTv = findViewById(R.id.tv_title);
        loaderLayout = findViewById(R.id.rl_loader);
        imageLoader = findViewById(R.id.imageLoader);

        titleTv.setText("Profile");

        employeeProfileModel = new EmployeeProfileResponse();

        dbManager = new DBManager(this);
        dbManager.open();

        Cursor cursor = dbManager.fetchProfileParams();
        if (cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID));
        }

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
                DialogHandler.getSharedInstance().popmenu(view, ProfileActivity.this)
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
            Toast.makeText(this, "Coming Soon!!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(), NotificationsActivity.class);
//            startActivity(intent);
        });

    }

    private void getEmployeeProfile() {
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(this)) {
            if (!userId.isEmpty()) {
//                animateLoaderIcon();
                Log.e(TAG, "getEmployeeProfile: api call");
                RequestBody requestBody = new FormBody.Builder()
                        .add("UserId", userId)
                        .build();
                Call<EmployeeProfileResponse> call = apiUtils.getAPIService(this).getEmployeeProfile(requestBody);
                call.enqueue(new Callback<EmployeeProfileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<EmployeeProfileResponse> call, @NonNull Response<EmployeeProfileResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            loaderLayout.setVisibility(View.GONE);

                            if (response.body().status != null && response.body().status == 1) {

                                employeeProfileModel = response.body();
                                dbManager.insertProfile(employeeProfileModel);

                                pagerTabAdapter = new ProfileViewPagerAdapter(ProfileActivity.this, getSupportFragmentManager());
                                viewPager.setAdapter(pagerTabAdapter);
                                viewPager.setOffscreenPageLimit(4);
                                Log.e(TAG, "onResponse: working");
                                tabsLayout.setupWithViewPager(viewPager);

                                LinearLayout layout3 = ((LinearLayout) ((LinearLayout) tabsLayout.getChildAt(0)).getChildAt(0));
                                LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) layout3.getLayoutParams();
                                layoutParams3.weight = 1f; // e.g. 0.5f
                                layout3.setLayoutParams(layoutParams3);

                                LinearLayout layout2 = ((LinearLayout) ((LinearLayout) tabsLayout.getChildAt(0)).getChildAt(1));
                                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layout2.getLayoutParams();
                                layoutParams2.weight = 1f; // e.g. 0.5f
                                layout2.setLayoutParams(layoutParams2);

                                LinearLayout layout4 = ((LinearLayout) ((LinearLayout) tabsLayout.getChildAt(0)).getChildAt(2));
                                LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) layout4.getLayoutParams();
                                layoutParams4.weight = 1f; // e.g. 0.5f
                                layout4.setLayoutParams(layoutParams4);

                                LinearLayout layout5 = ((LinearLayout) ((LinearLayout) tabsLayout.getChildAt(0)).getChildAt(3));
                                LinearLayout.LayoutParams layoutParams5 = (LinearLayout.LayoutParams) layout5.getLayoutParams();
                                layoutParams5.weight = 1f; // e.g. 0.5f
                                layout5.setLayoutParams(layoutParams5);

                                LinearLayout layout6 = ((LinearLayout) ((LinearLayout) tabsLayout.getChildAt(0)).getChildAt(4));
                                LinearLayout.LayoutParams layoutParams6 = (LinearLayout.LayoutParams) layout5.getLayoutParams();
                                layoutParams6.weight = 1f; // e.g. 0.5f
                                layout6.setLayoutParams(layoutParams6);

                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<EmployeeProfileResponse> call, @NonNull Throwable t) {
                        loaderLayout.setVisibility(View.GONE);
                        Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());
                    }
                });

            } else {
                loaderLayout.setVisibility(View.GONE);
                Log.e(TAG, "getEmployeeProfile: User id is null");
            }

        } else {
            loaderLayout.setVisibility(View.GONE);
            DialogHandler.getSharedInstance().dialogGeneric1Btn(this, true, "Connection Error", "OK", "Please check your internet connection and try again later", new DialogClickListener() {
                @Override
                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                    alertDialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        employeeProfileModel = dbManager.getProfile();

        if (employeeProfileModel.id == null) {
            getEmployeeProfile();
            Log.e(TAG, "onResume: calling api: ");

        } else {
            if (pagerTabAdapter == null) {
                Log.e(TAG, "onResume: else if working");
                pagerTabAdapter = new ProfileViewPagerAdapter(ProfileActivity.this, getSupportFragmentManager());
                viewPager.setAdapter(pagerTabAdapter);
                viewPager.setOffscreenPageLimit(4);
                tabsLayout.setupWithViewPager(viewPager);
            }
//            LinearLayout layout3 = ((LinearLayout) ((LinearLayout) tabsLayout.getChildAt(0)).getChildAt(0));
//            LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) layout3.getLayoutParams();
//            layoutParams3.weight = 1f; // e.g. 0.5f
//            layout3.setLayoutParams(layoutParams3);
//
//            LinearLayout layout2 = ((LinearLayout) ((LinearLayout) tabsLayout.getChildAt(0)).getChildAt(1));
//            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layout2.getLayoutParams();
//            layoutParams2.weight = 1f; // e.g. 0.5f
//            layout2.setLayoutParams(layoutParams2);
//
//            LinearLayout layout4 = ((LinearLayout) ((LinearLayout) tabsLayout.getChildAt(0)).getChildAt(2));
//            LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) layout4.getLayoutParams();
//            layoutParams4.weight = 1f; // e.g. 0.5f
//            layout4.setLayoutParams(layoutParams4);
//
//            LinearLayout layout5 = ((LinearLayout) ((LinearLayout) tabsLayout.getChildAt(0)).getChildAt(3));
//            LinearLayout.LayoutParams layoutParams5 = (LinearLayout.LayoutParams) layout5.getLayoutParams();
//            layoutParams5.weight = 1f; // e.g. 0.5f
//            layout5.setLayoutParams(layoutParams5);

        }

    }

    /**
     * Method to animate logo as a loader image
     */
    private void animateLoaderIcon() {
        loaderLayout.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(R.drawable.ic_ihclogo)
                .into(imageLoader);
        imageLoader.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.blink_animation));
    }
}