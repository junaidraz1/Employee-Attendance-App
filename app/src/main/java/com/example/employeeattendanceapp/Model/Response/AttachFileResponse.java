package com.example.employeeattendanceapp.Model.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class AttachFileResponse implements Serializable {

    @SerializedName("Status")
    public Integer status;

    @SerializedName("Path")
    public String filePath;

    @SerializedName("ErrorMessage")
    public String errorMessage;

}
