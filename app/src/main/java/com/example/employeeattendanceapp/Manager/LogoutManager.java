package com.example.employeeattendanceapp.Manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;


import com.example.employeeattendanceapp.Activity.LoginActivity;
import com.example.employeeattendanceapp.DB.DBManager;
import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Listeners.DialogClickListener;
import com.example.employeeattendanceapp.Model.Response.LogoutResponse;
import com.example.employeeattendanceapp.Retrofit.apiUtils;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class LogoutManager {

    public static String TAG = "LoginManager";
    public static String logout = "";

    public static void userLogout(Activity activity, String title) {
        PrefsManager prefsManager = new PrefsManager(activity);
        DBManager dbManager = new DBManager(activity);

        if (ConnectionManager.getSharedInstance().isNetworkAvailable(activity)) {

            if (prefsManager.getUserId() != null && prefsManager.getToken() != null) {
                RequestBody requestBody = new FormBody.Builder()
                        .add("UserId", prefsManager.getUserId())
                        .add("DeviceToken", prefsManager.getToken())
                        .build();

                Call<LogoutResponse> call = apiUtils.getAPIService(activity).logoutEmployee(requestBody);
                call.enqueue(new Callback<LogoutResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LogoutResponse> call, @NonNull Response<LogoutResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {

//                            HomeActivity.logoutModelResponse = response.body();
                            if (response.body().status == 1) {
                                dbManager.open();
                                dbManager.deleteTables();
                                prefsManager.clearLogin();
                                prefsManager.clearToken();
                                prefsManager.clearAtdStatus();
//
                                Intent intent = new Intent(activity, LoginActivity.class);
                                activity.startActivity(intent);
                                Log.d(TAG, "Status " + response.body().status);
                                Log.d(TAG, "Logout " + logout);
                            } else {
                                Log.d(TAG, "Status " + response.body().status);
                                Log.d(TAG, "Logout " + logout);

//                                if (response.body().errorMessage == null) {
                                DialogHandler.getSharedInstance().dialogGeneric1Btn(activity, true, title, "OK", "Please try again later ", new DialogClickListener() {
                                    @Override
                                    public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                        alertDialog.dismiss();
                                    }
                                });
                            }
                        } else {
                            DialogHandler.getSharedInstance().dialogGeneric1Btn(activity, true, title, "OK", "Please try again later ", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    alertDialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LogoutResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, "userLogout: OnFailure " + t.getLocalizedMessage());
                    }
                });
            }
        }
    }
}
