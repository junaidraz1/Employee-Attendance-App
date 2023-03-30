package com.example.employeeattendanceapp.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.employeeattendanceapp.Activity.EditProfileActivity;
import com.example.employeeattendanceapp.Activity.ProfileActivity;
import com.example.employeeattendanceapp.AdapterViewPager.ProfileViewPagerAdapter;
import com.example.employeeattendanceapp.DB.DBManager;
import com.example.employeeattendanceapp.DB.DatabaseHelper;
import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Listeners.DialogClickListener;
import com.example.employeeattendanceapp.Manager.ConnectionManager;
import com.example.employeeattendanceapp.Model.BloodGroupData;
import com.example.employeeattendanceapp.Model.DepartmentData;
import com.example.employeeattendanceapp.Model.GenderData;
import com.example.employeeattendanceapp.Model.MaritalStatusData;
import com.example.employeeattendanceapp.Model.Response.BloodGroupResponse;
import com.example.employeeattendanceapp.Model.Response.EmployeeProfileResponse;
import com.example.employeeattendanceapp.Model.Response.GenderResponse;
import com.example.employeeattendanceapp.Model.Response.GetDepartmentResponse;
import com.example.employeeattendanceapp.Model.Response.MaritalStatusResponse;
import com.example.employeeattendanceapp.Model.Response.UpdateProfileResponse;
import com.example.employeeattendanceapp.Model.SubDepartmentData;
import com.example.employeeattendanceapp.Model.TitleData;
import com.example.employeeattendanceapp.Model.Response.TitleResponse;
import com.example.employeeattendanceapp.R;
import com.example.employeeattendanceapp.Retrofit.apiUtils;
import com.google.android.datatransport.cct.internal.LogEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class EditProfilePersonalFragment extends Fragment {

    String TAG = "EditProfilePersonalFragment", userId = "", selectedSpinnerTitle, selectedSpinnerGender, selectedSpinnerMS, selectedSpinnerBG, getDOBValue = "",
            titleId, genderId, maritalStatusId, bloodGroupId, departmentName;

    Spinner spTitle, spGender, spMaritalStatus, spBloodGroup, spDepartment, spSubDepartment;
    RelativeLayout loaderLayout;
    ImageView imageLoader;
    EditText etFirstName, etMiddleName, etLastName;
    TextView tvDob, tvYears, tvMonths, tvDays, tvDepartment;
    Button updateProfileBtn;

    ArrayList<String> titleNameList, genderNameList, maritalStatusNameList, bloodGroupNameList, subDepartmentNameList;
    ArrayList<TitleData> titleIdList;
    ArrayList<GenderData> genderIdList;
    ArrayList<MaritalStatusData> maritalStatusIdList;
    ArrayList<BloodGroupData> bloodGroupIdList;
//    ArrayList<DepartmentData> departmentIdList;
//    ArrayList<SubDepartmentData> subDepartmentIdList;

    ArrayAdapter<String> titleAdapter, genderAdapter, maritalStatusAdapter, bloodGroupAdapter, departmentAdapter;

    int spinnerPosition, year, month, day;

    DBManager dbManager;

    /**
     * New Instance
     *
     * @return
     */
    public static EditProfilePersonalFragment newInstance() {
        return new EditProfilePersonalFragment();
    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_personal, container, false);

        spTitle = view.findViewById(R.id.sp_title);
        spGender = view.findViewById(R.id.sp_gender);
        spMaritalStatus = view.findViewById(R.id.sp_maritalStatus);
        spBloodGroup = view.findViewById(R.id.sp_bloodGroup);
//        spDepartment = view.findViewById(R.id.sp_department);
        loaderLayout = view.findViewById(R.id.rl_loader);
        imageLoader = view.findViewById(R.id.imageLoader);
        tvDob = view.findViewById(R.id.tv_dob);
        tvYears = view.findViewById(R.id.tv_years);
        tvMonths = view.findViewById(R.id.tv_months);
        tvDays = view.findViewById(R.id.tv_days);
        tvDepartment = view.findViewById(R.id.tv_designation);
        etFirstName = view.findViewById(R.id.et_firstName);
        etMiddleName = view.findViewById(R.id.et_middleName);
        etLastName = view.findViewById(R.id.et_lastName);
        updateProfileBtn = view.findViewById(R.id.bt_save);

        titleNameList = new ArrayList<>();
        genderNameList = new ArrayList<>();
        maritalStatusNameList = new ArrayList<>();
        bloodGroupNameList = new ArrayList<>();
//        departmentNameList = new ArrayList<>();
        subDepartmentNameList = new ArrayList<>();
        titleIdList = new ArrayList<>();
        genderIdList = new ArrayList<>();
        maritalStatusIdList = new ArrayList<>();
        bloodGroupIdList = new ArrayList<>();
//        departmentIdList = new ArrayList<>();
//        subDepartmentIdList = new ArrayList<>();

        dbManager = new DBManager(requireActivity());
        dbManager.open();

        Cursor cursor = dbManager.fetchProfileParams();
        if (cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID));
            cursor.close();
        }

        /**
         * method that contains selection listeners for spinners
         */
        spinnerSelectListeners();

        /**
         * methods that contains API call to get title, gender, marital status, blood group
         */
        getTitle();
        getGender();
        getMaritalStatus();
        getBloodGroup();
