package com.example.employeeattendanceapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.employeeattendanceapp.Activity.ProfileActivity;
import com.example.employeeattendanceapp.R;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class ViewNextOfKinFragment extends Fragment {

    String TAG = "ViewNextOfKinFragment";

    TextView tvNOKfName, tvNOKlName, tvNOKRelation, tvNOKCNIC, tvNOKMobNum;

    /**
     * New Instance
     *
     * @return
     */
    public static ViewNextOfKinFragment newInstance() {
        return new ViewNextOfKinFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_nextof_kin, container, false);

        tvNOKfName = view.findViewById(R.id.tv_nokFirstName);
        tvNOKlName = view.findViewById(R.id.tv_nokLastName);
        tvNOKRelation = view.findViewById(R.id.tv_nokRelation);
        tvNOKCNIC = view.findViewById(R.id.tv_nokCnic);
        tvNOKMobNum = view.findViewById(R.id.tv_nokMobileNo);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ProfileActivity.employeeProfileModel != null) {

            if (ProfileActivity.employeeProfileModel.nOKFirstName != null) {
                if (!ProfileActivity.employeeProfileModel.nOKFirstName.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.nOKFirstName.equalsIgnoreCase("null")) {
                    tvNOKfName.setText(ProfileActivity.employeeProfileModel.nOKFirstName);
                } else {
                    tvNOKfName.setText("-");
                }
            } else {
                tvNOKfName.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.nOKLastName != null) {
                if (!ProfileActivity.employeeProfileModel.nOKLastName.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.nOKLastName.equalsIgnoreCase("null")) {
                    tvNOKlName.setText(ProfileActivity.employeeProfileModel.nOKLastName);
                } else {
                    tvNOKlName.setText("-");
                }
            } else {
                tvNOKlName.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.nOKRelation != null) {
                if (!ProfileActivity.employeeProfileModel.nOKRelation.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.nOKRelation.equalsIgnoreCase("null")) {
                    tvNOKRelation.setText(ProfileActivity.employeeProfileModel.nOKRelation);
                } else {
                    tvNOKRelation.setText("-");
                }
            } else {
                tvNOKRelation.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.nOKCNICNumber != null) {
                if (!ProfileActivity.employeeProfileModel.nOKCNICNumber.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.nOKCNICNumber.equalsIgnoreCase("null")) {
                    tvNOKCNIC.setText(ProfileActivity.employeeProfileModel.nOKCNICNumber);
                } else {
                    tvNOKCNIC.setText("-");
                }
            } else {
                tvNOKCNIC.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.nOKCellNumber != null) {
                if (!ProfileActivity.employeeProfileModel.nOKCellNumber.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.nOKCellNumber.equalsIgnoreCase("null")) {
                    tvNOKMobNum.setText(ProfileActivity.employeeProfileModel.nOKCellNumber);
                } else {
                    tvNOKMobNum.setText("-");
                }
            } else {
                tvNOKMobNum.setText("-");
            }
        }

    }
}