package com.example.employeeattendanceapp.Manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class ConnectionManager {

    private static ConnectionManager sharedInstance;

    public static ConnectionManager getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new ConnectionManager();
        }
        return sharedInstance;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
