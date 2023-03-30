package com.example.employeeattendanceapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.employeeattendanceapp.AdapterRecyclerView.AttendanceHistoryAdapter;
import com.example.employeeattendanceapp.DB.DBManager;
import com.example.employeeattendanceapp.DB.DatabaseHelper;
import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Listeners.RecyclerClickListener;
import com.example.employeeattendanceapp.Manager.ConnectionManager;
import com.example.employeeattendanceapp.Model.AttendanceHistoryData;
import com.example.employeeattendanceapp.Model.Response.GetAttendanceHistoryResponse;
import com.example.employeeattendanceapp.R;
import com.example.employeeattendanceapp.Retrofit.apiUtils;
import com.example.employeeattendanceapp.Utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class AttendanceActivity extends AppCompatActivity {

    String TAG = "AttendanceActivity", fromDate = "", toDate = "", userId = "";
    ImageView img_back, imageLoader, ivSearchAtdHistory;
    TextView tvTitle, tvFromDate, tvToDate;
    RelativeLayout notificationsLayout, profileLayout, homeLayout, fromDateLayout, toDateLayout, loaderLayout, noRecordLayout;

    RecyclerView rvAttendanceHistory;

    RecyclerView.LayoutManager layoutManager;
    AttendanceHistoryAdapter attendanceHistoryAdapter;

    DBManager dbManager;

    ArrayList<AttendanceHistoryData> attendanceHistoryData;

    int mYear2, mMonth2, mDay2, pageStart = 0, totalData = 0;
    ;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        notificationsLayout = findViewById(R.id.rl_notifications);
        homeLayout = findViewById(R.id.rl_home);
        profileLayout = findViewById(R.id.rl_profile);
        tvTitle = findViewById(R.id.tv_title);
        tvFromDate = findViewById(R.id.txt_fromDate);
        tvToDate = findViewById(R.id.txt_toDate);
        img_back = findViewById(R.id.iv_back);
        rvAttendanceHistory = findViewById(R.id.rv_attendanceHistory);
        fromDateLayout = findViewById(R.id.layout_fromDate);
        toDateLayout = findViewById(R.id.layout_toDate);
        noRecordLayout = findViewById(R.id.rl_noRecordFound);
        loaderLayout = findViewById(R.id.rl_loader);
        imageLoader = findViewById(R.id.imageLoader);
        ivSearchAtdHistory = findViewById(R.id.iv_search);

        attendanceHistoryData = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
        dbManager = new DBManager(this);
        dbManager.open();

        Cursor cursor = dbManager.fetchProfileParams();
        if (cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID));
        }

        tvTitle.setText("Attendance");

        rvAttendanceHistory.setVisibility(View.GONE);
        noRecordLayout.setVisibility(View.VISIBLE);

        /**
         * method that contains implementation of click listeners
         */
        clickListeners();

        /**
         * Calling recyclerview scrollListener for pagination
         **/
        scrollListener();
    }

    private void clickListeners() {
        /**
         * home icon layout click listener
         */
        homeLayout.setOnClickListener(view12 -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        /**
         * notifications layout click listener
         */
        notificationsLayout.setOnClickListener(view13 -> {
            Toast.makeText(this, "Coming Soon !!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
//            startActivity(intent);
        });

        /**
         * profile layout click listener
         */
        profileLayout.setOnClickListener(view14 ->
                startActivity(new Intent(AttendanceActivity.this, ProfileActivity.class))
        );

        /**
         * back icon click listener
         */
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /**
         * to set "from" date range
         */
        fromDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("fromDate");
            }
        });

        /**
         * to set "to" date range
         */
        toDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("toDate");
            }
        });

        ivSearchAtdHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvFromDate.getText().toString().isEmpty()) {
                    Toast.makeText(AttendanceActivity.this, "Please select from date", Toast.LENGTH_SHORT).show();

                } else if (tvToDate.getText().toString().isEmpty()) {
                    Toast.makeText(AttendanceActivity.this, "Please select to date", Toast.LENGTH_SHORT).show();

                } else {
                    /**
                     * network call to get attendance history
                     */
                    getAttendanceHistory(fromDate, toDate);
                }
            }
        });
    }

    /**
     * Method to get attendance history
     *
     * @param dateFrom
     * @param dateTo
     */
    private void getAttendanceHistory(String dateFrom, String dateTo) {
        showGifLoader();
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(this)) {
            RequestBody requestBody = new FormBody.Builder()
                    .add("StartDate", dateFrom)
                    .add("EndDate", dateTo)
                    .add("UserId", userId)
                    .add("start", String.valueOf(pageStart))
                    .add("length", String.valueOf(Constants.atdPageLength))
                    .build();

            Call<GetAttendanceHistoryResponse> call = apiUtils.getAPIService(this).getAttendanceHistory(requestBody);
            call.enqueue(new Callback<GetAttendanceHistoryResponse>() {
                @Override
                public void onResponse(@NonNull Call<GetAttendanceHistoryResponse> call, @NonNull Response<GetAttendanceHistoryResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().status > 0) {
                            loaderLayout.setVisibility(View.GONE);

                            totalData = response.body().totalRecord;

                            //to clear list if user calls this method for same event
                            if (pageStart == 0) {
                                attendanceHistoryData.clear();
                            }

                            attendanceHistoryData.addAll(response.body().data);

                            if (totalData > 0 && attendanceHistoryData.size() > 0) {
                                rvAttendanceHistory.setVisibility(View.VISIBLE);
                                noRecordLayout.setVisibility(View.GONE);
                                /**
                                 * method call to set attendance history adapter
                                 */
                                setAttendanceHistoryAdapter(attendanceHistoryData);
                            } else {
                                rvAttendanceHistory.setVisibility(View.GONE);
                                noRecordLayout.setVisibility(View.VISIBLE);
                            }

                        } else {
                            loaderLayout.setVisibility(View.GONE);
                        }
                    } else {
                        loaderLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetAttendanceHistoryResponse> call, @NonNull Throwable t) {
                    loaderLayout.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());
                }
            });

        } else {
            loaderLayout.setVisibility(View.GONE);
            DialogHandler.getSharedInstance().dialogGeneric1Btn(this, true, "Apply Leave"
                    , "Ok", "Please check your internet connection and try again", (status, message, alertDialog) -> alertDialog.dismiss());
        }

    }

    /**
     * to set recycler view adapter for months recycler view
     */
    private void setAttendanceHistoryAdapter(ArrayList<AttendanceHistoryData> attendanceHistoryData) {
        if (attendanceHistoryData != null) {
            attendanceHistoryAdapter = new AttendanceHistoryAdapter(this, attendanceHistoryData, new RecyclerClickListener() {
                @Override
                public void itemClickListener(int pos, String value) {

                    Log.e(TAG, "itemClickListener: selected month is: " + value + " at position: " + pos);
                }
            });
            rvAttendanceHistory.setAdapter(attendanceHistoryAdapter);
        }
        rvAttendanceHistory.setLayoutManager(layoutManager);
        attendanceHistoryAdapter.notifyDataSetChanged();
    }

    /**
     * Method to detect bottom of recycler view
     */
    private void scrollListener() {
        rvAttendanceHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                    dataPagination();
                }
            }
        });
    }

    /**
     * Method to generate API call to get students for attendance
     * according to length
     */
    private void dataPagination() {
        if (pageStart + Constants.atdPageLength < totalData) {
            pageStart = pageStart + Constants.atdPageLength;
            getAttendanceHistory(fromDate, toDate);
            Log.d(TAG + " scrollListener", "PAGE START" + pageStart);
        } else {
            Toast.makeText(AttendanceActivity.this, "All records are fetched", Toast.LENGTH_SHORT).show();
        }
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
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
                            fromDate = mYear2 + "-" + outputMonth + "-" + outputDay;
                        } else if (selectedDate.equalsIgnoreCase("toDate")) {
                            tvToDate.setText(mYear2 + "-" + outputMonth + "-" + outputDay);
                            toDate = mYear2 + "-" + outputMonth + "-" + outputDay;
                        }
                    }
                }, mYearParam, mMonthParam, mDayParam);

        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    /**
     * show gif loader
     */
    private void showGifLoader() {
        loaderLayout.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(R.drawable.ic_ihclogo)
                .into(imageLoader);
        imageLoader.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.blink_animation));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}