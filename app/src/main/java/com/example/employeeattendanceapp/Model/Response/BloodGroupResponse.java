package com.example.employeeattendanceapp.Model.Response;

import com.example.employeeattendanceapp.Model.BloodGroupData;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class BloodGroupResponse implements Serializable {


    @SerializedName("Status")
    public Integer status;

    @SerializedName("Data")
    public ArrayList<BloodGroupData> bloodGroupData;

}
