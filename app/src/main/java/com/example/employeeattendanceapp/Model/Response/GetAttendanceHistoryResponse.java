package com.example.employeeattendanceapp.Model.Response;

import com.example.employeeattendanceapp.Model.AttendanceHistoryData;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class GetAttendanceHistoryResponse implements Serializable {

    @SerializedName("Status")
    public Integer status;

    @SerializedName("Data")
    public ArrayList<AttendanceHistoryData> data;

    @SerializedName("TotalRecord")
    public int totalRecord;

    @SerializedName("FilterRecord")
    public int filterRecord;




}
