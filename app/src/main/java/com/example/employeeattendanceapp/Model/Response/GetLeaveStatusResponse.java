package com.example.employeeattendanceapp.Model.Response;

import com.example.employeeattendanceapp.Model.LeaveStatusData;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class GetLeaveStatusResponse implements Serializable {

    @SerializedName("Table")
    public ArrayList<LeaveStatusData> data;

}
