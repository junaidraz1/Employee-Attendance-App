package com.example.employeeattendanceapp.Model.Response;

import com.example.employeeattendanceapp.Model.SubDepartmentData;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class GetSubDepartmentResponse implements Serializable {

    @SerializedName("Status")
    public Integer status;

    @SerializedName("Data")
    public ArrayList<SubDepartmentData> data;
}
