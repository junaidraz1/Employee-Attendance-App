package com.example.employeeattendanceapp.Retrofit;

import android.app.Activity;
import android.util.Log;
import com.example.employeeattendanceapp.Activity.SplashActivity;
import com.example.employeeattendanceapp.Utils.RemoteConfigs;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class apiUtils {

    public static apiInterface getAPIService(Activity activity) {
        try {
            SplashActivity.getRemoteConfigs(activity);

        } catch (Exception e) {
            Log.e("Firebase Remote Exp", e.toString());
        }
        //Log.e("RemoteConfigs", RemoteConfigs.baseurlLive);
        return apiClient.getClient(activity, RemoteConfigs.baseurlLive).create(apiInterface.class);
    }
}
