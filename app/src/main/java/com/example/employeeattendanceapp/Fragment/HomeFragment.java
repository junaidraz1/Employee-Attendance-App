package com.example.employeeattendanceapp.Fragment;

import static android.content.Context.LOCATION_SERVICE;
import static android.media.CamcorderProfile.get;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.employeeattendanceapp.Activity.AttendanceActivity;
import com.example.employeeattendanceapp.Activity.EditProfileActivity;
import com.example.employeeattendanceapp.Activity.LeavesActivity;
import com.example.employeeattendanceapp.Activity.ProfileActivity;
import com.example.employeeattendanceapp.DB.DBManager;
import com.example.employeeattendanceapp.DB.DatabaseHelper;
import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Listeners.DialogClickListener;
import com.example.employeeattendanceapp.Manager.ConnectionManager;
import com.example.employeeattendanceapp.Manager.PrefsManager;
import com.example.employeeattendanceapp.Model.Request.SubmitAttendanceRequest;
import com.example.employeeattendanceapp.Model.Response.AttachFileResponse;
import com.example.employeeattendanceapp.Model.Response.EmployeeProfileResponse;
import com.example.employeeattendanceapp.Model.Response.SubmitAttendanceResponse;
import com.example.employeeattendanceapp.Model.Response.UpdateProfileResponse;
import com.example.employeeattendanceapp.R;
import com.example.employeeattendanceapp.Retrofit.apiUtils;
import com.example.employeeattendanceapp.Utils.RemoteConfigs;
import com.example.employeeattendanceapp.Utils.Utility;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.rishabhharit.roundedimageview.RoundedImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */


public class HomeFragment extends Fragment {
    String TAG = "HomeFragment", addressVal = "", empId = "", empNum = "", filePath = "", attachmentServerPath = "";
    Uri destination;
    boolean isCheckedIn;
    boolean isCheckedOut;

    TextView tvGreetings, tvName, tvDesignation, tvAtdStatus;
    ImageView ivLoader;
    RoundedImageView rivProfilePic;
    RelativeLayout menuLayout, leaveLayout, loaderLayout, attendanceLayout, editProfileLayout;
    Button checkinBtn, checkoutBtn;
    DBManager dbManager;

    LocationManager locationManager;
    PrefsManager prefsManager;

    SubmitAttendanceRequest submitAttendanceRequest;
    EmployeeProfileResponse updateProfileModel;

    int placeholder;

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tvGreetings = root.findViewById(R.id.tv_greeting);
        tvName = root.findViewById(R.id.tv_empName);
        tvDesignation = root.findViewById(R.id.tv_designation);
        tvAtdStatus = root.findViewById(R.id.tv_atdStatus);
        ivLoader = root.findViewById(R.id.imageLoader);
        rivProfilePic = root.findViewById(R.id.iv_profilePic);
        loaderLayout = root.findViewById(R.id.rl_loader);
        menuLayout = root.findViewById(R.id.rl_menu);
        attendanceLayout = root.findViewById(R.id.rl_attendance);
        editProfileLayout = root.findViewById(R.id.rl_editProfile);
        leaveLayout = root.findViewById(R.id.rl_leave);
        checkinBtn = root.findViewById(R.id.btn_checkIn);
        checkoutBtn = root.findViewById(R.id.btn_checkOut);

        placeholder = R.drawable.placeholder;
        prefsManager = new PrefsManager(requireActivity());
        updateProfileModel = new EmployeeProfileResponse();

        if (Utility.getSharedInstance().timeConstraint()) {
            isCheckedIn = false;
            isCheckedOut = false;
        }

        if (prefsManager.getAtdStatus().equals("0")) {
            tvAtdStatus.setText("Checked In");
            isCheckedIn = true;

        } else if (prefsManager.getAtdStatus().equals("1")) {
            tvAtdStatus.setText("Checked Out");
            isCheckedIn = true;
            isCheckedOut = true;
        } else {
            tvAtdStatus.setText("Not Checked In");
        }

