package com.example.employeeattendanceapp.AdapterViewPager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.employeeattendanceapp.Fragment.ViewContactFragment;
import com.example.employeeattendanceapp.Fragment.ViewEducationFragment;
import com.example.employeeattendanceapp.Fragment.ViewExperienceFragment;
import com.example.employeeattendanceapp.Fragment.ViewNextOfKinFragment;
import com.example.employeeattendanceapp.Fragment.ViewPersonalFragment;
import com.example.employeeattendanceapp.R;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class ProfileViewPagerAdapter extends FragmentStatePagerAdapter {

    String TAG = "ProfileViewPagerAdapter";

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.personal_header, R.string.contact_header, R.string.nok_header, R.string.education_header,
            R.string.experience_header};
    private final Context mContext;


    public ProfileViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ViewPersonalFragment.newInstance();
            case 1:
                return ViewContactFragment.newInstance();
            case 2:
                return ViewNextOfKinFragment.newInstance();
            case 3:
                return ViewEducationFragment.newInstance();
            case 4:
                return ViewExperienceFragment.newInstance();
            default:
                return ViewPersonalFragment.newInstance();
        }
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }


    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }


}
