package com.example.employeeattendanceapp.Model.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class SubmitAttendanceResponse implements Serializable {

    @SerializedName("Status")
    public Integer status;

    @SerializedName("SuccessMessage")
    public String errorMessage;

}