//        getDepartment();

        /**
         * method to open date picker to update date of birth
         */
        tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        if (ProfileActivity.employeeProfileModel != null) {

            if (ProfileActivity.employeeProfileModel.personTitleId != null) {
                if (!ProfileActivity.employeeProfileModel.personTitleId.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.personTitleId.equalsIgnoreCase("null")) {
                    titleId = ProfileActivity.employeeProfileModel.personTitleId;
                }
            }

            if (ProfileActivity.employeeProfileModel.firstName != null) {
                if (!ProfileActivity.employeeProfileModel.firstName.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.firstName.equalsIgnoreCase("null")) {
                    etFirstName.setText(ProfileActivity.employeeProfileModel.firstName);
                }
            }

            if (ProfileActivity.employeeProfileModel.middleName != null) {
                if (!ProfileActivity.employeeProfileModel.middleName.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.middleName.equalsIgnoreCase("null")) {
                    etMiddleName.setText(ProfileActivity.employeeProfileModel.middleName);

                }
            }

            if (ProfileActivity.employeeProfileModel.lastName != null) {
                if (!ProfileActivity.employeeProfileModel.lastName.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.lastName.equalsIgnoreCase("null")) {
                    etLastName.setText(ProfileActivity.employeeProfileModel.lastName);
                }
            }

            if (ProfileActivity.employeeProfileModel.genderId != null) {
                if (!ProfileActivity.employeeProfileModel.genderId.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.genderId.equalsIgnoreCase("null")) {
                    genderId = ProfileActivity.employeeProfileModel.genderId;
                }
            }

            if (ProfileActivity.employeeProfileModel.maritalStatusId != null) {
                if (!ProfileActivity.employeeProfileModel.maritalStatusId.isEmpty()) {
                    maritalStatusId = ProfileActivity.employeeProfileModel.maritalStatusId;
                }
            }

            if (ProfileActivity.employeeProfileModel.bloodGroupId != null) {
                if (!ProfileActivity.employeeProfileModel.bloodGroupId.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.bloodGroupId.equalsIgnoreCase("null")) {
                    bloodGroupId = ProfileActivity.employeeProfileModel.bloodGroupId;
                }
            }

            if (ProfileActivity.employeeProfileModel.dateOfBirth != null) {
                if (!ProfileActivity.employeeProfileModel.dateOfBirth.isEmpty()) {
                    String[] separated = ProfileActivity.employeeProfileModel.dateOfBirth.split("T");
                    tvDob.setText(separated[0]);
                }
            }

            if (ProfileActivity.employeeProfileModel.age != null) {
                if (!ProfileActivity.employeeProfileModel.age.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.age.equalsIgnoreCase("null")) {
                    String[] sp = ProfileActivity.employeeProfileModel.age.split(" ");
                    tvYears.setText(sp[0]);
                    tvMonths.setText(sp[1]);
                    tvDays.setText(sp[2]);
                }
            }

            if (ProfileActivity.employeeProfileModel.department != null) {
                if (!ProfileActivity.employeeProfileModel.department.isEmpty()) {
                    tvDepartment.setText(ProfileActivity.employeeProfileModel.department);
                }
            }
        }

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = etFirstName.getText().toString();
                String dateOfBirth = tvDob.getText().toString();

                if (selectedSpinnerTitle.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please select title", Toast.LENGTH_SHORT).show();

                } else if (fName.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please enter first name", Toast.LENGTH_SHORT).show();

                } else if (selectedSpinnerGender.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please select gender", Toast.LENGTH_SHORT).show();

                } else if (selectedSpinnerMS.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please select marital status", Toast.LENGTH_SHORT).show();

                } else if (dateOfBirth.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please select date of birth", Toast.LENGTH_SHORT).show();

                } else {

                    if (EditProfileActivity.updateProfileModel.cNICNumber == null) {
                        EditProfileActivity.updateProfileModel.cNICNumber = "";
                    }
                    if (EditProfileActivity.updateProfileModel.userName == null) {
                        EditProfileActivity.updateProfileModel.userName = "";
                    }
                    if (EditProfileActivity.updateProfileModel.prefix == null) {
                        EditProfileActivity.updateProfileModel.prefix = "";
                    }
                    if (EditProfileActivity.updateProfileModel.middleName == null) {
                        EditProfileActivity.updateProfileModel.middleName = "";
                    }
                    if (EditProfileActivity.updateProfileModel.lastName == null) {
                        EditProfileActivity.updateProfileModel.lastName = "";
                    }
                    if (EditProfileActivity.updateProfileModel.bloodGroup == null) {
                        EditProfileActivity.updateProfileModel.bloodGroup = "";
                    }
                    if (EditProfileActivity.updateProfileModel.bloodGroupId == null) {
                        EditProfileActivity.updateProfileModel.bloodGroupId = "";
                    }
                    if (EditProfileActivity.updateProfileModel.relationshipTypeId == null) {
                        EditProfileActivity.updateProfileModel.relationshipTypeId = "";
                    }
                    if (EditProfileActivity.updateProfileModel.relationshipTypeName == null) {
                        EditProfileActivity.updateProfileModel.relationshipTypeName = "";
                    }
                    if (EditProfileActivity.updateProfileModel.guardianName == null) {
                        EditProfileActivity.updateProfileModel.guardianName = "";
                    }
                    if (EditProfileActivity.updateProfileModel.picturePath == null) {
                        EditProfileActivity.updateProfileModel.picturePath = "";
                    }
                    if (EditProfileActivity.updateProfileModel.country == null) {
                        EditProfileActivity.updateProfileModel.country = "";
                    }
                    if (EditProfileActivity.updateProfileModel.countryId == null) {
                        EditProfileActivity.updateProfileModel.countryId = "";
                    }
                    if (EditProfileActivity.updateProfileModel.stateOrProvince == null) {
                        EditProfileActivity.updateProfileModel.stateOrProvince = "";
                    }
                    if (EditProfileActivity.updateProfileModel.stateOrProvinceId == null) {
                        EditProfileActivity.updateProfileModel.stateOrProvinceId = "";
                    }
                    if (EditProfileActivity.updateProfileModel.city == null) {
                        EditProfileActivity.updateProfileModel.city = "";
                    }
                    if (EditProfileActivity.updateProfileModel.cityId == null) {
                        EditProfileActivity.updateProfileModel.cityId = "";
                    }
                    if (EditProfileActivity.updateProfileModel.address == null) {
                        EditProfileActivity.updateProfileModel.address = "";
                    }
                    if (EditProfileActivity.updateProfileModel.cellNumber == null) {
                        EditProfileActivity.updateProfileModel.cellNumber = "";
                    }
                    if (EditProfileActivity.updateProfileModel.telephoneNumber == null) {
                        EditProfileActivity.updateProfileModel.telephoneNumber = "";
                    }
                    if (EditProfileActivity.updateProfileModel.email == null) {
                        EditProfileActivity.updateProfileModel.email = "";
                    }
                    if (EditProfileActivity.updateProfileModel.nOKFirstName == null) {
                        EditProfileActivity.updateProfileModel.nOKFirstName = "";
                    }
                    if (EditProfileActivity.updateProfileModel.nOKLastName == null) {
                        EditProfileActivity.updateProfileModel.nOKLastName = "";
                    }
                    if (EditProfileActivity.updateProfileModel.nOKRelation == null) {
                        EditProfileActivity.updateProfileModel.nOKRelation = "";
                    }
                    if (EditProfileActivity.updateProfileModel.nOKRelationshipTypeId == null) {
                        EditProfileActivity.updateProfileModel.nOKRelationshipTypeId = "";
                    }
                    if (EditProfileActivity.updateProfileModel.nOKCNICNumber == null) {
                        EditProfileActivity.updateProfileModel.nOKCNICNumber = "";
                    }
                    if (EditProfileActivity.updateProfileModel.nOKCellNumber == null) {
                        EditProfileActivity.updateProfileModel.nOKCellNumber = "";
                    }
                    if (EditProfileActivity.updateProfileModel.department == null) {
                        departmentName = tvDepartment.getText().toString();
                        if (!departmentName.equals("")) {
                            EditProfileActivity.updateProfileModel.department = departmentName;
                        } else {
                            EditProfileActivity.updateProfileModel.department = "";
                        }
                    }
                    if (EditProfileActivity.updateProfileModel.departmentId == null) {
                        EditProfileActivity.updateProfileModel.departmentId = "";
                    }


                    EditProfileActivity.updateProfileModel.title = selectedSpinnerTitle;
                    EditProfileActivity.updateProfileModel.personTitleId = titleId;
                    EditProfileActivity.updateProfileModel.firstName = fName;
                    EditProfileActivity.updateProfileModel.middleName = etMiddleName.getText().toString();
                    EditProfileActivity.updateProfileModel.lastName = etLastName.getText().toString();
                    EditProfileActivity.updateProfileModel.gender = selectedSpinnerGender;
                    EditProfileActivity.updateProfileModel.genderId = genderId;
                    EditProfileActivity.updateProfileModel.maritalStatus = selectedSpinnerMS;
                    EditProfileActivity.updateProfileModel.maritalStatusId = maritalStatusId;
                    EditProfileActivity.updateProfileModel.dateOfBirth = dateOfBirth;
                    EditProfileActivity.updateProfileModel.age = tvYears.getText().toString() + " " + tvMonths.getText().toString() + " " + tvDays.getText().toString();

                    updateProfile(EditProfileActivity.updateProfileModel.cNICNumber, EditProfileActivity.updateProfileModel.userName,
                            EditProfileActivity.updateProfileModel.personTitleId, EditProfileActivity.updateProfileModel.title, EditProfileActivity.updateProfileModel.prefix,
                            EditProfileActivity.updateProfileModel.firstName, EditProfileActivity.updateProfileModel.middleName,
                            EditProfileActivity.updateProfileModel.lastName, EditProfileActivity.updateProfileModel.gender,
                            EditProfileActivity.updateProfileModel.genderId, EditProfileActivity.updateProfileModel.relationshipTypeId,
                            EditProfileActivity.updateProfileModel.relationshipTypeName, EditProfileActivity.updateProfileModel.guardianName,
                            EditProfileActivity.updateProfileModel.maritalStatusId, EditProfileActivity.updateProfileModel.maritalStatus,
                            EditProfileActivity.updateProfileModel.dateOfBirth, EditProfileActivity.updateProfileModel.picturePath,
                            EditProfileActivity.updateProfileModel.country, EditProfileActivity.updateProfileModel.countryId,
                            EditProfileActivity.updateProfileModel.stateOrProvince, EditProfileActivity.updateProfileModel.stateOrProvinceId,
                            EditProfileActivity.updateProfileModel.city, EditProfileActivity.updateProfileModel.cityId,
                            EditProfileActivity.updateProfileModel.address, EditProfileActivity.updateProfileModel.cellNumber,
                            EditProfileActivity.updateProfileModel.telephoneNumber, EditProfileActivity.updateProfileModel.email,
                            EditProfileActivity.updateProfileModel.nOKFirstName, EditProfileActivity.updateProfileModel.nOKLastName,
                            EditProfileActivity.updateProfileModel.nOKRelation, EditProfileActivity.updateProfileModel.nOKRelationshipTypeId,
                            EditProfileActivity.updateProfileModel.nOKCNICNumber, EditProfileActivity.updateProfileModel.nOKCellNumber,
                            EditProfileActivity.updateProfileModel.bloodGroup, EditProfileActivity.updateProfileModel.bloodGroupId,
                            EditProfileActivity.updateProfileModel.age, EditProfileActivity.updateProfileModel.departmentId,
                            EditProfileActivity.updateProfileModel.department);

                }
            }
        });


        return view;
    }

    private void updateProfile(String cNICNumber, String userName, String personTitleId,
                               String title, String prefix, String firstName,
                               String middleName, String lastName, String gender,
                               String genderId, String relationshipTypeId, String relationshipTypeName,
                               String guardianName, String maritalStatusId, String maritalStatus,
                               String dateOfBirth, String picturePath, String country, String countryId,
                               String stateOrProvince, String stateOrProvinceId, String city, String cityId,
                               String address, String cellNumber, String telephoneNumber, String email,
                               String nOKFirstName, String nOKLastName, String nOKRelation, String nOKRelationshipTypeId,
                               String nOKCNICNumber, String nOKCellNumber, String bloodGroup, String bloodGroupId,
                               String age, String departmentId, String department) {

        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            animateLoaderIcon();

            RequestBody requestBody = new FormBody.Builder()
                    .add("UserId", userId)
                    .add("CNICNumber", cNICNumber)
                    .add("UserName", userName)
                    .add("PersonTitleId", personTitleId)
                    .add("Title", title)
                    .add("Prefix", prefix)
                    .add("FirstName", firstName)
                    .add("MiddleName", middleName)
                    .add("LastName", lastName)
                    .add("Gender", gender)
                    .add("GenderId", genderId) //
                    .add("RelationshipTypeId", relationshipTypeId)  // GuardianRelationId
                    .add("RelationshipTypeName", relationshipTypeName) //
                    .add("GuardianName", guardianName)
                    .add("MaritalStatusId", maritalStatusId)
                    .add("MaritalStatus", maritalStatus)
                    .add("DateOfBirth", dateOfBirth)
                    .add("PicturePath", picturePath)
                    .add("Country", country)
                    .add("CountryId", countryId)
                    .add("StateOrProvince", stateOrProvince)
                    .add("StateOrProvinceId", stateOrProvinceId)
                    .add("City", city)
                    .add("CityId", cityId) //
                    .add("Address", address)
                    .add("CellNumber", cellNumber)
                    .add("TelephoneNumber", telephoneNumber)
                    .add("Email", email)
                    .add("NOKFirstName", nOKFirstName)
                    .add("NOKLastName", nOKLastName)
                    .add("NOKRelation", nOKRelation)
                    .add("NOKRelationshipTypeId", nOKRelationshipTypeId)
                    .add("NOKCNICNumber", nOKCNICNumber)
                    .add("NOKCellNumber", nOKCellNumber)
                    .add("BloodGroup", bloodGroup)
                    .add("BloodGroupId", bloodGroupId)
                    .add("Age", age)
                    .add("DepartmentId", departmentId)
                    .add("Department", department)
                    .build();

            Call<UpdateProfileResponse> call = apiUtils.getAPIService(requireActivity()).updateEmployeeProfile(requestBody);
            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(@NonNull Call<UpdateProfileResponse> call, @NonNull Response<UpdateProfileResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        loaderLayout.setVisibility(View.GONE);
                        if (response.body().status >= 1) {
                            getEmployeeProfile();

                        } else {
                            loaderLayout.setVisibility(View.GONE);
                            Log.e(TAG, "onResponse: response status is: " + response.body().status);
                        }
                    } else {
                        loaderLayout.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: response is null");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UpdateProfileResponse> call, @NonNull Throwable t) {
                    loaderLayout.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());
                }
            });

        } else {
            loaderLayout.setVisibility(View.GONE);
            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Connection Error", "OK", "Please check your internet connection and try again later", new DialogClickListener() {
                @Override
                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                    alertDialog.dismiss();
                }
            });
        }
    }

    private void getEmployeeProfile() {
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            if (!userId.isEmpty()) {
//                animateLoaderIcon();
                Log.e(TAG, "getEmployeeProfile: api call");
                RequestBody requestBody = new FormBody.Builder()
                        .add("UserId", userId)
                        .build();
                Call<EmployeeProfileResponse> call = apiUtils.getAPIService(requireActivity()).getEmployeeProfile(requestBody);
                call.enqueue(new Callback<EmployeeProfileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<EmployeeProfileResponse> call, @NonNull Response<EmployeeProfileResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            loaderLayout.setVisibility(View.GONE);

                            if (response.body().status != null && response.body().status == 1) {

                                EmployeeProfileResponse employeeProfileModel = response.body();

                                dbManager.insertProfile(employeeProfileModel);

                                ProfileActivity.employeeProfileModel = response.body();
                                EditProfileActivity.updateProfileModel = response.body();

                                if (getActivity() != null) {
                                    DialogHandler.getSharedInstance().genericDialogSuccess(getActivity(), true, "Profile Updated", "Profile Updated Successfully", "OK", new DialogClickListener() {
                                        @Override
                                        public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                            //getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
                                            //getActivity().finishAffinity();
                                            getActivity().onBackPressed();
                                            alertDialog.dismiss();
                                        }
                                    });
                                }

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
            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Connection Error", "OK", "Please check your internet connection and try again later", new DialogClickListener() {
                @Override
                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                    alertDialog.dismiss();
                }
            });
        }
    }


