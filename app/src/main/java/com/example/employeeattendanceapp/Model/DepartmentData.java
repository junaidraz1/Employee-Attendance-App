package com.example.employeeattendanceapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class DepartmentData implements Serializable {

    @SerializedName("UserId")
    public String depUserId;

    @SerializedName("DepartmentId")
    public String depId;
}
