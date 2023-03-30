package com.example.employeeattendanceapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class CountryData implements Serializable {

    @SerializedName("Id")
    public String id;

    @SerializedName("Name")
    public String name;

}
