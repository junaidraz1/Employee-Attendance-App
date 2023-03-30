package com.example.employeeattendanceapp.Model.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class GetLeaveBalanceResponse implements Serializable {

    @SerializedName("LeaveTypeName")
    public String leaveTypeName;

    @SerializedName("AllocatedLeaves")
    public String leavesAllocated;

    @SerializedName("AvailedLeaves")
    public String leavesAvailed;

}
