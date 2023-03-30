package com.example.employeeattendanceapp.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.employeeattendanceapp.AdapterRecyclerView.EducationAdapter;
import com.example.employeeattendanceapp.AdapterRecyclerView.ExperienceAdapter;
import com.example.employeeattendanceapp.DB.DBManager;
import com.example.employeeattendanceapp.DB.DatabaseHelper;
import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Listeners.DialogClickListener;
import com.example.employeeattendanceapp.Manager.ConnectionManager;
import com.example.employeeattendanceapp.Model.EducationData;
import com.example.employeeattendanceapp.Model.ExperienceData;
import com.example.employeeattendanceapp.Model.Response.EducationDetailsResponse;
import com.example.employeeattendanceapp.Model.Response.ExperienceDetailResponse;
import com.example.employeeattendanceapp.R;
import com.example.employeeattendanceapp.Retrofit.apiUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

public class ViewExperienceFragment extends Fragment {

    String TAG = "ViewExperienceFragment", userId = "";
    RelativeLayout noRecordLayout;
    RecyclerView experienceRecycler;
    LinearLayout totalExpLayout;
    TextView tvTotalExp;


    ExperienceAdapter experienceAdapter;
    RecyclerView.LayoutManager layoutManager;

    DBManager dbManager;
    ArrayList<ExperienceData> experienceDataList;

    /**
     * New Instance
     *
     * @return
     */
    public static ViewExperienceFragment newInstance() {
        return new ViewExperienceFragment();
    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_experience, container, false);

        experienceRecycler = view.findViewById(R.id.rv_experience);
        noRecordLayout = view.findViewById(R.id.rl_noRecordFound);
        totalExpLayout = view.findViewById(R.id.ll_tExperience);
        tvTotalExp = view.findViewById(R.id.tv_totalExp);

        layoutManager = new LinearLayoutManager(requireActivity());

        experienceDataList = new ArrayList<>();

        dbManager = new DBManager(requireActivity());
        dbManager.open();

        Cursor cursor = dbManager.fetchProfileParams();
        if (cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID));
        }

        getExperienceDetails();

        return view;
    }

    private void getExperienceDetails() {

        if (ConnectionManager.getSharedInstance().isNetworkAvailable(requireActivity())) {

            if (!userId.isEmpty()) {

                RequestBody requestBody = new FormBody.Builder()
                        .add("UserId", userId)
                        .build();

                Call<ExperienceDetailResponse> call = apiUtils.getAPIService(requireActivity()).getExperienceDetails(requestBody);
                call.enqueue(new Callback<ExperienceDetailResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(@NonNull Call<ExperienceDetailResponse> call, @NonNull Response<ExperienceDetailResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().status > 0) {

                                experienceDataList = response.body().data;

                                if (experienceDataList != null && experienceDataList.size() > 0) {
                                    dbManager.insertExperienceDetails(experienceDataList);
                                    setExperienceAdapter(experienceDataList);

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
                    public void onFailure(@NonNull Call<ExperienceDetailResponse> call, @NonNull Throwable t) {
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

    private void setExperienceAdapter(ArrayList<ExperienceData> experienceDataList) {
        experienceAdapter = new ExperienceAdapter(requireActivity(), experienceDataList);
        experienceRecycler.setAdapter(experienceAdapter);
        experienceRecycler.setLayoutManager(layoutManager);
        experienceAdapter.notifyDataSetChanged();

    }

    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ViewExperienceFragment");
        experienceDataList = dbManager.getExperienceData();

        if (experienceDataList == null) {
            getExperienceDetails();

        } else {
            setExperienceAdapter(experienceDataList);
        }
    }


}