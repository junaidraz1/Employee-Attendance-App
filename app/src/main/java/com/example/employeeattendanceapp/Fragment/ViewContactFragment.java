package com.example.employeeattendanceapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

public class ViewContactFragment extends Fragment {

    String TAG = "ViewProfileContactFragment";

    TextView country, state, city, address, mobileNo, telephoneNumber, email;
    RelativeLayout editIconLayout;

    DBManager dbManager;


    /**
     * New Instance
     *
     * @return
     */
    public static ViewContactFragment newInstance() {
        return new ViewContactFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_contact, container, false);
        country = view.findViewById(R.id.tv_country);
        state = view.findViewById(R.id.tv_state);
        city = view.findViewById(R.id.tv_city);
        address = view.findViewById(R.id.tv_address);
        mobileNo = view.findViewById(R.id.tv_mobileNo);
        telephoneNumber = view.findViewById(R.id.tv_telephoneNumber);
        email = view.findViewById(R.id.tv_email);
        editIconLayout = view.findViewById(R.id.rl_edit);

        dbManager = new DBManager(requireActivity());
        dbManager.open();

        /**
         * edit icon click listener
         */
        editIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_ = new Intent(getActivity(), EditProfileActivity.class);
                intent_.putExtra("tab", 1);
                startActivity(intent_);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        ProfileActivity.employeeProfileModel = dbManager.getProfile();

        if (ProfileActivity.employeeProfileModel != null) {

            if (ProfileActivity.employeeProfileModel.country != null) {
                if (!ProfileActivity.employeeProfileModel.country.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.country.equalsIgnoreCase("null")) {
                    country.setText(ProfileActivity.employeeProfileModel.country);
                } else {
                    country.setText("-");
                }
            } else {
                country.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.stateOrProvince != null) {
                if (!ProfileActivity.employeeProfileModel.stateOrProvince.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.stateOrProvince.equalsIgnoreCase("null")) {
                    state.setText(ProfileActivity.employeeProfileModel.stateOrProvince);
                } else {
                    state.setText("-");
                }
            } else {
                state.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.city != null) {
                if (!ProfileActivity.employeeProfileModel.city.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.city.equalsIgnoreCase("null")) {
                    city.setText(ProfileActivity.employeeProfileModel.city);

                } else {
                    city.setText("-");
                }
            } else {
                city.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.address != null) {
                if (!ProfileActivity.employeeProfileModel.address.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.address.equalsIgnoreCase("null")) {
                    address.setText(ProfileActivity.employeeProfileModel.address);
                } else {
                    address.setText("-");
                }
            } else {
                address.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.cellNumber != null) {
                if (!ProfileActivity.employeeProfileModel.cellNumber.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.cellNumber.equalsIgnoreCase("null")) {
                    mobileNo.setText(ProfileActivity.employeeProfileModel.cellNumber);
                } else {
                    mobileNo.setText("-");
                }
            } else {
                mobileNo.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.telephoneNumber != null) {
                if (!ProfileActivity.employeeProfileModel.telephoneNumber.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.telephoneNumber.equalsIgnoreCase("null")) {
                    telephoneNumber.setText(ProfileActivity.employeeProfileModel.telephoneNumber);
                } else {
                    telephoneNumber.setText("-");
                }
            } else {
                telephoneNumber.setText("-");
            }

            if (ProfileActivity.employeeProfileModel.email != null) {
                if (!ProfileActivity.employeeProfileModel.email.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.email.equalsIgnoreCase("null")) {
                    email.setText(ProfileActivity.employeeProfileModel.email);
                } else {
                    email.setText("-");
                }
            } else {
                email.setText("-");
            }
        }
    }

}