package com.example.employeeattendanceapp.AdapterViewPager;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.employeeattendanceapp.Fragment.EditProfileContactFragment;
import com.example.employeeattendanceapp.Fragment.EditProfilePersonalFragment;
import com.example.employeeattendanceapp.R;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class EditProfileViewPagerAdapter extends FragmentPagerAdapter {

    String TAG = "EditProfileViewPagerAdapter";

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.personal_header, R.string.contact_header};
    private final Context mContext;

    public EditProfileViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EditProfilePersonalFragment.newInstance();
            case 1:
                return EditProfileContactFragment.newInstance();
            default:
                return EditProfilePersonalFragment.newInstance();
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