        dbManager = new DBManager(requireActivity());
        dbManager.open();

        Cursor cursor = dbManager.fetchProfileParams();
        if (cursor.moveToFirst()) {
            empId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID));
            empNum = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMPLOYEE_NUMBER));
        }
        cursor.close();

        submitAttendanceRequest = new SubmitAttendanceRequest();

        /**
         * menu layout click listener
         */
        menuLayout.setOnClickListener(v -> {
            if (getActivity() != null) {
                DialogHandler.getSharedInstance().popmenu(v, getActivity());
            }
        });

        /**
         * leave layout click listener
         */
        leaveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LeavesActivity.class);
                startActivity(intent);
            }
        });

        /**
         * attendance layout click listener
         */
        attendanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AttendanceActivity.class);
                startActivity(intent);
            }
        });

        /**
         * upload profile picture layout click listener
         */
        editProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmployeeProfile();
            }
        });


        /**
         * check in button click listener
         */
        checkinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                    locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Log.e("TAG", "onClick: if");
                        OnGPS();
                    } else {
                        Log.e("TAG", "onClick: else");
                        if (!isCheckedIn) {
                            getLocation("0");
                        } else {
                            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Check In", "Ok", "You are already checked in for today", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    alertDialog.dismiss();
                                }
                            });
                        }
                    }
                }
            }
        });

        /**
         * check out button click listener
         */
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                    locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Log.e("TAG", "onClick: if");
                        OnGPS();
                    } else {
                        Log.e("TAG", "onClick: else");
                        if (!isCheckedIn) {
                            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Check Out", "Ok", "You are not checked in for today. Please check in before you check out", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    alertDialog.dismiss();
                                }
                            });
                        } else if (isCheckedOut) {
                            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Check Out", "Ok", "You already checked out for today", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    alertDialog.dismiss();
                                }
                            });
                        } else {
                            getLocation("1");
                        }
                    }
                }
            }
        });

        return root;
    }

    private void OnGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage("Enable Location ").setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation(String atdState) {
        showGifLoader();
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            Log.e("TAG", "getLocation: if");
        } else {
            Log.e("TAG", "getLocation: else");
            try {
                Log.e("TAG", "Try: ");
                locationManager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        Thread thread = new Thread(() -> {
                            try {
                                Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                addressVal = addresses.get(0).getAddressLine(0);

                                Log.e(TAG, "onLocationChanged: address is: " + addressVal);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        thread.start();

                        if (!addressVal.isEmpty()) {
                            loaderLayout.setVisibility(View.GONE);
                            Log.e(TAG, "onLocationChanged: inside if");
                            String lat = String.valueOf(location.getLatitude());
                            String lon = String.valueOf(location.getLongitude());
                            Log.e(TAG, "onLocationChanged: lat is: " + lat);
                            Log.e(TAG, "onLocationChanged: lon is: " + lon);
                            locationManager.removeUpdates(this);

                            if ((!lat.isEmpty() && !lon.isEmpty() && !addressVal.isEmpty()) && !atdState.isEmpty()) {
                                showSubmitDialog(lat, lon, addressVal, atdState);
                            } else {
                                Log.e(TAG, "onLocationChanged: Lat, long and address value is empty");
                            }
                        }
                    }

                    @Override
                    public void onProviderEnabled(@NonNull String provider) {

                    }

                    @Override
                    public void onProviderDisabled(@NonNull String provider) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showSubmitDialog(String lat, String lon, String addressVal, String AtdState) {
        String currentTime = new SimpleDateFormat("HH:mm a", Locale.getDefault()).format(new Date());

        String stateMsg = "";
        if (AtdState.equals("0")) {
            stateMsg = "check in";
        } else if (AtdState.equals("1")) {
            stateMsg = "check out";
        }

        DialogHandler.getSharedInstance().genericDiaglog2btn(requireActivity(), true, "Mark Attendance",
                "Do you want to " + stateMsg + " for today at: " + currentTime, "Yes", "No", new DialogClickListener() {
                    @Override
                    public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                        if (status.equals("1")) {
                            submitAttendanceRequest.userId = empId;
                            submitAttendanceRequest.empNumber = empNum;
                            submitAttendanceRequest.locationLat = lat;
                            submitAttendanceRequest.locationLong = lon;
                            submitAttendanceRequest.locationAddress = addressVal;
                            submitAttendanceRequest.atdState = AtdState;
                            alertDialog.dismiss();
                            submitAttendance(submitAttendanceRequest);

                        } else {
                            alertDialog.dismiss();
                        }
                    }
                });
    }


    /**
     * Method to submit attendance
     *
     * @param submitAttendanceRequest
     */
    private void submitAttendance(SubmitAttendanceRequest submitAttendanceRequest) {
        showGifLoader();
        if (getActivity() != null) {
            if (ConnectionManager.getSharedInstance().isNetworkAvailable(getActivity())) {
                Call<SubmitAttendanceResponse> call = apiUtils.getAPIService(getActivity()).submitAttendance(submitAttendanceRequest);
                call.enqueue(new Callback<SubmitAttendanceResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubmitAttendanceResponse> call, @NonNull Response<SubmitAttendanceResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().status == 1) {
                                loaderLayout.setVisibility(View.GONE);
                                if (!response.body().errorMessage.isEmpty()) {
                                    DialogHandler.getSharedInstance().genericDialogSuccess(getActivity(), true, "Mark Attendance", response.body().errorMessage,
                                            "OK", new DialogClickListener() {
                                                @Override
                                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                                    alertDialog.dismiss();
                                                }
                                            });
                                } else {
                                    DialogHandler.getSharedInstance().genericDialogSuccess(getActivity(), true, "Mark Attendance", "Attendance submitted successfully",
                                            "OK", new DialogClickListener() {
                                                @Override
                                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                                    alertDialog.dismiss();
                                                }
                                            });
                                }

                                if (submitAttendanceRequest.atdState.equalsIgnoreCase("0")) {
                                    isCheckedIn = true;
                                    prefsManager.setAtdStatus("0");
                                    tvAtdStatus.setText("Checked In");
                                } else {
                                    isCheckedOut = true;
                                    prefsManager.setAtdStatus("1");
                                    tvAtdStatus.setText("Checked Out");
                                }

                            } else {
                                loaderLayout.setVisibility(View.GONE);
                                Log.e(TAG, "onResponse: status failure with status: " + response.body().status);
                            }
                        } else {
                            loaderLayout.setVisibility(View.GONE);
                            Log.e(TAG, "onResponse: response is not successful");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubmitAttendanceResponse> call, @NonNull Throwable t) {
                        loaderLayout.setVisibility(View.GONE);
                        Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());

                    }
                });
            } else {
                loaderLayout.setVisibility(View.GONE);
                DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Check In"
                        , "Ok", "Please check your internet connection and try again", (status, message, alertDialog) -> alertDialog.dismiss());
            }
        }
    }

    /**
     * on activity result
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 11: {
                if (data != null) {
                    //  ArrayList<String> photos = data.getStringArrayListExtra(GligarPicker.IMAGES_RESULT);

                    //String[] photos = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
                    //Uri fileUri = Uri.parse(photos[0]);

                    Uri fileUri = data.getData();
                    filePath = fileUri.getPath();
                    destination = Uri.fromFile(new File(requireActivity().getCacheDir(), "cropped"));
                    /*CropImage.activity(Uri.fromFile(new File(filePath)))
                            .start(EditProfile2Activity.this);*/
