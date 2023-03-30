package com.example.employeeattendanceapp.Model.Response;

import com.example.employeeattendanceapp.Model.EducationData;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class GetEducationResponse implements Serializable {

    @SerializedName("Status")
    public int Status;

    public ArrayList<EducationData> Data;
}
