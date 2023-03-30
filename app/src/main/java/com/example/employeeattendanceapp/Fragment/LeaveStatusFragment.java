package com.example.employeeattendanceapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.employeeattendanceapp.Activity.LoginActivity;
import com.example.employeeattendanceapp.AdapterRecyclerView.ExperienceAdapter;
import com.example.employeeattendanceapp.AdapterRecyclerView.LeaveStatusAdapter;
import com.example.employeeattendanceapp.DB.DBManager;
import com.example.employeeattendanceapp.DB.DatabaseHelper;
import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Listeners.RecyclerClickListener;
import com.example.employeeattendanceapp.Manager.ConnectionManager;
import com.example.employeeattendanceapp.Model.LeaveStatusData;
import com.example.employeeattendanceapp.Model.Response.GetLeaveStatusResponse;
import com.example.employeeattendanceapp.R;
import com.example.employeeattendanceapp.Retrofit.apiUtils;
import com.example.employeeattendanceapp.Utils.Constants;

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

public class LeaveStatusFragment extends Fragment {

    String TAG = "LeaveStatusFragment", userId = "";

    RelativeLayout loaderLayout;
    ImageView imageLoader;

    RecyclerView rvLeaveStatus;
    RecyclerView.LayoutManager layoutManager;

    LeaveStatusAdapter leaveStatusAdapter;

    DBManager dbManager;

    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<LeaveStatusData> leaveStatusDataArrayList;

    int startIndex = 0;

    /**
     * New Instance
     *
     * @return
     */
    public static LeaveStatusFragment newInstance() {
        return new LeaveStatusFragment();
    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave_status, container, false);

        imageLoader = view.findViewById(R.id.imageLoader);
        loaderLayout = view.findViewById(R.id.rl_loader);
        rvLeaveStatus = view.findViewById(R.id.rv_leaveStatus);
        swipeRefreshLayout = view.findViewById(R.id.sl_swipeLayout);

        layoutManager = new LinearLayoutManager(requireActivity());
        dbManager = new DBManager(requireActivity());
        dbManager.open();

        Cursor cursor = dbManager.fetchProfileParams();
        if (cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID));
        }

        leaveStatusDataArrayList = new ArrayList<>();

        /**
         * network call to get leave status
         */
        getLeaveStatus();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /**
                 * network call to get leave status
                 */
                getLeaveStatus();
            }
        });

        return view;
    }

    private void getLeaveStatus() {
        animateLoaderIcon();
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {
            RequestBody requestBody = new FormBody.Builder()
                    .add("UserId", userId)
                    .add("Start", String.valueOf(startIndex))
                    .add("Length", String.valueOf(Constants.pageLength))
                    .add("FilterRecord", "")
                    .build();

            Call<GetLeaveStatusResponse> call = apiUtils.getAPIService(requireActivity()).getLeaveStatus(requestBody);
            call.enqueue(new Callback<GetLeaveStatusResponse>() {
                @Override
                public void onResponse(@NonNull Call<GetLeaveStatusResponse> call, @NonNull Response<GetLeaveStatusResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().data != null && response.body().data.size() > 0) {
                            loaderLayout.setVisibility(View.GONE);
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            leaveStatusDataArrayList = response.body().data;
                            dbManager.insertLeaveStatusDetails(leaveStatusDataArrayList);
                            setAdapter(leaveStatusDataArrayList);
                        } else {
                            loaderLayout.setVisibility(View.GONE);
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            Log.e(TAG, "onResponse: data is null: " + response.body().data.size());
                        }
                    } else {
                        loaderLayout.setVisibility(View.GONE);
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetLeaveStatusResponse> call, @NonNull Throwable t) {
                    loaderLayout.setVisibility(View.GONE);
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });


        } else {
            loaderLayout.setVisibility(View.GONE);
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Connection Error"
                    , "Ok", "Please check your internet connection and try again", (status, message, alertDialog) -> alertDialog.dismiss());
        }
    }

    private void setAdapter(ArrayList<LeaveStatusData> leaveStatusDataArrayList) {
        leaveStatusAdapter = new LeaveStatusAdapter(requireActivity(), leaveStatusDataArrayList, new RecyclerClickListener() {
            @Override
            public void itemClickListener(int pos, String value) {
                Log.e(TAG, "itemClickListener: position is: " + pos);
                Log.e(TAG, "itemClickListener: link is: " + value);
                try {
                    String url = Constants.baseUrl + value;
                    Log.e(TAG, url);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    Log.e(TAG, "URL OPEN EXP: " + e.toString());
                    Toast.makeText(getActivity(), "Some Error Occurred, Please Try Again Later!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rvLeaveStatus.setAdapter(leaveStatusAdapter);
        rvLeaveStatus.setLayoutManager(layoutManager);
        leaveStatusAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        leaveStatusDataArrayList = dbManager.getLeaveStatusData();

        if (leaveStatusDataArrayList == null) {

            /**
             * network call to get leave status
             */
            getLeaveStatus();

        } else {
            setAdapter(leaveStatusDataArrayList);
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