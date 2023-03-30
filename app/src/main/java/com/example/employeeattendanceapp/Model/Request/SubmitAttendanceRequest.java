package com.example.employeeattendanceapp.Model.Request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class SubmitAttendanceRequest implements Serializable {

    @SerializedName("EmployeeNumber")
    public String empNumber;

    @SerializedName("AttState")
    public String atdState;

    @SerializedName("UserId")
    public String userId;

    @SerializedName("LocationLongitude")
    public String locationLong;

    @SerializedName("LocationLattitude")
    public String locationLat;

    @SerializedName("LocationAddress")
    public String locationAddress;


}
