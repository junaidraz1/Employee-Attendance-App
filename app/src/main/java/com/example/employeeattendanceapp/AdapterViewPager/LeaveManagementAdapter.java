package com.example.employeeattendanceapp.AdapterViewPager;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.employeeattendanceapp.Fragment.ApplyLeaveFragment;
import com.example.employeeattendanceapp.Fragment.LeaveBalanceFragment;
import com.example.employeeattendanceapp.Fragment.LeaveStatusFragment;
import com.example.employeeattendanceapp.R;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class LeaveManagementAdapter extends FragmentStatePagerAdapter {

    String TAG = "LeaveManagementAdapter";

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.apply_leave, R.string.leave_status};
    private final Context mContext;


    public LeaveManagementAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ApplyLeaveFragment.newInstance();
            case 1:
                return LeaveStatusFragment.newInstance();
//            case 2:
//                return LeaveBalanceFragment.newInstance();
            default:
                return null;
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
