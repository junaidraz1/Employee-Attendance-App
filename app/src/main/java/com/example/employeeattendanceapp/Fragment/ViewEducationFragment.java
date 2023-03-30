package com.example.employeeattendanceapp.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.RecoverySystem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.employeeattendanceapp.AdapterRecyclerView.EducationAdapter;
import com.example.employeeattendanceapp.DB.DBManager;
import com.example.employeeattendanceapp.DB.DatabaseHelper;
import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Listeners.DialogClickListener;
import com.example.employeeattendanceapp.Manager.ConnectionManager;
import com.example.employeeattendanceapp.Model.EducationData;
import com.example.employeeattendanceapp.Model.Response.EducationDetailsResponse;
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

public class ViewEducationFragment extends Fragment {

    String TAG = "ViewEducationFragment", userId = "";

    RecyclerView educationRecyclerView;
    RelativeLayout noRecordLayout;

    EducationAdapter educationAdapter;
    RecyclerView.LayoutManager layoutManager;

    DBManager dbManager;
    ArrayList<EducationData> educationDataList;

    /**
     * New Instance
     *
     * @return
     */
    public static ViewEducationFragment newInstance() {
        return new ViewEducationFragment();
    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_education, container, false);

        educationRecyclerView = view.findViewById(R.id.rv_education);
        noRecordLayout = view.findViewById(R.id.rl_noRecordFound);

        Log.e(TAG, "onCreateView: ViewEducationFragment");

        layoutManager = new LinearLayoutManager(requireActivity());

        educationDataList = new ArrayList<>();

        dbManager = new DBManager(requireActivity());
        dbManager.open();

        Cursor cursor = dbManager.fetchProfileParams();
        if (cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID));
        }

        getEducationDetails();


        return view;
    }

    private void getEducationDetails() {
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {

            if (!userId.isEmpty()) {
                RequestBody requestBody = new FormBody.Builder()
                        .add("UserId", userId)
                        .build();

                Call<EducationDetailsResponse> call = apiUtils.getAPIService(requireActivity()).getEducationDetails(requestBody);
                call.enqueue(new Callback<EducationDetailsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<EducationDetailsResponse> call, @NonNull Response<EducationDetailsResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().status > 0) {

                                educationDataList = response.body().data;
                                Log.e(TAG, "onResponse: size: " + educationDataList.size());

                                if (educationDataList != null && educationDataList.size() > 0) {
                                    dbManager.insertEducationDetails(educationDataList);
                                    setEducationAdapter(educationDataList);

                                } else {
                                    noRecordLayout.setVisibility(View.VISIBLE);
                                }


                            } else {
                                Log.e(TAG, "onResponse: response status is 0");
                            }
                        } else {
                            Log.e(TAG, "onResponse: response not successfull");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<EducationDetailsResponse> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure: response failed: " + t.getLocalizedMessage());

                    }
                });


            } else {
                Log.e(TAG, "getEducationDetails: user id is empty");
            }

        } else {
            DialogHandler.getSharedInstance().dialogGeneric1Btn(requireActivity(), true, "Connection Error", "Ok", "Please check your internet connection and try again", new DialogClickListener() {
                @Override
                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                    alertDialog.dismiss();
                }
            });
        }

    }

    private void setEducationAdapter(ArrayList<EducationData> educationDataList) {

        educationAdapter = new EducationAdapter(requireActivity(), educationDataList);

        educationRecyclerView.setAdapter(educationAdapter);
        educationRecyclerView.setLayoutManager(layoutManager);
        educationAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ViewEducationFragment");
        educationDataList = dbManager.getEducationData();

        if (educationDataList == null) {
            getEducationDetails();

        } else {
            setEducationAdapter(educationDataList);
        }
    }
}