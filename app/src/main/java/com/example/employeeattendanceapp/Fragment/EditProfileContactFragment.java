package com.example.employeeattendanceapp.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.employeeattendanceapp.Activity.EditProfileActivity;
import com.example.employeeattendanceapp.Activity.ProfileActivity;
import com.example.employeeattendanceapp.DB.DBManager;
import com.example.employeeattendanceapp.DB.DatabaseHelper;
import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Listeners.DialogClickListener;
import com.example.employeeattendanceapp.Manager.ConnectionManager;
import com.example.employeeattendanceapp.Model.CityData;
import com.example.employeeattendanceapp.Model.CountryData;
import com.example.employeeattendanceapp.Model.Response.EmployeeProfileResponse;
import com.example.employeeattendanceapp.Model.Response.GetCityResponse;
import com.example.employeeattendanceapp.Model.Response.GetCountryResponse;
import com.example.employeeattendanceapp.Model.Response.GetStateResponse;
import com.example.employeeattendanceapp.Model.Response.UpdateProfileResponse;
import com.example.employeeattendanceapp.Model.StateData;
import com.example.employeeattendanceapp.R;
import com.example.employeeattendanceapp.Retrofit.apiUtils;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class EditProfileContactFragment extends Fragment {

    String TAG = "EditProfileContactFragment", userId = "", selectedCountry = "", selectedCountryId = "", selectedState = "", selectedStateId = "", selectedCity = "", selectedCityId = "";

    Spinner spCountry, spState, spCity;
    RelativeLayout loaderLayout;
    EditText etMobileNumber, etAddress, etTelephoneNumber;
    ImageView imageLoader;

    ArrayList<String> countryNameList, StateNameList, CityNameList;
    ArrayList<CountryData> countryIdList;
    ArrayList<StateData> stateIdList;
    ArrayList<CityData> cityIdList;

    ArrayAdapter<String> countryAdapter, stateAdapter, cityAdapter;

    Button updateProfileBtn;

    DBManager dbManager;

    /**
     * New Instance
     *
     * @return
     */
    public static EditProfileContactFragment newInstance() {
        return new EditProfileContactFragment();
    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_contact, container, false);

        spCountry = view.findViewById(R.id.sp_country);
        spState = view.findViewById(R.id.sp_state);
        spCity = view.findViewById(R.id.sp_city);
        etMobileNumber = view.findViewById(R.id.et_mobileNumber);
        etTelephoneNumber = view.findViewById(R.id.et_telephoneNumber);
        etAddress = view.findViewById(R.id.et_address);
        loaderLayout = view.findViewById(R.id.rl_loader);
        imageLoader = view.findViewById(R.id.imageLoader);
        updateProfileBtn = view.findViewById(R.id.bt_save);

        countryNameList = new ArrayList<>();
        countryIdList = new ArrayList<>();
        stateIdList = new ArrayList<>();
        cityIdList = new ArrayList<>();

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
         * method that contains network call to get countries
         */
        getCountry();

        if (ProfileActivity.employeeProfileModel != null) {

            if (ProfileActivity.employeeProfileModel.countryId != null) {
                if (!ProfileActivity.employeeProfileModel.countryId.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.countryId.equalsIgnoreCase("null")) {
                    selectedCountryId = ProfileActivity.employeeProfileModel.countryId;
                }
            }

            if (ProfileActivity.employeeProfileModel.stateOrProvinceId != null) {
                if (!ProfileActivity.employeeProfileModel.stateOrProvinceId.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.stateOrProvinceId.equalsIgnoreCase("null")) {
                    selectedStateId = ProfileActivity.employeeProfileModel.stateOrProvinceId;
                }
            }

            if (ProfileActivity.employeeProfileModel.cityId != null) {
                if (!ProfileActivity.employeeProfileModel.cityId.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.cityId.equalsIgnoreCase("null")) {
                    selectedCountryId = ProfileActivity.employeeProfileModel.cityId;

                }
            }

            if (ProfileActivity.employeeProfileModel.address != null) {
                if (!ProfileActivity.employeeProfileModel.address.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.address.equalsIgnoreCase("null")) {
                    etAddress.setText(ProfileActivity.employeeProfileModel.address);
                }
            }

            if (ProfileActivity.employeeProfileModel.cellNumber != null) {
                if (!ProfileActivity.employeeProfileModel.cellNumber.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.cellNumber.equalsIgnoreCase("null")) {
                    etMobileNumber.setText(ProfileActivity.employeeProfileModel.cellNumber);
                }
            }

            if (ProfileActivity.employeeProfileModel.telephoneNumber != null) {
                if (!ProfileActivity.employeeProfileModel.telephoneNumber.isEmpty() &&
                        !ProfileActivity.employeeProfileModel.telephoneNumber.equalsIgnoreCase("null")) {
                    etTelephoneNumber.setText(ProfileActivity.employeeProfileModel.telephoneNumber);
                }
            }
        }

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCountry.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please select country", Toast.LENGTH_SHORT).show();

                } else if (selectedState.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please select state", Toast.LENGTH_SHORT).show();

                } else if (selectedCity.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please select city", Toast.LENGTH_SHORT).show();

                } else if (etAddress.getText().toString().isEmpty()) {
                    Toast.makeText(requireActivity(), "Please enter address", Toast.LENGTH_SHORT).show();

                } else if (etMobileNumber.getText().toString().isEmpty()) {
                    Toast.makeText(requireActivity(), "Please enter mobile number", Toast.LENGTH_SHORT).show();

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
                    if (EditProfileActivity.updateProfileModel.title == null) {
                        EditProfileActivity.updateProfileModel.title = "";
                    }
                    if (EditProfileActivity.updateProfileModel.personTitleId == null) {
                        EditProfileActivity.updateProfileModel.personTitleId = "";
                    }
                    if (EditProfileActivity.updateProfileModel.firstName == null) {
                        EditProfileActivity.updateProfileModel.firstName = "";
                    }
                    if (EditProfileActivity.updateProfileModel.middleName == null) {
                        EditProfileActivity.updateProfileModel.middleName = "";
                    }
                    if (EditProfileActivity.updateProfileModel.lastName == null) {
                        EditProfileActivity.updateProfileModel.lastName = "";
                    }
                    if (EditProfileActivity.updateProfileModel.maritalStatus == null) {
                        EditProfileActivity.updateProfileModel.maritalStatus = "";
                    }
                    if (EditProfileActivity.updateProfileModel.maritalStatusId == null) {
                        EditProfileActivity.updateProfileModel.maritalStatusId = "";
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
                        EditProfileActivity.updateProfileModel.department = "";
                    }
                    if (EditProfileActivity.updateProfileModel.departmentId == null) {
                        EditProfileActivity.updateProfileModel.departmentId = "";
                    }

                    EditProfileActivity.updateProfileModel.country = selectedCountry;
                    EditProfileActivity.updateProfileModel.countryId = selectedCountryId;
                    EditProfileActivity.updateProfileModel.stateOrProvince = selectedState;
                    EditProfileActivity.updateProfileModel.stateOrProvinceId = selectedStateId;
                    EditProfileActivity.updateProfileModel.city = selectedCity;
                    EditProfileActivity.updateProfileModel.cityId = selectedCityId;
                    EditProfileActivity.updateProfileModel.address = etAddress.getText().toString();
                    EditProfileActivity.updateProfileModel.cellNumber = etMobileNumber.getText().toString();

                    if (!etTelephoneNumber.getText().toString().isEmpty()) {
                        EditProfileActivity.updateProfileModel.telephoneNumber = etTelephoneNumber.getText().toString();

                    } else {
                        EditProfileActivity.updateProfileModel.telephoneNumber = "";
                    }

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
                               String dateOfBirth, String picturePath, String country,
                               String countryId, String stateOrProvince, String stateOrProvinceId,
                               String city, String cityId, String address, String cellNumber,
                               String telephoneNumber, String email, String nOKFirstName,
                               String nOKLastName, String nOKRelation, String nOKRelationshipTypeId,
                               String nOKCNICNumber, String nOKCellNumber, String bloodGroup,
                               String bloodGroupId, String age, String departmentId, String department) {

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

    private void getCountry() {
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            animateLoaderIcon();
            Call<GetCountryResponse> call = apiUtils.getAPIService(requireActivity()).getCountries();
            call.enqueue(new Callback<GetCountryResponse>() {
                @Override
                public void onResponse(@NonNull Call<GetCountryResponse> call, @NonNull Response<GetCountryResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        loaderLayout.setVisibility(View.GONE);

                        if (response.body().status != null &&
                                response.body().status == 1 &&
                                response.body().data != null) {

                            countryIdList = response.body().data;

                            countryNameList.add("Select Country");
                            for (int i = 0; i < response.body().data.size(); i++) {
                                countryNameList.add(response.body().data.get(i).name);
                            }

                            countryAdapter = new ArrayAdapter<>(getActivity(),
                                    R.layout.custom_spinnerdesign, R.id.tv_customspinneritem, countryNameList);
                            countryAdapter.setDropDownViewResource(R.layout.custom_spinnerdesign);
                            spCountry.setAdapter(countryAdapter);

                            int index = 0;
                            if (ProfileActivity.employeeProfileModel != null &&
                                    ProfileActivity.employeeProfileModel.countryId != null) {
                                for (int i = 0; i < countryIdList.size(); i++) {
                                    if (countryIdList.get(i).id.equals(ProfileActivity.employeeProfileModel.countryId)) {
                                        index = i;
                                    }
                                }
                                selectedCountryId = countryIdList.get(index).id;
                                selectedCountry = countryIdList.get(index).name;
                                Log.e(TAG, "onResponse: selected is: " + selectedCountry);
                                Log.e(TAG, "onResponse: selected id is: " + selectedCountryId);
                                Log.e(TAG, "onResponse: selected index is: " + index);
                            }
                            spCountry.setSelection(index + 1);
                        }
                    } else {
                        loaderLayout.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: response is not successfull");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetCountryResponse> call, @NonNull Throwable t) {
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

    private void spinnerSelectListeners() {

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StateNameList = new ArrayList<>();
                CityNameList = new ArrayList<>();
                if (position <= 0) {
                    selectedCountryId = "";
                    selectedCountry = "";
                    Log.d(TAG, "SPIDCHECK CountryId 1 : " + selectedCountryId);
                    Log.d(TAG, "SPIDCHECK Country Name 1: " + selectedCountry);
                    spState.setAdapter(null);
                    spCity.setAdapter(null);

                } else {
                    selectedCountryId = countryIdList.get(--position).id;
                    selectedCountry = parent.getItemAtPosition(++position).toString();
                    Log.d(TAG, "SPIDCHECK CountryId : " + selectedCountryId);
                    Log.d(TAG, "SPIDCHECK Country Name : " + selectedCountry);
                    getStateData(selectedCountryId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "CALLCHECK state item listener called");
                CityNameList = new ArrayList<>();
                // position -1 because in listspinner we add an extra String at 0
                if (position <= 0) {
                    selectedStateId = "";
                    selectedState = "";
                    spCity.setAdapter(null);
                    Log.d(TAG, "SPIDCHECK StateId : " + selectedStateId);
                    Log.d(TAG, "SPIDCHECK State Name : " + selectedState);
                } else {
                    selectedStateId = stateIdList.get(--position).id;
                    selectedState = parent.getItemAtPosition(++position).toString();
                    Log.d(TAG, "SPIDCHECK StateId : " + selectedStateId);
                    Log.d(TAG, "SPIDCHECK State Name : " + selectedState);
                    getCityData(selectedStateId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "CALLCHECK state item listener called");
                CityNameList = new ArrayList<>();
                // position -1 because in listspinner we add an extra String at 0
                if (position <= 0) {
                    selectedCityId = "";
                    selectedCity = "";
                    Log.d(TAG, "SPIDCHECK CityId : " + selectedCityId);
                    Log.d(TAG, "SPIDCHECK City Name : " + selectedCity);
                } else {
                    selectedCityId = cityIdList.get(--position).Id;
                    selectedCity = parent.getItemAtPosition(++position).toString();
                    Log.d(TAG, "SPIDCHECK CityId : " + selectedCityId);
                    Log.d(TAG, "SPIDCHECK City Name : " + selectedCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getStateData(String stateId) {
        animateLoaderIcon();
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            if (!stateId.isEmpty()) {
                RequestBody requestBody = new FormBody.Builder()
                        .add("CountryId", stateId)
                        .build();
                Call<GetStateResponse> call = apiUtils.getAPIService(requireActivity()).getStates(requestBody);
                call.enqueue(new Callback<GetStateResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GetStateResponse> call, @NonNull Response<GetStateResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            loaderLayout.setVisibility(View.GONE);

                            if (response.body().status != null &&
                                    response.body().status == 1 &&
                                    response.body().data != null) {

                                stateIdList = response.body().data;

                                StateNameList.add("Select State");
                                for (int i = 0; i < response.body().data.size(); i++) {
                                    StateNameList.add(response.body().data.get(i).name);
                                }

                                stateAdapter = new ArrayAdapter<>(getActivity(),
                                        R.layout.custom_spinnerdesign, R.id.tv_customspinneritem, StateNameList);
                                stateAdapter.setDropDownViewResource(R.layout.custom_spinnerdesign);
                                spState.setAdapter(stateAdapter);

                                int index = 0;
                                if (ProfileActivity.employeeProfileModel != null &&
                                        ProfileActivity.employeeProfileModel.stateOrProvinceId != null) {
                                    for (int i = 0; i < stateIdList.size(); i++) {
                                        if (stateIdList.get(i).id.equals(ProfileActivity.employeeProfileModel.stateOrProvinceId)) {
                                            index = i;
                                        }
                                    }
                                    selectedStateId = stateIdList.get(index).id;
                                    selectedState = stateIdList.get(index).name;
                                    Log.e(TAG, "onResponse: selected is: " + selectedState);
                                    Log.e(TAG, "onResponse: selected id is: " + selectedStateId);
                                    Log.e(TAG, "onResponse: selected index is: " + index);
                                }
                                spState.setSelection(index + 1);
                            }
                        } else {
                            loaderLayout.setVisibility(View.GONE);
                            Log.e(TAG, "onResponse: response is not successfull");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GetStateResponse> call, @NonNull Throwable t) {
                        loaderLayout.setVisibility(View.GONE);
                        Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());
                    }
                });
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

    private void getCityData(String selectedStateId) {
        animateLoaderIcon();
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            if (!selectedStateId.isEmpty()) {
                RequestBody requestBody = new FormBody.Builder()
                        .add("StateORProvinceId", selectedStateId)
                        .build();
                Call<GetCityResponse> call = apiUtils.getAPIService(requireActivity()).getCities(requestBody);
                call.enqueue(new Callback<GetCityResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GetCityResponse> call, @NonNull Response<GetCityResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            loaderLayout.setVisibility(View.GONE);

                            if (response.body().Status == 1) {
                                Log.e(TAG, "onResponse: status is not null");
                                cityIdList = response.body().data;

                                CityNameList.add("Select City");
                                for (int i = 0; i < response.body().data.size(); i++) {
                                    CityNameList.add(response.body().data.get(i).Name);
                                }

                                cityAdapter = new ArrayAdapter<>(getActivity(),
                                        R.layout.custom_spinnerdesign, R.id.tv_customspinneritem, CityNameList);
                                cityAdapter.setDropDownViewResource(R.layout.custom_spinnerdesign);
                                spCity.setAdapter(cityAdapter);

                                int index = 0;
                                if (ProfileActivity.employeeProfileModel != null &&
                                        ProfileActivity.employeeProfileModel.cityId != null) {
                                    for (int i = 0; i < cityIdList.size(); i++) {
                                        if (cityIdList.get(i).Id.equals(ProfileActivity.employeeProfileModel.cityId)) {
                                            index = i;
                                        }
                                    }
                                    selectedCityId = cityIdList.get(index).Id;
                                    selectedCity = cityIdList.get(index).Name;
                                    Log.e(TAG, "onResponse: selected city is: " + selectedCity);
                                    Log.e(TAG, "onResponse: selected city id is: " + selectedCityId);
                                    Log.e(TAG, "onResponse: selected city index is: " + index);
                                } else {
                                    Log.e(TAG, "onResponse: model is null");
                                }
                                spCity.setSelection(index + 1);

                            } else {
                                Log.e(TAG, "onResponse: status is null");
                            }
                        } else {
                            loaderLayout.setVisibility(View.GONE);
                            Log.e(TAG, "onResponse: response is not successfull");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GetCityResponse> call, @NonNull Throwable t) {
                        loaderLayout.setVisibility(View.GONE);
                        Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());
                    }
                });
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