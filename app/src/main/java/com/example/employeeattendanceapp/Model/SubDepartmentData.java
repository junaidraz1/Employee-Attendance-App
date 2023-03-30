package com.example.employeeattendanceapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class SubDepartmentData implements Serializable {

    @SerializedName("Id")
    public String subDepId;

    @SerializedName("Name")
    public String subDepName;
}