//                    Glide.with(requireActivity())
//                            .load(filePath)
//                            .placeholder(R.drawable.placeholder)
//                            .into(rivProfilePic);
                    Log.e("uri", fileUri + "");
                    attachFile(filePath);

                } else {
                    Log.e(TAG, "Data is Null");
                }
                break;
            }
        }
    }

    private void attachFile(String path) {
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            File file = new File(path);

            Log.e(TAG, "attachHealthReport: file is: " + file);
            Log.e(TAG, "attachHealthReport: path is: " + path);

            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToSend = MultipartBody.Part.createFormData("fileUpload", file.getName(), requestFile);

            Call<AttachFileResponse> call = apiUtils.getAPIService(requireActivity()).attachFile(fileToSend);
            call.enqueue(new Callback<AttachFileResponse>() {
                @Override
                public void onResponse(@NonNull Call<AttachFileResponse> call, @NonNull Response<AttachFileResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().status >= 1) {

                            attachmentServerPath = response.body().filePath;

                            if (!attachmentServerPath.isEmpty()) {
                                Glide.with(requireActivity())
                                        .load(RemoteConfigs.baseurlLive + attachmentServerPath)
                                        .placeholder(placeholder)
                                        //.error(R.drawable.placeholder)
                                        .into(rivProfilePic);
                                updateProfileModel.picturePath = attachmentServerPath;
                                dbManager.updateProfile(updateProfileModel);

                                if (updateProfileModel != null) {

                                    updateProfileModel.picturePath = attachmentServerPath;

                                    if (updateProfileModel.cNICNumber == null) {
                                        updateProfileModel.cNICNumber = "";
                                    }
                                    if (updateProfileModel.userName == null) {
                                        updateProfileModel.userName = "";
                                    }
                                    if (updateProfileModel.prefix == null) {
                                        updateProfileModel.prefix = "";
                                    }
                                    if (updateProfileModel.title == null) {
                                        updateProfileModel.title = "";
                                    }
                                    if (updateProfileModel.personTitleId == null) {
                                        updateProfileModel.personTitleId = "";
                                    }
                                    if (updateProfileModel.firstName == null) {
                                        updateProfileModel.firstName = "";
                                    }
                                    if (updateProfileModel.middleName == null) {
                                        updateProfileModel.middleName = "";
                                    }
                                    if (updateProfileModel.lastName == null) {
                                        updateProfileModel.lastName = "";
                                    }
                                    if (updateProfileModel.bloodGroup == null) {
                                        updateProfileModel.bloodGroup = "";
                                    }
                                    if (updateProfileModel.bloodGroupId == null) {
                                        updateProfileModel.bloodGroupId = "";
                                    }
                                    if (updateProfileModel.gender == null) {
                                        updateProfileModel.gender = "";
                                    }
                                    if (updateProfileModel.genderId == null) {
                                        updateProfileModel.genderId = "";
                                    }
                                    if (updateProfileModel.maritalStatus == null) {
                                        updateProfileModel.maritalStatus = "";
                                    }
                                    if (updateProfileModel.maritalStatusId == null) {
                                        updateProfileModel.maritalStatusId = "";
                                    }
                                    if (updateProfileModel.relationshipTypeId == null) {
                                        updateProfileModel.relationshipTypeId = "";
                                    }
                                    if (updateProfileModel.relationshipTypeName == null) {
                                        updateProfileModel.relationshipTypeName = "";
                                    }
                                    if (updateProfileModel.dateOfBirth == null) {
                                        updateProfileModel.dateOfBirth = "";
                                    }
                                    if (updateProfileModel.guardianName == null) {
                                        updateProfileModel.guardianName = "";
                                    }
                                    if (updateProfileModel.country == null) {
                                        updateProfileModel.country = "";
                                    }
                                    if (updateProfileModel.countryId == null) {
                                        updateProfileModel.countryId = "";
                                    }
                                    if (updateProfileModel.stateOrProvince == null) {
                                        updateProfileModel.stateOrProvince = "";
                                    }
                                    if (updateProfileModel.stateOrProvinceId == null) {
                                        updateProfileModel.stateOrProvinceId = "";
                                    }
                                    if (updateProfileModel.city == null) {
                                        updateProfileModel.city = "";
                                    }
                                    if (updateProfileModel.cityId == null) {
                                        updateProfileModel.cityId = "";
                                    }
                                    if (updateProfileModel.address == null) {
                                        updateProfileModel.address = "";
                                    }
                                    if (updateProfileModel.cellNumber == null) {
                                        updateProfileModel.cellNumber = "";
                                    }
                                    if (updateProfileModel.telephoneNumber == null) {
                                        updateProfileModel.telephoneNumber = "";
                                    }
                                    if (updateProfileModel.email == null) {
                                        updateProfileModel.email = "";
                                    }
                                    if (updateProfileModel.nOKFirstName == null) {
                                        updateProfileModel.nOKFirstName = "";
                                    }
                                    if (updateProfileModel.nOKLastName == null) {
                                        updateProfileModel.nOKLastName = "";
                                    }
                                    if (updateProfileModel.nOKRelation == null) {
                                        updateProfileModel.nOKRelation = "";
                                    }
                                    if (updateProfileModel.nOKRelationshipTypeId == null) {
                                        updateProfileModel.nOKRelationshipTypeId = "";
                                    }
                                    if (updateProfileModel.nOKCNICNumber == null) {
                                        updateProfileModel.nOKCNICNumber = "";
                                    }
                                    if (updateProfileModel.nOKCellNumber == null) {
                                        updateProfileModel.nOKCellNumber = "";
                                    }
                                    if (updateProfileModel.age == null) {
                                        updateProfileModel.age = "";
                                    }
                                    if (updateProfileModel.department == null) {
                                        updateProfileModel.department = "";
                                    }
                                    if (updateProfileModel.departmentId == null) {
                                        updateProfileModel.departmentId = "";
                                    }

                                    updateProfile(updateProfileModel.cNICNumber, updateProfileModel.userName,
                                            updateProfileModel.personTitleId, updateProfileModel.title, updateProfileModel.prefix,
                                            updateProfileModel.firstName, updateProfileModel.middleName,
                                            updateProfileModel.lastName, updateProfileModel.gender,
                                            updateProfileModel.genderId, updateProfileModel.relationshipTypeId,
                                            updateProfileModel.relationshipTypeName, updateProfileModel.guardianName,
                                            updateProfileModel.maritalStatusId, updateProfileModel.maritalStatus,
                                            updateProfileModel.dateOfBirth, updateProfileModel.picturePath,
                                            updateProfileModel.country, updateProfileModel.countryId,
                                            updateProfileModel.stateOrProvince, updateProfileModel.stateOrProvinceId,
                                            updateProfileModel.city, updateProfileModel.cityId,
                                            updateProfileModel.address, updateProfileModel.cellNumber,
                                            updateProfileModel.telephoneNumber, updateProfileModel.email,
                                            updateProfileModel.nOKFirstName, updateProfileModel.nOKLastName,
                                            updateProfileModel.nOKRelation, updateProfileModel.nOKRelationshipTypeId,
                                            updateProfileModel.nOKCNICNumber, updateProfileModel.nOKCellNumber,
                                            updateProfileModel.bloodGroup, updateProfileModel.bloodGroupId,
                                            updateProfileModel.age, updateProfileModel.departmentId,
                                            updateProfileModel.department);
                                }

                            }
                        } else {
                            Log.e(TAG, "onResponse: status is less than 1");
                        }

                    } else {
                        Log.e(TAG, "onResponse: body is null");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AttachFileResponse> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                }
            });

        } else {
            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Upload Failed"
                    , "Ok", "Please check your internet connection and try again", (status, message, alertDialog) -> alertDialog.dismiss());
        }
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
            showGifLoader();
            RequestBody requestBody = new FormBody.Builder()
                    .add("UserId", empId)
                    .add("CNICNumber", cNICNumber)
                    .add("UserName", userName)
                    .add("PersonTitleId", personTitleId)
                    .add("Title", title)
                    .add("Prefix", prefix)
                    .add("FirstName", firstName)
                    .add("MiddleName", middleName)
                    .add("LastName", lastName)
                    .add("Gender", gender)
                    .add("GenderId", genderId)
                    .add("RelationshipTypeId", relationshipTypeId)
                    .add("RelationshipTypeName", relationshipTypeName)
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
                    .add("CityId", cityId)
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
                            DialogHandler.getSharedInstance().genericDialogSuccess(requireActivity(), true, "Profile Updated", "Profile picture updated successfully", "OK", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    //getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
                                    //getActivity().finishAffinity();
                                    alertDialog.dismiss();
                                }
                            });
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
            if (!empId.isEmpty()) {
//                animateLoaderIcon();
                Log.e(TAG, "getEmployeeProfile: api call");
                RequestBody requestBody = new FormBody.Builder()
                        .add("UserId", empId)
                        .build();
                Call<EmployeeProfileResponse> call = apiUtils.getAPIService(requireActivity()).getEmployeeProfile(requestBody);
                call.enqueue(new Callback<EmployeeProfileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<EmployeeProfileResponse> call, @NonNull Response<EmployeeProfileResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            loaderLayout.setVisibility(View.GONE);

                            if (response.body().status != null && response.body().status == 1) {

                                updateProfileModel = response.body();

                                dbManager.insertProfile(updateProfileModel);

                                Glide.with(requireActivity())
                                        .load(RemoteConfigs.baseurlLive + updateProfileModel.picturePath)
                                        .placeholder(R.drawable.placeholder)
                                        .into(rivProfilePic);

                                ImagePicker.with(HomeFragment.this)
                                        .cropSquare()                    //Crop image(Optional), Check Customization for more option
                                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                        .maxResultSize(512, 512)    //Final image resolution will be less than 1080 x 1080(Optional)
                                        .galleryOnly()    //User can only select image from Gallery
                                        .start(11);

//                                if (getActivity() != null) {
//                                    DialogHandler.getSharedInstance().genericDialogSuccess(getActivity(), true, "Profile Updated", "Profile Updated Successfully", "OK", new DialogClickListener() {
//                                        @Override
//                                        public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
//                                            //getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
//                                            //getActivity().finishAffinity();
//                                            getActivity().onBackPressed();
//                                            alertDialog.dismiss();
//                                        }
//                                    });
//                                }

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

    /**
     * On Resume
     */
    @SuppressLint("Range")
    @Override
    public void onResume() {
        super.onResume();
        try {
            Cursor cursor = dbManager.fetchProfileParams();
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FULL_NAME));
                String designation = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESIGNATION));
                String path = cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMAGE_PATH));

                tvName.setText(name);
                tvGreetings.setText(Utility.getSharedInstance().getWishingMessage());
                tvDesignation.setText(designation);

                // Log.e("Home image url", RemoteConfigs.baseurlLive + image);
                try {
//                    if (gender != null && !gender.equalsIgnoreCase("null")) {
//                        if (gender.equalsIgnoreCase("male")) {
//                            placeholder = R.drawable.ic_male;
//                        } else if (gender.equalsIgnoreCase("female")) {
//                            placeholder = R.drawable.ic_female;
//                        }
//                    }

                    Glide.with(requireActivity())
                            .load(RemoteConfigs.baseurlLive + path)
                            .placeholder(placeholder)
                            //.error(R.drawable.placeholder)
                            .into(rivProfilePic);

                } catch (Exception e) {
                    Log.e(TAG, "Glide Exp: " + e.toString());
                }
                Log.e("HomeFrag", RemoteConfigs.baseurlLive + path);
            }
            cursor.close();

        } catch (Exception e) {
            Log.e("Home EXP", e.toString());
        }
    }


    /**
     * show gif loader
     */
    private void showGifLoader() {
        loaderLayout.setVisibility(View.VISIBLE);
        if (getActivity() != null) {
            Glide.with(getActivity())
                    .load(R.drawable.ic_ihclogo)
                    .into(ivLoader);
            ivLoader.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                    R.anim.blink_animation));
        }
    }
}