//    private void getDepartment() {
//        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
//            animateLoaderIcon();
//            RequestBody requestBody = new FormBody.Builder()
//                    .add("UserId", userId)
//                    .build();
//            Call<GetDepartmentResponse> call = apiUtils.getAPIService(requireActivity()).getDepartments(requestBody);
//            call.enqueue(new Callback<GetDepartmentResponse>() {
//                @Override
//                public void onResponse(@NonNull Call<GetDepartmentResponse> call, @NonNull Response<GetDepartmentResponse> response) {
//
//                    if (response.isSuccessful() && response.body() != null) {
//                        loaderLayout.setVisibility(View.GONE);
//
//                        if (response.body().status != null &&
//                                response.body().status == 1 &&
//                                response.body().data != null) {
//
//                            departmentIdList = response.body().data;
//
////                            departmentNameList.add("Select Department");
//                            for (int i = 0; i < response.body().data.size(); i++) {
//                                getSubDepartment(response.body().data.get(i).depId);
////                                departmentNameList.add(response.body().data.get(i).de);
//                            }
//
////                            titleAdapter = new ArrayAdapter<>(getActivity(),
////                                    R.layout.custom_spinnerdesign, R.id.tv_customspinneritem, titleNameList);
////                            titleAdapter.setDropDownViewResource(R.layout.custom_spinnerdesign);
////                            spTitle.setAdapter(titleAdapter);
////
////                            int index = 0;
////                            if (ProfileActivity.employeeProfileModel != null &&
////                                    ProfileActivity.employeeProfileModel.personTitleId != null) {
////                                for (int i = 0; i < titleIdList.size(); i++) {
////                                    if (titleIdList.get(i).titleId.equals(ProfileActivity.employeeProfileModel.personTitleId)) {
////                                        index = i;
////                                    }
////                                }
////                                selectedSpinnerTitle = titleIdList.get(index).titleName;
////                                titleId = titleIdList.get(index).titleId;
////                                Log.e(TAG, "onResponse: selected is: " + selectedSpinnerTitle);
////                                Log.e(TAG, "onResponse: selected id is: " + titleId);
////                                Log.e(TAG, "onResponse: selected index is: " + index);
////                            }
////                            spTitle.setSelection(index + 1);
//                        }
//                    } else {
//                        loaderLayout.setVisibility(View.GONE);
//                        Log.e(TAG, "onResponse: response is not successfull");
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<GetDepartmentResponse> call, @NonNull Throwable t) {
//                    loaderLayout.setVisibility(View.GONE);
//                    Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());
//                }
//            });
//        }
//    }

    /**
     * Method that contains network call to get title
     */
    private void getTitle() {
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            animateLoaderIcon();
            Call<TitleResponse> call = apiUtils.getAPIService(requireActivity()).getTitle();
            call.enqueue(new Callback<TitleResponse>() {
                @Override
                public void onResponse(@NonNull Call<TitleResponse> call, @NonNull Response<TitleResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        loaderLayout.setVisibility(View.GONE);

                        if (response.body().status != null &&
                                response.body().status == 1 &&
                                response.body().titleData != null) {

                            titleIdList = response.body().titleData;

                            titleNameList.add("Select Title");
                            for (int i = 0; i < response.body().titleData.size(); i++) {
                                titleNameList.add(response.body().titleData.get(i).titleName);
                            }

                            titleAdapter = new ArrayAdapter<>(getActivity(),
                                    R.layout.custom_spinnerdesign, R.id.tv_customspinneritem, titleNameList);
                            titleAdapter.setDropDownViewResource(R.layout.custom_spinnerdesign);
                            spTitle.setAdapter(titleAdapter);

                            int index = 0;
                            if (ProfileActivity.employeeProfileModel != null &&
                                    ProfileActivity.employeeProfileModel.personTitleId != null) {
                                for (int i = 0; i < titleIdList.size(); i++) {
                                    if (titleIdList.get(i).titleId.equals(ProfileActivity.employeeProfileModel.personTitleId)) {
                                        index = i;
                                    }
                                }
                                selectedSpinnerTitle = titleIdList.get(index).titleName;
                                titleId = titleIdList.get(index).titleId;
                                Log.e(TAG, "onResponse: selected is: " + selectedSpinnerTitle);
                                Log.e(TAG, "onResponse: selected id is: " + titleId);
                                Log.e(TAG, "onResponse: selected index is: " + index);
                            }
                            spTitle.setSelection(index + 1);
                        }
                    } else {
                        loaderLayout.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: response is not successfull");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TitleResponse> call, @NonNull Throwable t) {
                    loaderLayout.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());
                }
            });
        } else {
            loaderLayout.setVisibility(View.GONE);
            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Connection Error", "OK", "Please check your internet connection and try again later", new DialogClickListener() {
                @Override
                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                    alertDialog.dismiss();
                }
            });
        }
    }

    /**
     * Method that contains network call to get genders
     */
    private void getGender() {
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            animateLoaderIcon();
            Call<GenderResponse> call = apiUtils.getAPIService(requireActivity()).getGender();
            call.enqueue(new Callback<GenderResponse>() {
                @Override
                public void onResponse(@NonNull Call<GenderResponse> call, @NonNull Response<GenderResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        loaderLayout.setVisibility(View.GONE);

                        if (response.body().status != null && response.body().status == 1 && response.body().genderData != null) {

                            genderIdList = response.body().genderData;

                            genderNameList.add("Select Gender");
                            for (int i = 0; i < response.body().genderData.size(); i++) {
                                genderNameList.add(response.body().genderData.get(i).genderName);
                            }

                            genderAdapter = new ArrayAdapter<>(getActivity(),
                                    R.layout.custom_spinnerdesign, R.id.tv_customspinneritem, genderNameList);
                            genderAdapter.setDropDownViewResource(R.layout.custom_spinnerdesign);
                            spGender.setAdapter(genderAdapter);

                            int index = 0;
                            if (ProfileActivity.employeeProfileModel != null &&
                                    ProfileActivity.employeeProfileModel.genderId != null) {
                                for (int i = 0; i < genderIdList.size(); i++) {
                                    if (genderIdList.get(i).genderId.equals(ProfileActivity.employeeProfileModel.genderId)) {
                                        index = i;
                                    }
                                }
                                selectedSpinnerGender = genderIdList.get(index).genderName;
                                genderId = genderIdList.get(index).genderId;
                                Log.e(TAG, "onResponse: selected is: " + selectedSpinnerGender);
                                Log.e(TAG, "onResponse: selected id is: " + genderId);
                                Log.e(TAG, "onResponse: selected index is: " + index);
                            }
                            spGender.setSelection(index + 1);
                        }
                    } else {
                        loaderLayout.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: response is not successfull");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GenderResponse> call, @NonNull Throwable t) {
                    loaderLayout.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());
                }
            });
        } else {
            loaderLayout.setVisibility(View.GONE);
            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Connection Error", "OK", "Please check your internet connection and try again later", new DialogClickListener() {
                @Override
                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                    alertDialog.dismiss();
                }
            });
        }
    }

    /**
     * Method that contains network call to get marital status
     */
    private void getMaritalStatus() {
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            animateLoaderIcon();
            Call<MaritalStatusResponse> call = apiUtils.getAPIService(requireActivity()).getMaritalStatus();
            call.enqueue(new Callback<MaritalStatusResponse>() {
                @Override
                public void onResponse(@NonNull Call<MaritalStatusResponse> call, @NonNull Response<MaritalStatusResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        loaderLayout.setVisibility(View.GONE);

                        if (response.body().status != null && response.body().status == 1 && response.body().maritalStatusData != null) {

                            maritalStatusIdList = response.body().maritalStatusData;

                            maritalStatusNameList.add("Select Marital Status");
                            for (int i = 0; i < response.body().maritalStatusData.size(); i++) {
                                maritalStatusNameList.add(response.body().maritalStatusData.get(i).maritalStatusName);
                            }

                            maritalStatusAdapter = new ArrayAdapter<>(getActivity(),
                                    R.layout.custom_spinnerdesign, R.id.tv_customspinneritem, maritalStatusNameList);
                            maritalStatusAdapter.setDropDownViewResource(R.layout.custom_spinnerdesign);
                            spMaritalStatus.setAdapter(maritalStatusAdapter);

                            int index = 0;
                            if (ProfileActivity.employeeProfileModel != null &&
                                    ProfileActivity.employeeProfileModel.maritalStatusId != null) {
                                for (int i = 0; i < maritalStatusIdList.size(); i++) {
                                    if (maritalStatusIdList.get(i).maritalStatusId.equals(ProfileActivity.employeeProfileModel.maritalStatusId)) {
                                        index = i;
                                    }
                                }
                                selectedSpinnerMS = maritalStatusIdList.get(index).maritalStatusName;
                                maritalStatusId = maritalStatusIdList.get(index).maritalStatusId;
                                Log.e(TAG, "onResponse: selected is: " + selectedSpinnerMS);
                                Log.e(TAG, "onResponse: selected id is: " + maritalStatusId);
                                Log.e(TAG, "onResponse: selected index is: " + index);
                            }
                            spMaritalStatus.setSelection(index + 1);
                        }
                    } else {
                        loaderLayout.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: response is not successfull");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MaritalStatusResponse> call, @NonNull Throwable t) {
                    loaderLayout.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());
                }
            });
        } else {
            loaderLayout.setVisibility(View.GONE);
            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Connection Error", "OK", "Please check your internet connection and try again later", new DialogClickListener() {
                @Override
                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                    alertDialog.dismiss();
                }
            });
        }
    }

    /**
     * Method that contains network call to get blood group
     */
    private void getBloodGroup() {
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            animateLoaderIcon();
            Call<BloodGroupResponse> call = apiUtils.getAPIService(requireActivity()).getBloodGroup();
            call.enqueue(new Callback<BloodGroupResponse>() {
                @Override
                public void onResponse(@NonNull Call<BloodGroupResponse> call, @NonNull Response<BloodGroupResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        loaderLayout.setVisibility(View.GONE);

                        if (response.body().status != null && response.body().status == 1 && response.body().bloodGroupData != null) {

                            bloodGroupIdList = response.body().bloodGroupData;

                            bloodGroupNameList.add("Select Blood Group");
                            for (int i = 0; i < response.body().bloodGroupData.size(); i++) {
                                bloodGroupNameList.add(response.body().bloodGroupData.get(i).bloodGroupName);
                            }

                            bloodGroupAdapter = new ArrayAdapter<>(getActivity(),
                                    R.layout.custom_spinnerdesign, R.id.tv_customspinneritem, bloodGroupNameList);
                            bloodGroupAdapter.setDropDownViewResource(R.layout.custom_spinnerdesign);
                            spBloodGroup.setAdapter(bloodGroupAdapter);

                            int index = 0;
                            if (ProfileActivity.employeeProfileModel != null &&
                                    ProfileActivity.employeeProfileModel.bloodGroupId != null) {
                                for (int i = 0; i < bloodGroupIdList.size(); i++) {
                                    if (bloodGroupIdList.get(i).bloodGroupId.equals(ProfileActivity.employeeProfileModel.bloodGroupId)) {
                                        index = i;
                                    }
                                }
                                selectedSpinnerBG = bloodGroupIdList.get(index).bloodGroupName;
                                bloodGroupId = bloodGroupIdList.get(index).bloodGroupId;
                                Log.e(TAG, "onResponse: selected is: " + selectedSpinnerBG);
                                Log.e(TAG, "onResponse: selected id is: " + bloodGroupId);
                                Log.e(TAG, "onResponse: selected index is: " + index);
                            }
                            spBloodGroup.setSelection(index + 1);
                        }
                    } else {
                        loaderLayout.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: response is not successfull");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BloodGroupResponse> call, @NonNull Throwable t) {
                    loaderLayout.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());
                }
            });
        } else {
            loaderLayout.setVisibility(View.GONE);
            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Connection Error", "OK", "Please check your internet connection and try again later", new DialogClickListener() {
                @Override
                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                    alertDialog.dismiss();
                }
            });
        }
    }

    /**
     * Method that contains spinner selection implementation
     */
    private void spinnerSelectListeners() {

        spTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedSpinnerTitle = parent.getItemAtPosition(position).toString();
                    titleId = titleIdList.get(--position).titleId;
                    Log.e("EditPersonalFrag", "titleOnItemSelected: Selected title " + selectedSpinnerTitle);
                    Log.e("EditPersonalFrag", "titleOnItemSelected: Selected title id " + titleId);

                    spinnerPosition = titleAdapter.getPosition(selectedSpinnerTitle);
                    Log.e("EditPersonalFrag", "spinner title position " + spinnerPosition);
                } else {
                    selectedSpinnerTitle = "";
                    spinnerPosition = titleAdapter.getPosition(selectedSpinnerTitle);
                    Log.e("EditPersonalFrag", "titleOnItemSelected: Selected title " + selectedSpinnerTitle);
                    Log.e("EditPersonalFrag", "spinner title position " + spinnerPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedSpinnerGender = parent.getItemAtPosition(position).toString();
                    genderId = genderIdList.get(--position).genderId;
                    Log.e("EditPersonalFrag", "titleOnItemSelected: Selected gender: " + selectedSpinnerGender);
                    Log.e("EditPersonalFrag", "titleOnItemSelected: Selected gender id: " + genderId);

                    spinnerPosition = genderAdapter.getPosition(selectedSpinnerGender);
                    Log.e("EditPersonalFrag", "spinner gender position: " + spinnerPosition);
                } else {
                    selectedSpinnerGender = "";
                    spinnerPosition = genderAdapter.getPosition(selectedSpinnerGender);
                    Log.e("EditPersonalFrag", "titleOnItemSelected: Selected gender: " + selectedSpinnerGender);
                    Log.e("EditPersonalFrag", "spinner gender position: " + spinnerPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spMaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedSpinnerMS = parent.getItemAtPosition(position).toString();
                    maritalStatusId = maritalStatusIdList.get(--position).maritalStatusId;
                    Log.e("EditPersonalFrag", "titleOnItemSelected: Selected marital: " + selectedSpinnerMS);
                    Log.e("EditPersonalFrag", "titleOnItemSelected: Selected marital id: " + maritalStatusId);

                    spinnerPosition = maritalStatusAdapter.getPosition(selectedSpinnerMS);
                    Log.e("EditPersonalFrag", "spinner marital position: " + spinnerPosition);
                } else {
                    selectedSpinnerMS = "";
                    spinnerPosition = maritalStatusAdapter.getPosition(selectedSpinnerMS);
                    Log.e("EditPersonalFrag", "titleOnItemSelected: Selected marital: " + selectedSpinnerMS);
                    Log.e("EditPersonalFrag", "spinner marital position: " + spinnerPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedSpinnerBG = parent.getItemAtPosition(position).toString();
                    bloodGroupId = bloodGroupIdList.get(--position).bloodGroupId;
                    Log.e("EditPersonalFrag", "titleOnItemSelected: Selected blood group: " + selectedSpinnerBG);
                    Log.e("EditPersonalFrag", "titleOnItemSelected: Selected blood group id: " + bloodGroupId);

                    spinnerPosition = bloodGroupAdapter.getPosition(selectedSpinnerBG);
                    Log.e("EditPersonalFrag", "spinner blood group position: " + spinnerPosition);
                } else {
                    selectedSpinnerBG = "";
                    spinnerPosition = bloodGroupAdapter.getPosition(selectedSpinnerBG);
                    Log.e("EditPersonalFrag", "titleOnItemSelected: Selected blood group: " + selectedSpinnerBG);
                    Log.e("EditPersonalFrag", "spinner blood group position: " + spinnerPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * method that contains implementation of calculating date
     */
    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    year = year1;
                    month = monthOfYear;
                    day = dayOfMonth;


                    try {
                        String dobDate = year1 + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                        Date currentDate = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        Date dateOfBirth = sdf.parse(dobDate);

                        if (dateOfBirth != null && dateOfBirth.after(currentDate)) {
                            Toast.makeText(getActivity(), "Error: Invalid date of birth, try again!", Toast.LENGTH_SHORT).show();

                        } else {
                            getDOBValue = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            tvDob.setText(getDOBValue);
                            findAge(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR), dayOfMonth, (monthOfYear + 1), year1);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("DOB EXP", e.toString());
                    }

                }, year, month, day);

        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    /**
     * method to calculate age
     *
     * @param current_date
     * @param current_month
     * @param current_year
     * @param birth_date
     * @param birth_month
     * @param birth_year
     */
    public void findAge(int current_date, int current_month,
                        int current_year, int birth_date,
                        int birth_month, int birth_year) {

        int month[] = {31, 28, 31, 30, 31, 30, 31,
                31, 30, 31, 30, 31};

        // if birth date is greater then current
        // birth_month, then do not count this month
        // and add 30 to the date so as to subtract
        // the date and get the remaining days
        if (birth_date > current_date) {
            current_month = current_month - 1;
            current_date = current_date + month[birth_month - 1];
        }

        // if birth month exceeds current month,
        // then do not count this year and add
        // 12 to the month so that we can subtract
        // and find out the difference
        if (birth_month > current_month) {
            current_year = current_year - 1;
            current_month = current_month + 12;
        }

        // calculate date, month, year
        int calculated_date = current_date - birth_date;
        int calculated_month = current_month - birth_month;
        int calculated_year = current_year - birth_year;

        // print the present age
        //Log.e("AGE", "Years: " + calculated_year + " Months: " + calculated_month + " Days: " + calculated_date);

        tvDays.setText("" + calculated_date + "d");
        tvMonths.setText("" + calculated_month + "m");
        tvYears.setText("" + calculated_year + "y");
    }


    /**
     * Method to animate logo as a loader image
     */
    private void animateLoaderIcon() {
        loaderLayout.setVisibility(View.VISIBLE);
        Glide.with(requireActivity())
                .load(R.drawable.ic_ihclogo)
                .into(imageLoader);
        imageLoader.startAnimation(AnimationUtils.loadAnimation(requireActivity(),
                R.anim.blink_animation));
    }

}