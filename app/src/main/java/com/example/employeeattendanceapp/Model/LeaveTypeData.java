package com.example.employeeattendanceapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class LeaveTypeData implements Serializable {

    @SerializedName("Id")
    public String leaveId;

    @SerializedName("Name")
    public String leaveName;
}
