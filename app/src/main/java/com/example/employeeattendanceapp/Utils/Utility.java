package com.example.employeeattendanceapp.Utils;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */


public class Utility {

    public static Utility mInstance = null;


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 124;

    public static Utility getSharedInstance() {
        if (mInstance == null) {
            mInstance = new Utility();
        }
        return mInstance;
    }

    /**
     * Get Wishing Message
     *
     * @return
     */
    public String getWishingMessage() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            return "Good Morning! ,";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            return "Good Afternoon! ,";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            return "Good Evening! ,";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            return "Good Night! ,";
        }

        return null;
    }

    public boolean timeConstraint() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        boolean canMark = false;

        if (timeOfDay >= 07) {
            canMark = true;
        }
        return canMark;
    }

    public String calculateDays(String start, String end) {

        String DATE_FORMAT = "yyyy-MM-dd";
        Log.d("test", "start date: " + start);
        Log.d("test", "end date: " + end);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            if (startDate != null && endDate != null) {
                numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(numberOfDays);
    }

    @SuppressLint("SimpleDateFormat")
    public ArrayList<String> getMonths() {

        ArrayList<String> months = new ArrayList<String>();

        for (int i = 0; i < 12; i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            cal.set(Calendar.MONTH, i);
            String month_name = month_date.format(cal.getTime());

            months.add(month_name);

//            Log.e("arrayTest", "getMonths: " + months );
            Log.e("arrayTest", "getMonths: " + months);
        }
        return months;
    }

    private long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    public ArrayList<String> getPreviousYears() {
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -0);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Select Year");

        for (int i = 0; i <= 2; i++) {
            arrayList.add(String.valueOf(prevYear.get(Calendar.YEAR) - i));
        }
        return arrayList;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkReadStoragePermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(context)
                            .setCancelable(true)
                            .setTitle("Permission necessary")
                            .setMessage("Read External Storage permission is necessary!")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do Nothing
                                }
                            })
                            .show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;

            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkWritePermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(context)
                            .setCancelable(true)
                            .setTitle("Permission necessary")
                            .setMessage("Write External storage permission is necessary, Do you want to allow in order to continue?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do Nothing
                                }
                            })
                            .show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


}
