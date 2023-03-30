package com.example.employeeattendanceapp.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.employeeattendanceapp.Activity.LeavesActivity;
import com.example.employeeattendanceapp.DB.DBManager;
import com.example.employeeattendanceapp.DB.DatabaseHelper;
import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Listeners.DialogClickListener;
import com.example.employeeattendanceapp.Manager.ConnectionManager;
import com.example.employeeattendanceapp.Model.DepartmentData;
import com.example.employeeattendanceapp.Model.LeaveTypeData;
import com.example.employeeattendanceapp.Model.LineManagerData;
import com.example.employeeattendanceapp.Model.Request.SubmitLeaveRequest;
import com.example.employeeattendanceapp.Model.Response.AttachFileResponse;
import com.example.employeeattendanceapp.Model.Response.GetLeaveBalanceResponse;
import com.example.employeeattendanceapp.Model.Response.GetLeaveTypeResponse;
import com.example.employeeattendanceapp.Model.Response.GetLineManagerResponse;
import com.example.employeeattendanceapp.Model.Response.SubmitLeaveResponse;
import com.example.employeeattendanceapp.Model.SubDepartmentData;
import com.example.employeeattendanceapp.R;
import com.example.employeeattendanceapp.Retrofit.apiUtils;
import com.example.employeeattendanceapp.Utils.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class ApplyLeaveFragment extends Fragment {

    String TAG = "ApplyLeaveFragment", fromDate = "", toDate = "", fromTime = "", toTime = "", fromSDate = "",
            currentDate = "", currentTime = "", selectedLeaveTypeName = "", selectLeaveTypeId = "",
            selectLineManagerName = "", selectLineManagerId = "", userId = "", orgId = "", attachmentMime = "",
            attachmentServerPath = "", days = "";
    ;
    LinearLayout dateLayout, timeLayout, leaveBalanceLayout;
    RelativeLayout fromDateLayout, toDateLayout, fromTimeLayout, toTimeLayout, loaderLayout, attachmentLayout, fromDateShortLeaveLayout;
    ImageView imageLoader, ivAttachment;
    TextView tvFromDate, tvToDate, tvFromShortLeaveDate, tvFromTime, tvToTime, tvTotalLeaves, tvAvailedLeaves, tvRemainingLeave, tvleaveDays;
    CheckBox cbShortLeave;
    EditText etLeaveDescription, etLeaveTitle, etLineManagerRemarks;
    Button submitLeaveBtn;
    Spinner spLeaveType, spLineManager, spDepartment, spSubDepartment;

    DBManager dbManager;
    SubmitLeaveRequest submitLeaveRequest;

    ArrayList<String> leaveTypeNameList, lineManagerNameList, departmentNameList, subDepartmentNameList;
    ArrayList<LeaveTypeData> leaveTypeIdList;
    ArrayList<LineManagerData> lineManagerIdList;
    ArrayList<DepartmentData> departmentIdList;
    ArrayList<SubDepartmentData> subDepartmentIdList;
    ArrayAdapter<String> leaveTypeAdapter, lineManagerAdapter, departmentAdapter, subDepartmentAdapter;

    int mYear2, mMonth2, mDay2, spinnerPosition, totalLeaves, availedLeaves, remainingLeaves;


    /**
     * New Instance
     *
     * @return
     */
    public static ApplyLeaveFragment newInstance() {
        return new ApplyLeaveFragment();
    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_apply_leave, container, false);

        dateLayout = root.findViewById(R.id.ll_date);
        timeLayout = root.findViewById(R.id.ll_time);
        fromDateLayout = root.findViewById(R.id.layout_fromDate);
        fromDateShortLeaveLayout = root.findViewById(R.id.layout_fromSDate);
        toDateLayout = root.findViewById(R.id.layout_toDate);
        fromTimeLayout = root.findViewById(R.id.layout_fromTime);
        toTimeLayout = root.findViewById(R.id.layout_toTime);
        loaderLayout = root.findViewById(R.id.rl_loader);
        leaveBalanceLayout = root.findViewById(R.id.ll_leaveBalance);
        attachmentLayout = root.findViewById(R.id.rl_uploadImage);
        imageLoader = root.findViewById(R.id.imageLoader);
        ivAttachment = root.findViewById(R.id.iv_addAttach);
        tvFromDate = root.findViewById(R.id.txt_fromDate);
        tvFromShortLeaveDate = root.findViewById(R.id.txt_fromSDate);
        tvToDate = root.findViewById(R.id.txt_toDate);
        tvFromTime = root.findViewById(R.id.txt_fromTime);
        tvToTime = root.findViewById(R.id.txt_toTime);
        tvTotalLeaves = root.findViewById(R.id.tv_totalLeaves);
        tvAvailedLeaves = root.findViewById(R.id.tv_availedLeaves);
        tvRemainingLeave = root.findViewById(R.id.tv_remainingLeaves);
        tvleaveDays = root.findViewById(R.id.tv_numOfDays);
        cbShortLeave = root.findViewById(R.id.cb_shortLeave);
        spLeaveType = root.findViewById(R.id.sp_leaveType);
        spLineManager = root.findViewById(R.id.sp_lineManager);
        submitLeaveBtn = root.findViewById(R.id.btn_submit);
        etLeaveDescription = root.findViewById(R.id.et_description);
        etLeaveTitle = root.findViewById(R.id.et_leaveTitle);
        etLineManagerRemarks = root.findViewById(R.id.et_lineManagerRemarks);
//        spDepartment = root.findViewById(R.id.sp_department);
//        spSubDepartment = root.findViewById(R.id.sp_subDepartment);

        submitLeaveRequest = new SubmitLeaveRequest();
        dbManager = new DBManager(requireActivity());
        dbManager.open();

        Cursor cursor = dbManager.fetchProfileParams();
        if (cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID));
            orgId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORGANIZATION_ID));
            cursor.close();
        }

        leaveTypeNameList = new ArrayList<>();
        lineManagerNameList = new ArrayList<>();
        leaveTypeIdList = new ArrayList<>();
        lineManagerIdList = new ArrayList<>();

        /**
         * Set Current Date, time in leave from date and from time by default
         */
        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        tvFromDate.setText(currentDate);
        tvFromTime.setText(currentTime);
        tvFromShortLeaveDate.setText(currentDate);

        cbShortLeave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dateLayout.setVisibility(View.GONE);
                    timeLayout.setVisibility(View.VISIBLE);
                } else {
                    dateLayout.setVisibility(View.VISIBLE);
                    timeLayout.setVisibility(View.GONE);
                }
            }
        });

        /**
         * From Date Layout click listener
         */
        fromDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("fromDate");
            }
        });

        /**
         * From short leave Date Layout click listener
         */
        fromDateShortLeaveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("fromDate");
            }
        });

        /**
         * To Date Layout click listener
         */
        toDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("toDate");
            }
        });


        /**
         * From Time Layout click listener
         */
        fromTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker("fromTime");
            }
        });

        /**
         * To Time Layout click listener
         */
        toTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker("toTime");
            }
        });

        /**
         * API call to get leave types
         */
        getLeaveType();

        /**
         * API call to get line manager
         */
        if (!userId.isEmpty() && !orgId.isEmpty()) {
            getLineManager(userId, orgId);
        }

        /**
         * Method that contains spinner selection implementation
         */
        spinnerSelectListeners();

        /**
         * Attach file click listener
         */
        attachmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = Utility.getSharedInstance().checkReadStoragePermission(requireActivity());

                if (result) {
                    browseClick(102);
                }

            }
        });

        /**
         * submit leave functionality
         */
        submitLeaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cbShortLeave.isChecked()) {
                    if (tvFromTime.getText().toString().isEmpty()) {
                        Toast.makeText(requireActivity(), "Please select leave starting time", Toast.LENGTH_SHORT).show();

                    } else if (tvToTime.getText().toString().isEmpty()) {
                        Toast.makeText(requireActivity(), "Please select leave ending time", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (tvFromDate.getText().toString().isEmpty()) {
                        Toast.makeText(requireActivity(), "Please select leave starting date", Toast.LENGTH_SHORT).show();

                    } else if (tvToDate.getText().toString().isEmpty()) {
                        Toast.makeText(requireActivity(), "Please select leave ending date", Toast.LENGTH_SHORT).show();

                    }
                }
                if (selectLeaveTypeId.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please select leave type", Toast.LENGTH_SHORT).show();

                } else if (etLeaveTitle.getText().toString().isEmpty()) {
                    Toast.makeText(requireActivity(), "Please enter leave title", Toast.LENGTH_SHORT).show();

                } else if (etLeaveDescription.getText().toString().isEmpty()) {
                    Toast.makeText(requireActivity(), "Please describe reason for leave", Toast.LENGTH_SHORT).show();

                } else if (selectLineManagerId.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please select line manager", Toast.LENGTH_SHORT).show();

                } else if (totalLeaves == 0) {
                    DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Apply Leave", "Ok",
                            "No leaves allocated for " + selectedLeaveTypeName + ". Please choose another leave type", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    alertDialog.dismiss();
                                }
                            });
                } else if (remainingLeaves < 1) {
                    Log.e(TAG, "onClick: value is " + remainingLeaves);
                    DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Apply Leave", "Ok",
                            "No remaining leave balance for " + selectedLeaveTypeName + ". You have already availed your leaves", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    alertDialog.dismiss();
                                }
                            });
                }
