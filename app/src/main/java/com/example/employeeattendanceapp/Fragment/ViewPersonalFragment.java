package com.example.employeeattendanceapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.employeeattendanceapp.Activity.EditProfileActivity;
import com.example.employeeattendanceapp.Activity.ProfileActivity;
import com.example.employeeattendanceapp.DB.DBManager;
import com.example.employeeattendanceapp.R;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class ViewPersonalFragment extends Fragment {

    String TAG = "ViewProfilePersonalFragment";

    TextView titlePrefix, firstName, middleName, lastName, cnicNo, bloodGroup, gender, age, dob, maritalStatus,
            department;
    RelativeLayout editIconLayout;

    DBManager dbManager;

    /**
     * New Instance
     *
     * @return
     */
    public static ViewPersonalFragment newInstance() {
        return new ViewPersonalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_personal, container, false);

        titlePrefix = view.findViewById(R.id.tv_titlePrefix);
        firstName = view.findViewById(R.id.tv_firstName);
        middleName = view.findViewById(R.id.tv_middleName);
        lastName = view.findViewById(R.id.tv_lastName);
        cnicNo = view.findViewById(R.id.tv_cnic);
        bloodGroup = view.findViewById(R.id.tv_bloodGroup);
        gender = view.findViewById(R.id.tv_gender);
        age = view.findViewById(R.id.tv_age);
        dob = view.findViewById(R.id.tv_dob);
        maritalStatus = view.findViewById(R.id.tv_maritalStatus);
        department = view.findViewById(R.id.tv_department);
        editIconLayout = view.findViewById(R.id.rl_edit);

        dbManager = new DBManager(requireActivity());
        dbManager.open();

        /**
         * edit icon click listener
         */
        editIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("tab", 0);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

//        ProfileActivity.employeeProfileModel = dbManager.getProfile();

        if (ProfileActivity.employeeProfileModel != null) {

            if (ProfileActivity.employeeProfileModel.title != null) {
                if (!ProfileActivity.employeeProfileModel.title.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.title.equalsIgnoreCase("null")) {
                    titlePrefix.setText(ProfileActivity.employeeProfileModel.title);
                } else {
                    titlePrefix.setText("-");
                }
            } else {
                titlePrefix.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.firstName != null) {
                if (!ProfileActivity.employeeProfileModel.firstName.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.firstName.equalsIgnoreCase("null")) {
                    firstName.setText(ProfileActivity.employeeProfileModel.firstName);
                } else {
                    firstName.setText("-");
                }
            } else {
                firstName.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.middleName != null) {
                if (!ProfileActivity.employeeProfileModel.middleName.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.middleName.equalsIgnoreCase("null")) {
                    middleName.setText(ProfileActivity.employeeProfileModel.middleName);

                } else {
                    middleName.setText("-");
                }
            } else {
                middleName.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.lastName != null) {
                if (!ProfileActivity.employeeProfileModel.lastName.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.lastName.equalsIgnoreCase("null")) {
                    lastName.setText(ProfileActivity.employeeProfileModel.lastName);
                } else {
                    lastName.setText("-");
                }
            } else {
                lastName.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.cNICNumber != null) {
                if (!ProfileActivity.employeeProfileModel.cNICNumber.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.cNICNumber.equalsIgnoreCase("null")) {
                    cnicNo.setText(ProfileActivity.employeeProfileModel.cNICNumber);
                } else {
                    cnicNo.setText("-");
                }
            } else {
                cnicNo.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.bloodGroup != null) {
                if (!ProfileActivity.employeeProfileModel.bloodGroup.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.bloodGroup.equalsIgnoreCase("null")) {
                    bloodGroup.setText(ProfileActivity.employeeProfileModel.bloodGroup);
                } else {
                    bloodGroup.setText("-");
                }
            } else {
                bloodGroup.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.gender != null) {
                if (!ProfileActivity.employeeProfileModel.gender.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.gender.equalsIgnoreCase("null")) {
                    gender.setText(ProfileActivity.employeeProfileModel.gender);
                } else {
                    gender.setText("-");
                }
            } else {
                gender.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.age != null) {
                if (!ProfileActivity.employeeProfileModel.age.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.age.equalsIgnoreCase("null")) {
                    age.setText(ProfileActivity.employeeProfileModel.age);
                } else {
                    age.setText("-");
                }
            } else {
                age.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.dateOfBirth != null) {
                if (!ProfileActivity.employeeProfileModel.dateOfBirth.isEmpty()) {
                    String[] separated = ProfileActivity.employeeProfileModel.dateOfBirth.split("T");
                    dob.setText(separated[0]);
                } else {
                    dob.setText("-");
                }
            } else {
                dob.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.maritalStatus != null) {
                if (!ProfileActivity.employeeProfileModel.maritalStatus.isEmpty()) {
                    maritalStatus.setText(ProfileActivity.employeeProfileModel.maritalStatus);
                } else {
                    maritalStatus.setText("-");
                }
            } else {
                maritalStatus.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.department != null) {
                if (!ProfileActivity.employeeProfileModel.department.isEmpty()) {
                    department.setText(ProfileActivity.employeeProfileModel.department);
                } else {
                    department.setText("-");
                }
            } else {
                department.setText("-");
            }
        }
    }
}