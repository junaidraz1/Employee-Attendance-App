package com.example.employeeattendanceapp.Model.Response;

import com.example.employeeattendanceapp.Model.CityData;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class GetCityResponse implements Serializable {

    @SerializedName("Status")
    public Integer Status;

    @SerializedName("Data")
    public ArrayList<CityData> data;
}