//                else {
//                    if (fromDate.isEmpty()) {
//                        days = calculateDays(currentDate, "", toDate);
//
//                    } else {
//                        days = calculateDays("", fromDate, toDate);
//                    }
//
//                    if (Integer.parseInt(days) > remainingLeaves) {
//                        DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Apply Leave", "Ok",
//                                "Your remaining leaves for " + selectedLeaveTypeName + " are less than allocated leaves", new DialogClickListener() {
//                                    @Override
//                                    public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
//                                        alertDialog.dismiss();
//                                    }
//                                });
//                    } else {
//
//
//                        Toast.makeText(requireActivity(), "Leave submitted", Toast.LENGTH_SHORT).show();
//                    }
//                }

                else {

                    /**
                     * call to method to calculate days based on to and from dates
                     */
                    if (fromDate.isEmpty()) {
                        days = calculateDays(currentDate, "", toDate);

                    } else {
                        days = calculateDays("", fromDate, toDate);
                    }

                    /**
                     * check if calculated days for which leave is applied, are greater or less than allocated leaves
                     */
                    if (Integer.parseInt(days) > totalLeaves) {
                        DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Apply Leave", "Ok",
                                "Applying leave for " + Integer.parseInt(days) + " days, leaves allowed for "
                                        + selectedLeaveTypeName + " are of " + totalLeaves + " days", new DialogClickListener() {
                                    @Override
                                    public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                        alertDialog.dismiss();
                                    }
                                });
                    } else {
                        submitLeaveRequest.leaveTypeId = selectLeaveTypeId;
                        submitLeaveRequest.leaveTitle = etLeaveTitle.getText().toString();
                        submitLeaveRequest.leaveDescription = etLeaveDescription.getText().toString();
                        submitLeaveRequest.leaveTypeId = selectLeaveTypeId;

                        if (cbShortLeave.isChecked()) {

                            /**
                             * check if user selected new from date
                             */
                            if (fromDate.isEmpty()) {
                                submitLeaveRequest.leaveEndDateTime = currentDate + "T" + toTime;

                                /**
                                 * check if user selected new from time
                                 */
                                if (fromTime.isEmpty()) {
                                    submitLeaveRequest.leaveStartDateTime = currentDate + "T" + currentTime;
                                } else {
                                    submitLeaveRequest.leaveStartDateTime = currentDate + "T" + fromTime;
                                }
                            } else {
                                submitLeaveRequest.leaveEndDateTime = fromDate + "T" + toTime;
                                if (fromTime.isEmpty()) {
                                    submitLeaveRequest.leaveStartDateTime = fromDate + "T" + currentTime;
                                } else {
                                    submitLeaveRequest.leaveStartDateTime = fromDate + "T" + fromTime;
                                }
                            }
                            submitLeaveRequest.leaveNumberofDays = "";
                            submitLeaveRequest.isShortLeave = 1;

                        } else {
                            if (fromDate.isEmpty()) {
                                submitLeaveRequest.leaveStartDateTime = currentDate;

                            } else {
                                submitLeaveRequest.leaveStartDateTime = fromDate;
                            }

                            submitLeaveRequest.leaveEndDateTime = toDate;
                            if (fromDate.isEmpty()) {
                                days = calculateDays(currentDate, "", toDate);

                            } else {
                                days = calculateDays("", fromDate, toDate);
                            }

                            submitLeaveRequest.leaveNumberofDays = days;
                            submitLeaveRequest.isShortLeave = 0;
                        }

                        if (!attachmentServerPath.isEmpty()) {
                            submitLeaveRequest.leaveAttachmentPath = attachmentServerPath;

                        } else {
                            submitLeaveRequest.leaveAttachmentPath = "";
                        }

                        submitLeaveRequest.leaveUserId = userId;
                        submitLeaveRequest.leaveIsActive = "1";
                        submitLeaveRequest.leaveCreatedById = userId;
                        submitLeaveRequest.leaveModifiedById = userId;
                        submitLeaveRequest.lineManagerId = selectLineManagerId;
                        submitLeaveRequest.lineManagerRemarks = etLineManagerRemarks.getText().toString();

                        /**
                         * API call to submit leave
                         */
                        submitLeaveCall(submitLeaveRequest);

                    }
                }
            }
        });

        return root;
    }

    private String calculateDays(String currentdate, String fromdate, String todate) {
        if (fromdate.isEmpty()) {
            days = Utility.getSharedInstance().calculateDays(currentdate, todate);

        } else {
            days = Utility.getSharedInstance().calculateDays(fromdate, todate);
        }
        return days;
    }

    private void submitLeaveCall(SubmitLeaveRequest submitLeaveRequest) {
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            showGifLoader();
            Call<SubmitLeaveResponse> call = apiUtils.getAPIService(requireActivity()).submitLeave(submitLeaveRequest);
            call.enqueue(new Callback<SubmitLeaveResponse>() {
                @Override
                public void onResponse(@NonNull Call<SubmitLeaveResponse> call, @NonNull Response<SubmitLeaveResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        loaderLayout.setVisibility(View.GONE);
                        if (response.body().status >= 1) {
                            DialogHandler.getSharedInstance().genericDialogSuccess(requireActivity(), true, "Apply Leave", "Leave submitted successfully",
                                    "Ok", new DialogClickListener() {
                                        @Override
                                        public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                            alertDialog.dismiss();
                                            LeavesActivity.viewPager.setCurrentItem(1, true);
                                        }
                                    });
                        } else {
                            if (!response.body().errorMessage.isEmpty()) {
                                DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Apply Leave", "Ok",
                                        response.body().errorMessage, new DialogClickListener() {
                                            @Override
                                            public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                                alertDialog.dismiss();
                                            }
                                        });
                            } else {
                                DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Apply Leave", "Ok",
                                        "Please try again later", new DialogClickListener() {
                                            @Override
                                            public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                                alertDialog.dismiss();
                                            }
                                        });
                            }
                        }
                    } else {
                        loaderLayout.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: response body is null");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SubmitLeaveResponse> call, @NonNull Throwable t) {
                    loaderLayout.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());
                }
            });
        } else {
            loaderLayout.setVisibility(View.GONE);
            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Apply Leave"
                    , "Ok", "Please check your internet connection and try again", (status, message, alertDialog) -> alertDialog.dismiss());

        }
    }

    private void getLeaveType() {
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            showGifLoader();
            Call<GetLeaveTypeResponse> call = apiUtils.getAPIService(requireActivity()).getLeaveType();
            call.enqueue(new Callback<GetLeaveTypeResponse>() {
                @Override
                public void onResponse(@NonNull Call<GetLeaveTypeResponse> call, @NonNull Response<GetLeaveTypeResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().status >= 1) {
                            loaderLayout.setVisibility(View.GONE);

                            leaveTypeIdList = response.body().leaveTypeData;
                            leaveTypeNameList.add("Select Leave Type");

                            if (leaveTypeIdList != null && leaveTypeIdList.size() > 0) {
                                for (int i = 0; i < leaveTypeIdList.size(); i++) {
                                    leaveTypeNameList.add(leaveTypeIdList.get(i).leaveName);
                                }

                                leaveTypeAdapter = new ArrayAdapter<>(requireActivity(),
                                        R.layout.custom_spinnerdesign, R.id.tv_customspinneritem, leaveTypeNameList);
                                leaveTypeAdapter.setDropDownViewResource(R.layout.custom_spinnerdesign);
                                spLeaveType.setAdapter(leaveTypeAdapter);

                            } else {
                                Log.e(TAG, "onResponse: leaveTypeIdList is null");
                            }
                        } else {
                            loaderLayout.setVisibility(View.GONE);
                            Log.e(TAG, "onResponse: response status is less than 1");
                        }
                    } else {
                        loaderLayout.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: response is not successful");

                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetLeaveTypeResponse> call, @NonNull Throwable t) {
                    loaderLayout.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: response failed" + t.getLocalizedMessage());
                }
            });
        } else {
            loaderLayout.setVisibility(View.GONE);
            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Apply Leave"
                    , "Ok", "Please check your internet connection and try again", (status, message, alertDialog) -> alertDialog.dismiss());
        }
    }

    private void getLineManager(String uId, String orgId) {
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            showGifLoader();

            RequestBody requestBody = new FormBody.Builder()
                    .add("UserId", uId)
                    .add("OrganizationId", orgId)
                    .build();

            Call<GetLineManagerResponse> call = apiUtils.getAPIService(requireActivity()).getLineManager(requestBody);
            call.enqueue(new Callback<GetLineManagerResponse>() {
                @Override
                public void onResponse(@NonNull Call<GetLineManagerResponse> call, @NonNull Response<GetLineManagerResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().status >= 1) {
                            loaderLayout.setVisibility(View.GONE);

                            lineManagerIdList = response.body().lineManagerData;
                            lineManagerNameList.add("Select Line Manager");

                            if (lineManagerIdList != null && lineManagerIdList.size() > 0) {
                                for (int i = 0; i < lineManagerIdList.size(); i++) {
                                    lineManagerNameList.add(lineManagerIdList.get(i).lineManagerName);
                                }

                                lineManagerAdapter = new ArrayAdapter<>(requireActivity(),
                                        R.layout.custom_spinnerdesign, R.id.tv_customspinneritem, lineManagerNameList);
                                lineManagerAdapter.setDropDownViewResource(R.layout.custom_spinnerdesign);
                                spLineManager.setAdapter(lineManagerAdapter);

                            } else {
                                Log.e(TAG, "onResponse: lineManagerIdList is null");
                            }
                        } else {
                            loaderLayout.setVisibility(View.GONE);
                            Log.e(TAG, "onResponse: response status is less than 1");
                        }
                    } else {
                        loaderLayout.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: response is not successful");

                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetLineManagerResponse> call, @NonNull Throwable t) {
                    loaderLayout.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: response failed" + t.getLocalizedMessage());
                }
            });
        } else {
            loaderLayout.setVisibility(View.GONE);
            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Apply Leave"
                    , "Ok", "Please check your internet connection and try again", (status, message, alertDialog) -> alertDialog.dismiss());
        }
    }

    private void spinnerSelectListeners() {
        spLeaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedLeaveTypeName = parent.getItemAtPosition(position).toString();
                    selectLeaveTypeId = leaveTypeIdList.get(--position).leaveId;
                    Log.e(TAG, "titleOnItemSelected: Selected leaveType: " + selectedLeaveTypeName);
                    Log.e(TAG, "titleOnItemSelected: Selected leaveType id: " + selectLeaveTypeId);

                    spinnerPosition = leaveTypeAdapter.getPosition(selectedLeaveTypeName);
                    Log.e(TAG, "spinner leaveType position " + spinnerPosition);

                    if (!selectLeaveTypeId.isEmpty() || !selectLeaveTypeId.equals("")) {
                        getLeaveBalance(selectLeaveTypeId);
                    }
                } else {
                    selectedLeaveTypeName = "";
                    spinnerPosition = leaveTypeAdapter.getPosition(selectedLeaveTypeName);
                    Log.e(TAG, "titleOnItemSelected: Selected leaveType " + selectedLeaveTypeName);
                    Log.e(TAG, "spinner title position " + spinnerPosition);
                    leaveBalanceLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spLineManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectLineManagerName = parent.getItemAtPosition(position).toString();
                    selectLineManagerId = lineManagerIdList.get(--position).lineManagerId;
                    Log.e(TAG, "titleOnItemSelected: Selected lineManager: " + selectLineManagerName);
                    Log.e(TAG, "titleOnItemSelected: Selected lineManager id: " + selectLineManagerId);

                    spinnerPosition = lineManagerAdapter.getPosition(selectLineManagerName);
                    Log.e(TAG, "spinner lineManager position " + spinnerPosition);
                } else {
                    selectLineManagerName = "";
                    spinnerPosition = lineManagerAdapter.getPosition(selectLineManagerName);
                    Log.e(TAG, "titleOnItemSelected: Selected lineManager " + selectLineManagerName);
                    Log.e(TAG, "spinner title position " + spinnerPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getLeaveBalance(String selectLeaveTypeId) {
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            RequestBody requestBody = new FormBody.Builder()
                    .add("UserId", userId)
                    .add("LeaveTypeId", selectLeaveTypeId)
                    .build();

            Call<GetLeaveBalanceResponse> call = apiUtils.getAPIService(requireActivity()).getLeaveBalance(requestBody);
            call.enqueue(new Callback<GetLeaveBalanceResponse>() {
                @Override
                public void onResponse(Call<GetLeaveBalanceResponse> call, Response<GetLeaveBalanceResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().leavesAllocated != null && response.body().leavesAvailed != null) {
                            leaveBalanceLayout.setVisibility(View.VISIBLE);
                            tvTotalLeaves.setText("Allowed: " + response.body().leavesAllocated);
                            tvAvailedLeaves.setText("Availed: " + response.body().leavesAvailed);

                            totalLeaves = Integer.parseInt(response.body().leavesAllocated);
                            availedLeaves = Integer.parseInt(response.body().leavesAvailed);

                            remainingLeaves = totalLeaves - availedLeaves;

                            tvRemainingLeave.setText("Remaining: " + remainingLeaves);
                        }

                    } else {
                        Log.e(TAG, "onResponse: response not successfull");

                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetLeaveBalanceResponse> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());
                }
            });


        } else {

        }

    }

    /**
     * Open Gallery to upload attachment picture
     */
    public void browseClick(int code) {
        Log.e("browseFile", "onClick: inside browseClick");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), code);
        } catch (Exception ex) {
            System.out.println("browseClick :" + ex);//android.content.ActivityNotFoundException ex
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            try {
                Uri uri = data.getData();
                ivAttachment.setImageURI(uri);
                String filename = "";
                String mimeType = requireActivity().getContentResolver().getType(uri);
                if (mimeType == null) {
                    Log.e("mimeType", "null");
                    String path = getPath(getActivity(), uri);
                    if (path != null) {
                        File file = new File(path);
                        filename = file.getName();
                        Log.e("fileCheck", "onActivityResult: file path: " + path);
                    }

                } else {

                    Log.e("mimeType", mimeType);
                    attachmentMime = mimeType;

                    Uri returnUri = data.getData();
                    Cursor returnCursor = getActivity().getContentResolver().query(returnUri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    //Log.e("nameIndex", "" + nameIndex);
                    returnCursor.moveToFirst();
                    if (nameIndex != 2) {
                        String[] str = mimeType.split("/");
                        Long tsLong = System.currentTimeMillis() / 1000;
                        String ts = tsLong.toString();
                        filename = getFileName(uri) + "." + str[1];
                    } else {
                        filename = returnCursor.getString(nameIndex);
                    }
                    Log.e("fileName", filename);
                    //String size = Long.toString(returnCursor.getLong(sizeIndex));
                }
                //File fileSave = getActivity().getExternalFilesDir(null);
                String sourcePath = getActivity().getExternalFilesDir(null).toString();
                try {
                    String path = sourcePath + "/" + filename;
                    Log.e("fileCheck", "onActivityResult: file path: " + path);
//                        attachedPrescriptionTV.setText(path);
                    copyFileStream(new File(sourcePath + "/" + filename), uri, getActivity());
                    attachFile(path);
//                        uploadFile(path);
//                        Toast.makeText(getActivity(), "File is selected, please upload!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
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

                            DialogHandler.getSharedInstance().genericDialogSuccess(requireActivity(), true, "Upload Attachment", response.body().errorMessage, "Ok", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    alertDialog.dismiss();
                                }
                            });

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

    /**
     * Show Time Picker
     */
    public void showTimePicker(String selectedTime) {
        final Calendar cldr = Calendar.getInstance();
        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.MINUTE, minute);

                String output = String.format("%02d:%02d", hourOfDay, minute);
                if (selectedTime.equalsIgnoreCase("fromTime")) {
                    tvFromTime.setText(output);
                    fromTime = output;
                } else if (selectedTime.equalsIgnoreCase("toTime")) {
                    tvToTime.setText(output);
                    toTime = output;
                }
            }
        }, hour, minutes, false);

        tpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tpd.show();
    }

    /**
     * Show Date Picker
     */
    @SuppressLint("DefaultLocale")
    private void showDatePicker(String selectedDate) {
        Calendar c = Calendar.getInstance();
        int mYearParam = mYear2;
        int mMonthParam = mMonth2 - 1;
        int mDayParam = mDay2;

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mMonth2 = monthOfYear + 1;
                        mYear2 = year;
                        mDay2 = dayOfMonth;
                        String outputMonth = String.format("%02d", mMonth2);
                        String outputDay = String.format("%02d", mDay2);
                        if (selectedDate.equalsIgnoreCase("fromDate")) {
                            tvFromDate.setText(mYear2 + "-" + outputMonth + "-" + outputDay);
                            tvFromShortLeaveDate.setText(mYear2 + "-" + outputMonth + "-" + outputDay);
                            fromDate = mYear2 + "-" + outputMonth + "-" + outputDay;
                        } else if (selectedDate.equalsIgnoreCase("toDate")) {
                            tvToDate.setText(mYear2 + "-" + outputMonth + "-" + outputDay);
                            toDate = mYear2 + "-" + outputMonth + "-" + outputDay;
                        }
                    }
                }, mYearParam, mMonthParam, mDayParam);

        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    /**
     * show gif loader
     */
    private void showGifLoader() {
        loaderLayout.setVisibility(View.VISIBLE);
        if (getActivity() != null) {
            Glide.with(getActivity())
                    .load(R.drawable.ic_ihclogo)
                    .into(imageLoader);
            imageLoader.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                    R.anim.blink_animation));
        }
    }

    public static String getPath(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // DocumentProvider
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get File Name
     *
     * @param uri
     * @return
     */
    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /**
     * Copy File Stream
     *
     * @param dest
     * @param uri
     * @param context
     * @throws IOException
     */
    private void copyFileStream(File dest, Uri uri, Context context)
            throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
    }

    /**
     * get data column
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * is external storage document
     *
     * @param uri
     * @return
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * is Downloads Document
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * is media document
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * is google photos Uri
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}