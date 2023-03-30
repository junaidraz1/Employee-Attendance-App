package com.example.employeeattendanceapp.Model.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class SubmitLeaveResponse implements Serializable {

    @SerializedName("Status")
    public Integer status;

    @SerializedName("Message")
    public String errorMessage;

}
