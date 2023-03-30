package com.example.employeeattendanceapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employeeattendanceapp.R;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class LeaveBalanceFragment extends Fragment {

    String TAG = "LeaveBalanceFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leave_balance, container, false);
    }

    /**
     * New Instance
     *
     * @return
     */
    public static LeaveBalanceFragment newInstance() {
        return new LeaveBalanceFragment();
    }
}