package com.example.employeeattendanceapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.employeeattendanceapp.AdapterViewPager.EditProfileViewPagerAdapter;
import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Model.Response.EmployeeProfileResponse;
import com.example.employeeattendanceapp.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.rishabhharit.roundedimageview.RoundedImageView;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class EditProfileActivity extends AppCompatActivity {

    String TAG = "EditProfileActivity";
    ImageView back, imageLoader;
    AppBarLayout tabsAppbar;
    EditProfileViewPagerAdapter editProfileViewPagerAdapter;
    public static ViewPager viewPager;
    RoundedImageView profileImage;
    public static EmployeeProfileResponse updateProfileModel;
    RelativeLayout menu, notificationsLayout, profileLayout, homeLayout;
    int tab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        menu = findViewById(R.id.rl_menu);
        back = findViewById(R.id.iv_back);
        profileImage = findViewById(R.id.iv_pic);
        viewPager = findViewById(R.id.view_pager);
        tabsAppbar = findViewById(R.id.layout_tabs);
        imageLoader = findViewById(R.id.imageLoader);
        notificationsLayout = findViewById(R.id.rl_notifications);
        homeLayout = findViewById(R.id.rl_home);
        profileLayout = findViewById(R.id.rl_profile);

        /**
         * Method that contains implementation of click listeners
         */
        clickListeners();


        if (getIntent().hasExtra("tab")) {
            tab = getIntent().getExtras().getInt("tab");
        }

        editProfileViewPagerAdapter = new EditProfileViewPagerAdapter(EditProfileActivity.this, getSupportFragmentManager());
        viewPager.setAdapter(editProfileViewPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(tab);
    }

    private void clickListeners() {

        /**
         * back icon click listener
         */
        back.setOnClickListener(view1 -> onBackPressed());

        /**
         * menu icon click listener
         */
        menu.setOnClickListener(view2 -> {
            DialogHandler.getSharedInstance().popmenu(view2, EditProfileActivity.this);
        });

        /**
         * home icon layout click listener
         */
        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        /**
         * notifications layout click listener
         */
        notificationsLayout.setOnClickListener(view13 -> {
            Toast.makeText(this, "Coming Soon!!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(EditProfile2Activity.this, NotificationsActivity.class);
//            startActivity(intent);
        });

        /**
         * wallet layout click listener
         */
        profileLayout.setOnClickListener(view14 -> {
            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
            startActivity(intent);

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateProfileModel = ProfileActivity.employeeProfileModel;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}